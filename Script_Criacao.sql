
CREATE DATABASE if not exists `prognosticos`;
USE PROGNOSTICOS;
CREATE TABLE if not exists `prognosticos` (
  `idPrognosticos` int(11) NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `competicao` varchar(45) NOT NULL,
  `timeCasa` varchar(45) NOT NULL,
  `timeFora` varchar(45) NOT NULL,
  `prog` varchar(30) NOT NULL,
  `result` varchar(6) NOT NULL,
  `link` varchar(200) NOT NULL,
  `placar` varchar(5),
  `logoTimes` varchar(450),
  PRIMARY KEY (`idPrognosticos`)
); 
 
