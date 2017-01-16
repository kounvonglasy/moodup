SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE TABLE `categorie` (
  `idCategorie` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `categorie` (`idCategorie`, `name`) VALUES
(1, 'Transports en commun'),
(2, 'Transports routier');

CREATE TABLE `image` (
  `idImage` int(11) NOT NULL,
  `content` blob,
  `title` varchar(45) DEFAULT NULL,
  `creationDate` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `incident` (
  `idIncident` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `title` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `duration` int(11) NOT NULL DEFAULT '10',
  `description` varchar(100) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `idSeverite` int(11) NOT NULL,
  `idType` int(11) NOT NULL,
  `isConfirmed` tinyint(1) DEFAULT '0',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `like` (
  `idLike` int(11) NOT NULL,
  `idIncident` int(11) NOT NULL,
  `idUser` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `severity` (
  `idSeverite` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `severity` (`idSeverite`, `name`) VALUES
(1, 'Perturbation du trafic'),
(2, 'Retard'),
(3, 'Interruption temporaire'),
(4, 'Arrêt > 1 jour');

CREATE TABLE `type` (
  `idType` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `idCategorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `type` (`idType`, `name`, `idCategorie`) VALUES
(1, 'Metro', 1),
(2, 'Taxi', 2);

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `login` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `idImage` int(11) DEFAULT NULL,
  `salt` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `user` (`idUser`, `name`, `firstName`, `email`, `login`, `password`, `status`, `idImage`, `salt`) VALUES
(10, 'kounvonglasy', 'kevin', 'kouvonglasykevin@gmail.com', 'kkounvonglasy', 'ea6c5e90ccad7f0d1c03d32aaa402d79a5bb112f97fe06faca30dc55d4c77ae8', NULL, NULL, 'fecd8a259b1392a3'),
(11, 'Talhaoui', 'Hassan', 'htalhaoui@gmail.com', 'htalhaoui', 'f7bd1c37b5f2eb0f195d2adc53945c242b05d9bd928c0abc4bc0b298752064c7', NULL, NULL, 'c3037c85c04efdfd'),
(12, 'Liu', 'Hang', 'lhang@gmail.com', 'liuhang', '534497d9d9fb00a35744b61c538f899957d369fee12a79c51aea8ffa77a970a5', NULL, NULL, '44c545cf7bf7774d'),
(13, 'Berrahil', 'Karim', 'karim.berrahil@gmail.com', 'kberrahil', 'df48b43030f63df21d1627a9efb5d70331bf565470fb504aab056bf165337c6c', NULL, NULL, '94a7d8c47394adb6'),
(14, 'Simion', 'Evy', 'esimion@gmail.com', 'esimion', 'db2d744056a919941c9ceba4a47f1ac917cb899ce82b5c72eb69e66197c4e457', NULL, NULL, '6d5e41bf9d3311a1');


ALTER TABLE `categorie`
  ADD PRIMARY KEY (`idCategorie`);

ALTER TABLE `image`
  ADD UNIQUE KEY `idImage_UNIQUE` (`idImage`);

ALTER TABLE `incident`
  ADD PRIMARY KEY (`idIncident`,`idSeverite`,`idType`),
  ADD UNIQUE KEY `title` (`title`),
  ADD KEY `fk_Incident_Utilisateur_idx` (`idUser`),
  ADD KEY `fk_Incident_Severite1_idx` (`idSeverite`),
  ADD KEY `fk_Incident_Type1_idx` (`idType`);

ALTER TABLE `like`
  ADD PRIMARY KEY (`idLike`),
  ADD UNIQUE KEY `idIncident` (`idIncident`,`idUser`);

ALTER TABLE `severity`
  ADD PRIMARY KEY (`idSeverite`);

ALTER TABLE `type`
  ADD PRIMARY KEY (`idType`,`idCategorie`),
  ADD KEY `fk_Type_Categorie1_idx` (`idCategorie`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `idImage_UNIQUE` (`idImage`),
  ADD UNIQUE KEY `login` (`login`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_Utilisateur_Image1_idx` (`idImage`);


ALTER TABLE `categorie`
  MODIFY `idCategorie` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
ALTER TABLE `image`
  MODIFY `idImage` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `incident`
  MODIFY `idIncident` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `like`
  MODIFY `idLike` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `severity`
  MODIFY `idSeverite` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
ALTER TABLE `type`
  MODIFY `idType` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

ALTER TABLE `incident`
  ADD CONSTRAINT `fk_Incident_Severite1` FOREIGN KEY (`idSeverite`) REFERENCES `severity` (`idSeverite`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Incident_Type1` FOREIGN KEY (`idType`) REFERENCES `type` (`idType`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Incident_Utilisateur` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `type`
  ADD CONSTRAINT `fk_Type_Categorie1` FOREIGN KEY (`idCategorie`) REFERENCES `categorie` (`idCategorie`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `user`
  ADD CONSTRAINT `fk_Utilisateur_Image1` FOREIGN KEY (`idImage`) REFERENCES `image` (`idImage`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
