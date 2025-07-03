SELECT * FROM DBA_USERS;

ALTER SESSION SET CURRENT_SCHEMA = DEVELOPER2;

-- Crear tabla de tipos de estado de orden
CREATE TABLE order_status_type (
    id_type_state INT PRIMARY KEY,
    name VARCHAR(90) NOT NULL,
    CONSTRAINT chk_order_status_name CHECK (name IN ('Pendiente', 'Entregado', 'Cancelado', 'Proceso', 'Finalizado'))
);

-- Crear tabla de tipos de pago
CREATE TABLE payment_type (
    id_payment_type INT PRIMARY KEY,
    name VARCHAR(90) NOT NULL,
    CONSTRAINT chk_payment_type_name CHECK (name IN ('Efectivo', 'Tarjeta', 'Yape', 'Transferencia')) 
);

-- Crear tabla de categorías de productos
CREATE TABLE Category (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,  -- ID autoincremental
    name VARCHAR2(255) NOT NULL,                         -- Nombre de la categoría
    description VARCHAR2(1000),                          -- Descripción de la categoría
    status VARCHAR2(20) DEFAULT 'A',                     -- Estado de la categoría (activo/inactivo)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,      -- Fecha de creación
    CONSTRAINT uq_category_name UNIQUE (name)            -- Restricción UNIQUE en el campo name
);


-- Crear tabla de productos
CREATE TABLE Product (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,  -- ID autoincremental
    name VARCHAR2(255) NOT NULL UNIQUE,                  -- Nombre del producto
    description VARCHAR2(1000),                          -- Descripción del producto
    price NUMBER(10,2) NOT NULL,                         -- Precio del producto
    status NUMBER(1) DEFAULT 1 CHECK (status IN (0, 1)), -- Estado del producto (activo/inactivo)
    image_url VARCHAR2(500),                             -- URL de la imagen del producto
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,      -- Fecha de creación
    category_id NUMBER,                                  -- Relación con la categoría
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
        REFERENCES Category(id)
);

ALTER TABLE Product
ADD CONSTRAINT uq_product_name UNIQUE (name);


CREATE TABLE sales_ticket (
    ticket_id NUMBER PRIMARY KEY,                         
    sale_date TIMESTAMP NOT NULL,                          
    total_payment NUMBER(6,2) CHECK (total_payment > 0),  
    delivery VARCHAR2(2),                                  
    delivery_address VARCHAR2(150),                        
    note VARCHAR2(200),                                    
    user_id NUMBER,                                        
    id_type_state NUMBER,                                  
    id_payment_type NUMBER,                                 
    CONSTRAINT fk_sales_ticket_user FOREIGN KEY (user_id)
        REFERENCES DEVELOPER1.restaurant_user(user_id),  
    CONSTRAINT fk_sales_ticket_status FOREIGN KEY (id_type_state)
        REFERENCES DEVELOPER2.order_status_type(id_type_state),  
    CONSTRAINT fk_sales_ticket_payment FOREIGN KEY (id_payment_type)
        REFERENCES DEVELOPER2.payment_type(id_payment_type),      
    CONSTRAINT chk_sales_ticket_total CHECK (total_payment > 0)  
);


-- Crear tabla de detalles de productos en ventas
CREATE TABLE product_detail (
    id_detail_product NUMBER PRIMARY KEY,         
    amount INT NOT NULL CHECK (amount > 0),        
    ticket_id INT,                                 
    id INT,                                
    FOREIGN KEY (ticket_id) REFERENCES sales_ticket(ticket_id), 
    FOREIGN KEY (id) REFERENCES product(id),   
    CONSTRAINT chk_product_detail_amount CHECK (amount <= 10)  
);


-- Crear secuencias
CREATE SEQUENCE seq_order_status_type START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payment_type START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_category START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_product_detail START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SALES_TICKET_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;


GRANT SELECT ON seq_sales_ticket TO DEVELOPER2;
GRANT SELECT ON seq_product_detail TO DEVELOPER2;

-- Otorgar permiso SELECT sobre la tabla order_status_type
GRANT SELECT ON ORDER_STATUS_TYPE TO DEVELOPER1;

-- Otorgar permiso REFERENCES sobre las tablas order_status_type para que DEVELOPER1 pueda usar claves foráneas
GRANT REFERENCES ON ORDER_STATUS_TYPE TO DEVELOPER1;


SELECT * FROM order_status_type;
SELECT * FROM category;
SELECT * FROM payment_type;
SELECT * FROM product;

SELECT * FROM sales_ticket;
SELECT * FROM product_detail;



-- Insertar tipos de estado de orden
INSERT INTO order_status_type (id_type_state, name) 
VALUES (seq_order_status_type.NEXTVAL, 'Pendiente');
   
INSERT INTO order_status_type (id_type_state, name) 
VALUES (seq_order_status_type.NEXTVAL, 'Proceso');
   
INSERT INTO order_status_type (id_type_state, name) 
VALUES (seq_order_status_type.NEXTVAL, 'Entregado');
   
INSERT INTO order_status_type (id_type_state, name) 
VALUES (seq_order_status_type.NEXTVAL, 'Cancelado');
   
INSERT INTO order_status_type (id_type_state, name) 
VALUES (seq_order_status_type.NEXTVAL, 'Entregado');



-- Insertar tipos de pago
INSERT INTO payment_type (id_payment_type, name)
VALUES (seq_payment_type.NEXTVAL, 'Efectivo');

INSERT INTO payment_type (id_payment_type, name)
VALUES (seq_payment_type.NEXTVAL, 'Tarjeta');

INSERT INTO payment_type (id_payment_type, name)
VALUES (seq_payment_type.NEXTVAL, 'Yape');

INSERT INTO payment_type (id_payment_type, name)
VALUES (seq_payment_type.NEXTVAL, 'Transferencia');

INSERT INTO payment_type (id_payment_type, name)
VALUES (seq_payment_type.NEXTVAL, 'Efectivo');


-- Insertar categorías de productos corregido para la tabla Category

INSERT INTO Category (name, status) VALUES ('Entradas', 'A');
INSERT INTO Category (name, status) VALUES ('Platos Principales', 'A');
INSERT INTO Category (name, status) VALUES ('Postres', 'A');
INSERT INTO Category (name, status) VALUES ('Bebidas', 'A');
INSERT INTO Category (name, status) VALUES ('Aperitivos', 'A');


-- Insertar productos (comidas criollas)
INSERT INTO product (name, description, price, status, image_url, created_at, category_id) VALUES
('Seco de Cordero', 'Plato tradicional peruano de cordero guisado con cilantro y especias', 45.00, 'A', 'http://example.com/seco_de_cordero.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 2);

INSERT INTO product (name, description, price, status, image_url, created_at, category_id) VALUES
('Ají de Gallina', 'Crema de pollo deshilachado con salsa de ají amarillo y nueces', 43.50, 'A', 'http://example.com/aji_de_gallina.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 2);

INSERT INTO product (name, description, price, status, image_url, created_at, category_id) VALUES
('Ceviche de Pescado', 'Fresco pescado marinado en limón con cebolla roja y cilantro', 50.00, 'A', 'http://example.com/ceviche_pescado.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 1);

INSERT INTO product (name, description, price, status, image_url, created_at, category_id) VALUES
('Chupe de Camarones', 'Sopa cremosa de camarones con leche, papas y queso', 47.00, 'A', 'http://example.com/chupe_de_camarones.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 2);

INSERT INTO product (name, description, price, status, image_url, created_at, category_id) VALUES
('Pollo a la Brasa', 'Pollo marinado y asado a la perfección acompañado de papas fritas', 44.00, 'A', 'http://example.com/pollo_a_la_brasa.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 2);


-- Insertar producto "Tallarines Verdes" con precio mayor a 40
INSERT INTO product (name, description, price, status, image_url, created_at, category_id)
VALUES ('Tallarines Verdes', 'Plato tradicional peruano, pasta con salsa de albahaca y espinaca, acompañado de carne de res', 42.00, 'A', 'http://example.com/tallarines_verdes.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 2);

-- Insertar producto "Arroz con Mariscos" con precio mayor a 40
INSERT INTO product (name, description, price, status, image_url, created_at, category_id)
VALUES ('Arroz con Mariscos', 'Delicioso arroz con mariscos frescos, típicamente peruano, preparado con ají amarillo y especias', 48.00, 'A', 'http://example.com/arroz_con_mariscos.jpg', TO_TIMESTAMP('2025-05-06 22:37:51', 'YYYY-MM-DD HH24:MI:SS'), 2);




-- Insertar registros en sales_ticket
-- Ticket 1 (venta de ceviche)
INSERT INTO sales_ticket (ticket_id, sale_date, total_payment, delivery, delivery_address, note, user_id, id_type_state, id_payment_type)
VALUES (seq_sales_ticket.NEXTVAL, TO_TIMESTAMP('2025-04-09 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 120.00, 'SI', 'Av. Siempre Viva 123', 'Venta de almuerzo', 1, 1, 1);

-- Ticket 2 (venta de lomo saltado)
INSERT INTO sales_ticket (ticket_id, sale_date, total_payment, delivery, delivery_address, note, user_id, id_type_state, id_payment_type)
VALUES (seq_sales_ticket.NEXTVAL, TO_TIMESTAMP('2025-04-09 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 80.00, 'NO', 'Av. Ficticia 456', 'Venta de cena', 2, 2, 2);

-- Ticket 3 (venta de ají de gallina)
INSERT INTO sales_ticket (ticket_id, sale_date, total_payment, delivery, delivery_address, note, user_id, id_type_state, id_payment_type)
VALUES (seq_sales_ticket.NEXTVAL, TO_TIMESTAMP('2025-04-09 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 150.00, 'SI', 'Calle Larga 789', 'Venta de bebidas', 3, 3, 3);

-- Ticket 4 (venta de causas rellenas)
INSERT INTO sales_ticket (ticket_id, sale_date, total_payment, delivery, delivery_address, note, user_id, id_type_state, id_payment_type)
VALUES (seq_sales_ticket.NEXTVAL, TO_TIMESTAMP('2025-04-09 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 95.00, 'NO', 'Calle Cortada 101', 'Venta de postres', 4, 4, 4);

-- Ticket 5 (venta de pisco sour)
INSERT INTO sales_ticket (ticket_id, sale_date, total_payment, delivery, delivery_address, note, user_id, id_type_state, id_payment_type)
VALUES (seq_sales_ticket.NEXTVAL, TO_TIMESTAMP('2025-04-09 16:15:00', 'YYYY-MM-DD HH24:MI:SS'), 50.00, 'SI', 'Av. Primavera 202', 'Venta de entradas', 5, 1, 1);



-- Insertar detalles de productos en ventas
-- Insertar detalles de productos en ventas (corregido con ticket_ids válidos)

INSERT INTO product_detail (id_detail_product, amount, ticket_id, id)
VALUES (seq_product_detail.NEXTVAL, 3, 6, 1);  -- 3 ceviches para el ticket 1

INSERT INTO product_detail (id_detail_product, amount, ticket_id, id)
VALUES (seq_product_detail.NEXTVAL, 2, 7, 2);  -- 2 lomos saltados para el ticket 2

INSERT INTO product_detail (id_detail_product, amount, ticket_id, id)
VALUES (seq_product_detail.NEXTVAL, 1, 8, 3);  -- 1 ají de gallina para el ticket 3

INSERT INTO product_detail (id_detail_product, amount, ticket_id, id)
VALUES (seq_product_detail.NEXTVAL, 4, 9, 4);  -- 4 causas rellenas para el ticket 4

INSERT INTO product_detail (id_detail_product, amount, ticket_id, id)
VALUES (seq_product_detail.NEXTVAL, 2, 10, 5);  -- 2 pisco sour para el ticket 5


SELECT sequence_name, last_number 
FROM user_sequences 
WHERE sequence_name = 'SALES_TICKET_SEQ';



SELECT * FROM CATEGORY;
SELECT * FROM PRODUCT;


UPDATE Product
SET
    name = 'Trucha Frita',
    description = 'Crocante y deliciosa trucha frita',
    price = 100.50,
    status = 1, -- activo
    image_url = 'https://nueva.url/trucha_frita.jpg',
    category_id = 4
WHERE
    id = 39; -- ID del producto que quieres modificar


DELETE FROM Category WHERE id = 7;