-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 19, 2019 at 02:17 PM
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

CREATE TABLE `audits`
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

CREATE TABLE `backups`
(
    `id`          int(11)   NOT NULL,
    `dateCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `path`        text      NOT NULL,
    `nextBackup`  datetime       DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- --------------------------------------------------------

--
-- Table structure for table `carwash`
--

CREATE TABLE `carwash`
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
VALUES (1, 'STEVE', 'KCY205Y', '34556470', 'COMPLETED', NULL, '300', '0702653268', '', '2019-09-03 20:00:36',
        '0000-00-00'),
       (2, 'SNM', 'SNM', '34556470', '', NULL, NULL, 'SNM', NULL, '2019-03-30 08:34:53', '0000-00-00'),
       (3, ',MZNCX.,', 'X,ZCM.', ',MC,.', NULL, NULL, NULL, 'MCN.ZX', NULL, '2019-03-30 07:22:00', '0000-00-00'),
       (4, 'SNM', '121212', '121212', 'completed', 'washer 1', '300', '121', NULL, '2019-04-01 19:00:59', '0000-00-00'),
       (5, 'STEVE', 'KCZ 108 N', '34556470', 'COMPLETED', 'MULINGE', '300', '0702653268', NULL, '2019-04-02 16:41:29',
        '0000-00-00'),
       (6, 'SNM', ',MCXZ', 'DASF,', 'complete', NULL, NULL, ',SDM', NULL, '2019-04-05 17:53:27', '0000-00-00'),
       (7, '', '', '', 'complete', NULL, NULL, '', NULL, '2019-04-16 06:20:42', '0000-00-00'),
       (8, 'STEVE', 'KCD 567 N', '34556470', 'PENDING', NULL, NULL, '0702653268', NULL, '2019-05-02 14:42:00',
        '0000-00-00'),
       (9, 'SNM', ' MBNMBM', 'VBNMBMN', 'complete', NULL, NULL, 'BVMBMB', NULL, '2019-05-03 13:24:05', '0000-00-00'),
       (10, 'SNM', 'SMDNM', 'SMDNSMD', 'PENDING', NULL, NULL, 'SMNDMSD', NULL, '2019-08-23 14:27:10', '0000-00-00');

-- --------------------------------------------------------

--
-- Table structure for table `chats`
--

CREATE TABLE `chats`
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
-- Table structure for table `errors`
--

CREATE TABLE `errors`
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

CREATE TABLE `sales`
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
VALUES (1, '2019-08-20 19:02:15.649', 968168, 'muemasn@nanotechsoftwares.co.ke', '31832', '1000000', 'CASH', 1),
       (2, '2019-09-02 19:24:56.543', 1223, 'muemasn@nanotechsoftwares.co.ke', '777', '2000', 'CASH', 1),
       (3, '2019-09-02 19:24:56.543', 4444, 'muemasn@nanotechsoftwares.co.ke', '1111', '5555', 'CASH', 1),
       (4, '2019-09-02 19:28:05.537', 8888, 'muemasn@nanotechsoftwares.co.ke', '1112', '10000', 'CASH', 1);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings`
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

CREATE TABLE `settings_user_map`
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

CREATE TABLE `shifts`
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

CREATE TABLE `solditems`
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
VALUES (1, 'TOYOTA LANDCRUISER V8', '1223', '789', '2019-08-20 19:02:15.649', 'TOYOTA'),
       (2, 'BENTLEY MULLSANE', '3221', '1', '2019-08-20 19:02:15.649', 'BENTLEY'),
       (3, 'TOYOTA LANDCRUISER V8', '1223', '1', '2019-09-02 19:24:56.543', 'TOYOTA'),
       (4, 'TOYOTA LANDCRUISER V8', '1223', '1', '2019-09-02 19:24:56.543', 'TOYOTA'),
       (5, 'BENTLEY MULLSANE', '3221', '1', '2019-09-02 19:24:56.543', 'BENTLEY'),
       (6, 'TOYOTA LANDCRUISER V8', '1223', '2', '2019-09-02 19:28:05.537', 'TOYOTA'),
       (7, 'BENTLEY MULLSANE', '3221', '2', '2019-09-02 19:28:05.537', 'BENTLEY');

-- --------------------------------------------------------

--
-- Table structure for table `stocks`
--

CREATE TABLE `stocks`
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
VALUES (1, 'TOYOTA LANDCRUISER V8', '1', '1157', 'TOYOTA', '1223', NULL, ''),
       (2, 'BENTLEY MULLSANE', '2', '2996', 'BENTLEY', '3221', NULL, ''),
       (3, 'RANGE ROVER SENTINEL', '3', '378873', 'RANGE ROVER', '1212', NULL,
        'D:\\PROJECTS\\NanotechSoftwares\\poswithcarwash\\1568189936673'),
       (4, 'MERCEDES MAYBACH S CLASS', '4', '459', 'MERCEDES', '3000', NULL,
        'D:\\PROJECTS\\NanotechSoftwares\\poswithcarwash\\156819058860601-mercedes-benz-vehicles-the-new-mercedes-maybach-pullman-vv-222-3400x1440.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `subscribers`
--

CREATE TABLE `subscribers`
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

CREATE TABLE `suppliers`
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

CREATE TABLE `systems`
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

CREATE TABLE `systemsettings`
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
VALUES (1, 'backup', 'security', 'STARTUP BACKUP');

-- --------------------------------------------------------

--
-- Table structure for table `system_errors_map`
--

CREATE TABLE `system_errors_map`
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

CREATE TABLE `timers`
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

CREATE TABLE `users`
(
    `id`                  int(11)     NOT NULL,
    `employeename`        text,
    `subscribername`      text,
    `rank`                tinyint(4)           DEFAULT NULL,
    `email`               text,
    `admin`               tinyint(1)  NOT NULL DEFAULT '0',
    `password`            text,
    `activated`           int(11)     NOT NULL DEFAULT '0',
    `status`              varchar(80)          DEFAULT 'admin activation required',
    `employeeid`          text,
    `hash`                text,
    `subscriberkey`       text,
    `backupemail`         text,
    `backupemailPassword` text,
    `passwordhint`        varchar(30) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `employeename`, `subscribername`, `rank`, `email`, `admin`, `password`, `activated`,
                     `status`, `employeeid`, `hash`, `subscriberkey`, `backupemail`, `backupemailPassword`,
                     `passwordhint`)
VALUES (1, 'admin', NULL, NULL, 'snm@gmail.com', 1,
        'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a3815f23f3eab1d8b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec',
        1, 'active', '34556470', '641b276a48ca927a99446eeffb4a3af4', 'q', 'muemasnyamai@gmail.com',
        '3ye3iknxLQ7UdHKk1Ex0XBI1cj7m2pkG2cH5hZp4j3s=:YWJjZGVmOTg3NjU0MzIxMA==', 'admin'),
       (2, 'test', NULL, NULL, 'muemasn@nanotechsoftwares.co.ke', 0,
        'ee26b0dd4af7e749aa1a8ee3c1ae9923f618980772e473f8819a5d494edb27ac185f8a0e1d5f84f88bc887fd67b143732c34cc5fa9ad8e6f57f5028a8ff',
        1, 'active', 'test', '8fa59888c91207811c306769a4dab769', 'q', 'muemasn@nanotechsoftwares.co.ke', '', ''),
       (3, 'mwende', NULL, NULL, 'mwendemich@gmail.com', 0,
        '9b4352e074e93890a55d659e3eb6863dcfdcba430d86492ce254c3c75b4a809fd120bc5a11adea61dc5d3ad32b1a7718146f82af6ec1b8294889a0da71',
        1, 'active', '34638088', '8e12cc3d25d84c9e85a070bb99b0be50', 'q', NULL, '', ''),
       (4, 'qwerty', NULL, NULL, 'qwerty@gmail.com', 0,
        'dd3e512642c97ca3f747f9a76e374fbda73f9292823c0313be9d78add7cdd8f72235af0c553dd26797e78e1854edee0ae02f8aba74b66dfce1af114e32f8',
        1, 'active', '356456564', '49ff6f7849a5387afdbad4e82f9c1838', 'q', NULL, '', ''),
       (5, 'asdf', NULL, NULL, 'asdf@gmail.com', 0,
        '401b9eab3c013d4ca54922bb82bec8fd5318192ba75f21d8b37274298fb337591abd3e44453b954555b7a0812e1081c39b74293f765eae731f5a65ed1',
        1, 'active', '121223232', '2a63335c6ddcef4dee9291c46c99a299', 'q', NULL, '', '');

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
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `carwash`
--
ALTER TABLE `carwash`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 11;

--
-- AUTO_INCREMENT for table `chats`
--
ALTER TABLE `chats`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

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
    AUTO_INCREMENT = 5;

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
    AUTO_INCREMENT = 8;

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
    AUTO_INCREMENT = 2;

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
    AUTO_INCREMENT = 6;

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
