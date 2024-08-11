-- Create DRMS Database
DROP DATABASE IF EXISTS drms;

CREATE DATABASE drms
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE drms;

-- Create users table
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password_hash CHAR(100) NOT NULL,
                       salt CHAR(100) NOT NULL,
                       role ENUM('Owner', 'Super Admin', 'Admin') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Create LetterTypes table
DROP TABLE IF EXISTS LetterTypes;

CREATE TABLE LetterTypes (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             type VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO LetterTypes (type) VALUES
                                   ('Disciplinary Inquiry'),
                                   ('Complaint'),
                                   ('PSC'),
                                   ('ESC'),
                                   ('Pub'),
                                   ('Bribery'),
                                   ('From Province'),
                                   ('From Zone'),
                                   ('From Branch'),
                                   ('For an existing file');

-- Create SubjectClerks table
DROP TABLE IF EXISTS SubjectClerks;

CREATE TABLE SubjectClerks (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO SubjectClerks (name) VALUES
                                     ('Lumi'),
                                     ('Nimal'),
                                     ('Amal'),
                                     ('Kamal'),
                                     ('Tamal');

-- Create ActionTakens table
DROP TABLE IF EXISTS ActionTakens;

CREATE TABLE ActionTakens (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              action VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO ActionTakens (action) VALUES
                                      ('Kill'),
                                      ('Cutout Left Hand'),
                                      ('Cutout Right Hand'),
                                      ('Cutout Right Leg'),
                                      ('Cutout Left Leg');

-- Create Letters table
DROP TABLE IF EXISTS Letters;

CREATE TABLE Letters (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         senderName VARCHAR(255) NOT NULL,
                         letterFileNo VARCHAR(100) NOT NULL,
                         letterDate DATE NOT NULL,
                         letterTypeId INT NOT NULL,
                         letterSubject VARCHAR(255) NOT NULL,
                         subjectClerkId INT NOT NULL,
                         receivedDate DATE NOT NULL,
                         fileRelatedToLetter VARCHAR(100) NOT NULL,
                         actionTakenId INT,
                         dateOfActionTaken DATE NOT NULL,
                         specialNote TEXT,
                         createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (letterTypeId) REFERENCES LetterTypes(id),
                         FOREIGN KEY (subjectClerkId) REFERENCES SubjectClerks(id),
                         FOREIGN KEY (actionTakenId) REFERENCES ActionTakens(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO Letters (
    senderName,
    letterFileNo,
    letterDate,
    letterTypeId,
    letterSubject,
    subjectClerkId,
    receivedDate,
    fileRelatedToLetter,
    actionTakenId,
    dateOfActionTaken
) VALUES
      ('John Doe', 'File123', '2023-01-01', 1, 'Subject A', 1, '2023-01-02', 'FileRel1', 1, '2023-01-03'),
      ('Jane Smith', 'File124', '2023-02-01', 2, 'Subject B', 2, '2023-02-02', 'FileRel2', 2, '2023-02-03'),
      ('Alice Johnson', 'File125', '2023-03-01', 3, 'Subject C', 3, '2023-03-02', 'FileRel3', 3, '2023-03-03'),
      ('Bob Brown', 'File126', '2023-04-01', 4, 'Subject D', 4, '2023-04-02', 'FileRel4', 4, '2023-04-03'),
      ('Carol White', 'File127', '2023-05-01', 5, 'Subject E', 5, '2023-05-02', 'FileRel5', 5, '2023-05-03');


-- Provinces
DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO employees (first_name, last_name, email) VALUES
                                                         ('John', 'Doe', 'john.doe@example.com'),
                                                         ('Jane', 'Smith', 'jane.smith@example.com'),
                                                         ('Alice', 'Johnson', 'alice.johnson@example.com');

COMMIT;