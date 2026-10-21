INSERT INTO regiones (id, nombre) VALUES (1, 'Suramerica');
INSERT INTO regiones (id, nombre) VALUES (2, 'Centroamerica');
INSERT INTO regiones (id, nombre) VALUES (3, 'Norteamerica');
INSERT INTO regiones (id, nombre) VALUES (4, 'Europa');
INSERT INTO regiones (id, nombre) VALUES (5, 'Asia');
INSERT INTO regiones (id, nombre) VALUES (6, 'Africa');
INSERT INTO regiones (id, nombre) VALUES (7, 'Oceania');
INSERT INTO regiones (id, nombre) VALUES (8, 'Antartida');

ALTER TABLE regiones ALTER COLUMN id RESTART WITH 9;

INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Linus', 'Torvalds', 'linus@linux.org', 'linus.jpg', 4, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('James', 'Gosling', 'james@java.com', 'james.jpg', 3, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Dennis', 'Ritchie', 'dennis@bell.com', 'dennis.jpg', 3, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Grace', 'Hopper', 'grace@navy.mil', 'grace.jpg', 3, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Ada', 'Lovelace', 'ada@babbage.io', 'ada.jpg', 4, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Alan', 'Turing', 'alan@bletchley.uk', 'alan.jpg', 4, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Margaret', 'Hamilton', 'margaret@nasa.gov', 'margaret.jpg', 3, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Tim', 'Berners-Lee', 'tim@w3.org', 'tim.jpg', 4, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Guido', 'van Rossum', 'guido@python.org', 'guido.jpg', 4, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Ken', 'Thompson', 'ken@bell.com', 'ken.jpg', 3, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Bjarne', 'Stroustrup', 'bjarne@cpp.org', 'bjarne.jpg', 4, '2026-01-01');
INSERT INTO clientes (nombre, apellido, email, foto, region_id, create_at) VALUES ('Brian', 'Kernighan', 'brian@bell.com', 'brian.jpg', 3, '2026-01-01');

ALTER TABLE clientes ALTER COLUMN id RESTART WITH 13;