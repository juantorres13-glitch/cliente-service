INSERT INTO regiones (id, nombre) VALUES (1, 'Suramerica');
INSERT INTO regiones (id, nombre) VALUES (2, 'Centroamerica');
INSERT INTO regiones (id, nombre) VALUES (3, 'Norteamerica');
INSERT INTO regiones (id, nombre) VALUES (4, 'Europa');
INSERT INTO regiones (id, nombre) VALUES (5, 'Asia');
INSERT INTO regiones (id, nombre) VALUES (6, 'Africa');
INSERT INTO regiones (id, nombre) VALUES (7, 'Oceania');
INSERT INTO regiones (id, nombre) VALUES (8, 'Antartida');

ALTER TABLE regiones ALTER COLUMN id RESTART WITH 9;

INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Linus', 'Torvalds', 'linus@linux.org', 'linus.jpg', 4);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('James', 'Gosling', 'james@java.com', 'james.jpg', 3);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Dennis', 'Ritchie', 'dennis@bell.com', 'dennis.jpg', 3);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Grace', 'Hopper', 'grace@navy.mil', 'grace.jpg', 3);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Ada', 'Lovelace', 'ada@babbage.io', 'ada.jpg', 4);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Alan', 'Turing', 'alan@bletchley.uk', 'alan.jpg', 4);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Margaret', 'Hamilton', 'margaret@nasa.gov', 'margaret.jpg', 3);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Tim', 'Berners-Lee', 'tim@w3.org', 'tim.jpg', 4);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Guido', 'van Rossum', 'guido@python.org', 'guido.jpg', 4);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Ken', 'Thompson', 'ken@bell.com', 'ken.jpg', 3);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Bjarne', 'Stroustrup', 'bjarne@cpp.org', 'bjarne.jpg', 4);
INSERT INTO clientes (nombre, apellido, email, foto, region_id) VALUES ('Brian', 'Kernighan', 'brian@bell.com', 'brian.jpg', 3);