package com.inditex.controller;

import com.inditex.controller.dto.PricesOutDto;
import com.inditex.controlleradvice.RestExceptionHandler;
import com.inditex.domain.Brand;
import com.inditex.domain.PriceList;
import com.inditex.domain.Prices;
import com.inditex.domain.Product;
import com.inditex.domain.enums.Currency;
import com.inditex.repository.PricesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-it.properties")
class PricesControllerIT {


    @SuppressWarnings("unused")
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PricesRepository pricesRepository;

    PricesOutDto expectedPriceList1 =
            new PricesOutDto(1, 35455, 1,
                    LocalDateTime.parse("2020-06-14T00:00:00"), LocalDateTime.parse("2020-12-31T23:59:59"),
                    BigDecimal.valueOf(35.50).setScale(2, RoundingMode.HALF_UP));


    //Test 1: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)
    @Test
    void testDateRangesCase1_10_00_14() {
        String date = "2020-06-14 10:00:00";

        PricesOutDto response = buildOkRequest(date);
        Assertions.assertEquals(expectedPriceList1, response);
    }

    //Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)
    @Test
    void testDateRangesCase2_16_00_14() {
        String date = "2020-06-14 16:00:00";

        PricesOutDto expectedResult =
                new PricesOutDto(1, 35455, 2,
                        LocalDateTime.parse("2020-06-14T15:00:00"), LocalDateTime.parse("2020-06-14T18:30:00"),
                        BigDecimal.valueOf(25.45).setScale(2, RoundingMode.HALF_UP));
        PricesOutDto response = buildOkRequest(date);
        Assertions.assertEquals(expectedResult, response);
    }

    //Test 3: petición a las 21:00 del día 14 del producto 35455   para la brand 1 (ZARA)
    @Test
    void testDateRangesCase3_21_00_14() {
        String date = "2020-06-14 21:00:00";

        PricesOutDto response = buildOkRequest(date);
        Assertions.assertEquals(expectedPriceList1, response);
    }

    //Test 4: petición a las 10:00 del día 15 del producto 35455   para la brand 1 (ZARA)
    @Test
    void testDateRangesCase4_10_00_15() {
        String date = "2020-06-15 10:00:00";
        PricesOutDto expectedResult =
                new PricesOutDto(1, 35455, 3,
                        LocalDateTime.parse("2020-06-15T00:00:00"), LocalDateTime.parse("2020-06-15T11:00:00"),
                        BigDecimal.valueOf(30.5).setScale(2, RoundingMode.HALF_UP));
        PricesOutDto response = buildOkRequest(date);
        Assertions.assertEquals(expectedResult, response);
    }

    //Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)
    @Test
    void testDateRangesCase5_21_00_16() {
        String date = "2020-06-16 21:00:00";
        PricesOutDto expectedResult =
                new PricesOutDto(1, 35455, 4,
                        LocalDateTime.parse("2020-06-15T16:00:00"), LocalDateTime.parse("2020-12-31T23:59:59"),
                        BigDecimal.valueOf(38.95).setScale(2, RoundingMode.HALF_UP));
        PricesOutDto response = buildOkRequest(date);
        Assertions.assertEquals(expectedResult, response);
    }

    @Test
    void testHighestPriority() {

        // set start date and end date with same value for all prices
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

        // persist 3 different prices with different in the same period with different priorities
        Prices prices1 = pricesRepository.save(BuildPrices(0, startDate, endDate, BigDecimal.valueOf(5)));
        Prices prices2 = pricesRepository.save(BuildPrices(1, startDate, endDate, BigDecimal.valueOf(10)));
        Prices prices3 = pricesRepository.save(BuildPrices(2, startDate, endDate, BigDecimal.valueOf(15)));

        // format date so that request understands it
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateRequest = startDate.plus(1, ChronoUnit.HOURS).format(formatter);

        PricesOutDto response = buildOkRequest(dateRequest);
        // check that the prices response is with the highest priority
        Assertions.assertEquals(prices3.getPrice().setScale(2, RoundingMode.HALF_UP), response.price());

        //modify priority of price 1 to highest
        prices1.setPriority(3);
        pricesRepository.save(prices1);

        PricesOutDto response2 = buildOkRequest(dateRequest);
        // check that the prices response is with the highest priority
        Assertions.assertEquals(prices1.getPrice().setScale(2, RoundingMode.HALF_UP), response2.price());

        //modify priority of price 2 to highest
        prices2.setPriority(4);
        pricesRepository.save(prices2);

        PricesOutDto response3 = buildOkRequest(dateRequest);
        // check that the prices response is with the highest priority
        Assertions.assertEquals(prices2.getPrice().setScale(2, RoundingMode.HALF_UP), response3.price());
    }

    //Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)
    @Test
    void testControllerInputs() {
        String date = "2020-06-16 21:00:00";
        // request without product id
        ResponseEntity<RestExceptionHandler.ErrorResponse> r = restTemplate.getForEntity(
                String.format("http://localhost:%s/v1/prices?applicationDate=%s&brandId=1", port, date),
                RestExceptionHandler.ErrorResponse.class);
        Assertions.assertEquals("productId cannot be null",  Objects.requireNonNull(r.getBody()).message());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());


        // request without brand id
        ResponseEntity<RestExceptionHandler.ErrorResponse> r1 = restTemplate.getForEntity(
                String.format("http://localhost:%s/v1/prices?applicationDate=%s&productId=35455", port, date),
                RestExceptionHandler.ErrorResponse.class);
        Assertions.assertEquals("brandId cannot be null",  Objects.requireNonNull(r1.getBody()).message());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, r1.getStatusCode());

        // request without applicationDate
        ResponseEntity<RestExceptionHandler.ErrorResponse> r2 = restTemplate.getForEntity(
                String.format("http://localhost:%s/v1/prices?brandId=1&productId=35455", port),
                RestExceptionHandler.ErrorResponse.class);
        Assertions.assertEquals("applicationDate cannot be null",  Objects.requireNonNull(r2.getBody()).message());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, r2.getStatusCode());

        // request without parameters
        ResponseEntity<RestExceptionHandler.ErrorResponse> r3 = restTemplate.getForEntity(
                String.format("http://localhost:%s/v1/prices", port),
                RestExceptionHandler.ErrorResponse.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, r3.getStatusCode());

        // request with no result
        ResponseEntity<RestExceptionHandler.ErrorResponse> r4 = restTemplate.getForEntity(
                String.format("http://localhost:%s/v1/prices?applicationDate=%s&brandId=1&productId=354551", port, date),
                RestExceptionHandler.ErrorResponse.class);
        Assertions.assertEquals("There is not any price set for the date 2020-06-16T21:00",
                Objects.requireNonNull(r4.getBody()).message());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());

    }


    /**
     * Builds and calls request to retrieve PricesOutDto using the specified date.
     *
     * @param date The date parameter for the request.
     * @return The PricesOutDto object obtained from the request.
     */
    private PricesOutDto buildOkRequest(String date) {
        ResponseEntity<PricesOutDto> response = buildRequest(date);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        return response.getBody();
    }

    private ResponseEntity<PricesOutDto> buildRequest(String date) {
        return restTemplate.getForEntity(
                String.format("http://localhost:%s/v1/prices?productId=35455&applicationDate=%s&brandId=1", port, date),
                PricesOutDto.class);
    }


    private Prices BuildPrices(Integer priority, LocalDateTime startDate, LocalDateTime endDate, BigDecimal price) {
        return Prices.builder()
                .brand(Brand.builder().id(1).build())
                .priority(priority)
                .startDate(startDate)
                .endDate(endDate)
                .price(price)
                .priceList(PriceList.builder().id(1).build())
                .product(Product.builder().id(35455).build())
                .curr(Currency.EUR)
                .build();
    }
}
