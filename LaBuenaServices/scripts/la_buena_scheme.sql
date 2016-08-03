
CREATE SCHEMA IF NOT EXISTS `la_buena_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

USE `la_buena_db` ;

-- -----------------------------------------------------
-- Table `la_buena_db`.`branch`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `la_buena_db`.`branch` (
  `id_branch` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(320) NOT NULL,
  `name` VARCHAR(250) NOT NULL,
  `created_at` timestamp default current_timestamp,
   `updated_at` timestamp NULL,
  PRIMARY KEY (`id_branch`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `la_buena_db`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `la_buena_db`.`location` (
  `id_location` INT NOT NULL AUTO_INCREMENT,
  `latitude` DECIMAL(8,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  `created_at` timestamp default current_timestamp,
   `updated_at` timestamp NULL,
    PRIMARY KEY (`id_location`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `la_buena_db`.`client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `la_buena_db`.`client` (
  `id_client` INT NOT NULL AUTO_INCREMENT,
  `id_location` INT NULL,
  `email` VARCHAR(320) NOT NULL,
   `name` VARCHAR(250) NULL,
 `created_at` timestamp default current_timestamp,
 `updated_at` timestamp NULL,
  PRIMARY KEY (`id_client`),
  CONSTRAINT `fk_client_location`
    FOREIGN KEY (`id_location`)
    REFERENCES `la_buena_db`.`location` (`id_location`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `la_buena_db`.`biker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `la_buena_db`.`biker` (
  `id_biker` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(320) NOT NULL,
  `name` VARCHAR(250) NOT NULL,
  `stock` TINYINT NULL DEFAULT 0,
  `phone` VARCHAR(15) NOT NULL,
  `id_branch` INT NOT NULL,
  `created_at` timestamp default current_timestamp,
   `updated_at` timestamp NULL,
  PRIMARY KEY (`id_biker`),
  CONSTRAINT `fk_biker_branch`
    FOREIGN KEY (`id_branch`)
    REFERENCES `la_buena_db`.`branch` (`id_branch`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `la_buena_db`.`biker_location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `la_buena_db`.`biker_location` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_biker` INT NOT NULL,
  `id_location` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_biker_location_biker`
    FOREIGN KEY (`id_biker`)
    REFERENCES `la_buena_db`.`biker` (`id_biker`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
       
  CONSTRAINT `fk_biker_location_location`
    FOREIGN KEY (`id_location`)
    REFERENCES `la_buena_db`.`location` (`id_location`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `la_buena_db`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `la_buena_db`.`order` (
  `id_order` INT NOT NULL AUTO_INCREMENT,
  `id_client` INT NOT NULL,
  `quantity` TINYINT NOT NULL,
  `delivered` BIT NOT NULL DEFAULT 0,
  `id_biker` INT NULL,
  `created_at` timestamp default current_timestamp,
   `updated_at` timestamp NULL,
  PRIMARY KEY (`id_order`),
  
  CONSTRAINT `fk_order_client`
    FOREIGN KEY (`id_client`)
    REFERENCES `la_buena_db`.`client` (`id_client`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    
  CONSTRAINT `fk_order_biker`
    FOREIGN KEY (`id_biker`)
    REFERENCES `la_buena_db`.`biker` (`id_biker`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

