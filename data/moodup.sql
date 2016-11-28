-- phpMyAdmin SQL Dump
-- version 4.5.5.1
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Lun 28 Novembre 2016 à 11:24
-- Version du serveur :  5.7.11
-- Version de PHP :  5.6.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `moodup`
--

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `idCategorie` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `categorie`
--

INSERT INTO `categorie` (`idCategorie`, `name`) VALUES
(1, 'Transports en commun'),
(2, 'Transports routier');

-- --------------------------------------------------------

--
-- Structure de la table `image`
--

CREATE TABLE `image` (
  `idImage` int(11) NOT NULL,
  `content` blob,
  `title` varchar(45) DEFAULT NULL,
  `creationDate` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `incident`
--

CREATE TABLE `incident` (
  `idIncident` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `title` varchar(45) DEFAULT NULL,
  `creationDate` date DEFAULT NULL,
  `duration` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `abus` tinyint(1) DEFAULT NULL,
  `like` tinyint(1) DEFAULT NULL,
  `idSeverite` int(11) NOT NULL,
  `idType` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `incident`
--

INSERT INTO `incident` (`idIncident`, `idUser`, `title`, `creationDate`, `duration`, `description`, `address`, `abus`, `like`, `idSeverite`, `idType`) VALUES
(1, 1, 'Probleme sur la ligne 4', '2016-11-09', NULL, 'Malaise voyageur à la station Montparnasse', NULL, NULL, NULL, 3, 1);

-- --------------------------------------------------------

--
-- Structure de la table `severity`
--

CREATE TABLE `severity` (
  `idSeverite` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `severity`
--

INSERT INTO `severity` (`idSeverite`, `name`) VALUES
(1, 'Faible'),
(2, 'Moyen'),
(3, 'Fort'),
(4, 'Grave');

-- --------------------------------------------------------

--
-- Structure de la table `type`
--

CREATE TABLE `type` (
  `idType` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `idCategorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `type`
--

INSERT INTO `type` (`idType`, `name`, `idCategorie`) VALUES
(1, 'Metro', 1),
(2, 'Taxi', 2);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

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

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`idUser`, `name`, `firstName`, `email`, `login`, `password`, `status`, `idImage`) VALUES
(1, 'kounvonglasy', 'kevin', 'kounvonglaskevin@gmail.com', 'kkounvonglasy', 'test', NULL, NULL);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`idCategorie`);

--
-- Index pour la table `image`
--
ALTER TABLE `image`
  ADD UNIQUE KEY `idImage_UNIQUE` (`idImage`);

--
-- Index pour la table `incident`
--
ALTER TABLE `incident`
  ADD PRIMARY KEY (`idIncident`,`idSeverite`,`idType`),
  ADD KEY `fk_Incident_Utilisateur_idx` (`idUser`),
  ADD KEY `fk_Incident_Severite1_idx` (`idSeverite`),
  ADD KEY `fk_Incident_Type1_idx` (`idType`);

--
-- Index pour la table `severity`
--
ALTER TABLE `severity`
  ADD PRIMARY KEY (`idSeverite`);

--
-- Index pour la table `type`
--
ALTER TABLE `type`
  ADD PRIMARY KEY (`idType`,`idCategorie`),
  ADD KEY `fk_Type_Categorie1_idx` (`idCategorie`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `idImage_UNIQUE` (`idImage`),
  ADD KEY `fk_Utilisateur_Image1_idx` (`idImage`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `idCategorie` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `image`
--
ALTER TABLE `image`
  MODIFY `idImage` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `incident`
--
ALTER TABLE `incident`
  MODIFY `idIncident` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `severity`
--
ALTER TABLE `severity`
  MODIFY `idSeverite` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT pour la table `type`
--
ALTER TABLE `type`
  MODIFY `idType` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `incident`
--
ALTER TABLE `incident`
  ADD CONSTRAINT `fk_Incident_Severite1` FOREIGN KEY (`idSeverite`) REFERENCES `severity` (`idSeverite`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Incident_Type1` FOREIGN KEY (`idType`) REFERENCES `type` (`idType`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Incident_Utilisateur` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `type`
--
ALTER TABLE `type`
  ADD CONSTRAINT `fk_Type_Categorie1` FOREIGN KEY (`idCategorie`) REFERENCES `categorie` (`idCategorie`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_Utilisateur_Image1` FOREIGN KEY (`idImage`) REFERENCES `image` (`idImage`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;