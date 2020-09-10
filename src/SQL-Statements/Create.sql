SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS RIGHTS;
CREATE TABLE RIGHTS (
                      role varchar(100) NOT NULL,
                      booking_device boolean NOT NULL,
                      edit_device boolean NOT NULL,
                      add_device boolean NOT NULL,
                      view_device boolean  NOT NULL,
                      delete_device boolean  NOT NULL,
                      add_user boolean  NOT NULL,
                      delete_user boolean NOT NULL,
                      edit_user boolean  NOT NULL,
                      delete_booking boolean DEFAULT NULL,
                      edit_booking boolean DEFAULT NULL,
                      PRIMARY KEY (role)
);

DROP TABLE IF EXISTS WORKER;
CREATE TABLE WORKER (
                      worker_id int NOT NULL AUTO_INCREMENT,
                      password varchar(255) DEFAULT NULL,
                      e_mail varchar(255) DEFAULT NULL,
                      name varchar(255) DEFAULT NULL,
                      surname varchar(255) DEFAULT NULL,
                      role varchar(100) DEFAULT NULL,
                      PRIMARY KEY (worker_id),
                      FOREIGN KEY(role) REFERENCES RIGHTS(role)
);



DROP TABLE IF EXISTS PROJECT;
CREATE TABLE PROJECT (
                       project_id int NOT NULL AUTO_INCREMENT,
                       name varchar(255) DEFAULT NULL,
                       street varchar(255) DEFAULT NULL,
                       postcode varchar(255) DEFAULT NULL,
                       city varchar(255) DEFAULT NULL,
                       PRIMARY KEY (project_id)
);

DROP TABLE IF EXISTS DEVICE_STATUS;
CREATE TABLE DEVICE_STATUS(
                            device_status_id int not null auto_increment,
                            description VARCHAR(255),
                            PRIMARY KEY(device_status_id)
);

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS LOCATION;
CREATE TABLE LOCATION (
                        location_id int not null auto_increment,
                        speed int,
                        angle int,
                        longitude float,
                        latitude float,
                        altitude float,
                        timesstamp timestamp,
                        PRIMARY KEY(location_id)
);

DROP TABLE IF EXISTS BEACON;
CREATE TABLE BEACON (
                      uuid VARCHAR(32),
                      major VARCHAR(4),
                      minor VARCHAR(4),
                      PRIMARY KEY(major, minor)
);

DROP TABLE IF EXISTS BEACON_POSITION;
CREATE TABLE BEACON_POSITION(
                              major VARCHAR(4) not null,
                              minor VARCHAR(4) not null,
                              location_id int not null,
                              rssi int,
                              FOREIGN KEY (major, minor) REFERENCES BEACON(major, minor),
                              FOREIGN KEY (location_id) REFERENCES LOCATION(location_id)
);



DROP TABLE IF EXISTS DEVICE;
CREATE TABLE DEVICE (
                      inventory_number int NOT NULL AUTO_INCREMENT,
                      designation varchar(255) DEFAULT NULL,
                      serial_number varchar(255) DEFAULT NULL,
                      gurantee date DEFAULT NULL,
                      note text,
                      device_status int,
                      beacon_minor VARCHAR(255),
                      beacon_major VARCHAR(255),
                      PRIMARY KEY (inventory_number),
                      FOREIGN KEY (device_status) REFERENCES DEVICE_STATUS(device_status_id),
                      FOREIGN KEY(beacon_major, beacon_minor) REFERENCES BEACON(major, minor)
);

DROP TABLE IF EXISTS BORROWS;
CREATE TABLE BORROWS (
                       loan_day date NOT NULL DEFAULT '0000-00-00',
                       loan_end date DEFAULT NULL,
                       loan_period time DEFAULT NULL,
                       worker_id int DEFAULT NULL,
                       inventory_number int DEFAULT NULL,
                       project_id int DEFAULT NULL,
                       FOREIGN KEY(worker_id) REFERENCES WORKER(worker_id),
                       FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number),
                       FOREIGN KEY(project_id) REFERENCES PROJECT(project_id)
);


DROP TABLE IF EXISTS CATEGORY;
CREATE TABLE CATEGORY(
                       major VARCHAR(4),
                       category VARCHAR(255),
                       FOREIGN KEY(major) REFERENCES BEACON(major)
);

DROP TABLE IF EXISTS UVV;
CREATE TABLE UVV(
                  uvv_id int not null auto_increment,
                  inventory_number int not null,
                  timestamp timestamp,
                  status boolean,
                  PRIMARY KEY(uvv_id),
                  FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number)
);

DROP TABLE IF EXISTS TUEV;
CREATE TABLE TUEV(
                   tuev_id int not null auto_increment,
                   inventory_number int not null,
                   timestamp timestamp,
                   status boolean,
                   PRIMARY KEY(tuev_id),
                   FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number)
);

DROP TABLE IF EXISTS REPAIR;
CREATE TABLE REPAIR(
                     repair_id int not null auto_increment,
                     inventory_number int not null,
                     timestamp timestamp,
                     status boolean,
                     note VARCHAR(255),
                     PRIMARY KEY(repair_id),
                     FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number)
);