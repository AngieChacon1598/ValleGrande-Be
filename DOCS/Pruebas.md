# Documento con Datos Reales para Operaciones CRUD en Reservas y Detalles de Reserva

## 1. Estructura de las Entidades

### Reservation
```json
{
  "reservationId": Integer,
  "reservationName": String,
  "reservationDate": Date (formato ISO),
  "orderStatusType": {
    "idTypeState": Integer
  },
  "restaurantUser": {
    "userId": Integer
  }
}
```

### ReservationDetail
```json
{
  "idReservationDetail": Integer,
  "numberPeople": Integer,
  "reservationMethod": String,
  "request": String,
  "restaurantTable": {
    "tableId": Integer
  },
  "reservation": {
    "reservationId": Integer
  }
}
```

## 2. Datos de Referencia

### OrderStatusType (Estados de Reserva)
```
ID 1: Pendiente
ID 2: Proceso
ID 3: Entregado
ID 4: Cancelado
```

### RestaurantTable (Mesas disponibles)
```
ID 1: Mesa 101, Capacidad 4, Estado 'A' (Activo)
ID 2: Mesa 102, Capacidad 2, Estado 'I' (Inactivo)
ID 3: Mesa 103, Capacidad 6, Estado 'A' (Activo)
ID 4: Mesa 104, Capacidad 4, Estado 'A' (Activo)
ID 5: Mesa 105, Capacidad 3, Estado 'I' (Inactivo)
```

## 3. Ejemplos para Operaciones CRUD

### 3.1 Crear una Reserva (POST a /api/reservations)

```json
{
  "reservationName": "Cena de Aniversario",
  "reservationDate": "2025-05-20T19:00:00",
  "orderStatusType": {
    "idTypeState": 1
  },
  "restaurantUser": {
    "userId": 1
  }
}
```

### 3.2 Listar Reservas (GET a /api/reservations)

Este endpoint devolverá todas las reservas en formato DTO.

### 3.3 Obtener una Reserva por ID (GET a /api/reservations/{id})

Este endpoint devolverá una reserva específica en formato DTO.

### 3.4 Modificar una Reserva (PUT a /api/reservations/{id})

```json
{
  "reservationName": "Cena de Aniversario Modificada",
  "reservationDate": "2025-05-20T20:00:00",
  "orderStatusType": {
    "idTypeState": 2
  }
}
```

### 3.5 Eliminar una Reserva (DELETE a /api/reservations/{id})

Este endpoint realizará una eliminación lógica de la reserva con el ID especificado, cambiando su estado a "Cancelado".

### 3.6 Restaurar una Reserva (PUT a /api/reservations/{id}/restore)

Este endpoint restaurará una reserva previamente eliminada lógicamente, cambiando su estado a "Pendiente".

### 3.7 Obtener Reservas Activas (GET a /api/reservations/active)

Este endpoint devolverá todas las reservas que no estén en estado "Cancelado".

### 3.8 Obtener Reservas Canceladas (GET a /api/reservations/canceled)

Este endpoint devolverá todas las reservas que estén en estado "Cancelado".

### 3.6 Crear un Detalle de Reserva (POST a /api/reservation-details)

```json
{
  "numberPeople": 4,
  "reservationMethod": "Online",
  "request": "Mesa cerca de la ventana",
  "restaurantTable": {
    "tableId": 1
  },
  "reservation": {
    "reservationId": 1
  }
}
```

### 3.7 Listar Detalles de Reserva (GET a /api/reservation-details)

Este endpoint devolverá todos los detalles de reserva en formato DTO.

### 3.8 Obtener un Detalle de Reserva por ID (GET a /api/reservation-details/{id})

Este endpoint devolverá un detalle de reserva específico en formato DTO.

### 3.9 Modificar un Detalle de Reserva (PUT a /api/reservation-details/{id})

```json
{
  "numberPeople": 6,
  "reservationMethod": "Teléfono",
  "request": "Mesa cerca de la ventana con vista al jardín",
  "restaurantTable": {
    "tableId": 3
  }
}
```

### 3.10 Eliminar un Detalle de Reserva (DELETE a /api/reservation-details/{id})

Este endpoint realizará una eliminación lógica del detalle de reserva con el ID especificado, cambiando su estado a "I" (Inactivo).

### 3.11 Restaurar un Detalle de Reserva (PUT a /api/reservation-details/{id}/restore)

Este endpoint restaurará un detalle de reserva previamente eliminado lógicamente, cambiando su estado a "A" (Activo).

### 3.12 Obtener Detalles de Reserva Activos (GET a /api/reservation-details/active)

Este endpoint devolverá todos los detalles de reserva que estén en estado "A" (Activo).

### 3.13 Obtener Detalles de Reserva Inactivos (GET a /api/reservation-details/inactive)

Este endpoint devolverá todos los detalles de reserva que estén en estado "I" (Inactivo).

## 4. Ejemplos Adicionales para Diferentes Escenarios

### 4.1 Reserva para Evento Corporativo

```json
{
  "reservationName": "Evento Corporativo XYZ",
  "reservationDate": "2025-06-15T18:30:00",
  "orderStatusType": {
    "idTypeState": 1
  },
  "restaurantUser": {
    "userId": 2
  }
}
```

### 4.2 Detalle para Reserva de Evento Corporativo

```json
{
  "numberPeople": 6,
  "reservationMethod": "Email",
  "request": "Área privada, proyector disponible",
  "restaurantTable": {
    "tableId": 3
  },
  "reservation": {
    "reservationId": 2
  }
}
```

### 4.3 Reserva para Celebración Familiar

```json
{
  "reservationName": "Cumpleaños de Juan",
  "reservationDate": "2025-07-10T13:00:00",
  "orderStatusType": {
    "idTypeState": 1
  },
  "restaurantUser": {
    "userId": 3
  }
}
```

### 4.4 Detalle para Celebración Familiar

```json
{
  "numberPeople": 8,
  "reservationMethod": "En persona",
  "request": "Decoración especial, pastel sorpresa",
  "restaurantTable": {
    "tableId": 4
  },
  "reservation": {
    "reservationId": 3
  }
}
```

### 4.5 Cambiar Estado de una Reserva (de Pendiente a Proceso)

```json
{
  "orderStatusType": {
    "idTypeState": 2
  }
}
```

### 4.6 Cambiar Estado de una Reserva (de Proceso a Cancelado)

```json
{
  "orderStatusType": {
    "idTypeState": 4
  }
}
```

## 5. Notas Importantes

1. **Eliminación Física vs Lógica**: El sistema implementa eliminación lógica para las reservas (cambiando su estado a "Cancelado") y detalles de reserva (cambiando su estado a "I"). Existen endpoints específicos para restaurar registros eliminados lógicamente (/api/reservations/{id}/restore y /api/reservation-details/{id}/restore).

2. **Estados de Reserva**: Las reservas utilizan los mismos tipos de estado que los pedidos (Pendiente, Proceso, Entregado, Cancelado).

3. **IDs Existentes**: Para crear reservas y detalles, asegúrate de usar IDs existentes para:
   - `userId` en `restaurantUser`
   - `idTypeState` en `orderStatusType`
   - `tableId` en `restaurantTable`

4. **Capacidad de Mesas**: El sistema valida que el número de personas no exceda la capacidad de la mesa seleccionada.

5. **Límite de Personas**: El número máximo de personas por reserva es 8.

6. **Estado de Mesas**: Solo se pueden reservar mesas con estado 'A' (Activo).

7. **Flujo Completo de Prueba**:
   - Crear una reserva (POST)
   - Obtener la reserva creada (GET)
   - Modificar la reserva (PUT)
   - Verificar los cambios (GET)
   - Eliminar lógicamente la reserva (DELETE)
   - Verificar que aparece como cancelada (GET /api/reservations/canceled)
   - Restaurar la reserva (PUT /api/reservations/{id}/restore)
   - Verificar que aparece como activa (GET /api/reservations/active)