-- MySQL dump 10.13  Distrib 5.5.28, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: papermandb
-- ------------------------------------------------------
-- Server version	5.5.28-0ubuntu0.12.10.2

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

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `papermandb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `papermandb`;

--
-- Table structure for table `kendaraan`
--

DROP TABLE IF EXISTS `kendaraan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kendaraan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keterangan` varchar(255) DEFAULT NULL,
  `noLambung` int(11) NOT NULL,
  `noMesin` varchar(255) DEFAULT NULL,
  `noPolisi` varchar(255) DEFAULT NULL,
  `noRangka` varchar(255) DEFAULT NULL,
  `noSTNK` varchar(255) DEFAULT NULL,
  `tglJatuhTempo` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `noLambung` (`noLambung`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kendaraan`
--

LOCK TABLES `kendaraan` WRITE;
/*!40000 ALTER TABLE `kendaraan` DISABLE KEYS */;
INSERT INTO `kendaraan` VALUES (1,'jajal',101,'1K2J3KJ12','123JKH12J','12KJ31J24H','KJ4JKJ4KK4J','2013-01-18');
/*!40000 ALTER TABLE `kendaraan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kendaraan_pth`
--

DROP TABLE IF EXISTS `kendaraan_pth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kendaraan_pth` (
  `id_pth` int(11) NOT NULL AUTO_INCREMENT,
  `keterangan` varchar(255) DEFAULT NULL,
  `noLambung` int(11) NOT NULL,
  `noMesin` varchar(255) DEFAULT NULL,
  `noPolisi` varchar(255) DEFAULT NULL,
  `noRangka` varchar(255) DEFAULT NULL,
  `noSTNK` varchar(255) DEFAULT NULL,
  `tglJatuhTempo` date DEFAULT NULL,
  PRIMARY KEY (`id_pth`),
  UNIQUE KEY `noLambung` (`noLambung`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kendaraan_pth`
--

LOCK TABLES `kendaraan_pth` WRITE;
/*!40000 ALTER TABLE `kendaraan_pth` DISABLE KEYS */;
/*!40000 ALTER TABLE `kendaraan_pth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `komposisi_setoran`
--

DROP TABLE IF EXISTS `komposisi_setoran`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `komposisi_setoran` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nilai_angsuran` decimal(19,2) DEFAULT NULL,
  `nama_komposisi` varchar(255) DEFAULT NULL,
  `nilai_tabungan` decimal(19,2) DEFAULT NULL,
  `id_sistem` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1A19C5E76AD97BE7` (`id_sistem`),
  CONSTRAINT `FK1A19C5E76AD97BE7` FOREIGN KEY (`id_sistem`) REFERENCES `sistem` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `komposisi_setoran`
--

LOCK TABLES `komposisi_setoran` WRITE;
/*!40000 ALTER TABLE `komposisi_setoran` DISABLE KEYS */;
INSERT INTO `komposisi_setoran` VALUES (1,150000.00,'BIRU',25000.00,1);
/*!40000 ALTER TABLE `komposisi_setoran` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `number_factory`
--

DROP TABLE IF EXISTS `number_factory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `number_factory` (
  `id` int(11) NOT NULL,
  `ArmadaType` varchar(255) DEFAULT NULL,
  `TransactionType` varchar(255) DEFAULT NULL,
  `last_number` int(11) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `number_factory`
--

LOCK TABLES `number_factory` WRITE;
/*!40000 ALTER TABLE `number_factory` DISABLE KEYS */;
INSERT INTO `number_factory` VALUES (1,'BIRU','SETORAN',3,'2013-01-23');
/*!40000 ALTER TABLE `number_factory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pengemudi`
--

DROP TABLE IF EXISTS `pengemudi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pengemudi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alamat` varchar(255) DEFAULT NULL,
  `kontrakBulan` int(11) DEFAULT NULL,
  `kontrakHari` int(11) DEFAULT NULL,
  `kota` varchar(255) DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `nrp` varchar(255) NOT NULL,
  `tglMasuk` date DEFAULT NULL,
  `id_ref_kendaraan` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nrp` (`nrp`),
  UNIQUE KEY `id_ref_kendaraan` (`id_ref_kendaraan`),
  KEY `FK4A3A786418B9B4E5` (`id_ref_kendaraan`),
  CONSTRAINT `FK4A3A786418B9B4E5` FOREIGN KEY (`id_ref_kendaraan`) REFERENCES `kendaraan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pengemudi`
--

LOCK TABLES `pengemudi` WRITE;
/*!40000 ALTER TABLE `pengemudi` DISABLE KEYS */;
INSERT INTO `pengemudi` VALUES (1,'SURABAYA',0,1000,'SYURABAYA','WAWAN','2211030115','2013-01-18',1);
/*!40000 ALTER TABLE `pengemudi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pengemudi_pth`
--

DROP TABLE IF EXISTS `pengemudi_pth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pengemudi_pth` (
  `id_pth` int(11) NOT NULL AUTO_INCREMENT,
  `alamat` varchar(255) DEFAULT NULL,
  `kontrakBulan` int(11) DEFAULT NULL,
  `kontrakHari` int(11) DEFAULT NULL,
  `kota` varchar(255) DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `nrp` varchar(255) NOT NULL,
  `tglMasuk` date DEFAULT NULL,
  `id_ref_kendaraan` int(11) NOT NULL,
  PRIMARY KEY (`id_pth`),
  UNIQUE KEY `nrp` (`nrp`),
  UNIQUE KEY `id_ref_kendaraan` (`id_ref_kendaraan`),
  KEY `FK3DEC8889F07B86A3` (`id_ref_kendaraan`),
  CONSTRAINT `FK3DEC8889F07B86A3` FOREIGN KEY (`id_ref_kendaraan`) REFERENCES `kendaraan_pth` (`id_pth`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pengemudi_pth`
--

LOCK TABLES `pengemudi_pth` WRITE;
/*!40000 ALTER TABLE `pengemudi_pth` DISABLE KEYS */;
/*!40000 ALTER TABLE `pengemudi_pth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sec_user`
--

DROP TABLE IF EXISTS `sec_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_add_user` bit(1) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `role_change_bonus` bit(1) DEFAULT NULL,
  `role_change_sistem` bit(1) DEFAULT NULL,
  `role_change_compose` bit(1) DEFAULT NULL,
  `role_do_closing` bit(1) DEFAULT NULL,
  `role_change_saldoAwal` bit(1) DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `user_login` varchar(255) DEFAULT NULL,
  `no_telp` varchar(255) DEFAULT NULL,
  `role_open_closing` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_status` varchar(255) DEFAULT NULL,
  `tglUpdate` date DEFAULT NULL,
  `TTL` varchar(255) DEFAULT NULL,
  `role_view_transaction` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_login` (`user_login`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_user`
--

LOCK TABLES `sec_user` WRITE;
/*!40000 ALTER TABLE `sec_user` DISABLE KEYS */;
INSERT INTO `sec_user` VALUES (1,'','rungkut','','','','','','SUPER USER','root','0318700689','','1234','SUPER_ADMIN','2013-01-14','surabaya, 26 05 1992','');
/*!40000 ALTER TABLE `sec_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sistem`
--

DROP TABLE IF EXISTS `sistem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sistem` (
  `id` int(11) NOT NULL,
  `alamat_perusahaan` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `kota` varchar(255) DEFAULT NULL,
  `nama_perusahaan` varchar(255) DEFAULT NULL,
  `no_telp` varchar(255) DEFAULT NULL,
  `tglKerja` date DEFAULT NULL,
  `last_logged_in` varchar(255) DEFAULT NULL,
  `last_editedBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCA3AFA1FB99B1D59` (`last_editedBy`),
  KEY `FKCA3AFA1F9A16E29` (`last_logged_in`),
  CONSTRAINT `FKCA3AFA1F9A16E29` FOREIGN KEY (`last_logged_in`) REFERENCES `sec_user` (`user_login`),
  CONSTRAINT `FKCA3AFA1FB99B1D59` FOREIGN KEY (`last_editedBy`) REFERENCES `sec_user` (`user_login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sistem`
--

LOCK TABLES `sistem` WRITE;
/*!40000 ALTER TABLE `sistem` DISABLE KEYS */;
INSERT INTO `sistem` VALUES (1,'JL KENDANGSARI NO 57','mandala@gmail.com','SURABAYA','PT MANDALA SATATA GAMA','0315665930','2013-01-23','root',NULL);
/*!40000 ALTER TABLE `sistem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_closing_bulanan`
--

DROP TABLE IF EXISTS `tr_closing_bulanan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_closing_bulanan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `closed_for` varchar(255) DEFAULT NULL,
  `hutang_sld_awal` decimal(19,2) DEFAULT '0.00',
  `periodeBulan` date DEFAULT NULL,
  `ref_setoranKe` int(11) DEFAULT NULL,
  `tglClosing` date DEFAULT NULL,
  `total_angsuran` decimal(19,2) DEFAULT '0.00',
  `total_bayar` decimal(19,2) DEFAULT '0.00',
  `total_cicilan` decimal(19,2) DEFAULT '0.00',
  `total_kasbon` decimal(19,2) DEFAULT '0.00',
  `total_overtime` decimal(19,2) DEFAULT '0.00',
  `total_setoran` decimal(19,2) DEFAULT '0.00',
  `total_tabungan` decimal(19,2) DEFAULT '0.00',
  `user_set` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_closing_bulanan`
--

LOCK TABLES `tr_closing_bulanan` WRITE;
/*!40000 ALTER TABLE `tr_closing_bulanan` DISABLE KEYS */;
INSERT INTO `tr_closing_bulanan` VALUES (1,NULL,NULL,'2013-01-18',NULL,'2013-01-18',0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL),(2,'CLOSING_SALDO_AWAL',NULL,'2013-01-18',3,NULL,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,NULL);
/*!40000 ALTER TABLE `tr_closing_bulanan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_closing_tahunan`
--

DROP TABLE IF EXISTS `tr_closing_tahunan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_closing_tahunan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `closed_for` varchar(255) DEFAULT NULL,
  `periodeAkhir` date DEFAULT NULL,
  `periodeAwal` date DEFAULT NULL,
  `tglClosing` date DEFAULT NULL,
  `total_angsuran` decimal(19,2) DEFAULT '0.00',
  `total_bayar` decimal(19,2) DEFAULT '0.00',
  `total_cicilan` decimal(19,2) DEFAULT '0.00',
  `total_kasbon` decimal(19,2) DEFAULT '0.00',
  `total_overtime` decimal(19,2) DEFAULT '0.00',
  `total_setoran` decimal(19,2) DEFAULT '0.00',
  `total_tabungan` decimal(19,2) DEFAULT '0.00',
  `user_set` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_closing_tahunan`
--

LOCK TABLES `tr_closing_tahunan` WRITE;
/*!40000 ALTER TABLE `tr_closing_tahunan` DISABLE KEYS */;
/*!40000 ALTER TABLE `tr_closing_tahunan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_pth_closing_bulanan`
--

DROP TABLE IF EXISTS `tr_pth_closing_bulanan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_pth_closing_bulanan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `closed_for` varchar(255) DEFAULT NULL,
  `periodeBulan` date DEFAULT NULL,
  `ref_setoranKe` int(11) DEFAULT NULL,
  `tglClosing` date DEFAULT NULL,
  `total_angsuran` decimal(19,2) DEFAULT '0.00',
  `total_bayar` decimal(19,2) DEFAULT '0.00',
  `total_cicilan` decimal(19,2) DEFAULT '0.00',
  `total_kasbon` decimal(19,2) DEFAULT '0.00',
  `total_overtime` decimal(19,2) DEFAULT '0.00',
  `total_setoran` decimal(19,2) DEFAULT '0.00',
  `total_tabungan` decimal(19,2) DEFAULT '0.00',
  `user_set` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_pth_closing_bulanan`
--

LOCK TABLES `tr_pth_closing_bulanan` WRITE;
/*!40000 ALTER TABLE `tr_pth_closing_bulanan` DISABLE KEYS */;
/*!40000 ALTER TABLE `tr_pth_closing_bulanan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_putih_closing_tahunan`
--

DROP TABLE IF EXISTS `tr_putih_closing_tahunan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_putih_closing_tahunan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `closed_for` varchar(255) DEFAULT NULL,
  `periodeAkhir` date DEFAULT NULL,
  `periodeAwal` date DEFAULT NULL,
  `tglClosing` date DEFAULT NULL,
  `total_angsuran` decimal(19,2) DEFAULT '0.00',
  `total_bayar` decimal(19,2) DEFAULT '0.00',
  `total_cicilan` decimal(19,2) DEFAULT '0.00',
  `total_kasbon` decimal(19,2) DEFAULT '0.00',
  `total_overtime` decimal(19,2) DEFAULT '0.00',
  `total_setoran` decimal(19,2) DEFAULT '0.00',
  `total_tabungan` decimal(19,2) DEFAULT '0.00',
  `user_set` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_putih_closing_tahunan`
--

LOCK TABLES `tr_putih_closing_tahunan` WRITE;
/*!40000 ALTER TABLE `tr_putih_closing_tahunan` DISABLE KEYS */;
/*!40000 ALTER TABLE `tr_putih_closing_tahunan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_setoran`
--

DROP TABLE IF EXISTS `tr_setoran`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_setoran` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `closing_status` varchar(255) DEFAULT NULL,
  `setoran_counter` int(11) DEFAULT NULL,
  `jalan_state` varchar(255) DEFAULT NULL,
  `kode_setoran` varchar(255) NOT NULL,
  `tr_status` varchar(255) DEFAULT NULL,
  `tglJatuhTempo` date DEFAULT NULL,
  `tglSPO` date DEFAULT NULL,
  `tglSetoran` date DEFAULT NULL,
  `total_hutang` decimal(19,2) DEFAULT NULL,
  `total_setoran` decimal(19,2) DEFAULT NULL,
  `kode_user` varchar(255) DEFAULT NULL,
  `id_closing_bulan` int(11) DEFAULT NULL,
  `id_closing_tahun` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kode_setoran` (`kode_setoran`),
  KEY `FKBAAD3FF11FE03FDB` (`id_closing_bulan`),
  KEY `FKBAAD3FF1B6B4BA77` (`id_closing_tahun`),
  CONSTRAINT `FKBAAD3FF11FE03FDB` FOREIGN KEY (`id_closing_bulan`) REFERENCES `tr_closing_bulanan` (`id`),
  CONSTRAINT `FKBAAD3FF1B6B4BA77` FOREIGN KEY (`id_closing_tahun`) REFERENCES `tr_closing_tahunan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_setoran`
--

LOCK TABLES `tr_setoran` WRITE;
/*!40000 ALTER TABLE `tr_setoran` DISABLE KEYS */;
INSERT INTO `tr_setoran` VALUES (1,'AVAILABLE',1,'J','180113-SET-B-1','L','2013-01-18','2013-01-18','2013-01-18',0.00,175000.00,NULL,NULL,NULL),(2,'AVAILABLE',2,'J','180113-SET-B-2','L','2013-01-19','2013-01-18','2013-01-18',175000.00,0.00,NULL,NULL,NULL),(3,'AVAILABLE',3,'J','200113-SET-B-3','L','2013-01-20','2013-01-20','2013-01-20',0.00,250000.00,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tr_setoran` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_setoran_detail`
--

DROP TABLE IF EXISTS `tr_setoran_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_setoran_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cicilanOther` decimal(19,2) DEFAULT NULL,
  `angsuran` decimal(19,2) DEFAULT NULL,
  `pembayaran` decimal(19,2) DEFAULT NULL,
  `kasbon` decimal(19,2) DEFAULT NULL,
  `keterangan` varchar(255) DEFAULT NULL,
  `over_time` decimal(19,2) DEFAULT NULL,
  `tabungan` decimal(19,2) DEFAULT NULL,
  `id_pengemudi` int(11) NOT NULL,
  `id_kendaraan` int(11) NOT NULL,
  `id_setoran` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDF93345F6786AB91` (`id_kendaraan`),
  KEY `FKDF93345F3CDCDC7F` (`id_pengemudi`),
  KEY `FKDF93345FE2C73EDB` (`id_setoran`),
  CONSTRAINT `FKDF93345F3CDCDC7F` FOREIGN KEY (`id_pengemudi`) REFERENCES `pengemudi` (`id`),
  CONSTRAINT `FKDF93345F6786AB91` FOREIGN KEY (`id_kendaraan`) REFERENCES `kendaraan` (`id`),
  CONSTRAINT `FKDF93345FE2C73EDB` FOREIGN KEY (`id_setoran`) REFERENCES `tr_setoran` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_setoran_detail`
--

LOCK TABLES `tr_setoran_detail` WRITE;
/*!40000 ALTER TABLE `tr_setoran_detail` DISABLE KEYS */;
INSERT INTO `tr_setoran_detail` VALUES (1,0.00,150000.00,0.00,0.00,'jajal',0.00,25000.00,1,1,1),(2,0.00,150000.00,0.00,175000.00,'jajal',0.00,25000.00,1,1,2),(3,0.00,150000.00,75000.00,0.00,'jajal',0.00,25000.00,1,1,3);
/*!40000 ALTER TABLE `tr_setoran_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_setoran_detail_pth`
--

DROP TABLE IF EXISTS `tr_setoran_detail_pth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_setoran_detail_pth` (
  `id_detail_pth` int(11) NOT NULL AUTO_INCREMENT,
  `cicilanOther` decimal(19,2) DEFAULT NULL,
  `angsuran` decimal(19,2) DEFAULT NULL,
  `pembayaran` decimal(19,2) DEFAULT NULL,
  `kasbon` decimal(19,2) DEFAULT NULL,
  `keterangan` varchar(255) DEFAULT NULL,
  `over_time` decimal(19,2) DEFAULT NULL,
  `tabungan` decimal(19,2) DEFAULT NULL,
  `id_pengemudi_pth` int(11) NOT NULL,
  `id_kendaraan_pth` int(11) NOT NULL,
  `id_setoran_pth` int(11) NOT NULL,
  PRIMARY KEY (`id_detail_pth`),
  KEY `FKC040CF049F1D52F4` (`id_kendaraan_pth`),
  KEY `FKC040CF045C9DF1D4` (`id_pengemudi_pth`),
  KEY `FKC040CF049AA45FD4` (`id_setoran_pth`),
  CONSTRAINT `FKC040CF045C9DF1D4` FOREIGN KEY (`id_pengemudi_pth`) REFERENCES `pengemudi_pth` (`id_pth`),
  CONSTRAINT `FKC040CF049AA45FD4` FOREIGN KEY (`id_setoran_pth`) REFERENCES `tr_setoran_pth` (`idSetr_pth`),
  CONSTRAINT `FKC040CF049F1D52F4` FOREIGN KEY (`id_kendaraan_pth`) REFERENCES `kendaraan_pth` (`id_pth`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_setoran_detail_pth`
--

LOCK TABLES `tr_setoran_detail_pth` WRITE;
/*!40000 ALTER TABLE `tr_setoran_detail_pth` DISABLE KEYS */;
/*!40000 ALTER TABLE `tr_setoran_detail_pth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_setoran_pth`
--

DROP TABLE IF EXISTS `tr_setoran_pth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_setoran_pth` (
  `idSetr_pth` int(11) NOT NULL AUTO_INCREMENT,
  `closing_pth_status` varchar(255) DEFAULT NULL,
  `setoran_counter` int(11) DEFAULT NULL,
  `jalan_status` varchar(255) DEFAULT NULL,
  `kode_setoran_pth` varchar(255) NOT NULL,
  `tr_status` varchar(255) DEFAULT NULL,
  `tglJatuhTempo` date DEFAULT NULL,
  `tglSPO` date DEFAULT NULL,
  `tglSetoran` date DEFAULT NULL,
  `total_hutang` decimal(19,2) DEFAULT NULL,
  `total_setoran` decimal(19,2) DEFAULT NULL,
  `kode_user` varchar(255) DEFAULT NULL,
  `id_closing_pth_bulan` int(11) DEFAULT NULL,
  `id_closing_pth_tahun` int(11) DEFAULT NULL,
  PRIMARY KEY (`idSetr_pth`),
  UNIQUE KEY `kode_setoran_pth` (`kode_setoran_pth`),
  KEY `FK2166C196156108A8` (`id_closing_pth_bulan`),
  KEY `FK2166C196C3281CE8` (`id_closing_pth_tahun`),
  CONSTRAINT `FK2166C196156108A8` FOREIGN KEY (`id_closing_pth_bulan`) REFERENCES `tr_pth_closing_bulanan` (`id`),
  CONSTRAINT `FK2166C196C3281CE8` FOREIGN KEY (`id_closing_pth_tahun`) REFERENCES `tr_putih_closing_tahunan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_setoran_pth`
--

LOCK TABLES `tr_setoran_pth` WRITE;
/*!40000 ALTER TABLE `tr_setoran_pth` DISABLE KEYS */;
/*!40000 ALTER TABLE `tr_setoran_pth` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-23 15:40:31
