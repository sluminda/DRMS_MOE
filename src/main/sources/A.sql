-- Create DRMS Database
DROP DATABASE IF EXISTS drms;

CREATE DATABASE drms
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE drms;
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

-- Example of inserting module records
INSERT INTO modules (M_ID, M_Name, M_FX_ID, M_Action_ID, M_Image_Path) VALUES
                                                                           (1, 'Dashboard', 'dashboard', 'onSelectDashboard', '/com/dmb/drms/Images/Body/MainModules/dashboard-white.png'),
                                                                           (2, 'Daily Letters', 'dailyLetters', 'onSelectDailyLetters', '/com/dmb/drms/Images/Body/MainModules/letters-white.png'),
                                                                           (3, 'Inquiry', 'inquiry', 'onSelectInquiry', '/com/dmb/drms/Images/Body/MainModules/officer-white.png'),
                                                                           (4, 'Reports', 'reports', 'onSelectReports', '/com/dmb/drms/Images/Body/MainModules/report-white.png'),
                                                                           (5, 'Table Views', 'tableViews', 'onSelectTableViews', '/com/dmb/drms/Images/Body/MainModules/table-white.png'),
                                                                           (6, 'Master Tables', 'masterTables', 'onSelectMasterTables', '/com/dmb/drms/Images/Body/MainModules/master-white.png'),
                                                                           (7, 'User Management', 'userManagement', 'onSelectUserManagement', '/com/dmb/drms/Images/Body/MainModules/user-manage-white.png');



-- -----------------------------------------------------
-- Table `DRMS`.`Privileges`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`Privileges`;

CREATE TABLE IF NOT EXISTS `DRMS`.`Privileges` (
  `P_ID` INT NOT NULL AUTO_INCREMENT,
  `User_ID` INT NOT NULL,
  `M_ID` INT NOT NULL,
  `Created_At` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Updated_At` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`P_ID`),
  INDEX (`User_ID`),
  INDEX (`M_ID`),
  FOREIGN KEY (`User_ID`) REFERENCES `DRMS`.`Users` (`User_ID`)
    ON DELETE CASCADE    -- Deletes privileges when a user is deleted
    ON UPDATE NO ACTION,
  FOREIGN KEY (`M_ID`) REFERENCES `DRMS`.`Modules` (`M_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
