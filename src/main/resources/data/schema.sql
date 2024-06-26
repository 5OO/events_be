CREATE TABLE Events (
                        EventID INT AUTO_INCREMENT PRIMARY KEY,
                        EventName VARCHAR(255) NOT NULL,
                        EventDateTime DATETIME NOT NULL,
                        Location VARCHAR(255) NOT NULL,
                        AdditionalInfo VARCHAR(1000)
);

CREATE TABLE Individuals (
                             ParticipantID INT AUTO_INCREMENT PRIMARY KEY,
                             EventID INT,
                             FirstName VARCHAR(255) NOT NULL,
                             LastName VARCHAR(255) NOT NULL,
                             PersonalID VARCHAR(255) NOT NULL,
                             PaymentMethod VARCHAR(255),
                             AdditionalInfo VARCHAR(1500),
                             FOREIGN KEY (EventID) REFERENCES Events(EventID) ON DELETE CASCADE
                         );

CREATE TABLE Companies (
                           ParticipantID INT AUTO_INCREMENT PRIMARY KEY,
                           EventID INT,
                           LegalName VARCHAR(255) NOT NULL,
                           RegistrationCode VARCHAR(255) NOT NULL,
                           NumberOfParticipants INT NOT NULL,
                           PaymentMethod VARCHAR(255),
                           AdditionalInfo VARCHAR(5000),
                           FOREIGN KEY (EventID) REFERENCES Events(EventID) ON DELETE CASCADE
                       );
