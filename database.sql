-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema closecall
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema closecall
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `closecall` DEFAULT CHARACTER SET utf8 ;
USE `closecall` ;

-- -----------------------------------------------------
-- Table `closecall`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`users` ;

CREATE TABLE IF NOT EXISTS `closecall`.`users` (
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` CHAR(64) NOT NULL,
  `role` ENUM('PLAYER', 'HOST') NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`hosts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`hosts` ;

CREATE TABLE IF NOT EXISTS `closecall`.`hosts` (
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  PRIMARY KEY (`username`),
  CONSTRAINT `fk_hosts_users`
    FOREIGN KEY (`username`)
    REFERENCES `closecall`.`users` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`players` ;

CREATE TABLE IF NOT EXISTS `closecall`.`players` (
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  PRIMARY KEY (`username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`clubs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`clubs` ;

CREATE TABLE IF NOT EXISTS `closecall`.`clubs` (
  `clubName` VARCHAR(45) NOT NULL,
  `street` VARCHAR(45) NOT NULL,
  `number` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `zip` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `owner` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`clubName`, `owner`),
  INDEX `fk_clubs_hosts1_idx` (`owner` ASC) VISIBLE,
  CONSTRAINT `fk_clubs_hosts1`
    FOREIGN KEY (`owner`)
    REFERENCES `closecall`.`hosts` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`tournaments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`tournaments` ;

CREATE TABLE IF NOT EXISTS `closecall`.`tournaments` (
  `clubOwner` VARCHAR(45) NOT NULL,
  `clubName` VARCHAR(45) NOT NULL,
  `tournamentName` VARCHAR(45) NOT NULL,
  `tournamentFormat` VARCHAR(45) NOT NULL,
  `tournamentType` VARCHAR(45) NOT NULL,
  `matchFormat` VARCHAR(45) NOT NULL,
  `courtType` VARCHAR(45) NOT NULL,
  `teamsNumber` INT NOT NULL,
  `courtNumber` VARCHAR(45) NOT NULL,
  `joinFee` DOUBLE NOT NULL,
  `courtPrice` DOUBLE NOT NULL,
  `startDate` DATE NOT NULL,
  `endDate` DATE NOT NULL,
  `signupDeadline` DATE NOT NULL,
  PRIMARY KEY (`clubOwner`, `clubName`, `tournamentName`),
  CONSTRAINT `fk_tournaments_clubs1`
    FOREIGN KEY (`clubName` , `clubOwner`)
    REFERENCES `closecall`.`clubs` (`clubName` , `owner`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`teams`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`teams` ;

CREATE TABLE IF NOT EXISTS `closecall`.`teams` (
  `id` INT NOT NULL,
  `clubName` VARCHAR(45) NOT NULL,
  `clubOwner` VARCHAR(45) NOT NULL,
  `tournamentName` VARCHAR(45) NOT NULL,
  `player1` VARCHAR(45) NULL,
  `player2` VARCHAR(45) NULL,
  `status` ENUM('CONFIRMED', 'PENDING', 'PARTIAL', 'PENDING_PARTIAL') NOT NULL,
  PRIMARY KEY (`id`, `clubName`, `clubOwner`, `tournamentName`),
  INDEX `fk_teams_players1_idx` (`player1` ASC) VISIBLE,
  INDEX `fk_teams_players2_idx` (`player2` ASC) VISIBLE,
  CONSTRAINT `fk_teams_tournaments1`
    FOREIGN KEY (`clubName` , `clubOwner` , `tournamentName`)
    REFERENCES `closecall`.`tournaments` (`clubName` , `clubOwner` , `tournamentName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_teams_players1`
    FOREIGN KEY (`player1`)
    REFERENCES `closecall`.`players` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_teams_players2`
    FOREIGN KEY (`player2`)
    REFERENCES `closecall`.`players` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`favouriteClubs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`favouriteClubs` ;

CREATE TABLE IF NOT EXISTS `closecall`.`favouriteClubs` (
  `player` VARCHAR(45) NOT NULL,
  `clubName` VARCHAR(45) NOT NULL,
  `clubOwner` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`player`, `clubName`, `clubOwner`),
  INDEX `fk_players_has_clubs_clubs1_idx` (`clubName` ASC, `clubOwner` ASC) VISIBLE,
  INDEX `fk_players_has_clubs_players1_idx` (`player` ASC) VISIBLE,
  CONSTRAINT `fk_players_has_clubs_players1`
    FOREIGN KEY (`player`)
    REFERENCES `closecall`.`players` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_players_has_clubs_clubs1`
    FOREIGN KEY (`clubName` , `clubOwner`)
    REFERENCES `closecall`.`clubs` (`clubName` , `owner`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`hostNotifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`hostNotifications` ;

CREATE TABLE IF NOT EXISTS `closecall`.`hostNotifications` (
  `clubName` VARCHAR(45) NOT NULL,
  `host` VARCHAR(45) NOT NULL,
  `tournamentName` VARCHAR(45) NOT NULL,
  `player` VARCHAR(45) NOT NULL,
  `batchToken` CHAR(36) NULL,
  PRIMARY KEY (`host`, `clubName`, `tournamentName`, `player`),
  INDEX `player_idx` (`player` ASC) VISIBLE,
  CONSTRAINT `player`
    FOREIGN KEY (`player`)
    REFERENCES `closecall`.`players` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tournament`
    FOREIGN KEY (`host` , `clubName` , `tournamentName`)
    REFERENCES `closecall`.`tournaments` (`clubOwner` , `clubName` , `tournamentName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`invites`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`invites` ;

CREATE TABLE IF NOT EXISTS `closecall`.`invites` (
  `player` VARCHAR(45) NOT NULL,
  `clubOwner` VARCHAR(45) NOT NULL,
  `clubName` VARCHAR(45) NOT NULL,
  `tournamentName` VARCHAR(45) NOT NULL,
  `sendDate` DATE NOT NULL,
  `expireDate` DATE NOT NULL,
  `message` VARCHAR(400) NULL,
  `status` ENUM('ACCEPTED', 'DECLINED', 'PENDING', 'EXPIRED') NOT NULL,
  PRIMARY KEY (`player`, `clubOwner`, `clubName`, `tournamentName`),
  INDEX `tournament_idx` (`clubOwner` ASC, `clubName` ASC, `tournamentName` ASC) VISIBLE,
  CONSTRAINT `player_idx`
    FOREIGN KEY (`player`)
    REFERENCES `closecall`.`players` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tournament_idx`
    FOREIGN KEY (`clubOwner` , `clubName` , `tournamentName`)
    REFERENCES `closecall`.`tournaments` (`clubOwner` , `clubName` , `tournamentName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`prizes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`prizes` ;

CREATE TABLE IF NOT EXISTS `closecall`.`prizes` (
  `place` INT NOT NULL,
  `clubOwner` VARCHAR(45) NOT NULL,
  `clubName` VARCHAR(45) NOT NULL,
  `tournamentName` VARCHAR(45) NOT NULL,
  `value` DOUBLE NOT NULL,
  PRIMARY KEY (`place`, `clubOwner`, `clubName`, `tournamentName`),
  INDEX `fk_prizes_tournaments1_idx` (`clubOwner` ASC, `clubName` ASC, `tournamentName` ASC) VISIBLE,
  CONSTRAINT `fk_prizes_tournaments1`
    FOREIGN KEY (`clubOwner` , `clubName` , `tournamentName`)
    REFERENCES `closecall`.`tournaments` (`clubOwner` , `clubName` , `tournamentName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `closecall`.`playerNotifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `closecall`.`playerNotifications` ;

CREATE TABLE IF NOT EXISTS `closecall`.`playerNotifications` (
  `player` VARCHAR(45) NOT NULL,
  `clubOwner` VARCHAR(45) NOT NULL,
  `clubName` VARCHAR(45) NOT NULL,
  `tournamentName` VARCHAR(45) NOT NULL,
  `batchToken` CHAR(36) NULL,
  PRIMARY KEY (`player`, `clubOwner`, `clubName`, `tournamentName`),
  INDEX `fk_playerNotifications_tournaments1_idx` (`clubOwner` ASC, `clubName` ASC, `tournamentName` ASC) VISIBLE,
  CONSTRAINT `fk_playerNotifications_to_players`
    FOREIGN KEY (`player`)
    REFERENCES `closecall`.`players` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_playerNotifications_tournaments1`
    FOREIGN KEY (`clubOwner` , `clubName` , `tournamentName`)
    REFERENCES `closecall`.`tournaments` (`clubOwner` , `clubName` , `tournamentName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE = '';
DROP USER IF EXISTS User;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER 'User' IDENTIFIED BY 'user';

GRANT ALL ON TABLE `closecall`.`clubs` TO 'User';
GRANT ALL ON TABLE `closecall`.`favouriteClubs` TO 'User';
GRANT ALL ON TABLE `closecall`.`hostNotifications` TO 'User';
GRANT ALL ON TABLE `closecall`.`hosts` TO 'User';
GRANT ALL ON TABLE `closecall`.`invites` TO 'User';
GRANT ALL ON TABLE `closecall`.`playerNotifications` TO 'User';
GRANT ALL ON TABLE `closecall`.`players` TO 'User';
GRANT ALL ON TABLE `closecall`.`prizes` TO 'User';
GRANT ALL ON TABLE `closecall`.`teams` TO 'User';
GRANT ALL ON TABLE `closecall`.`tournaments` TO 'User';
GRANT ALL ON TABLE `closecall`.`users` TO 'User';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
