-- -----------------------------------------------------
-- Table `DRMS`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`Users`;

CREATE TABLE IF NOT EXISTS `DRMS`.`Users` (
  `User_ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) NOT NULL,
  `User_Name` CHAR(20) NOT NULL UNIQUE,
  `Email` CHAR(50) NOT NULL UNIQUE,
  `Phone` CHAR(10) NOT NULL UNIQUE,
  `NIC` CHAR(15) NOT NULL UNIQUE,
  `User_Role` ENUM('Owner', 'Super Admin', 'Admin') NOT NULL,
  `Is_Data_Entry_Operator` TINYINT NOT NULL,
  `Password_Hash` CHAR(100) NOT NULL,
  `Password_Salt` CHAR(100) NOT NULL,
  `Created_At` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Updated_At` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`User_ID`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;



-- -----------------------------------------------------
-- Table `DRMS`.`Modules`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`Modules`;

CREATE TABLE IF NOT EXISTS `DRMS`.`Modules` (
  `M_ID` INT NOT NULL AUTO_INCREMENT,
  `M_Name` VARCHAR(50) NOT NULL,
  `M_FX_ID` VARCHAR(50) NOT NULL,
  `M_Action_ID` VARCHAR(50) NOT NULL,
  `M_Image_Path` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`M_ID`),
  UNIQUE (`M_ID`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;



-- -----------------------------------------------------
-- Table `DRMS`.`Privilages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`Privilages`;

CREATE TABLE IF NOT EXISTS `DRMS`.`Privilages` (
  `P_ID` INT NOT NULL AUTO_INCREMENT,
  `User_ID` INT NOT NULL,
  `M_ID` INT NOT NULL,
  `Created_At` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Updated_At` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`P_ID`),
  INDEX (`User_ID`),
  INDEX (`M_ID`),
  FOREIGN KEY (`User_ID`) REFERENCES `DRMS`.`Users` (`User_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`M_ID`) REFERENCES `DRMS`.`Modules` (`M_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
