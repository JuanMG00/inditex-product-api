package com.inditex.controller.dto;

import com.inditex.domain.Prices;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PricesSpecification(Integer brandId, LocalDateTime applicationDate,
                                  Integer productId) implements Specification<Prices> {
    @Override
    public Predicate toPredicate(Root<Prices> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        validateSpecification();

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));

        predicates.add(criteriaBuilder.equal(root.get("product").get("id"), productId));

        predicates.add(createDateRangePredicate(criteriaBuilder, root));

        Subquery<Long> maxPrioritySubQuery = query.subquery(Long.class);
        Root<Prices> subQueryRoot = maxPrioritySubQuery.from(Prices.class);
        maxPrioritySubQuery.select(criteriaBuilder.max(subQueryRoot.get("priority")));
        Predicate maxPriorityPredicate = criteriaBuilder.equal(root.get("priority"), maxPrioritySubQuery);
        predicates.add(maxPriorityPredicate);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Creates a predicate to filter prices based on a given date.
     *
     * @param criteriaBuilder The CriteriaBuilder instance.
     * @param root            The root entity (Prices) to build the predicate from.
     * @return The predicate representing the date range condition.
     */
    private Predicate createDateRangePredicate(CriteriaBuilder criteriaBuilder, Root<Prices> root) {
        Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), applicationDate);
        Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), applicationDate);

        return criteriaBuilder.and(startDatePredicate, endDatePredicate);
    }

    private void validateSpecification() {
        if (applicationDate == null) {
            throw new IllegalArgumentException("application date cannot be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        if (brandId == null) {
            throw new IllegalArgumentException("Brand id cannot be null");
        }
    }
}
