-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: apsirece_java
-- ------------------------------------------------------
-- Server version	8.0.41

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

--
-- Table structure for table `tb_documentos`
--

DROP TABLE IF EXISTS `tb_documentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_documentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nome_arquivo_original` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nome_arquivo_armazenado` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caminho_relativo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo_mime` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tamanho_bytes` bigint DEFAULT NULL,
  `usuario_id` bigint DEFAULT NULL,
  `data_upload` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `data_criacao` date DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `tb_documentos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_documentos`
--

LOCK TABLES `tb_documentos` WRITE;
/*!40000 ALTER TABLE `tb_documentos` DISABLE KEYS */;
INSERT INTO `tb_documentos` VALUES (1,'Documento Teste','CARLOS_576.054.407-15_CARTA.pdf','9f294cbf-c568-4716-9216-96af4841393a.pdf','uploads','application/pdf',116858,NULL,'2026-04-21 16:12:40',NULL,NULL,NULL);
/*!40000 ALTER TABLE `tb_documentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_favoritos`
--

DROP TABLE IF EXISTS `tb_favoritos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_favoritos` (
  `usuario_id` bigint NOT NULL,
  `link_id` bigint NOT NULL,
  PRIMARY KEY (`usuario_id`,`link_id`),
  KEY `link_id` (`link_id`),
  CONSTRAINT `tb_favoritos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tb_favoritos_ibfk_2` FOREIGN KEY (`link_id`) REFERENCES `tb_links` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_favoritos`
--

LOCK TABLES `tb_favoritos` WRITE;
/*!40000 ALTER TABLE `tb_favoritos` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_favoritos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_grupo_membros`
--

DROP TABLE IF EXISTS `tb_grupo_membros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_grupo_membros` (
  `grupo_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`grupo_id`,`usuario_id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `tb_grupo_membros_ibfk_1` FOREIGN KEY (`grupo_id`) REFERENCES `tb_grupos` (`id`),
  CONSTRAINT `tb_grupo_membros_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_grupo_membros`
--

LOCK TABLES `tb_grupo_membros` WRITE;
/*!40000 ALTER TABLE `tb_grupo_membros` DISABLE KEYS */;
INSERT INTO `tb_grupo_membros` VALUES (1,1),(2,1),(1,2);
/*!40000 ALTER TABLE `tb_grupo_membros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_grupos`
--

DROP TABLE IF EXISTS `tb_grupos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_grupos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `criado_por_id` bigint DEFAULT NULL,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `criado_por_id` (`criado_por_id`),
  CONSTRAINT `tb_grupos_ibfk_1` FOREIGN KEY (`criado_por_id`) REFERENCES `tb_usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_grupos`
--

LOCK TABLES `tb_grupos` WRITE;
/*!40000 ALTER TABLE `tb_grupos` DISABLE KEYS */;
INSERT INTO `tb_grupos` VALUES (1,'Geral APS Irecê',1,'2026-05-01 17:28:41'),(2,'Administradores',1,'2026-05-01 20:00:25');
/*!40000 ALTER TABLE `tb_grupos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_links`
--

DROP TABLE IF EXISTS `tb_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_links` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `categoria` enum('SISTEMA','FORMULARIO','LINK_EXTERNO','MANUAL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `contador_cliques` bigint DEFAULT '0',
  `data_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_links`
--

LOCK TABLES `tb_links` WRITE;
/*!40000 ALTER TABLE `tb_links` DISABLE KEYS */;
INSERT INTO `tb_links` VALUES (1,'Sistema de RH','http://rh.intraprev.gov.br','SISTEMA',10,'2026-04-20 23:56:17'),(2,'Formulário de Férias','http://portal.gov/ferias','FORMULARIO',5,'2026-04-20 23:56:17'),(3,'Manual do Servidor','http://portal.gov/manual.pdf','MANUAL',2,'2026-04-20 23:56:17');
/*!40000 ALTER TABLE `tb_links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_mensagens`
--

DROP TABLE IF EXISTS `tb_mensagens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_mensagens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conteudo` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `remetente_id` bigint NOT NULL,
  `destinatario_id` bigint DEFAULT NULL,
  `grupo_id` bigint DEFAULT NULL,
  `lida` tinyint(1) DEFAULT '0',
  `data_envio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `remetente_id` (`remetente_id`),
  KEY `destinatario_id` (`destinatario_id`),
  KEY `grupo_id` (`grupo_id`),
  CONSTRAINT `tb_mensagens_ibfk_1` FOREIGN KEY (`remetente_id`) REFERENCES `tb_usuarios` (`id`),
  CONSTRAINT `tb_mensagens_ibfk_2` FOREIGN KEY (`destinatario_id`) REFERENCES `tb_usuarios` (`id`),
  CONSTRAINT `tb_mensagens_ibfk_3` FOREIGN KEY (`grupo_id`) REFERENCES `tb_grupos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_mensagens`
--

LOCK TABLES `tb_mensagens` WRITE;
/*!40000 ALTER TABLE `tb_mensagens` DISABLE KEYS */;
INSERT INTO `tb_mensagens` VALUES (1,'Olá, joao.servidor',1,2,NULL,0,'2026-05-03 21:30:18'),(2,'Olá, admin',2,1,NULL,0,'2026-05-03 21:30:37'),(3,'Mensagem para o grupo Geral',1,NULL,1,0,'2026-05-03 21:30:49'),(4,'Aparece para os demais, não para quem enviou',1,NULL,1,0,'2026-05-03 21:31:26'),(5,'Voltando à conversa com joao.servidor',1,2,NULL,0,'2026-05-03 21:32:14'),(6,'Escutando',2,1,NULL,0,'2026-05-03 21:32:32'),(7,'João envia mensagem para o grupo Geral',2,NULL,1,0,'2026-05-03 21:33:46'),(8,'Mensagem de admin',1,NULL,1,0,'2026-05-03 21:34:09'),(9,'Oi, sou joao.servidor enviando mensagem no grupo Geral',2,NULL,1,0,'2026-05-03 21:45:12'),(10,'A mensagem não chegou em tempo real',1,NULL,1,0,'2026-05-03 21:45:23'),(11,'E agora?',2,NULL,1,0,'2026-05-03 21:45:32'),(12,'Nada...',1,NULL,1,0,'2026-05-03 21:45:37'),(13,'Tente mais uma vez',2,NULL,1,0,'2026-05-03 21:46:04'),(14,'Ainda não chegou',1,NULL,1,0,'2026-05-03 21:46:09'),(15,'A mensagem particular chega logo',1,2,NULL,0,'2026-05-03 21:46:23'),(16,'Sim, instataneamente',2,1,NULL,0,'2026-05-03 21:46:31'),(17,'Mensagem privada',1,2,NULL,0,'2026-05-03 22:01:10'),(18,'Chegou imediatamente',2,1,NULL,0,'2026-05-03 22:01:18'),(19,'Olá, admin',2,1,NULL,0,'2026-05-03 22:13:30'),(20,'Olá, joao.servidor',1,2,NULL,0,'2026-05-03 22:13:38'),(21,'Nova mensagem para admin',2,1,NULL,0,'2026-05-03 22:19:36'),(22,'Chegou a mensagem em joao.servidor',1,2,NULL,0,'2026-05-03 22:19:45'),(23,'Conferir se ainda está funcionando em tempo real',1,2,NULL,0,'2026-05-03 22:36:40'),(24,'Sim, chegou imediatamente a mensagem enviada pelo admin para joao.servidor',2,1,NULL,0,'2026-05-03 22:36:58'),(25,'Mensagem particular de admin para joao.servidor',1,2,NULL,0,'2026-05-03 22:52:20'),(26,'Resposta de joao.servidor para admin',2,1,NULL,0,'2026-05-03 22:52:32'),(27,'Olá, joao.servidor!',1,2,NULL,0,'2026-05-03 23:14:57'),(28,'Olá, admin!',2,1,NULL,0,'2026-05-03 23:15:03'),(29,'Boa noite, admin',2,1,NULL,0,'2026-05-03 23:49:07'),(30,'Boa noite!',1,2,NULL,0,'2026-05-03 23:49:17'),(31,'Mensagem joao.servidor para grupo Geral',2,NULL,1,0,'2026-05-03 23:49:33'),(32,'Mensagem de admin para grupo Geral',1,NULL,1,0,'2026-05-03 23:50:03'),(33,'As mensagens não estão aparecendo em tempo real',2,1,NULL,0,'2026-05-03 23:50:31'),(34,'Nova mensagem de admin para joao.servidor',1,2,NULL,0,'2026-05-03 23:53:43');
/*!40000 ALTER TABLE `tb_mensagens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_perfis`
--

DROP TABLE IF EXISTS `tb_perfis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_perfis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_perfis`
--

LOCK TABLES `tb_perfis` WRITE;
/*!40000 ALTER TABLE `tb_perfis` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_perfis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_servidores`
--

DROP TABLE IF EXISTS `tb_servidores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_servidores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint DEFAULT NULL,
  `nome_completo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `matricula` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `cargo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status_afastamento` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_inicio_afastamento` date DEFAULT NULL,
  `data_fim_afastamento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_id` (`usuario_id`),
  UNIQUE KEY `matricula` (`matricula`),
  CONSTRAINT `tb_servidores_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_servidores`
--

LOCK TABLES `tb_servidores` WRITE;
/*!40000 ALTER TABLE `tb_servidores` DISABLE KEYS */;
INSERT INTO `tb_servidores` VALUES (1,1,'Administrador do Sistema','99901','1990-01-01','Desenvolvedor/TI','ATIVO',NULL,NULL),(2,2,'João Servidor da Silva','99902','1985-05-15','Técnico Administrativo','ATIVO',NULL,NULL);
/*!40000 ALTER TABLE `tb_servidores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_usuario_perfis`
--

DROP TABLE IF EXISTS `tb_usuario_perfis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_usuario_perfis` (
  `usuario_id` bigint NOT NULL,
  `perfil_id` bigint NOT NULL,
  PRIMARY KEY (`usuario_id`,`perfil_id`),
  KEY `perfil_id` (`perfil_id`),
  CONSTRAINT `tb_usuario_perfis_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tb_usuario_perfis_ibfk_2` FOREIGN KEY (`perfil_id`) REFERENCES `tb_perfis` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_usuario_perfis`
--

LOCK TABLES `tb_usuario_perfis` WRITE;
/*!40000 ALTER TABLE `tb_usuario_perfis` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_usuario_perfis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_usuarios`
--

DROP TABLE IF EXISTS `tb_usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ativo` int DEFAULT NULL,
  `online` int DEFAULT NULL,
  `avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'default-avatar.png',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_usuarios`
--

LOCK TABLES `tb_usuarios` WRITE;
/*!40000 ALTER TABLE `tb_usuarios` DISABLE KEYS */;
INSERT INTO `tb_usuarios` VALUES (1,'admin','{noop}123456','admin@inss.gov.br',1,0,'avatar_feminino.png','ADMIN','2026-05-01 17:28:24'),(2,'joao.servidor','{noop}123456','joao.servidor@inss.gov.br',1,0,'avatar_masculino.png','USER','2026-05-01 17:28:24');
/*!40000 ALTER TABLE `tb_usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'apsirece_java'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-04 18:19:18
