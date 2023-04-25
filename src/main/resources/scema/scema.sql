
DROP DATABASE ikmanRental;
CREATE DATABASE ikmanRental;
USE ikmanRental;


CREATE TABLE Admin(
                      AdminNIC VARCHAR(60),
                      FirsName VARCHAR(50),
                      LastName VARCHAR(30),
                      Gamil VARCHAR(30),
                      Password VARCHAR(30),
                      CONSTRAINT PRIMARY KEY (AdminNIC)
);

CREATE TABLE Customer(
                         CustomerNIC VARCHAR(30),
                         Gamil VARCHAR(30),
                         Contact VARCHAR(20),
                         Name VARCHAR(30),
                         AdminNIC VARCHAR(30),
                         CONSTRAINT PRIMARY KEY (CustomerNIC),
                         CONSTRAINT FOREIGN KEY(AdminNIC) REFERENCES Admin(AdminNIC)
                             ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Vehicle(
                        VehicleNumber VARCHAR(20) PRIMARY KEY,
                        Name VARCHAR(30) NOT NULL,
                        Type VARCHAR(30),
                        FuelToKm DECIMAL(10,2) NOT NULL,
                        KMH INT(10)NOT NULL,
                        Availability VARCHAR(30) NOT NULL,
                        Status VARCHAR(10),
                        Conditions VARCHAR(30)
);

CREATE TABLE Driver(
                       DriverNIC VARCHAR(30) PRIMARY KEY,
                       Gamil VARCHAR(40) NOT NULL,
                       Name VARCHAR(30) NOT NULL,
                       Gender VARCHAR(30) NOT NULL,
                       Status VARCHAR(10)
);

CREATE TABLE Booking(
                        BookingID VARCHAR(30) PRIMARY KEY,
                        Status VARCHAR(20) NOT NULL,
                        AmmountCost DECIMAL(10,2) NOT NULL,
                        RequriedDate DATE,
                        RideTO VARCHAR(30) NOT NULL,
                        Distance VARCHAR(30) NOT NULL,
                        CustomerNIC VARCHAR(30),
                        CONSTRAINT FOREIGN KEY(CustomerNIC) REFERENCES Customer(CustomerNIC)
                            ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE DriverShedeul(
                              BookingID VARCHAR(30),
                              DriverNIC VARCHAR(30),
                              CONSTRAINT FOREIGN KEY(BookingID) REFERENCES Booking(BookingID),
                              CONSTRAINT FOREIGN KEY(DriverNIC) REFERENCES Driver(DriverNIC),
                              PRIMARY KEY(BookingID,DriverNIC)

);

CREATE TABLE BookingDetail(
                              BookingID VARCHAR(30),
                              VehicleNumber VARCHAR(30),
                              CONSTRAINT FOREIGN KEY(BookingID) REFERENCES Booking(BookingID),
                              CONSTRAINT FOREIGN KEY(VehicleNumber) REFERENCES Vehicle(VehicleNumber),
                              PRIMARY KEY(BookingID,VehicleNumber)
);

CREATE TABLE Payment(
                        PaymentID VARCHAR(10) PRIMARY KEY,
                        Status VARCHAR(30) NOT NULL,
                        PaymentCost DECIMAL (10,2) NOT NULL,
                        DriverNIC VARCHAR(30),
                        CONSTRAINT FOREIGN KEY(DriverNIC) REFERENCES Driver(DriverNIC)
                            ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE VehicleIN(
                          VehicleInID VARCHAR(30) PRIMARY KEY,
                          VehicleNumber VARCHAR (30) NOT NULL,
                          DriverNIC VARCHAR(30) NOT NULL,
                          CurrentDate DATE,
                          BookingID VARCHAR(30) NOT NULL,
                          CONSTRAINT FOREIGN KEY(VehicleNumber) REFERENCES Vehicle(VehicleNumber)
                              ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE VehicleOUt(
                           VehicleOutID VARCHAR(30) PRIMARY KEY,
                           VehicleNumber VARCHAR (30) NOT NULL,
                           DriverNIC VARCHAR(30) NOT NULL,
                           Distance DECIMAL (10,2) NOT NULL,
                           BookingID VARCHAR(30) NOT NULL,
                           CONSTRAINT FOREIGN KEY(VehicleNumber) REFERENCES Vehicle(VehicleNumber)
                               ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Billing(
                        BillID VARCHAR(30) PRIMARY KEY,
                        BookingID VARCHAR(30),
                        CustomerNIC VARCHAR(30),
                        DriverNIC VARCHAR(30),
                        Cost DECIMAL (10,2),
                        VehicleNumber VARCHAR(30),
                        Date DATE ,
                        CONSTRAINT FOREIGN KEY(BookingID) REFERENCES Booking(BookingID)
                            ON UPDATE CASCADE ON DELETE CASCADE
);

select * from Admin;
select * from Billing;
select * from Booking;
select * from Customer;
select * from Driver;
select * from BookingDetail;
select * from DriverShedeul;
select * from VehicleOUt;
select * from VehicleOUt;
select * from Vehicle;
select * from Payment;
