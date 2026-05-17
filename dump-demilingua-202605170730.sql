-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: mysql-f84f66a-demilingua-db.e.aivencloud.com    Database: demilingua
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '39482a06-51b0-11f1-94e4-1a28a79dfef4:1-15,
4ce6877f-3a78-11f1-ab8e-9acd05c639f2:1-28,
83586ace-49c6-11f1-8515-72a02aee548c:1-33,
98470d44-506d-11f1-95c1-ba9a8882d5fb:1-41,
f8899c64-3f32-11f1-8230-c2f75c7e4c03:1-26';

--
-- Table structure for table `amistad`
--

DROP TABLE IF EXISTS `amistad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amistad` (
  `usuario_id_1` int NOT NULL,
  `usuario_id_2` int NOT NULL,
  `fecha_solicitud` datetime DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('PENDIENTE','ACEPTADO') COLLATE utf8mb4_general_ci DEFAULT 'PENDIENTE',
  PRIMARY KEY (`usuario_id_1`,`usuario_id_2`),
  KEY `fk_amistad_user2` (`usuario_id_2`),
  CONSTRAINT `fk_amistad_user1` FOREIGN KEY (`usuario_id_1`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_amistad_user2` FOREIGN KEY (`usuario_id_2`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amistad`
--

LOCK TABLES `amistad` WRITE;
/*!40000 ALTER TABLE `amistad` DISABLE KEYS */;
INSERT INTO `amistad` VALUES (1,2,'2026-05-07 15:37:48','ACEPTADO'),(1,4,'2026-05-07 15:37:48','ACEPTADO'),(3,1,'2026-05-07 15:37:48','PENDIENTE'),(5,2,'2026-05-07 15:37:48','ACEPTADO');
/*!40000 ALTER TABLE `amistad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso`
--

DROP TABLE IF EXISTS `curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curso` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idioma_id` int NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_general_ci,
  `dificultad` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idioma_id` (`idioma_id`),
  CONSTRAINT `curso_ibfk_1` FOREIGN KEY (`idioma_id`) REFERENCES `idioma` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` VALUES (1,1,'Inglés Básico','Aprende lo esencial: saludos, números y colores.','Fácil'),(2,1,'Inglés Intermedio','Tiempos verbales, viajes y rutinas diarias.','Media'),(3,1,'Inglés para Negocios','Vocabulario profesional y reuniones.','Difícil'),(4,2,'Francés Básico','Primeros pasos en la lengua del amor.','Fácil'),(5,2,'Francés Intermedio','Mejora tu fluidez y pronunciación.','Media'),(6,3,'Alemán Básico','Fundamentos y declinaciones iniciales.','Fácil'),(7,4,'Italiano para Viajeros','Pide comida, direcciones y transporte.','Fácil'),(8,5,'Portugués de Brasil','Cultura, expresiones y gramática.','Media'),(14,1,'Phrasal Verbs y Modismos','Domina las expresiones nativas más comunes en inglés.','Difícil'),(15,2,'Francés para Negocios','Vocabulario formal, redacción de emails y reuniones.','Difícil'),(16,3,'Alemán para Viajeros','Supervivencia en el aeropuerto, estación de tren y hotel.','Media'),(17,4,'Italiano Intermedio','Tiempos pasados, uso del condicional y vocabulario de arte.','Media'),(18,5,'Portugués Avanzado','Literatura, política, debates y noticias de actualidad.','Difícil');
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `division` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `xp_minimo` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
INSERT INTO `division` VALUES (1,'Bronce',0),(2,'Plata',500),(3,'Oro',1500),(4,'Platino',3000),(5,'Diamante',5000);
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ejercicio`
--

DROP TABLE IF EXISTS `ejercicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ejercicio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `test_id` int NOT NULL,
  `tipo` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `puntuacion` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `clase_id` (`test_id`),
  CONSTRAINT `ejercicio_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ejercicio`
--

LOCK TABLES `ejercicio` WRITE;
/*!40000 ALTER TABLE `ejercicio` DISABLE KEYS */;
INSERT INTO `ejercicio` VALUES (1,1,'opcion_multiple',10),(2,1,'traduccion',15),(3,1,'completar',10),(4,2,'opcion_multiple',10),(5,2,'traduccion',15),(6,4,'completar',20),(7,4,'opcion_multiple',10),(8,6,'opcion_multiple',10),(9,6,'traduccion',15),(10,8,'opcion_multiple',10),(11,9,'traduccion',20),(31,21,'opcion_multiple',10),(32,21,'traduccion',15),(33,21,'completar',10),(34,22,'traduccion',20),(35,22,'opcion_multiple',15),(36,23,'opcion_multiple',10),(37,23,'completar',15),(38,24,'traduccion',20),(39,25,'opcion_multiple',10),(40,25,'traduccion',15),(41,26,'completar',15),(42,26,'opcion_multiple',10),(43,27,'completar',15),(44,27,'opcion_multiple',10),(45,28,'traduccion',20),(46,29,'opcion_multiple',15),(47,29,'traduccion',20),(48,30,'completar',15);
/*!40000 ALTER TABLE `ejercicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idioma`
--

DROP TABLE IF EXISTS `idioma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idioma` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idioma`
--

LOCK TABLES `idioma` WRITE;
/*!40000 ALTER TABLE `idioma` DISABLE KEYS */;
INSERT INTO `idioma` VALUES (1,'Inglés'),(2,'Francés'),(3,'Alemán'),(4,'Italiano'),(5,'Portugués');
/*!40000 ALTER TABLE `idioma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objeto_ejercicio`
--

DROP TABLE IF EXISTS `objeto_ejercicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `objeto_ejercicio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ejercicio_id` int NOT NULL,
  `contenido` text COLLATE utf8mb4_general_ci NOT NULL,
  `respuesta_correcta` text COLLATE utf8mb4_general_ci NOT NULL,
  `opciones` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  PRIMARY KEY (`id`),
  KEY `ejercicio_id` (`ejercicio_id`),
  CONSTRAINT `objeto_ejercicio_ibfk_1` FOREIGN KEY (`ejercicio_id`) REFERENCES `ejercicio` (`id`),
  CONSTRAINT `objeto_ejercicio_chk_1` CHECK (json_valid(`opciones`))
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_ejercicio`
--

LOCK TABLES `objeto_ejercicio` WRITE;
/*!40000 ALTER TABLE `objeto_ejercicio` DISABLE KEYS */;
INSERT INTO `objeto_ejercicio` VALUES (21,1,'¿Cómo se dice \"Hola\"?','Hello','[\"Hello\", \"Goodbye\", \"Please\", \"Thanks\"]'),(22,2,'Traduce: \"Good morning\"','Buenos días',NULL),(23,3,'Completa: \"How ___ you?\"','are','[\"is\", \"am\", \"are\", \"be\"]'),(24,4,'¿Qué color es \"Blue\"?','Azul','[\"Rojo\", \"Azul\", \"Verde\", \"Amarillo\"]'),(25,5,'Traduce: \"The red car\"','El coche rojo',NULL),(26,6,'Completa: \"Yesterday I ___ to the park\"','went','[\"go\", \"went\", \"going\", \"gone\"]'),(27,7,'¿Qué significa \"Flight\"?','Vuelo','[\"Tren\", \"Vuelo\", \"Coche\", \"Barco\"]'),(28,8,'¿Cómo se dice \"Gracias\" en francés?','Merci','[\"Bonjour\", \"Merci\", \"Oui\", \"Non\"]'),(29,9,'Traduce: \"Je suis un homme\"','Yo soy un hombre',NULL),(30,10,'¿Cómo se dice \"Adiós\" en alemán?','Tschüss','[\"Hallo\", \"Danke\", \"Tschüss\", \"Bitte\"]'),(31,11,'Traduce: \"Vorrei una pizza margherita\"','Quisiera una pizza margarita',NULL),(32,31,'¿Qué significa el phrasal verb \"get over\"?','Superar','[\"Levantarse\", \"Superar\", \"Entender\", \"Rendirse\"]'),(33,32,'Traduce: \"I need to get away for a few days\"','Necesito escaparme unos días',NULL),(34,33,'Completa: \"We should get ___ together soon\"','together','[\"together\", \"along\", \"over\", \"by\"]'),(35,34,'Traduce el modismo: \"It is raining cats and dogs\"','Llueve a cántaros',NULL),(36,35,'¿Qué significa la expresión \"Break a leg\"?','Buena suerte','[\"Rómpete una pierna\", \"Buena suerte\", \"Ten cuidado\", \"Vete\"]'),(37,36,'¿Cómo se despide formalmente un email en francés?','Cordialement','[\"Bisous\", \"Salut\", \"Cordialement\", \"À plus\"]'),(38,37,'Completa: \"Veuillez trouver ___ ma candidature\"','ci-joint','[\"avec\", \"ci-joint\", \"ici\", \"dans\"]'),(39,38,'Traduce: \"Nous devons trouver un accord\"','Debemos llegar a un acuerdo',NULL),(40,39,'¿Cómo se dice \"Andén\" en alemán?','Gleis','[\"Zug\", \"Bahnhof\", \"Gleis\", \"Fahrkarte\"]'),(41,40,'Traduce: \"Der Zug hat Verspätung\"','El tren tiene retraso',NULL),(42,41,'Completa: \"Ich möchte ein ___ reservieren\"','Zimmer','[\"Zug\", \"Zimmer\", \"Essen\", \"Wasser\"]'),(43,42,'¿Qué significa \"Die Rechnung\"?','La factura','[\"La llave\", \"La factura\", \"El equipaje\", \"La cama\"]'),(44,43,'Completa: \"Ieri ___ andato al cinema\"','sono','[\"ho\", \"sono\", \"ha\", \"sei\"]'),(45,44,'¿Cuál es el participio correcto de \"Leggere\"?','Letto','[\"Leggiuto\", \"Letto\", \"Legge\", \"Lettura\"]'),(46,45,'Traduce: \"Questo quadro è bellissimo\"','Este cuadro es hermosísimo',NULL),(47,46,'¿Cómo se dice \"Cambio climático\" en portugués?','Mudanças climáticas','[\"Tempo ruim\", \"Mudanças climáticas\", \"Aquecimento\", \"Chuva\"]'),(48,47,'Traduce: \"Devemos proteger as florestas\"','Debemos proteger los bosques',NULL),(49,48,'Completa: \"Eu li a notícia no ___ de hoje\"','jornal','[\"livro\", \"jornal\", \"revista\", \"caderno\"]'),(50,31,'¿Qué significa el phrasal verb \"get over\"?','Superar','[\"Levantarse\", \"Superar\", \"Entender\", \"Rendirse\"]'),(51,32,'Traduce: \"I need to get away for a few days\"','Necesito escaparme unos días',NULL),(52,33,'Completa: \"We should get ___ together soon\"','together','[\"together\", \"along\", \"over\", \"by\"]'),(53,34,'Traduce el modismo: \"It is raining cats and dogs\"','Llueve a cántaros',NULL),(54,35,'¿Qué significa la expresión \"Break a leg\"?','Buena suerte','[\"Rómpete una pierna\", \"Buena suerte\", \"Ten cuidado\", \"Vete\"]'),(55,36,'¿Cómo se despide formalmente un email en francés?','Cordialement','[\"Bisous\", \"Salut\", \"Cordialement\", \"À plus\"]'),(56,37,'Completa: \"Veuillez trouver ___ ma candidature\"','ci-joint','[\"avec\", \"ci-joint\", \"ici\", \"dans\"]'),(57,38,'Traduce: \"Nous devons trouver un accord\"','Debemos llegar a un acuerdo',NULL),(58,39,'¿Cómo se dice \"Andén\" en alemán?','Gleis','[\"Zug\", \"Bahnhof\", \"Gleis\", \"Fahrkarte\"]'),(59,40,'Traduce: \"Der Zug hat Verspätung\"','El tren tiene retraso',NULL),(60,41,'Completa: \"Ich möchte ein ___ reservieren\"','Zimmer','[\"Zug\", \"Zimmer\", \"Essen\", \"Wasser\"]'),(61,42,'¿Qué significa \"Die Rechnung\"?','La factura','[\"La llave\", \"La factura\", \"El equipaje\", \"La cama\"]'),(62,43,'Completa: \"Ieri ___ andato al cinema\"','sono','[\"ho\", \"sono\", \"ha\", \"sei\"]'),(63,44,'¿Cuál es el participio correcto de \"Leggere\"?','Letto','[\"Leggiuto\", \"Letto\", \"Legge\", \"Lettura\"]'),(64,45,'Traduce: \"Questo quadro è bellissimo\"','Este cuadro es hermosísimo',NULL),(65,46,'¿Cómo se dice \"Cambio climático\" en portugués?','Mudanças climáticas','[\"Tempo ruim\", \"Mudanças climáticas\", \"Aquecimento\", \"Chuva\"]'),(66,47,'Traduce: \"Devemos proteger as florestas\"','Debemos proteger los bosques',NULL),(67,48,'Completa: \"Eu li a notícia no ___ de hoje\"','jornal','[\"livro\", \"jornal\", \"revista\", \"caderno\"]');
/*!40000 ALTER TABLE `objeto_ejercicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racha_diaria`
--

DROP TABLE IF EXISTS `racha_diaria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racha_diaria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `fecha` date NOT NULL,
  `xp_ganado` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_racha_usuario` (`usuario_id`),
  CONSTRAINT `fk_racha_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racha_diaria`
--

LOCK TABLES `racha_diaria` WRITE;
/*!40000 ALTER TABLE `racha_diaria` DISABLE KEYS */;
INSERT INTO `racha_diaria` VALUES (1,1,'2026-05-06',150),(2,1,'2026-05-07',50),(3,2,'2026-05-06',20),(4,4,'2026-05-05',100),(5,4,'2026-05-06',80),(6,4,'2026-05-07',200);
/*!40000 ALTER TABLE `racha_diaria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `id` int NOT NULL AUTO_INCREMENT,
  `curso_id` int NOT NULL,
  `titulo` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `curso_id` (`curso_id`),
  CONSTRAINT `test_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` VALUES (1,1,'Saludos y Despedidas'),(2,1,'Los Colores y Números'),(3,1,'En el Restaurante'),(4,2,'Pasado Simple vs Continuo'),(5,2,'Viajes y Aeropuertos'),(6,4,'Les Salutations (Saludos)'),(7,4,'La Famille (La Familia)'),(8,6,'Begrüßungen (Saludos)'),(9,7,'Pedir una Pizza'),(10,8,'Presentaciones en Portugués'),(21,14,'Phrasal Verbs con \"Get\"'),(22,14,'Idioms de uso diario'),(23,15,'Escribir un Email Formal'),(24,15,'Negociaciones'),(25,16,'En la Estación de Tren'),(26,16,'Reservas y Reclamaciones'),(27,17,'El Pasado Próximo'),(28,17,'Visita al Museo'),(29,18,'Debate sobre el Medio Ambiente'),(30,18,'Noticias y Periódicos'),(31,1,'Testing'),(32,1,'Otro'),(33,1,'Test'),(34,1,'Testing'),(35,1,'Testing'),(36,1,'Tesin');
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `correo` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `contrasena` text COLLATE utf8mb4_general_ci NOT NULL,
  `vidas` int DEFAULT '5',
  `racha_actual` int DEFAULT '0',
  `division_id` int DEFAULT NULL,
  `ultima_recarga` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`),
  KEY `fk_usuario_division` (`division_id`),
  CONSTRAINT `fk_usuario_division` FOREIGN KEY (`division_id`) REFERENCES `division` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'JoelAdmin','admin@demilingua.com','testing',5,12,5,'2026-05-15 18:51:53'),(2,'Laura99','laura@test.com','$2a$10$X/7p9wJ2P6V.B/X1iJ9V.O7Yf4Z7A9h5M9v8L2N8Q5B5V5M2C5',3,2,2,'2026-05-07 15:35:48'),(3,'Carlos_Dev','carlos@test.com','$2a$10$X/7p9wJ2P6V.B/X1iJ9V.O7Yf4Z7A9h5M9v8L2N8Q5B5V5M2C5',5,0,1,'2026-05-01 10:00:00'),(4,'AnaPolyglot','ana@test.com','$2a$10$X/7p9wJ2P6V.B/X1iJ9V.O7Yf4Z7A9h5M9v8L2N8Q5B5V5M2C5',4,45,4,'2026-05-07 15:35:48'),(5,'Miguel_Estudiante','miguel@test.com','$2a$10$X/7p9wJ2P6V.B/X1iJ9V.O7Yf4Z7A9h5M9v8L2N8Q5B5V5M2C5',0,5,3,'2026-05-07 15:35:48'),(12,'Sergio','sergiogrlpz@gmail.com','testing',0,0,1,'2026-05-15 17:24:54');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_idioma`
--

DROP TABLE IF EXISTS `usuario_idioma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_idioma` (
  `usuario_id` int NOT NULL,
  `idioma_id` int NOT NULL,
  `puntos` int NOT NULL,
  PRIMARY KEY (`usuario_id`,`idioma_id`),
  KEY `idioma_id` (`idioma_id`),
  CONSTRAINT `usuario_idioma_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `usuario_idioma_ibfk_2` FOREIGN KEY (`idioma_id`) REFERENCES `idioma` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_idioma`
--

LOCK TABLES `usuario_idioma` WRITE;
/*!40000 ALTER TABLE `usuario_idioma` DISABLE KEYS */;
INSERT INTO `usuario_idioma` VALUES (1,1,5200),(1,2,1500),(2,1,600),(3,3,120),(4,1,3100),(4,4,800),(5,2,1800),(12,1,135),(12,2,20);
/*!40000 ALTER TABLE `usuario_idioma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_test`
--

DROP TABLE IF EXISTS `usuario_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_test` (
  `usuario_id` int NOT NULL,
  `test_id` int NOT NULL,
  `puntuacion` int DEFAULT '0',
  PRIMARY KEY (`usuario_id`,`test_id`),
  KEY `test_id` (`test_id`),
  CONSTRAINT `usuario_test_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `usuario_test_ibfk_2` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_test`
--

LOCK TABLES `usuario_test` WRITE;
/*!40000 ALTER TABLE `usuario_test` DISABLE KEYS */;
INSERT INTO `usuario_test` VALUES (1,1,35),(1,2,25),(1,4,30),(2,1,35),(2,2,10),(4,1,35),(4,4,30),(4,5,20),(5,6,25),(5,7,15),(12,1,35),(12,4,30),(12,24,20);
/*!40000 ALTER TABLE `usuario_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'demilingua'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-17  7:30:55
