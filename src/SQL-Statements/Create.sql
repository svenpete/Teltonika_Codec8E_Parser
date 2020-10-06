SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS RIGHTS;
CREATE TABLE RIGHTS (
                      role VARCHAR(100) NOT NULL,
                      booking_device BOOLEAN NOT NULL,
                      edit_device BOOLEAN NOT NULL,
                      add_device BOOLEAN NOT NULL,
                      view_device BOOLEAN  NOT NULL,
                      delete_device BOOLEAN  NOT NULL,
                      add_user BOOLEAN  NOT NULL,
                      delete_user BOOLEAN NOT NULL,
                      edit_user BOOLEAN  NOT NULL,
                      delete_booking BOOLEAN NOT NULL,
                      edit_booking BOOLEAN NOT NULL,
                      picking BOOLEAN NOT NULL,
                      PRIMARY KEY (role)
);

DROP TABLE IF EXISTS WORKER;
CREATE TABLE WORKER (
                      worker_id INT NOT NULL AUTO_INCREMENT,
                      password VARCHAR(255),
                      e_mail VARCHAR(255),
                      name VARCHAR(255),
                      surname VARCHAR(255),
                      role VARCHAR(100),
                      PRIMARY KEY (worker_id),
                      FOREIGN KEY(role) REFERENCES RIGHTS(role)
);



DROP TABLE IF EXISTS PROJECT;
CREATE TABLE PROJECT (
                       project_id INT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255),
                       street VARCHAR(255),
                       postcode VARCHAR(255),
                       city VARCHAR(255),
                       PRIMARY KEY (project_id)
);

DROP TABLE IF EXISTS DEVICE_STATUS;
CREATE TABLE DEVICE_STATUS(
                            device_status_id INT NOT NULL AUTO_INCREMENT,
                            description VARCHAR(255),
                            PRIMARY KEY(device_status_id)
);


DROP TABLE IF EXISTS LOCATION;
CREATE TABLE LOCATION (
                        location_id INT NOT NULL AUTO_INCREMENT,
                        speed INT,
                        angle INT,
                        longitude FLOAT,
                        latitude FLOAT,
                        altitude FLOAT,
                        timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY(location_id)
);

DROP TABLE IF EXISTS BEACON;
CREATE TABLE BEACON (
                      uuid VARCHAR(32) NOT NULL DEFAULT 'DA95206921ED84C1ED97EA92306C5A7F',
                      major VARCHAR(4) NOT NULL DEFAULT 'FFFF',
                      minor VARCHAR(4) NOT NULL DEFAULT 'FFFF',
                      PRIMARY KEY(major, minor)
);

DROP TABLE IF EXISTS BEACON_POSITION;
CREATE TABLE BEACON_POSITION(
                              major VARCHAR(4) NOT NULL,
                              minor VARCHAR(4) NOT NULL,
                              location_id INT NOT NULL,
                              rssi INT,
                              FOREIGN KEY (major, minor) REFERENCES BEACON(major, minor),
                              FOREIGN KEY (location_id) REFERENCES LOCATION(location_id)
);



DROP TABLE IF EXISTS DEVICE;
CREATE TABLE DEVICE (
                      inventory_number INT NOT NULL AUTO_INCREMENT,
                      serial_number VARCHAR(255) DEFAULT NULL,
                      gurantee DATE DEFAULT NULL,
                      note TEXT,
                      device_status INT,
                      beacon_minor VARCHAR(4) DEFAULT 'FFFF',
                      beacon_major VARCHAR(4) DEFAULT 'FFFF',
                      model VARCHAR(255),
                      manufacturer VARCHAR(255),
                      latest_uvv INT ,
                      latest_tuev INT,
                      latest_repair INT,
                      latest_position INT,
                      date_of_change TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (inventory_number),
                      FOREIGN KEY (device_status) REFERENCES DEVICE_STATUS(device_status_id),
                      FOREIGN KEY(beacon_major, beacon_minor) REFERENCES BEACON(major, minor),
                      FOREIGN KEY(latest_uvv) REFERENCES UVV(uvv_id),
                      FOREIGN KEY(latest_tuev) REFERENCES TUEV(tuev_id),
                      FOREIGN KEY(latest_repair) REFERENCES REPAIR(repair_id),
                      FOREIGN KEY(latest_position) REFERENCES LOCATION(location_id)
);

DROP TABLE IF EXISTS BORROWS;
CREATE TABLE BORROWS (
                       loan_day DATE NOT NULL DEFAULT NOW(),
                       loan_end DATE DEFAULT NULL,
                       loan_period TIME DEFAULT NULL,
                       worker_id INT DEFAULT NULL,
                       inventory_number INT DEFAULT NULL,
                       project_id INT DEFAULT NULL,
                       FOREIGN KEY(worker_id) REFERENCES WORKER(worker_id) ON DELETE SET NULL,
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
                  uvv_id INT NOT NULL AUTO_INCREMENT,
                  inventory_number INT NOT NULL,
                  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                  state BOOLEAN,
                  PRIMARY KEY(uvv_id),
                  FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number)
);

DROP TABLE IF EXISTS TUEV;
CREATE TABLE TUEV(
                   tuev_id INT NOT NULL AUTO_INCREMENT,
                   inventory_number INT NOT NULL,
                   timestamp TIMESTAMP,
                   state BOOLEAN,
                   PRIMARY KEY(tuev_id),
                   FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number)
);

DROP TABLE IF EXISTS REPAIR;
CREATE TABLE REPAIR(
                     repair_id INT NOT NULL AUTO_INCREMENT,
                     inventory_number INT NOT NULL,
                     timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     state BOOLEAN,
                     note VARCHAR(255),
                     PRIMARY KEY(repair_id),
                     FOREIGN KEY(inventory_number) REFERENCES DEVICE(inventory_number)
);

DROP TABLE IF EXISTS DEVICE_HISTORY;
CREATE TABLE DEVICE_HISTORY(
	device_history_id INT NOT NULL AUTO_INCREMENT,
    inventory_number INT,
    serial_number VARCHAR(255),
    gurantee DATE,
    note TEXT,
    device_status INT,
    beacon_major VARCHAR(4),
    beacon_minor VARCHAR(4),
    model VARCHAR(255),
    manufacturer VARCHAR(255),
    latest_uvv INT,
    latest_tuev INT,
    latest_repair INT,
    latest_position INT,
    date_of_change TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (inventory_number),
    FOREIGN KEY (device_status) REFERENCES DEVICE_STATUS(device_status_id),
    FOREIGN KEY(beacon_major, beacon_minor) REFERENCES BEACON(major, minor),
    FOREIGN KEY(latest_uvv) REFERENCES UVV(uvv_id),
    FOREIGN KEY(latest_tuev) REFERENCES TUEV(tuev_id),
    FOREIGN KEY(latest_repair) REFERENCES REPAIR(repair_id),
    FOREIGN KEY(latest_position) REFERENCES LOCATION(location_id)
);