
-- Crear tabla de tipos de usuario
CREATE TABLE user_type (
    user_type_id INT PRIMARY KEY,
    name VARCHAR(90) NOT NULL,
    CONSTRAINT chk_user_type_name CHECK (name IN ('Administrador', 'Cliente', 'Mesero'))
);

-- Crear tabla de usuarios del restaurante
CREATE TABLE restaurant_user (
    user_id INT PRIMARY KEY,
    user_name VARCHAR(220) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    names VARCHAR(200) NOT NULL,
    surnames VARCHAR(200) NOT NULL,
    date_of_birth DATE NOT NULL, 
    address VARCHAR(150) NOT NULL,
    telephone CHAR(9) CHECK (telephone LIKE '9%' AND LENGTH(telephone) = 9),
    email VARCHAR(150) NOT NULL UNIQUE CHECK (email LIKE '%_@__%.__%'),
    document_type VARCHAR(25) NOT NULL,
    number_type VARCHAR(200) NOT NULL,
    state CHAR(1) CHECK (state IN ('A', 'I')) NOT NULL,  -- El estado debe ser 'A' o 'I'
    user_type_id INT NOT NULL,
    FOREIGN KEY (user_type_id) REFERENCES user_type(user_type_id),
    
    -- Validación de longitud de documento según tipo
    CONSTRAINT chk_document_length CHECK (
        (document_type = 'DNI' AND LENGTH(number_type) = 8) OR
        (document_type = 'CNE' AND LENGTH(number_type) = 12))
);

-- Crear tabla de mesas
CREATE TABLE restaurant_table (
    table_id INT PRIMARY KEY,
    table_number INT NOT NULL,
    ability INT NOT NULL CHECK (ability > 0),
    state CHAR(1) CHECK (state IN ('A', 'I')) NOT NULL,
    UNIQUE (table_number)
);


CREATE TABLE reservation (
    reservation_id INT PRIMARY KEY,
    reservation_name VARCHAR(90) NOT NULL,
    reservation_date TIMESTAMP NOT NULL,  
    id_type_state INT,
    user_id INT,
    CONSTRAINT fk_reservation_status FOREIGN KEY (id_type_state) 
        REFERENCES DEVELOPER2.order_status_type(id_type_state),
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) 
        REFERENCES DEVELOPER1.restaurant_user(user_id)
);


-- Crear tabla de detalles de reservas
CREATE TABLE reservation_detail (
    id_reservation_detail INT PRIMARY KEY,
    number_people INT NOT NULL CHECK (number_people > 0),
    reservation_method VARCHAR(50),
    request VARCHAR(200),
    table_id INT,
    reservation_id INT,
    FOREIGN KEY (table_id) REFERENCES restaurant_table(table_id),
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT chk_reservation_detail_people CHECK (number_people <= 8)
);


-- Otorgar permiso SELECT sobre las tablas user_type y restaurant_user
GRANT SELECT ON DEVELOPER1.user_type TO DEVELOPER2;
GRANT SELECT ON DEVELOPER1.restaurant_user TO DEVELOPER2;

-- Otorgar permiso REFERENCES sobre las tablas user_type y restaurant_user para que DEVELOPER2 pueda usar claves foráneas
GRANT REFERENCES ON DEVELOPER1.user_type TO DEVELOPER2;
GRANT REFERENCES ON DEVELOPER1.restaurant_user TO DEVELOPER2;

SELECT * FROM USER_TYPE;
SELECT * FROM RESTAURANT_USER;
SELECT * FROM RESTAURANT_TABLE;
SELECT * FROM RESERVATION;
SELECT * FROM RESERVATION_DETAIL;


-- Insertar 5 registros en la tabla restaurant_table
INSERT INTO restaurant_table (table_id, table_number, ability, state) VALUES (1, 101, 4, 'A');
INSERT INTO restaurant_table (table_id, table_number, ability, state) VALUES (2, 102, 2, 'I');
INSERT INTO restaurant_table (table_id, table_number, ability, state) VALUES (3, 103, 6, 'A');
INSERT INTO restaurant_table (table_id, table_number, ability, state) VALUES (4, 104, 4, 'A');
INSERT INTO restaurant_table (table_id, table_number, ability, state) VALUES (5, 105, 3, 'I');

-- Insertar 5 registros en la tabla reservation
INSERT INTO reservation (reservation_id, reservation_name, reservation_date, id_type_state, user_id) VALUES 
(1, 'Cena de negocios', TO_TIMESTAMP('2025-04-17 19:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 1);

INSERT INTO reservation (reservation_id, reservation_name, reservation_date, id_type_state, user_id) VALUES 
(2, 'Cena familiar', TO_TIMESTAMP('2025-04-17 20:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 2);

INSERT INTO reservation (reservation_id, reservation_name, reservation_date, id_type_state, user_id) VALUES 
(3, 'Reunión de trabajo', TO_TIMESTAMP('2025-04-18 13:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 3);

INSERT INTO reservation (reservation_id, reservation_name, reservation_date, id_type_state, user_id) VALUES 
(4, 'Cumpleaños', TO_TIMESTAMP('2025-04-18 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 4);

INSERT INTO reservation (reservation_id, reservation_name, reservation_date, id_type_state, user_id) VALUES 
(5, 'Almuerzo de empresa', TO_TIMESTAMP('2025-04-19 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 5);


-- Insertar 5 registros en la tabla reservation_detail
INSERT INTO reservation_detail (id_reservation_detail, number_people, reservation_method, request, table_id, reservation_id) VALUES 
(1, 4, 'Online', 'Mesa cerca de la ventana', 1, 1);

INSERT INTO reservation_detail (id_reservation_detail, number_people, reservation_method, request, table_id, reservation_id) VALUES 
(2, 6, 'Llamada', 'Sin preferencia', 2, 2);

INSERT INTO reservation_detail (id_reservation_detail, number_people, reservation_method, request, table_id, reservation_id) VALUES 
(3, 2, 'Online', 'Mesa para dos, cerca de la barra', 3, 3);

INSERT INTO reservation_detail (id_reservation_detail, number_people, reservation_method, request, table_id, reservation_id) VALUES 
(4, 8, 'En persona', 'Mesa grande para reunión', 4, 4);

INSERT INTO reservation_detail (id_reservation_detail, number_people, reservation_method, request, table_id, reservation_id) VALUES 
(5, 3, 'Email', 'Preferencia por una mesa silenciosa', 5, 5);


-- Conectado como DEVELOPER1
ALTER TABLE reservation_detail ADD state CHAR(1) DEFAULT 'A' CHECK (state IN ('A', 'I')) NOT NULL;