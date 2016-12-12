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
  `isConfirmed` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `incident` (`idIncident`, `idUser`, `title`, `creationDate`, `duration`, `description`, `address`, `idSeverite`, `idType`, `isConfirmed`) VALUES
(2, 1, 'yy', '2016-12-12 21:36:41', 5777, 'tt', NULL, 1, 1, 0);

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
(1, 'Faible'),
(2, 'Moyen'),
(3, 'Fort'),
(4, 'Grave');

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
  `password` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `idImage` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `user` (`idUser`, `name`, `firstName`, `email`, `login`, `password`, `status`, `idImage`) VALUES
(1, 'kounvonglasy', 'kevin', 'kounvonglaskevin@gmail.com', 'kkounvonglasy', 'test', NULL, NULL),
(2, 'talhaoui', 'hassan', 'hassan.talhaoui@gmail.com', 'htalhaoui', 'test', NULL, NULL),
(3, 'Liu', 'Hang', 'liu.hang@gmail.com', 'liuhang', 'test', NULL, NULL),
(4, 'Berrahil', 'Karim', 'karim.berrahil@gmail.com', 'kberrahil', 'test', NULL, NULL),
(5, 'simion', 'evy', 'evy.simion@gmail.com', 'esimion', 'test', NULL, NULL);


ALTER TABLE `categorie`
  ADD PRIMARY KEY (`idCategorie`);

ALTER TABLE `image`
  ADD UNIQUE KEY `idImage_UNIQUE` (`idImage`);

ALTER TABLE `incident`
  ADD PRIMARY KEY (`idIncident`,`idSeverite`,`idType`),
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
  MODIFY `idIncident` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
ALTER TABLE `like`
  MODIFY `idLike` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `severity`
  MODIFY `idSeverite` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
ALTER TABLE `type`
  MODIFY `idType` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

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
