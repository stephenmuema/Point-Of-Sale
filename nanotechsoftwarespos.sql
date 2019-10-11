-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 25, 2019 at 11:00 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nanotechsoftwarespos`
--

-- --------------------------------------------------------

--
-- Table structure for table `audits`
--

CREATE TABLE IF NOT EXISTS `audits`
(
    `id`         int(11)    NOT NULL,
    `company`    text       NOT NULL,
    `employeeid` int(11)    NOT NULL,
    `orderid`    int(11)    NOT NULL,
    `settled`    tinyint(1) NOT NULL DEFAULT '0',
    `amount`     text       NOT NULL,
    `time`       timestamp  NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `backups`
--

CREATE TABLE IF NOT EXISTS `backups`
(
    `id`          int(11)   NOT NULL,
    `dateCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `path`        text      NOT NULL,
    `nextBackup`  text
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `backups`
--

INSERT INTO `backups` (`id`, `dateCreated`, `path`, `nextBackup`)
VALUES (10, '2019-09-23 11:00:26',
        'D:\\NANOTECHSOFTWARES\\nanotechPOS\\backups\\23_9_2019_13_36_29_nanotechsoftwarespos_database_dump.zip',
        '1569582026540'),
       (11, '2019-09-23 11:00:37',
        'D:\\NANOTECHSOFTWARES\\nanotechPOS\\backups\\23_9_2019_14_00_29_nanotechsoftwarespos_database_dump.zip',
        '1569582037601'),
       (12, '2019-09-23 11:01:54',
        'D:\\NANOTECHSOFTWARES\\nanotechPOS\\backups\\23_9_2019_14_01_47_nanotechsoftwarespos_database_dump.zip',
        '1569582114779'),
       (13, '2019-09-23 11:03:24',
        'D:\\NANOTECHSOFTWARES\\nanotechPOS\\backups\\23_9_2019_14_03_18_nanotechsoftwarespos_database_dump.zip',
        '1569582204887'),
       (14, '2019-09-23 11:05:40',
        'D:\\NANOTECHSOFTWARES\\nanotechPOS\\backups\\23_9_2019_14_05_32_nanotechsoftwarespos_database_dump.zip',
        '1569582340182'),
       (15, '2019-09-23 11:06:57',
        'D:\\NANOTECHSOFTWARES\\nanotechPOS\\backups\\23_9_2019_14_06_51_nanotechsoftwarespos_database_dump.zip',
        '1569582417713'),
       (16, '2019-09-25 09:28:24', 'D:\\NANOTECHSOFTWARES\\25_9_2019_12_20_39_nanotechsoftwarespos_database_dump.zip',
        '1569749304143');

-- --------------------------------------------------------

--
-- Table structure for table `carwash`
--

CREATE TABLE IF NOT EXISTS `carwash`
(
    `id`                int(11)   NOT NULL,
    `ownername`         text,
    `registration`      text,
    `idnumber`          text,
    `status`            text,
    `washedby`          text,
    `cashpaid`          text,
    `contact`           text,
    `additionalcharges` text,
    `arrival time`      timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `date`              date           DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `carwash`
--

INSERT INTO `carwash` (`id`, `ownername`, `registration`, `idnumber`, `status`, `washedby`, `cashpaid`, `contact`,
                       `additionalcharges`, `arrival time`, `date`)
VALUES (1, 'STEVE', 'KDA365S', '4345556', 'PENDING', NULL, '', '0702653268', NULL, '2019-09-21 05:01:29', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `chats`
--

CREATE TABLE IF NOT EXISTS `chats`
(
    `id`         int(11)    NOT NULL,
    `receiver`   text       NOT NULL,
    `sender`     text       NOT NULL,
    `message`    text       NOT NULL,
    `time`       timestamp  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `readstatus` tinyint(1) NOT NULL,
    `chattype`   text       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `days`
--

CREATE TABLE IF NOT EXISTS `days`
(
    `id`         int(11) NOT NULL,
    `start_time` text    NOT NULL,
    `end_time`   text,
    `completed`  varchar(20) DEFAULT 'incomplete'
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `days`
--

INSERT INTO `days` (`id`, `start_time`, `end_time`, `completed`)
VALUES (11, '2019-09-25', '2019-09-25', 'complete'),
       (12, '2019-09-25', '2019-09-25', 'complete'),
       (13, '2019-09-25', NULL, 'incomplete');

-- --------------------------------------------------------

--
-- Table structure for table `errors`
--

CREATE TABLE IF NOT EXISTS `errors`
(
    `id`       int(11)    NOT NULL,
    `systemid` int(11)    NOT NULL,
    `errorid`  int(11)    NOT NULL,
    `resolved` varchar(3) NOT NULL DEFAULT 'NO'
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE IF NOT EXISTS `sales`
(
    `id`            int(11) NOT NULL,
    `transactionid` text,
    `cash`          int(11)    DEFAULT NULL,
    `seller`        text,
    `balance`       text,
    `moneypaid`     text,
    `method`        text,
    `completed`     tinyint(1) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`id`, `transactionid`, `cash`, `seller`, `balance`, `moneypaid`, `method`, `completed`)
VALUES (1, '2019-09-25 14:20:54.684', 21513, 'muemasn@nanotechsoftwares.co.ke', '487', '22000', 'CASH', 1);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE IF NOT EXISTS `settings`
(
    `id`            int(11)    NOT NULL,
    `hideicons`     varchar(3) NOT NULL DEFAULT 'YES',
    `printreceipts` varchar(3) NOT NULL DEFAULT 'YES',
    `makebackups`   varchar(3) NOT NULL DEFAULT 'YES'
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `settings_user_map`
--

CREATE TABLE IF NOT EXISTS `settings_user_map`
(
    `id`         int(11) NOT NULL,
    `userid`     int(11) NOT NULL,
    `settingsid` int(11) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `shifts`
--

CREATE TABLE IF NOT EXISTS `shifts`
(
    `id`     int(11)   NOT NULL,
    `userid` int(11)   NOT NULL,
    `start`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `end`    datetime  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `solditems`
--

CREATE TABLE IF NOT EXISTS `solditems`
(
    `id`            int(11) NOT NULL,
    `name`          text,
    `price`         text,
    `quantitysold`  text,
    `transactionid` text,
    `category`      text
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `solditems`
--

INSERT INTO `solditems` (`id`, `name`, `price`, `quantitysold`, `transactionid`, `category`)
VALUES (1, 'TOYOTA LANDCRUISER V8', '1223', '1', '2019-09-25 14:20:54.684', 'TOYOTA'),
       (2, 'BENTLEY MULLSANE', '3221', '2', '2019-09-25 14:20:54.684', 'BENTLEY'),
       (3, 'RANGE ROVER SENTINEL', '1212', '4', '2019-09-25 14:20:54.684', 'RANGE ROVER'),
       (4, 'MERCEDES MAYBACH S CLASS', '3000', '3', '2019-09-25 14:20:54.684', 'MERCEDES');

-- --------------------------------------------------------

--
-- Table structure for table `stocks`
--

CREATE TABLE IF NOT EXISTS `stocks`
(
    `id`       int(11) NOT NULL,
    `name`     text,
    `itemcode` text,
    `amount`   text,
    `category` text,
    `price`    text,
    `supplier` text,
    `path`     text    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `stocks`
--

INSERT INTO `stocks` (`id`, `name`, `itemcode`, `amount`, `category`, `price`, `supplier`, `path`)
VALUES (1, 'TOYOTA LANDCRUISER V8', '1', '1156', 'TOYOTA', '1223', NULL, ''),
       (2, 'BENTLEY MULLSANE', '2', '2994', 'BENTLEY', '3221', NULL, ''),
       (3, 'RANGE ROVER SENTINEL', '3', '378869', 'RANGE ROVER', '1212', NULL,
        'D:\\PROJECTS\\NanotechSoftwares\\poswithcarwash\\1568189936673'),
       (4, 'MERCEDES MAYBACH S CLASS', '4', '456', 'MERCEDES', '3000', NULL,
        'D:\\PROJECTS\\NanotechSoftwares\\poswithcarwash\\156819058860601-mercedes-benz-vehicles-the-new-mercedes-maybach-pullman-vv-222-3400x1440.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `subscribers`
--

CREATE TABLE IF NOT EXISTS `subscribers`
(
    `id`             int(11) NOT NULL,
    `companyname`    text    NOT NULL,
    `companyemail`   text    NOT NULL,
    `companyaddress` text    NOT NULL,
    `adminpassword`  int(11) NOT NULL,
    `stocksid`       int(11) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE IF NOT EXISTS `suppliers`
(
    `id`          int(11) NOT NULL,
    `name`        text,
    `email`       text,
    `phoneNumber` text,
    `location`    text
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `systems`
--

CREATE TABLE IF NOT EXISTS `systems`
(
    `id`      int(11) NOT NULL,
    `name`    text    NOT NULL,
    `license` text    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `systemsettings`
--

CREATE TABLE IF NOT EXISTS `systemsettings`
(
    `id`    int(11) NOT NULL,
    `name`  text    NOT NULL,
    `type`  text    NOT NULL,
    `value` text    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `systemsettings`
--

INSERT INTO `systemsettings` (`id`, `name`, `type`, `value`)
VALUES (3, 'backup', 'security', 'PERIODIC'),
       (4, 'backupLocation', 'security', 'D:\\NANOTECHSOFTWARES');

-- --------------------------------------------------------

--
-- Table structure for table `system_errors_map`
--

CREATE TABLE IF NOT EXISTS `system_errors_map`
(
    `id`       int(11)    NOT NULL,
    `systemid` int(11)    NOT NULL,
    `errorid`  int(11)    NOT NULL,
    `resolved` varchar(3) NOT NULL DEFAULT 'NO'
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `timers`
--

CREATE TABLE IF NOT EXISTS `timers`
(
    `id`         int(11) NOT NULL,
    `employeeid` int(11) NOT NULL,
    `check-in`   datetime DEFAULT NULL,
    `check-out`  datetime DEFAULT NULL,
    `letter`     text    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users`
(
    `id`                  int(11)    NOT NULL,
    `employeename`        text,
    `subscribername`      text,
    `rank`                tinyint(4)          DEFAULT NULL,
    `email`               text,
    `admin`               tinyint(1) NOT NULL DEFAULT '0',
    `password`            text,
    `activated`           int(11)    NOT NULL DEFAULT '0',
    `status`              varchar(80)         DEFAULT 'admin activation required',
    `employeeid`          text,
    `hash`                text,
    `subscriberkey`       text,
    `backupemail`         text,
    `backupemailPassword` text,
    `passwordhint`        varchar(30)         DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;



CREATE TABLE IF NOT EXISTS `ADDEDSYSTEMS`
(
    `id`        int(11) NOT NULL,
    `name`      text,
    `license`   text,
    `timeadded` tinyint(4) DEFAULT NULL

) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `employeename`, `subscribername`, `rank`, `email`, `admin`, `password`, `activated`,
                     `status`, `employeeid`, `hash`, `subscriberkey`, `backupemail`, `backupemailPassword`,
                     `passwordhint`)
VALUES (5, 'admin', NULL, NULL, 'muemasnyamai@gmail.com', 1,
        'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a3815f23f3eab1d8b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec',
        1, 'active', '34556470', 'e3af798efc7474f675a58d8e7f1ff39', NULL, 'muemasnyamai@gmail.com',
        '3ye3iknxLQ7UdHKk1Ex0XBI1cj7m2pkG2cH5hZp4j3s=:YWJjZGVmOTg3NjU0MzIxMA==', NULL),
       (6, 'test', NULL, NULL, 'muemasn@nanotechsoftwares.co.ke', 0,
        'ee26b0dd4af7e749aa1a8ee3c1ae9923f618980772e473f8819a5d494edb27ac185f8a0e1d5f84f88bc887fd67b143732c34cc5fa9ad8e6f57f5028a8ff',
        1, 'active', '34556470', 'f9b3fbc62dbf46a7e6d3c95db2ce8aa', NULL, NULL, NULL, 'test');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `audits`
--
ALTER TABLE `audits`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `backups`
--
ALTER TABLE `backups`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `carwash`
--
ALTER TABLE `carwash`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chats`
--
ALTER TABLE `chats`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `days`
--
ALTER TABLE `days`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `errors`
--
ALTER TABLE `errors`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `settings_user_map`
--
ALTER TABLE `settings_user_map`
    ADD PRIMARY KEY (`id`),
    ADD KEY `snm` (`settingsid`, `userid`),
    ADD KEY `userid` (`userid`);

--
-- Indexes for table `shifts`
--
ALTER TABLE `shifts`
    ADD PRIMARY KEY (`id`),
    ADD KEY `snm` (`userid`);

--
-- Indexes for table `solditems`
--
ALTER TABLE `solditems`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stocks`
--
ALTER TABLE `stocks`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `subscribers`
--
ALTER TABLE `subscribers`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `systems`
--
ALTER TABLE `systems`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `systemsettings`
--
ALTER TABLE `systemsettings`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `system_errors_map`
--
ALTER TABLE `system_errors_map`
    ADD PRIMARY KEY (`id`),
    ADD KEY `snm` (`errorid`, `systemid`),
    ADD KEY `systemid` (`systemid`);

--
-- Indexes for table `timers`
--
ALTER TABLE `timers`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `ADDEDSYSTEMS`
    ADD PRIMARY KEY (`id`);
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `audits`
--
ALTER TABLE `audits`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `backups`
--
ALTER TABLE `backups`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 17;

--
-- AUTO_INCREMENT for table `carwash`
--
ALTER TABLE `carwash`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT for table `chats`
--
ALTER TABLE `chats`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `days`
--
ALTER TABLE `days`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 14;

--
-- AUTO_INCREMENT for table `errors`
--
ALTER TABLE `errors`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `settings_user_map`
--
ALTER TABLE `settings_user_map`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `shifts`
--
ALTER TABLE `shifts`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `solditems`
--
ALTER TABLE `solditems`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 5;

--
-- AUTO_INCREMENT for table `stocks`
--
ALTER TABLE `stocks`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 5;

--
-- AUTO_INCREMENT for table `subscribers`
--
ALTER TABLE `subscribers`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `systemsettings`
--
ALTER TABLE `systemsettings`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 5;

--
-- AUTO_INCREMENT for table `system_errors_map`
--
ALTER TABLE `system_errors_map`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `timers`
--
ALTER TABLE `timers`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;
ALTER TABLE `ADDEDSYSTEMS`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `settings_user_map`
--
ALTER TABLE `settings_user_map`
    ADD CONSTRAINT `settings_user_map_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `settings_user_map_ibfk_2` FOREIGN KEY (`settingsid`) REFERENCES `settings` (`id`);

--
-- Constraints for table `shifts`
--
ALTER TABLE `shifts`
    ADD CONSTRAINT `shifts_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`);

--
-- Constraints for table `system_errors_map`
--
ALTER TABLE `system_errors_map`
    ADD CONSTRAINT `system_errors_map_ibfk_1` FOREIGN KEY (`systemid`) REFERENCES `systems` (`id`),
    ADD CONSTRAINT `system_errors_map_ibfk_2` FOREIGN KEY (`errorid`) REFERENCES `errors` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
