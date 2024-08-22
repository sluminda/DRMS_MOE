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



-- -----------------------------------------------------
-- Table `DRMS`.`TableViewMainCategory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`TableViewMainCategory`;

CREATE TABLE IF NOT EXISTS `DRMS`.`TableViewMainCategory` (
                                                              `TVMC_ID` INT NOT NULL AUTO_INCREMENT,
                                                              `TVMC_Name` VARCHAR(50) NOT NULL,
                                                              `TVMC_FX_ID` VARCHAR(50) NOT NULL,
                                                              `TVMC_Action_ID` VARCHAR(50) NOT NULL,
                                                              `TVMC_Image_Path` VARCHAR(150) NOT NULL,
                                                              PRIMARY KEY (`TVMC_ID`),
                                                              UNIQUE (`TVMC_ID`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Example of inserting module records
INSERT INTO TableViewMainCategory (TVMC_ID, TVMC_Name, TVMC_FX_ID, TVMC_Action_ID, TVMC_Image_Path) VALUES
                                                                                          (1, 'National School', 'nschool', 'onSelectNSchool', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/school.png'),
                                                                                          (2, 'National College of Education', 'ncoe', 'onSelectNCOE', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ncoe.png'),
                                                                                          (3, 'Teacher Training College', 'ttc', 'onSelectTTC', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ttc.png'),
                                                                                          (4, 'Ministry of Education', 'moe', 'onSelectMOE', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/moe.png'),
                                                                                          (5, 'State Printing Corporation', 'spc', 'onSelectSPC', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/spc.png');



-- -----------------------------------------------------
-- Table `DRMS`.`ProvincesTableViewCategory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DRMS`.`ProvincesTableViewCategory`;

CREATE TABLE IF NOT EXISTS `DRMS`.`ProvincesTableViewCategory` (
                                                  `PTVC_ID` INT NOT NULL AUTO_INCREMENT,
                                                  `PTVC_Name` VARCHAR(50) NOT NULL,
    `PTVC_FX_ID` VARCHAR(50) NOT NULL,
    `PTVC_Action_ID` VARCHAR(50) NOT NULL,
    `PTVC_Image_Path` VARCHAR(150) NOT NULL,
    PRIMARY KEY (`PTVC_ID`),
    UNIQUE (`PTVC_ID`)
    ) ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

-- Example of inserting provinces records
INSERT INTO ProvincesTableViewCategory (PTVC_ID, PTVC_Name, PTVC_FX_ID, PTVC_Action_ID, PTVC_Image_Path) VALUES
                                                                                                                (1, 'Central Province', 'central', 'onSelectCentral', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/central.png'),
                                                                                                                (2, 'Eastern Province', 'eastern', 'onSelectEastern', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/eastern.png'),
                                                                                                                (3, 'Northern Province', 'northern', 'onSelectNorthern', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/northern.png'),
                                                                                                                (4, 'Southern Province', 'southern', 'onSelectSouthern', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/southern.png'),
                                                                                                                (5, 'Western Province', 'western', 'onSelectWestern', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/western.png'),
                                                                                                                (6, 'North Central Province', 'north_central', 'onSelectNorthCentral', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/north-central.png'),
                                                                                                                (7, 'North Western Province', 'north_western', 'onSelectNorthWestern', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/north-western.png'),
                                                                                                                (8, 'Sabaragamuwa Province', 'sabaragamuwa', 'onSelectSabaragamuwa', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/sabaragamuwa.png'),
                                                                                                                (9, 'Uva Province', 'uva', 'onSelectUva', '/com/dmb/drms/Images/Body/MainModules/TableViewMainCategory/ProvincesTableViewCategory/uva.png');
