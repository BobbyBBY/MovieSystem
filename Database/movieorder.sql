-- MySQL dump 10.13  Distrib 5.7.22, for Win64 (x86_64)
--
-- Host: 39.105.17.143    Database: movieorder
-- ------------------------------------------------------
-- Server version	5.5.43-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audience`
--

DROP TABLE IF EXISTS `audience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audience` (
  `audience_phone` varchar(11) CHARACTER SET latin1 NOT NULL COMMENT '观众手机号\n',
  `audience_vipstatus` int(11) NOT NULL COMMENT '会员状态\n1：非会员\n2：会员',
  `vip_id` int(11) DEFAULT NULL COMMENT '会员id\n外键\n',
  PRIMARY KEY (`audience_phone`),
  KEY `audiencevip_fk_idx` (`vip_id`),
  CONSTRAINT `audiencevip_fk` FOREIGN KEY (`vip_id`) REFERENCES `vip` (`vip_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='观众\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audience`
--

LOCK TABLES `audience` WRITE;
/*!40000 ALTER TABLE `audience` DISABLE KEYS */;
INSERT INTO `audience` VALUES ('18888888888',1,0);
/*!40000 ALTER TABLE `audience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `harddisk`
--

DROP TABLE IF EXISTS `harddisk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `harddisk` (
  `harddisk_id` varchar(8) CHARACTER SET latin1 NOT NULL COMMENT '硬盘id\ns/n序列号',
  `harddisk_filmstudio` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '电影公司',
  `harddisk_decryptiontime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最早解密时间',
  `harddisk_validityduration` int(11) NOT NULL COMMENT '有效期时长\n单位秒',
  `harddisk_expirationtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '到期时间',
  `harddisk_password` varchar(45) CHARACTER SET latin1 NOT NULL COMMENT '解密密钥',
  PRIMARY KEY (`harddisk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='硬盘';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `harddisk`
--

LOCK TABLES `harddisk` WRITE;
/*!40000 ALTER TABLE `harddisk` DISABLE KEYS */;
INSERT INTO `harddisk` VALUES ('123','北京电影文化公司','2019-06-09 16:00:00',2592000,'2019-07-09 16:00:00','qwer');
/*!40000 ALTER TABLE `harddisk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie` (
  `movie_id` varchar(12) CHARACTER SET latin1 NOT NULL COMMENT '电影id\n3位地区国家码+1位影片介质+4位流水号+4位年份\n排次号、影片编码、影片排次号\nhttps://baike.baidu.com/item/%e6%8e%92%e6%ac%a1%e5%8f%b7/4897950?fr=aladdin',
  `harddisk_id` varchar(8) CHARACTER SET latin1 NOT NULL COMMENT '硬盘id',
  `movie_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '电影名称',
  `movie_doubanid` varchar(8) CHARACTER SET latin1 DEFAULT NULL COMMENT '豆瓣id',
  `movie_onlinetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上线时间',
  `movie_offlinetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '下线时间',
  `movie_introduction` text COLLATE utf8_bin COMMENT '简介',
  `movie_duration` int(11) NOT NULL COMMENT '播放时长\n单位秒',
  `movie_status` int(11) NOT NULL COMMENT '电影状态\n1：未上线\n2：已上线\n3：已下线\n4：永久关闭',
  PRIMARY KEY (`movie_id`),
  KEY `f_idx` (`harddisk_id`),
  CONSTRAINT `movieharddisk_fk` FOREIGN KEY (`harddisk_id`) REFERENCES `harddisk` (`harddisk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='电影';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
INSERT INTO `movie` VALUES ('001300012019','123','追龙2','30175306','2019-06-09 16:00:00','2019-07-09 16:00:00','导演: 王晶 / 关智耀',103,2),('001300022019','123','阿拉丁','26891256','2019-06-09 16:00:00','2019-07-09 16:00:00','导演: 盖·里奇',128,2),('001300032019','123','千与千寻','1291561','2019-06-20 16:00:00','2019-07-09 16:00:00','导演: 宫崎骏',125,1),('001300042019','123','追龙','26425068','2019-06-09 16:00:00','2019-06-10 16:00:00','导演: 王晶 / 关智耀',128,3);
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordertable`
--

DROP TABLE IF EXISTS `ordertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordertable` (
  `order_id` varchar(30) CHARACTER SET latin1 NOT NULL COMMENT '订单id\n14位日期时间+4位低截断场次号+4位低截断座位号+8位随机码\n',
  `audience_phone` varchar(11) CHARACTER SET latin1 NOT NULL COMMENT '观众手机号\n外键\n',
  `order_generatedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单生成时间\n',
  `order_totalprice` double NOT NULL COMMENT '订单总价\\\\n',
  `order_status` int(11) NOT NULL COMMENT '订单状态\n1：未付款\n2：未取票\n3：已取票\n4：已取消\n5：其他异常',
  PRIMARY KEY (`order_id`),
  KEY `orderaudience_fk_idx` (`audience_phone`),
  CONSTRAINT `orderaudience_fk` FOREIGN KEY (`audience_phone`) REFERENCES `audience` (`audience_phone`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordertable`
--

LOCK TABLES `ordertable` WRITE;
/*!40000 ALTER TABLE `ordertable` DISABLE KEYS */;
INSERT INTO `ordertable` VALUES ('1','18888888888','0000-00-00 00:00:00',60.5,2),('201906152346206671618019257772','18888888888','2019-06-15 15:46:20',80,1),('201906160100007611001907422711','18888888888','2019-06-15 17:00:00',60,1),('201906161022137882929299056244','18888888888','2019-06-16 02:22:13',60,1),('201906161022177739683482622576','18888888888','2019-06-16 02:22:17',60,1),('201906161022211772069507783746','18888888888','2019-06-16 02:22:21',30,1),('201906161023115383941692770648','18888888888','2019-06-16 02:23:11',30,1),('201906161023290629712165532686','18888888888','2019-06-16 02:23:29',30,1),('201906161026582346017826910254','18888888888','2019-06-16 02:26:58',30,1),('201906161036418774223971050825','18888888888','2019-06-16 02:36:41',60,1);
/*!40000 ALTER TABLE `ordertable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screening`
--

DROP TABLE IF EXISTS `screening`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screening` (
  `screening_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '场次id\n自增\n',
  `movie_id` varchar(12) CHARACTER SET latin1 NOT NULL COMMENT '电影id\n外键\n',
  `screeningroom_id` int(11) NOT NULL COMMENT '影厅id\n外键\n',
  `screening_price` double NOT NULL COMMENT '价格\\\\n',
  `screening_starttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间\n',
  `screening_specialeffect` int(11) NOT NULL COMMENT '电影特效\n1：2d\n\n2：3d\n',
  PRIMARY KEY (`screening_id`),
  KEY `screeningmovie_fk_idx` (`movie_id`),
  KEY `screeningscreeningroom_fk_idx` (`screeningroom_id`),
  CONSTRAINT `screeningmovie_fk` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `screeningscreeningroom_fk` FOREIGN KEY (`screeningroom_id`) REFERENCES `screeningroom` (`screeningroom_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='场次';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screening`
--

LOCK TABLES `screening` WRITE;
/*!40000 ALTER TABLE `screening` DISABLE KEYS */;
INSERT INTO `screening` VALUES (1,'001300012019',1,22.5,'2019-06-15 08:00:00',1),(2,'001300012019',1,25,'2019-06-15 08:30:00',1),(3,'001300012019',1,26,'2019-06-15 09:00:00',1),(4,'001300012019',1,27,'2019-06-14 09:30:00',1),(5,'001300012019',2,30,'2019-06-14 10:00:00',1),(6,'001300012019',2,40,'2019-06-14 10:20:00',1),(7,'001300012019',2,40.5,'2019-06-14 10:40:00',1),(8,'001300012019',2,40,'2019-06-14 11:00:00',1),(9,'001300012019',1,40,'2019-06-15 15:59:00',1),(10,'001300012019',1,30,'2019-06-16 11:00:00',1),(11,'001300012019',1,25,'2019-06-16 14:00:00',1),(12,'001300012019',1,25.5,'2019-06-15 15:00:00',1),(13,'001300022019',1,30,'2019-06-14 11:00:00',1),(14,'001300022019',2,30,'2019-06-14 12:00:00',1),(15,'001300022019',1,40,'2019-06-14 13:00:00',1),(16,'001300022019',2,40,'2019-06-14 14:00:00',1),(17,'001300032019',1,33.5,'2019-06-21 11:00:00',2),(18,'001300032019',2,34.5,'2019-06-21 12:00:00',2),(19,'001300032019',1,50,'2019-06-21 13:00:00',2),(20,'001300032019',2,30,'2019-06-21 14:00:00',2);
/*!40000 ALTER TABLE `screening` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screeningroom`
--

DROP TABLE IF EXISTS `screeningroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screeningroom` (
  `screeningroom_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '影厅id\n自增\n',
  `screeningroom_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '影厅名称\\\\n',
  `screeningroom_status` int(11) NOT NULL COMMENT '影厅状态\\n1：正常\\n2：维修中，暂时无法使用\\n3：永久关闭',
  PRIMARY KEY (`screeningroom_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='影厅';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screeningroom`
--

LOCK TABLES `screeningroom` WRITE;
/*!40000 ALTER TABLE `screeningroom` DISABLE KEYS */;
INSERT INTO `screeningroom` VALUES (1,'一号厅（商业银行厅）',1),(2,'二号厅（梅花厅）',1);
/*!40000 ALTER TABLE `screeningroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seat` (
  `seat_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '座位id\n自增\n',
  `screeningroom_id` int(11) NOT NULL COMMENT '影厅id\n外键\n',
  `seat_location` varchar(12) CHARACTER SET latin1 NOT NULL COMMENT '位置编号\n4位填充影厅id+2位总行数+2位总列数+2位行数+2位列数\n',
  `seat_name` varchar(45) COLLATE utf8_bin NOT NULL COMMENT '座位名称\\n',
  `seat_status` int(11) NOT NULL COMMENT '座位状态\n1：正常\n2：维修中，暂时无法使用\n3：永久关闭',
  PRIMARY KEY (`seat_id`),
  KEY `seatscreeningroom_fg_idx` (`screeningroom_id`),
  CONSTRAINT `seatscreeningroom_fg` FOREIGN KEY (`screeningroom_id`) REFERENCES `screeningroom` (`screeningroom_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='座位\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES (1,1,'000103040102','第一排2号',1),(2,1,'000103040103','第一排3号',1),(3,1,'000103040201','第二排1号',1),(4,1,'000103040202','第二排2号',1),(5,1,'000103040203','第二排3号',1),(6,1,'000103040204','第二排4号',1),(7,1,'000103040301','第三排1号',1),(8,1,'000103040302','第三排2号',1),(9,1,'000103040303','第三排3号',1),(10,1,'000103040304','第三排4号',1),(11,2,'000203040102','第一排2号',1),(12,2,'000203040103','第一排3号',1),(13,2,'000203040202','第二排2号',1),(14,2,'000203040203','第二排3号',1),(15,2,'000203040301','第三排1号',1),(16,2,'000203040302','第三排2号',1),(17,2,'000203040303','第三排3号',1),(18,2,'000203040304','第三排4号',1);
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `ticket_id` varchar(20) CHARACTER SET latin1 NOT NULL COMMENT '电影票id\n8位日期+4位低截断场次号+4位低截断座位号+4位随机码\n',
  `seat_id` int(11) NOT NULL COMMENT '座位id\n外键\n',
  `screening_id` int(11) NOT NULL COMMENT '场次id\n外键\n',
  `order_id` varchar(30) CHARACTER SET latin1 NOT NULL COMMENT '订单id\n外键\n',
  `ticket_status` int(11) NOT NULL COMMENT '电影票状态\n1：未取票\n2：已取票\n3：已取消',
  PRIMARY KEY (`ticket_id`),
  KEY `tickekseat_fk_idx` (`seat_id`),
  KEY `ticketcreening_fk_idx` (`screening_id`),
  KEY `ticketorder_fk_idx` (`order_id`),
  CONSTRAINT `tickekseat_fk` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ticketcreening_fk` FOREIGN KEY (`screening_id`) REFERENCES `screening` (`screening_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ticketorder_fk` FOREIGN KEY (`order_id`) REFERENCES `ordertable` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='电影票';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES ('1',1,1,'1',1),('20190615000100045267',4,1,'201906152346206671618019257772',1),('20190615000100058860',5,1,'201906152346206671618019257772',1),('20190616000100088825',8,1,'201906160100007611001907422711',1),('20190616000100096739',9,1,'201906160100007611001907422711',1),('20190616000100100723',10,1,'201906161026582346017826910254',1),('20190616001000067113',6,10,'201906161036418774223971050825',1),('20190616001000101636',10,10,'201906161036418774223971050825',1);
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vip`
--

DROP TABLE IF EXISTS `vip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vip` (
  `vip_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员id\n自增\n',
  `vip_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '会员名称\\n',
  PRIMARY KEY (`vip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='会员\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vip`
--

LOCK TABLES `vip` WRITE;
/*!40000 ALTER TABLE `vip` DISABLE KEYS */;
INSERT INTO `vip` VALUES (0,'null');
/*!40000 ALTER TABLE `vip` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-16 10:44:12
