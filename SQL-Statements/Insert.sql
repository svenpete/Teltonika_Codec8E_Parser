SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE RIGHTS;
INSERT INTO RIGHTS VALUES ('admin',true,true,true,true,true,true,true,true,true,true,true);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE WORKER;
INSERT INTO WORKER (password, e_mail, name, surname, role) VALUES('testing', 's.petersen@dallmann-bau.de', 'Petersen','Sven','admin');
INSERT INTO WORKER (password, e_mail, name, surname, role) VALUES('hallo123', 'l.kottmann@dallmann-bau.de', 'Kottmann','Louis','admin');
INSERT INTO WORKER (password, e_mail, name, surname, role) VALUES('moin', 'm.eickmann@dallmann-bau.de', 'Eickmann','Morten','admin');
INSERT INTO WORKER (password, e_mail, name, surname, role) VALUES('password123', 'k.bosse@dallmann-bau.de', 'Bosse','Kevin','admin');
INSERT INTO WORKER (password, e_mail, name, surname, role) VALUES('easy', 'd.dziersan@dallmann-bau.de', 'Dziersan','Dominik','admin');

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE PROJECT;
INSERT INTO PROJECT (name, street, postcode, city) VALUES('Landstraße','K187','49624','Lönningen');
INSERT INTO PROJECT (name, street, postcode, city) VALUES('Ortsumgehung','K643','49661','Cloppenburg');
INSERT INTO PROJECT (name, street, postcode, city) VALUES('Landstraße','K187','49624','Lönningen');
INSERT INTO PROJECT (name, street, postcode, city) VALUES('Ortsanierung','Lindenstraße','49808','Lingen');
INSERT INTO PROJECT (name, street, postcode, city) VALUES('Autobahn','A1','26127','Oldenburg');
INSERT INTO PROJECT (name, street, postcode, city) VALUES('Bundestraße','B187','49624','Lönningen');

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE LOCATION;
INSERT INTO LOCATION (speed, angle, longitude, latitude, altitude, timestamp) VALUES(0,0,8.037338333333333,52.27987,0,timestamp('2020-08-22 13:07:33'));
INSERT INTO LOCATION (speed, angle, longitude, latitude, altitude, timestamp) VALUES(0,0,9.037338333333333,50.27987,0,timestamp('2020-08-22 14:07:33'));
INSERT INTO LOCATION (speed, angle, longitude, latitude, altitude, timestamp) VALUES(0,0,15.037338333333333,50.27987,0,timestamp('2020-08-22 16:07:33'));
INSERT INTO LOCATION (speed, angle, longitude, latitude, altitude, timestamp) VALUES(0,0,22.037338333333333,50.27987,0,timestamp('2020-07-22 14:07:33'));
INSERT INTO LOCATION (speed, angle, longitude, latitude, altitude, timestamp) VALUES(0,0,22.037338333333333,53.27987,0,timestamp('2020-05-22 16:07:33'));

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE CATEGORY;
INSERT INTO CATEGORY(major, category) VALUES('0001','Rüttelplatten');
INSERT INTO CATEGORY(major, category) VALUES('0002','Stampfer');
INSERT INTO CATEGORY(major, category) VALUES('0003','Motorflex');
INSERT INTO CATEGORY(major, category) VALUES('0004','Rohrgreifer');
INSERT INTO CATEGORY(major, category) VALUES('0005','Kettensägen');
INSERT INTO CATEGORY(major, category) VALUES('0006','Motorhammer');
INSERT INTO CATEGORY(major, category) VALUES('0007','Leiter');
INSERT INTO CATEGORY(major, category) VALUES('0008','Exoten');

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE BEACON;
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0001', '0001');
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0002', '0001');
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0003', '0001');
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0004', '0001');
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0005', '0001');
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0006', '0001');
INSERT INTO BEACON VALUES('DA95206921ED84C1ED97EA92306C5A7F', '0007', '0001');

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE BEACON_POSITION;
INSERT INTO BEACON_POSITION VALUES('0001','0001', 1, -78);
INSERT INTO BEACON_POSITION VALUES('0001','0001', 2, -15);
INSERT INTO BEACON_POSITION VALUES('0001','0001', 3, -64);
INSERT INTO BEACON_POSITION VALUES('0001','0001', 4, 0);
INSERT INTO BEACON_POSITION VALUES('0002','0001', 2, -89);
INSERT INTO BEACON_POSITION VALUES('0002','0001', 3, -70);
INSERT INTO BEACON_POSITION VALUES('0002','0001', 4, -35);
INSERT INTO BEACON_POSITION VALUES('0002','0001', 5, -50);
INSERT INTO BEACON_POSITION VALUES('0003','0001', 3, -35);
INSERT INTO BEACON_POSITION VALUES('0004','0001', 4, -90);
INSERT INTO BEACON_POSITION VALUES('0005','0001', 5, -78);
INSERT INTO BEACON_POSITION VALUES('0006','0001', 3, 0);
INSERT INTO BEACON_POSITION VALUES('0007','0001', 4, -56);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE DEVICE_STATUS;
INSERT INTO  DEVICE_STATUS (description)  VALUES ('Verfügbar');
INSERT INTO  DEVICE_STATUS (description)  VALUES ('Ausgeliehen');
INSERT INTO  DEVICE_STATUS (description)  VALUES ('In Wartung');
INSERT INTO  DEVICE_STATUS (description)  VALUES ('Außer Betrieb');
INSERT INTO  DEVICE_STATUS (description)  VALUES ('Defekt');
INSERT INTO  DEVICE_STATUS (description)  VALUES ('Verschollen/Verschwunden');
INSERT INTO  DEVICE_STATUS (description)  VALUES ('Gestohlen');

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE DEVICE;
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(758921, 0235112, '2020-05-09', 'Hi', 1, '0001', '0001', "juckt niemanden", "same shit again",1,1,1,1);
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(654123, 0245212, '2020-07-10', 'Test', 2, '0001', '0002',"juckt niemanden", "same shit again",1,1,1,1);
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(569847, 0456587, '2020-05-22', 'POeter', 3, '0001', '0003',"juckt niemanden", "same shit again",1,1,1,1);
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(159742, 5742854, '2022-12-25', 'LOL', 4, '0001', '0004',"juckt niemanden", "same shit again",1,1,1,1);
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(234567, 4861563, '2024-10-19', 'xD', 5, '0001', '0005',"juckt niemanden", "same shit again",1,1,1,1);
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(745921, 1487422, '2020-05-09', 'Hier könnt ihr text stehen', 6, '0001', '0006',"juckt niemanden", "same shit again",1,1,1,1);
INSERT INTO DEVICE (inventory_number, serial_number, gurantee, note, device_status, beacon_minor, beacon_major, model, manufacturer, latest_uvv, latest_tuev, latest_repair, latest_position)
VALUES(547896, 9875621, '2020-05-09', 'Hi', 7, '0001', '0007',"juckt niemanden", "same shit again",1,1,1,1);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE REPAIR;
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (758921, current_timestamp(), false, 'NEUER MOTOR');
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (654123, current_timestamp(), true, 'NEUE REIFEN');
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (569847, current_timestamp(), false, 'NEUER FEDERN');
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (159742, current_timestamp(), false, 'NEUER VERGASE');
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (234567, current_timestamp(), true, 'Kilometerstand: 494');
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (745921, current_timestamp(), false, 'Testing');
INSERT INTO REPAIR (inventory_number, timestamp, status, note) VALUES (547896, current_timestamp(), false, 'Spiegel ab');

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE TUEV;
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (758921, current_timestamp(), false);
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (654123, current_timestamp(), true);
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (569847, current_timestamp(), false);
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (159742, current_timestamp(), true);
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (234567, current_timestamp(), false);
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (745921, current_timestamp(), true);
INSERT INTO TUEV (inventory_number, timestamp, status) VALUES (547896, current_timestamp(), false);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE UVV;
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (758921, current_timestamp(), false);
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (654123, current_timestamp(), true);
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (569847, current_timestamp(), false);
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (159742, current_timestamp(), true);
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (234567, current_timestamp(), false);
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (745921, current_timestamp(), true);
INSERT INTO UVV (inventory_number, timestamp, status) VALUES (547896, current_timestamp(), false);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE BORROWS;
INSERT INTO BORROWS VALUES(DATE('2020-08-22'),DATE('2020-08-25'), DATE('2020-08-25') - DATE('2020-08-22'), 1,758921,1);
INSERT INTO BORROWS VALUES(DATE('2020-08-14'),DATE('2020-09-25'), DATE('2020-08-25') - DATE('2020-08-22'), 6,745921,3);
INSERT INTO BORROWS VALUES(DATE('2020-08-22'),DATE('2020-08-25'), DATE('2020-08-25') - DATE('2020-08-22'), 2,569847,4);
INSERT INTO BORROWS VALUES(DATE('2020-08-22'),DATE('2020-08-25'), DATE('2020-08-25') - DATE('2020-08-22'), 4,159742,2);