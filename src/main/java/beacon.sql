USE Beacon;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS LOCATION;
CREATE TABLE LOCATION (
id int auto_increment,
speed int,
angle int,
longitude float,
latitude float,
altitude float,
timesstamp timestamp,
PRIMARY KEY(id)
);

DROP TABLE IF EXISTS BEACON;
CREATE TABLE BEACON (
uuid VARCHAR(32),
major VARCHAR(4),
minor VARCHAR(4),
rssi int,
location_id int,
PRIMARY KEY(major,minor),
FOREIGN KEY(location_id) REFERENCES LOCATION(id)
);

DROP TABLE IF EXISTS CATEGORY;
CREATE TABLE CATEGORY(
major VARCHAR(4),
category VARCHAR(255),
FOREIGN KEY(major) REFERENCES BEACON(major)
);

DROP TABLE IF EXISTS DEVICE;
CREATE TABLE DEVICE(
inventory_number int,
designation VARCHAR(255),
serial_number VARCHAR(255),
guarantee VARCHAR(255),
note VARCHAR(255),
beacon_minor VARCHAR(4),
beacon_major VARCHAR(4),
reservation_status boolean,
PRIMARY KEY(inventory_number),
FOREIGN KEY(beacon_major, beacon_minor) REFERENCES BEACON(major, minor));
