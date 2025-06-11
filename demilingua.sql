-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-06-2025 a las 13:20:32
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `demilingua`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `curso`
--

CREATE TABLE `curso` (
  `id` int(11) NOT NULL,
  `idioma_id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `dificultad` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `curso`
--

INSERT INTO `curso` (`id`, `idioma_id`, `nombre`, `descripcion`, `dificultad`) VALUES
(1, 2, 'Inglés Básico', 'Introducción al idioma inglés: gramática elemental, vocabulario cotidiano y práctica de conversación.', 'Básico'),
(2, 3, 'Francés Intermedio', 'Refuerzo de gramática, lectura de textos sencillos y diálogo en situaciones habituales.', 'Intermedio'),
(3, 1, 'Español Avanzado', 'Expresiones idiomáticas, estilo formal y redacción de textos complejos.', 'Avanzado'),
(4, 2, 'Inglés Conversacional', 'Curso enfocado en la práctica de conversación diaria: diálogos, pronunciación y situaciones cotidianas.', 'Intermedio'),
(5, 2, 'Business English', 'Inglés orientado a entornos empresariales: redacción de correos formales, presentaciones y vocabulario ejecutivo.', 'Avanzado'),
(6, 2, 'Inglés para Exámenes', 'Preparación intensiva para el examen TOEFL: comprensión lectora, expresión oral, escritura y audición.', 'Avanzado'),
(7, 2, 'Inglés para Niños', 'Curso lúdico de introducción al inglés para niños de 6 a 10 años: juegos, canciones y actividades interactivas.', 'Básico'),
(8, 3, 'Francés para Viajeros', 'Curso básico para viajeros: saludos, pedir direcciones, vocabulario de restaurantes y escenarios turísticos.', 'Básico'),
(9, 3, 'Francés Conversacional', 'Práctica de conversación diaria en francés: role-plays, ejercicios de escucha y fluidez oral.', 'Intermedio'),
(10, 3, 'Francés de Negocios', 'Vocabulario y expresiones en francés para entornos profesionales: negociaciones, reuniones y correos formales.', 'Avanzado'),
(11, 3, 'Gramática Avanzada de Francés', 'Profundización en estructuras gramaticales complejas: subjuntivo, concordancia de tiempos y voz pasiva.', 'Avanzado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ejercicio`
--

CREATE TABLE `ejercicio` (
  `id` int(11) NOT NULL,
  `test_id` int(11) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `puntuacion` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ejercicio`
--

INSERT INTO `ejercicio` (`id`, `test_id`, `tipo`, `puntuacion`) VALUES
(1, 1, 'Opción múltiple', 10),
(2, 1, 'Verdadero / Falso', 5),
(3, 1, 'Completar frases', 8),
(4, 2, 'Opción múltiple', 10),
(6, 2, 'Emparejar vocabulario', 7),
(7, 3, 'Completar frases', 10),
(8, 3, 'Emparejar sinónimos', 8),
(9, 3, 'Ordenar oraciones', 7),
(10, 4, 'Opción múltiple', 10),
(11, 4, 'Verdadero / Falso', 5),
(12, 5, 'Emparejar vocabulario', 8),
(13, 5, 'Completar frases', 8),
(14, 6, 'Opción múltiple', 10),
(15, 6, 'Completar frases', 8),
(16, 7, 'Opción múltiple', 10),
(17, 7, 'Emparejar sinónimos', 7),
(18, 8, 'Verdadero / Falso', 5),
(19, 8, 'Completar frases', 10),
(20, 9, 'Opción múltiple', 10),
(21, 9, 'Ordenar oraciones', 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `idioma`
--

CREATE TABLE `idioma` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `idioma`
--

INSERT INTO `idioma` (`id`, `nombre`) VALUES
(1, 'Español'),
(2, 'Ingles'),
(3, 'Frances');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `objeto_ejercicio`
--

CREATE TABLE `objeto_ejercicio` (
  `id` int(11) NOT NULL,
  `ejercicio_id` int(11) NOT NULL,
  `contenido` text NOT NULL,
  `respuesta_correcta` text NOT NULL,
  `opciones` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`opciones`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `objeto_ejercicio`
--

INSERT INTO `objeto_ejercicio` (`id`, `ejercicio_id`, `contenido`, `respuesta_correcta`, `opciones`) VALUES
(1, 1, 'Selecciona el saludo correcto para la mañana:', 'Good morning', '[\"Good night\",\"Good morning\",\"Good bye\",\"Good evening\"]'),
(2, 2, 'The plural of \"child\" is \"childs\".', 'Falso', '[\"Verdadero\",\"Falso\"]'),
(3, 3, 'I ____ a student.', 'am', NULL),
(4, 4, 'Choose the best reply:\n— “How are you?”', 'I’m fine, thanks.', '[\"I’m fine, thanks.\",\"See you soon.\",\"Good night.\",\"Two o’clock\"]'),
(5, 6, 'Relaciona las palabras con su traducción:\n1. Table  2. Chair  3. Door', '1-Mesa, 2-Silla, 3-Puerta', NULL),
(6, 7, 'Complete:\n“He ____ to school every day.”', 'goes', NULL),
(7, 8, 'Empareja sinónimos:\n1. Big  2. Happy  3. Fast', '1-Large, 2-Glad, 3-Quick', NULL),
(8, 9, 'Ordena las palabras para formar una oración:\n“tomorrow / see / I / you / will”', 'I will see you tomorrow', NULL),
(9, 10, 'Choose the correct phrasal verb for \"meet\":', 'meet up', '[\"meet up\",\"come across\",\"run into\",\"get by\"]'),
(10, 11, '“Role-plays improve fluency.”', 'Verdadero', '[\"Verdadero\",\"Falso\"]'),
(11, 12, 'Match the business terms:\n1. ROI  2. KPI  3. CRM', '1-Return on Investment, 2-Key Performance Indicator, 3-Customer Relationship Management', NULL),
(12, 13, 'The report ____ by Friday.', 'must be finished', NULL),
(13, 14, 'Select the best synonym for “efficient”:', 'effective', '[\"effective\",\"sufficient\",\"expensive\",\"redundant\"]'),
(14, 15, 'Complete:\n“He ____ the targets last quarter.”', 'exceeded', NULL),
(15, 16, 'Choose the correct translation for “equity” (finanzas):', 'patrimonio', '[\"equidad\",\"patrimonio\",\"liquidez\",\"crédito\"]'),
(16, 17, 'Match synonyms:\n1. Profit  2. Expense  3. Revenue', '1-Gain, 2-Cost, 3-Income', NULL),
(17, 18, '“A pitch deck must always have 20 slides.”', 'Falso', '[\"Verdadero\",\"Falso\"]'),
(18, 19, 'Fill in:\n“Our value proposition ____ customer pain points.”', 'solves', NULL),
(19, 20, 'Select the most formal email closing:', 'Kind regards,', '[\"Cheers,\",\"Later,\",\"Kind regards,\",\"See ya,\"]'),
(20, 21, 'Re-order to form a sentence:\n“draft / send / please / the / email”', 'Please send the draft email', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `test`
--

CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `curso_id` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `test`
--

INSERT INTO `test` (`id`, `curso_id`, `titulo`) VALUES
(1, 1, 'Quiz Vocabulario Inicial'),
(2, 1, 'Examen Unidad 1: Gramática Básica'),
(3, 1, 'Evaluación de Pronunciación A1'),
(4, 4, 'Role-play: Situaciones Diarias'),
(5, 4, 'Listening Test – Conversaciones Telefónicas'),
(6, 4, 'Examen Unidad 2: Fluidez y Expresiones'),
(7, 5, 'Quiz Terminología Empresarial'),
(8, 5, 'Presentaciones: Pitch de Ventas'),
(9, 5, 'Examen Final: Redacción de Correos Formales');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `contrasena` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nombre`, `correo`, `contrasena`) VALUES
(1, 'Juan Pérez', 'juan.perez@example.com', 'securepass123'),
(2, 'María García', 'maria.garcia@example.com', 'mypassword456'),
(3, 'Carlos López', 'carlos.lopez@example.com', 'lopez789'),
(4, 'Ana Martínez', 'ana.martinez@example.com', 'anaPass2023'),
(5, 'Pedro Sánchez', 'pedro.sanchez@example.com', 'sanchezPedro!'),
(6, 'Laura Fernández', 'laura.fernandez@example.com', 'fernandezLaura*'),
(7, 'Miguel Rodríguez', 'miguel.rodriguez@example.com', 'rodriguezMiguel2023'),
(8, 'Sofía Hernández', 'sofia.hernandez@example.com', 'sofiSafePass'),
(9, 'David González', 'david.gonzalez@example.com', 'davidG123'),
(10, 'Elena Díaz', 'elena.diaz@example.com', 'diazElena!456'),
(11, 'ggg', 'ggg@example.com', 'ggg123');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_idioma`
--

CREATE TABLE `usuario_idioma` (
  `usuario_id` int(11) NOT NULL,
  `idioma_id` int(11) NOT NULL,
  `puntos` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `curso`
--
ALTER TABLE `curso`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idioma_id` (`idioma_id`);

--
-- Indices de la tabla `ejercicio`
--
ALTER TABLE `ejercicio`
  ADD PRIMARY KEY (`id`),
  ADD KEY `clase_id` (`test_id`);

--
-- Indices de la tabla `idioma`
--
ALTER TABLE `idioma`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `objeto_ejercicio`
--
ALTER TABLE `objeto_ejercicio`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ejercicio_id` (`ejercicio_id`);

--
-- Indices de la tabla `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`),
  ADD KEY `curso_id` (`curso_id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Indices de la tabla `usuario_idioma`
--
ALTER TABLE `usuario_idioma`
  ADD PRIMARY KEY (`usuario_id`,`idioma_id`),
  ADD KEY `idioma_id` (`idioma_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `curso`
--
ALTER TABLE `curso`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `ejercicio`
--
ALTER TABLE `ejercicio`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `idioma`
--
ALTER TABLE `idioma`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `objeto_ejercicio`
--
ALTER TABLE `objeto_ejercicio`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `test`
--
ALTER TABLE `test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `curso`
--
ALTER TABLE `curso`
  ADD CONSTRAINT `curso_ibfk_1` FOREIGN KEY (`idioma_id`) REFERENCES `idioma` (`id`);

--
-- Filtros para la tabla `ejercicio`
--
ALTER TABLE `ejercicio`
  ADD CONSTRAINT `ejercicio_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`);

--
-- Filtros para la tabla `objeto_ejercicio`
--
ALTER TABLE `objeto_ejercicio`
  ADD CONSTRAINT `objeto_ejercicio_ibfk_1` FOREIGN KEY (`ejercicio_id`) REFERENCES `ejercicio` (`id`);

--
-- Filtros para la tabla `test`
--
ALTER TABLE `test`
  ADD CONSTRAINT `test_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id`);

--
-- Filtros para la tabla `usuario_idioma`
--
ALTER TABLE `usuario_idioma`
  ADD CONSTRAINT `usuario_idioma_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `usuario_idioma_ibfk_2` FOREIGN KEY (`idioma_id`) REFERENCES `idioma` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
