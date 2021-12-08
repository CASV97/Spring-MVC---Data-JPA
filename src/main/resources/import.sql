/*populate tables, utilizndo H2 base de datos de prueba e insertando datos en la tabla clients, es importante que este archivo se llave import.sql ya que JPA va a buscar el archivo con este nombre */
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(1, 'Pedro', 'Guzman','pguzman@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(2, 'El Peter', 'Velez','jvelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(3, 'Jonh', 'Velez','jvelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(4, 'Jonh', 'Velez','jvelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(5, 'Jonh', 'Velez','jvelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(6, 'Jonh', 'Velez','jvealz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(7, 'Jonh', 'Velez','jvealz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(8, 'Jonh', 'Vel1z','jvealz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(9, 'Jonh', 'Vel1z','jvealz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(10, 'Jonh', 'Ve1ez','jvaelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(11, 'Jonh', 'Ve1ez','jvaelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(12, 'Jasnh', 'V1lez','jvelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(13, 'Jasnh', 'V1lez','jselz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(14, 'Jasnh', 'Velez','jselz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(15, 'Jasnh', 'Velez','jselz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(16, 'Jasnh', 'Velez','jselz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(17, 'Jasnh', 'Veflez','jselz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(18, 'Jasnh', 'Veflez','jselz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(19, 'Jasnh', 'Veflez','jvelz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(20, 'Jo5h', 'Velfez','jvqlz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(21, 'Jo5h', 'Velfez','jvqlz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(22, 'Jo5h', 'Velez','jvqlz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(23, 'Jo5h', 'Velez','jvqlz@gmail.com','2020-11-13','');
INSERT INTO clients(id,first_name,last_name,email,create_at,photo) VALUES(24, 'Jo5h', 'Velez','jvqlz@gmail.com','2020-11-13','');

/* Populate products table*/

INSERT INTO products(name,price,create_at) VALUES('Panasonic Pantalla LCD', 255.50 ,NOW());
INSERT INTO products(name,price,create_at) VALUES('Sony Camara digital',500,NOW());
INSERT INTO products(name,price,create_at) VALUES('Apple ipod shuffle',999.99,NOW());
INSERT INTO products(name,price,create_at) VALUES('Sony Notebook z110',430,NOW());
INSERT INTO products(name,price,create_at) VALUES('Hewlerr packard Multifuncional D220',456,NOW());
INSERT INTO products(name,price,create_at) VALUES('Toshiba notebook asd1234', 330,NOW());
INSERT INTO products(name,price,create_at) VALUES('Mica Comoda 434 cajones', 4561,NOW());

/*Eg Invoices*/
INSERT INTO invoices(description,observation,client_id,create_at) VALUES('Factura Equipos de oficina',null,1,NOW());
INSERT INTO invoice_items(quantity,invoice_id,product_id) VALUES(1,1,1);
INSERT INTO invoice_items(quantity,invoice_id,product_id) VALUES(2,1,3);
INSERT INTO invoice_items(quantity,invoice_id,product_id) VALUES(3,1,4);
INSERT INTO invoice_items(quantity,invoice_id,product_id) VALUES(4,1,2);
INSERT INTO invoice_items(quantity,invoice_id,product_id) VALUES(5,1,5);

INSERT INTO invoices(description,observation,client_id,create_at) VALUES('Factura nueva ','observación alguna',1,NOW());
INSERT INTO invoice_items(quantity,invoice_id,product_id) VALUES(3,2,6);

/*Insertar usuarios*/
insert into users(username,password,enable) values('admin','$2a$10$.G9jh0wl0mUsDNKCpB59uOW5b.oiX0VefGJeKVS2cdbshhoKtt5n2',1);
insert into users(username,password,enable) values('ariel','$2a$10$KVHf1ZgCP4PAR3eNw7QGb.ivyf7oml6hR70JT/6XNbFFb0BIeyzd6',1);
insert into users(username,password,enable) values('debbie','$2a$10$tR0lStkEHzYcl66VRkJjFOgRb20idwai0RtGlinvPrQc4aTFf0IFK',1);

/*Insertar Roles*/
insert into authorities (user_id,authority) values(1,'ROLE_USER');
insert into authorities (user_id,authority) values(1,'ROLE_ADMIN');
insert into authorities (user_id,authority) values(2,'ROLE_USER');
insert into authorities (user_id,authority) values(2,'ROLE_ADMIN');
insert into authorities (user_id,authority) values(3,'ROLE_USER');
