SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `DSschema` DEFAULT CHARACTER SET utf8 ;
USE `DSschema` ;

-- -----------------------------------------------------
-- Table `DSschema`.`SoftwareAgents`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSschema`.`SoftwareAgents` (
  `hash` VARCHAR(64) NOT NULL,
  `online` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`hash`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DSschema`.`nmap_jobs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSschema`.`nmap_jobs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `params` VARCHAR(45) NULL DEFAULT NULL,
  `periodic` TINYINT(1) NULL DEFAULT NULL,
  `period` INT(11) NULL DEFAULT NULL,
  `insertion_time` TIMESTAMP NULL DEFAULT NULL,
  `SoftwareAgents_hash` VARCHAR(64) NULL DEFAULT NULL,
  `assigned` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `fk_nmap_jobs_SoftwareAgents_idx` (`SoftwareAgents_hash` ASC),
  CONSTRAINT `fk_nmap_jobs_SoftwareAgents`
    FOREIGN KEY (`SoftwareAgents_hash`)
    REFERENCES `DSschema`.`SoftwareAgents` (`hash`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DSschema`.`results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSschema`.`results` (
  `hash` VARCHAR(64) NOT NULL COMMENT 'SHA256 hash of the result text ',
  `results` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`hash`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DSschema`.`nmap_jobs_has_results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSschema`.`nmap_jobs_has_results` (
  `nmap_jobs_id` INT(11) NOT NULL,
  `results_hash` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`nmap_jobs_id`, `results_hash`),
  INDEX `fk_nmap_jobs_has_results_results1_idx` (`results_hash` ASC),
  INDEX `fk_nmap_jobs_has_results_nmap_jobs1_idx` (`nmap_jobs_id` ASC),
  CONSTRAINT `fk_nmap_jobs_has_results_nmap_jobs1`
    FOREIGN KEY (`nmap_jobs_id`)
    REFERENCES `DSschema`.`nmap_jobs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nmap_jobs_has_results_results1`
    FOREIGN KEY (`results_hash`)
    REFERENCES `DSschema`.`results` (`hash`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DSschema`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSschema`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `active` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
