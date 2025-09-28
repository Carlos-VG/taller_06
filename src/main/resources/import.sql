-- ===========================================
-- OFICINAS (primero, por FK en DOCENTE)
-- ===========================================
INSERT INTO oficina (nombre, ubicacion) VALUES ('Of-201', 'Bloque A, Piso 2');
INSERT INTO oficina (nombre, ubicacion) VALUES ('Of-305', 'Bloque B, Piso 3');

-- ===========================================
-- PERSONAS (base para herencia)
-- ===========================================
INSERT INTO persona (nombre, apellido, correo) VALUES ('Carlos', 'Perez', 'carlos.perez@unicauca.edu.co');
INSERT INTO persona (nombre, apellido, correo) VALUES ('Ana',    'Rojas', 'ana.rojas@unicauca.edu.co');
INSERT INTO persona (nombre, apellido, correo) VALUES ('Luis',   'Martinez','luis.martinez@unicauca.edu.co');

-- ===========================================
-- DOCENTES (hereda de persona)
-- OJO: aqui si debes usar los mismos IDs que en persona
-- ===========================================
-- Docente 1 (persona id=1) con oficina 1
INSERT INTO docente (id, oficina_id) VALUES (1, 1);
-- Docente 2 (persona id=2) con oficina 2
INSERT INTO docente (id, oficina_id) VALUES (2, 2);

-- ===========================================
-- ADMINISTRATIVO (hereda de persona)
-- tambien requiere mantener el id de persona
-- ===========================================
INSERT INTO administrativo (id, rol) VALUES (3, 'Secretario Academico');

-- ===========================================
-- ASIGNATURAS
-- ===========================================
INSERT INTO asignatura (nombre, codigo) VALUES ('Arquitectura Empresarial', 'ASI-101');
INSERT INTO asignatura (nombre, codigo) VALUES ('Bases de Datos II',         'ASI-102');

-- ===========================================
-- ESPACIOS FISICOS
-- ===========================================
INSERT INTO espacio_fisico (nombre, capacidad) VALUES ('Aula Tulcan 101', 40);
INSERT INTO espacio_fisico (nombre, capacidad) VALUES ('Laboratorio Redes 2', 28);

-- ===========================================
-- CURSOS (requiere asignatura)
-- ===========================================
INSERT INTO curso (nombre, asignatura_id) VALUES ('Arquitectura Empresarial - Grupo A', 1);
INSERT INTO curso (nombre, asignatura_id) VALUES ('Bases de Datos II - Grupo B',        2);

-- ===========================================
-- RELACION CURSO–DOCENTE (N:M)
-- (requiere curso y docente existentes)
-- ===========================================
INSERT INTO curso_docente (curso_id, docente_id) VALUES (1, 1);
INSERT INTO curso_docente (curso_id, docente_id) VALUES (1, 2);
INSERT INTO curso_docente (curso_id, docente_id) VALUES (2, 2);

-- ===========================================
-- FRANJAS HORARIAS (requiere curso y espacio)
-- ===========================================
INSERT INTO franja_horaria (dia, hora_inicio, hora_fin, curso_id, espacio_fisico_id) VALUES ('LUNES', '08:00:00', '10:00:00', 1, 1);
INSERT INTO franja_horaria (dia, hora_inicio, hora_fin, curso_id, espacio_fisico_id) VALUES ('MIERCOLES', '10:00:00', '12:00:00', 1, 2);
INSERT INTO franja_horaria (dia, hora_inicio, hora_fin, curso_id, espacio_fisico_id) VALUES ('VIERNES', '14:00:00', '16:00:00', 2, 1);
