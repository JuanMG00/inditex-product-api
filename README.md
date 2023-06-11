# inditex-product-api

Una API integral para acceder a información de productos, precios de Inditex

Acepta como parámetros de entrada: fecha de aplicación, identificador de producto, identificador de cadena. Devuelve
como datos de salida: identificador de producto, identificador de cadena, tarifa a aplicar, fechas de aplicación y
precio final a aplicar.

- ###### La aplicación usa java 17 y springboot 3.1.0 gestionado con maven
- Se ha utilizado una arquitectura hexagonal para desacoplar el dominio y el negocio.
- Se ha agregado una bbdd h2 con flyway para gestionar las tablas y la estructura de datos de la bbdd.
- La configuración se hace en el application.properties
- Se ha utilizado mapstructs para mapear entidad a dto de respuesta y así añadir más desacoplamiento.
- Existe un controller advice para capturar las excepciones generadas en el api y traducir la respuesta.
- Se ha añadido un custom converter en StringToLocalDateTimeConverter para hacer la conversión a LocalDateTime de este formato `yyyy-MM-dd HH:mm:ss` en el input

Existen test de integración como unitarios para probar el api.
Los test de integración replican el contexto levantando una instancia nueva de la bbdd 
de la api apuntando al application-it.properties 

end point generado es http://localhost:8080/v1/prices
debe tener estos parámetros obligatorios:

**brandId**: Integer representando el, id de la bbdd

**applicationDate**: LocalDateTime que tiene que estar en rango del priceList deseado. El formato tiene que
ser `yyyy-MM-dd HH:mm:ss`

**productId**: Integer representando el, id de la bbdd

- ejemplo de una llamada
válida: http://localhost:8080/v1/prices?productId=35455&applicationDate=2020-06-14 00:00:00&brandId=1

