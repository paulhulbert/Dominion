# Dominion


TODO: Compress strings for database
TODO: Make time not system dependent

Create statement for DB is 

CREATE TABLE `dominion`.`actions` (
  `ID` INT NOT NULL auto_increment,
  `time_created` MEDIUMTEXT NULL,
  `GameID` INT NULL,
  `Player` VARCHAR(45) NULL,
  `Details` VARCHAR(1000) NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));