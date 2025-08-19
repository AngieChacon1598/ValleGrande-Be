

//JSON PARA PRODUCTO CON CATEGORIA
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

//JSON PARA CATEGORIA
{
  "name": "Bebidas",
  "description": "Categoría para todo tipo de bebidas frías y calientes",
  "status": "A"
}


//JSON PARA USUARIOS
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


//Como ejecutar el proyecto

mvn clean install
mvn spring-boot:run

//Para ver la API
http://localhost:8083/api/products
http://localhost:8083/api/users

//Para ver la documentación de la API
http://localhost:8083/swagger-ui.html

//Para ver la base de datos
http://localhost:8083/h2-console

  
// Solicitud POST a la siguiente URL:
http://localhost:8083/api/sales

// Crear una venta

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

// Actualizar una venta

{
  "ticketId": 7,
  "saleDate": "2025-05-24T01:02:06.00724",
  "totalPayment": 25,
  "delivery": "SI",
  "deliveryAddress": "Dirección de prueba",
  "note": "Nota de prueba",
  "userId": 1,
  "id_type_state": 1,
  "id_payment_type": 1,
  "productDetails": [
    {
      "amount": 2,
      "productId": 28 
    }
  ]
}



//Actualizar una reservacion

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


//Crear una reservacion



// Crear la reservation_detail

{
  "numberPeople": 3, 
  "reservationMethod": "Online",
  "request": "Mesa tranquila con vista",
  "table": {
    "tableId": 1  
  },
  "reservation": {
    "reservationId": 22  // Colocar el id de la reservacion creada
  }
}