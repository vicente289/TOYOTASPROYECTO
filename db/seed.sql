USE admision;
INSERT INTO carrera (codigo, nombre) VALUES
('SIS','Ingeniería de Sistemas'),
('CIV','Ingeniería Civil'),
('ADM','Administración de Empresas'),
('DER','Derecho');

INSERT INTO examen (nombre, duracion_min, activo) VALUES ('Examen General de Admisión', 20, 1);

INSERT INTO pregunta (examen_id, enunciado, tipo) VALUES
(1, '¿Cuál es el resultado de 2 + 2?', 'OPCION_MULTIPLE'),
(1, 'Capital de Bolivia:', 'OPCION_MULTIPLE'),
(1, '¿Qué es H2O?', 'OPCION_MULTIPLE'),
(1, 'Seleccione el sinónimo de "rápido":', 'OPCION_MULTIPLE'),
(1, '¿Cuántos continentes hay (según convención común)?', 'OPCION_MULTIPLE');

INSERT INTO opcion (pregunta_id, texto, correcta) VALUES
(1, '3', 0), (1, '4', 1), (1, '5', 0), (1, '22', 0),
(2, 'Sucre (constitucional) / La Paz (sede de gobierno)', 1), (2, 'Santa Cruz', 0), (2, 'Cochabamba', 0), (2, 'Oruro', 0),
(3, 'Oxígeno', 0), (3, 'Agua', 1), (3, 'Hidrógeno', 0), (3, 'Dióxido de carbono', 0),
(4, 'Lento', 0), (4, 'Veloz', 1), (4, 'Pesado', 0), (4, 'Tardo', 0),
(5, '5', 0), (5, '6', 1), (5, '7', 0), (5, '4', 0);
