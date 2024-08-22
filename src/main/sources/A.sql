























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
  PRIMARY KEY (`M_ID`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Example of inserting module records
INSERT INTO modules (M_Name, M_FX_ID, M_Action_ID, M_Image_Path) VALUES
    ('Dashboard', 'dashboard', 'onSelectDashboard', '/com/dmb/drms/Images/Body/MainModules/dashboard-white.png'),
    ('Daily Letters', 'dailyLetters', 'onSelectDailyLetters', '/com/dmb/drms/Images/Body/MainModules/letters-white.png'),
    ('Inquiry', 'inquiry', 'onSelectInquiry', '/com/dmb/drms/Images/Body/MainModules/officer-white.png'),
    ('Reports', 'reports', 'onSelectReports', '/com/dmb/drms/Images/Body/MainModules/report-white.png'),
    ('Table Views', 'tableViews', 'onSelectTableViews', '/com/dmb/drms/Images/Body/MainModules/table-white.png'),
    ('Master Tables', 'masterTables', 'onSelectMasterTables', '/com/dmb/drms/Images/Body/MainModules/master-white.png'),
    ('User Management', 'userManagement', 'onSelectUserManagement', '/com/dmb/drms/Images/Body/MainModules/user-manage-white.png');

-- -----------------------------------------------------
-- Table `DRMS`.`SubModules`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`SubModules`;

CREATE TABLE IF NOT EXISTS `DRMS`.`SubModules` (
  `SM_ID` INT NOT NULL AUTO_INCREMENT,
  `M_ID` INT NOT NULL,
  `SM_Name` VARCHAR(50) NOT NULL,
  `SM_FX_ID` VARCHAR(50) NOT NULL,
  `SM_Action_ID` VARCHAR(50) NOT NULL,
  `SM_Image_Path` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`SM_ID`),
  FOREIGN KEY (`M_ID`) REFERENCES `DRMS`.`Modules` (`M_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Example of inserting submodule records
INSERT INTO submodules (M_ID, SM_Name, SM_FX_ID, SM_Action_ID, SM_Image_Path) VALUES
    (2, 'Daily Letters 01', 'subModule1', 'onSelectSubModule1', '/com/dmb/drms/Images/Body/SubModules/submodule1.png'),
    (2, 'Daily Letters 02', 'subModule2', 'onSelectSubModule2', '/com/dmb/drms/Images/Body/SubModules/submodule2.png'),
    (5, 'Central', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'Eastern', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'North Central', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'Northern', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'Western', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'North Western', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'Sabaragamuwa', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'Southern', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'Uva', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'SPC', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'MOE', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
    (5, 'NCOE', 'subModule7', 'onSelectSubModule7', '/com/dmb/drms/Images/Body/SubModules/submodule7.png');
-- -----------------------------------------------------
-- Table `DRMS`.`Privileges`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`Privileges`;

CREATE TABLE IF NOT EXISTS `DRMS`.`Privileges` (
  `P_ID` INT NOT NULL AUTO_INCREMENT,
  `User_ID` INT NOT NULL,
  `M_ID` INT NULL,
  `SM_ID` INT NULL,
  `Created_At` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Updated_At` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`P_ID`),
  INDEX (`User_ID`),
  INDEX (`M_ID`),
  INDEX (`SM_ID`),
  FOREIGN KEY (`User_ID`) REFERENCES `DRMS`.`Users` (`User_ID`)
    ON DELETE CASCADE    -- Deletes privileges when a user is deleted
    ON UPDATE NO ACTION,
  FOREIGN KEY (`M_ID`) REFERENCES `DRMS`.`Modules` (`M_ID`)
    ON DELETE CASCADE    -- Deletes privileges when a module is deleted
    ON UPDATE NO ACTION,
  FOREIGN KEY (`SM_ID`) REFERENCES `DRMS`.`SubModules` (`SM_ID`)
    ON DELETE CASCADE    -- Deletes privileges when a submodule is deleted
    ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Example of inserting privilege records
INSERT INTO privileges (User_ID, M_ID) VALUES
    (1, 1),
    (2, 2);

INSERT INTO privileges (User_ID, SM_ID) VALUES
    (1, 1),
    (2, 3);



































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
