# Documentación del Proyecto RestLosPinos

## Descripción General

RestLosPinos es una aplicación Spring Boot para la gestión de un restaurante, que incluye funcionalidades para manejar reservas, mesas, usuarios, productos, ventas y más. El proyecto está estructurado siguiendo una arquitectura de capas (controladores, servicios, repositorios y modelos) y utiliza Oracle Database como sistema de gestión de base de datos.

## Información Técnica

- **Versión de Java**: 17
- **Versión de Spring Boot**: 2.7.18
- **Base de Datos**: Oracle Database (Cloud)
- **Puerto del Servidor**: 8083
- **Documentación API**: Swagger/OpenAPI

## Estructura del Proyecto

```
RestLosPinos/
├── src/
│   ├── main/
│   │   ├── java/pe/edu/vallegrande/RestLosPinos/
│   │   │   ├── config/            # Configuraciones (CORS, OpenAPI)
│   │   │   ├── exception/         # Excepciones personalizadas
│   │   │   ├── model/             # Entidades JPA y DTOs
│   │   │   ├── repository/        # Interfaces de repositorio
│   │   │   ├── rest/              # Controladores REST
│   │   │   ├── service/           # Servicios de negocio
│   │   │   └── RestLosPinosApplication.java
│   │   └── resources/
│   │       └── application.yml    # Configuración de la aplicación
│   └── test/                      # Pruebas unitarias
├── scripts.sql/                   # Scripts SQL para inicialización
│   ├── admin.sql
│   ├── developer1.sql
│   └── developer2.sql
└── pom.xml                        # Dependencias y configuración Maven
```

## Esquemas de Base de Datos

El proyecto utiliza dos esquemas diferentes en la base de datos Oracle:

- **DEVELOPER1**: Contiene tablas relacionadas con reservas, mesas y usuarios.
- **DEVELOPER2**: Contiene tablas relacionadas con productos, ventas y estados de pedidos.

Esta separación de esquemas es un punto importante a considerar al trabajar con las relaciones entre entidades.

## Entidades Principales

### Esquema DEVELOPER1

1. **UserType**: Tipos de usuario en el sistema.
   - `@Table(name = "user_type", schema = "DEVELOPER1")`
   - Campos: userTypeId, name

2. **RestaurantUser**: Usuarios del sistema (clientes, empleados).
   - `@Table(name = "restaurant_user", schema = "DEVELOPER1")`
   - Relación: ManyToOne con UserType

3. **RestaurantTable**: Información sobre las mesas disponibles.
   - `@Table(name = "restaurant_table", schema = "DEVELOPER1")`

4. **Reservation**: Gestiona las reservas de clientes.
   - `@Table(name = "reservation", schema = "DEVELOPER1")`
   - Relación: ManyToOne con OrderStatusType (DEVELOPER2) y RestaurantUser

5. **ReservationDetail**: Detalles de reservas, incluyendo mesas asignadas.
   - `@Table(name = "reservation_detail", schema = "DEVELOPER1")`
   - Relación: ManyToOne con RestaurantTable y Reservation

### Esquema DEVELOPER2

1. **OrderStatusType**: Estados posibles de pedidos/reservas.
   - `@Table(name = "order_status_type", schema = "DEVELOPER2")`
   - Valores: Pendiente, Entregado, Cancelado, Proceso, Finalizado

2. **PaymentType**: Tipos de pago disponibles.
   - `@Table(name = "payment_type", schema = "DEVELOPER2")`
   - Valores: Efectivo, Tarjeta, Yape, Transferencia

3. **Category**: Categorías de productos.
   - `@Table(name = "category", schema = "DEVELOPER2")`

4. **Product**: Información de productos.
   - `@Table(name = "product", schema = "DEVELOPER2")`
   - Relación: ManyToOne con Category

5. **SalesTicket**: Tickets de venta.
   - `@Table(name = "sales_ticket", schema = "DEVELOPER2")`
   - Relación: ManyToOne con RestaurantUser (DEVELOPER1), OrderStatusType y PaymentType

6. **ProductDetail**: Detalles de productos en una venta.
   - `@Table(name = "product_detail", schema = "DEVELOPER2")`
   - Relación: ManyToOne con SalesTicket y Product

## Referencias Cruzadas Entre Esquemas

El proyecto maneja referencias cruzadas entre los esquemas DEVELOPER1 y DEVELOPER2. Estas son algunas de las relaciones importantes:

1. **Reservation (DEVELOPER1) → OrderStatusType (DEVELOPER2)**
   ```java
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_type_state")
   private OrderStatusType orderStatusType;
   ```

2. **SalesTicket (DEVELOPER2) → RestaurantUser (DEVELOPER1)**
   ```java
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id")
   private RestaurantUser restaurantUser;
   ```

## Permisos en la Base de Datos

Para permitir las referencias cruzadas entre esquemas, se han configurado los siguientes permisos:

1. **De DEVELOPER1 a DEVELOPER2**:
   ```sql
   GRANT SELECT ON DEVELOPER1.user_type TO DEVELOPER2;
   GRANT SELECT ON DEVELOPER1.restaurant_user TO DEVELOPER2;
   GRANT REFERENCES ON DEVELOPER1.user_type TO DEVELOPER2;
   GRANT REFERENCES ON DEVELOPER1.restaurant_user TO DEVELOPER2;
   ```

2. **De DEVELOPER2 a DEVELOPER1**:
   ```sql
   GRANT SELECT ON ORDER_STATUS_TYPE TO DEVELOPER1;
   GRANT REFERENCES ON ORDER_STATUS_TYPE TO DEVELOPER1;
   ```

## Solución a Problemas de Serialización JSON

El proyecto implementó soluciones para manejar errores de `EntityNotFoundException` durante la serialización JSON de entidades con relaciones entre esquemas:

### Solución 1: DTOs (Data Transfer Objects)

Se implementaron DTOs para separar la capa de persistencia de la capa de presentación:

- ReservationDTO
- ReservationDetailDTO
- RestaurantTableDTO
- SalesResponseDTO
- ProductDetailDTO

Los servicios incluyen métodos para convertir entidades a DTOs:

```java
// Ejemplo en ReservationService
public ReservationDTO convertToDTO(Reservation reservation) {
    ReservationDTO dto = new ReservationDTO();
    // Mapeo de propiedades
    return dto;
}
```

### Solución 2: Configuración de FetchType

Se configuró `FetchType.LAZY` en las relaciones críticas para mejorar el rendimiento:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "id_type_state")
private OrderStatusType orderStatusType;
```

## Manejo de Excepciones

El proyecto utiliza dos tipos principales de excepciones para manejar casos donde no se encuentran entidades:

1. **ResourceNotFoundException**: Utilizada en la mayoría de los servicios.
   ```java
   throw new ResourceNotFoundException("Reserva con ID " + id + " no encontrada");
   ```

2. **EntityNotFoundException**: Utilizada en algunos servicios como SalesService y ProductDetailService.
   ```java
   throw new EntityNotFoundException("Producto no encontrado: " + pdDto.getProductId());
   ```

## Configuración CORS

El proyecto está configurado para permitir solicitudes CORS desde varios orígenes:

```java
registry.addMapping("/**")
        .allowedOrigins(
            "http://localhost:8083",
            "http://localhost:8080",
            "http://10.0.2.2",
            "http://localhost:4200",
            "https://8083-firebase-as232s4t02-be-1748901890569.cluster-4xpux6pqdzhrktbhjf2cumyqtg.cloudworkstations.dev",
            "http://localhost"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
```

## Documentación API

La API está documentada usando OpenAPI/Swagger y está disponible en:

```
http://localhost:8083/swagger-ui.html
```

La configuración de OpenAPI incluye:

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("RestLosPinos API")
                    .version("1.0")
                    .description("API documentation for RestLosPinos application"))
            .addServersItem(new Server().url("http://localhost:8083"));
}
```

## Dependencias Principales

- Spring Boot Starter Web: Para crear la API REST
- Spring Boot Starter Data JPA: Para interactuar con la base de datos
- Oracle JDBC Driver: Para conectarse a Oracle Database
- Springdoc OpenAPI: Para la documentación de la API
- Lombok: Para reducir código boilerplate
- Jackson: Para manejo de JSON
- JUnit 5 y Mockito: Para pruebas unitarias

## Cómo Ejecutar el Proyecto

```bash
mvn clean install
mvn spring-boot:run
```

## Ejemplos de Uso

### Crear un Producto

```json
{
  "name": "Pachamanca",
  "description": "Tradicional plato andino cocinado bajo tierra con piedras calientes, que incluye una variedad de carnes como cerdo, pollo, cordero y cuy",
  "price": 30.00,
  "status": true,
  "imageUrl": "https://url.de.la.imagen/pachamanca.jpg",
  "category": {
    "id": 5
  }
}
```

### Crear una Categoría

```json
{
  "name": "Bebidas",
  "description": "Categoría para todo tipo de bebidas frías y calientes",
  "status": "A"
}
```

### Crear un Usuario

```json
{
  "userName": "otro.usuario.02",
  "password": "Password123",
  "names": "Maria",
  "surnames": "Lopez",
  "dateOfBirth": "1998-07-22",
  "address": "Avenida Siempre Viva 742",
  "telephone": "998765432",
  "email": "otro.usuario.02@example.com",
  "documentType": "DNI",
  "numberType": "98765432",
  "userType": {
    "userTypeId": 2
  }
}
```

### Crear una Venta

```json
{
  "saleDate": "2025-06-06T02:35:00",
  "totalPayment": 103.00,
  "delivery": "SI",
  "deliveryAddress": "Av. Los rosales 123",
  "note": "Entregar en la oficina",
  "userId": 13,
  "id_type_state": 1,
  "id_payment_type": 1,
  "productDetails": [
    {
      "amount": 2,
      "productId": 47
    },
    {
      "amount": 2,
      "productId": 44
    }
  ]
}
```

### Crear una Reservación

```json
{
  "reservationId": 3,  
  "reservationName": "Reunión de trabajo de profesores - Actualizada",
  "reservationDate": "2025-04-18T14:00:00", 
  "idTypeState": 1, 
  "userId": 3,
  "reservationDetails": [
    {
      "idReservationDetail": 3,
      "numberPeople": 2,
      "reservationMethod": "Online",
      "request": "Mesa para dos, cerca de la barra - NOTA: Mesa Activa Requerida",
      "tableId": 1
    }
  ]
}
```

## Endpoints Principales

- Productos: `http://localhost:8083/api/products`
- Usuarios: `http://localhost:8083/api/users`
- Ventas: `http://localhost:8083/api/sales`
- Reservaciones: `http://localhost:8083/api/reservations`
- Detalles de Reservaciones: `http://localhost:8083/api/reservation-details`
- Mesas: `http://localhost:8083/api/tables`

## Consideraciones Importantes

1. **Esquemas Múltiples**: Siempre especificar el esquema correcto en las anotaciones `@Table` de las entidades.
2. **Referencias Cruzadas**: Tener en cuenta las relaciones entre entidades de diferentes esquemas.
3. **DTOs**: Utilizar DTOs para evitar problemas de serialización con relaciones entre esquemas.
4. **FetchType**: Preferir `FetchType.LAZY` para mejorar el rendimiento, especialmente en relaciones entre esquemas.
5. **Manejo de Excepciones**: Utilizar `ResourceNotFoundException` o `EntityNotFoundException` para manejar casos donde no se encuentran entidades.

## Conclusión

RestLosPinos es un proyecto bien estructurado que demuestra buenas prácticas en el desarrollo de aplicaciones Spring Boot con múltiples esquemas de base de datos. La implementación de DTOs y la configuración adecuada de las relaciones entre entidades son aspectos clave para el correcto funcionamiento del sistema.