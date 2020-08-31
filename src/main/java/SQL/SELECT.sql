SELECT * FROM DEVICE
INNER JOIN TUEV
ON DEVICE.inventory_number = TUEV.inventory_number;

SELECT * FROM DEVICE
INNER JOIN DEVICE_STATUS
ON DEVICE.device_status = DEVICE_STATUS.device_status_id;

SELECT * FROM WORKER
LEFT JOIN RIGHTS
ON WORKER.role = RIGHTS.role;

SELECT * FROM WORKER 
WHERE e_mail = 's.petersen@dallmann-bau.de' AND password = 'testing';

SELECT DEVICE.inventory_number,designation,serial_number,gurantee,note,category, TUEV.timestamp AS NEXT_TUEV, UVV.timestamp AS NEXT_UVV  FROM DEVICE
INNER JOIN BEACON
ON DEVICE.beacon_major = BEACON.major AND DEVICE.beacon_minor = BEACON.minor
INNER JOIN CATEGORY
ON BEACON.major = CATEGORY.major
INNER JOIN DEVICE_STATUS
ON DEVICE.device_status = DEVICE_STATUS.device_status_id
INNER JOIN TUEV
ON DEVICE.inventory_number = TUEV.inventory_number
INNER JOIN UVV
ON DEVICE.inventory_number = UVV.inventory_number;

SELECT DEVICE.inventory_number,designation,serial_number,gurantee,note,category, TUEV.timestamp AS NEXT_TUEV, UVV.timestamp AS NEXT_UVV  FROM DEVICE
INNER JOIN BEACON
ON DEVICE.beacon_major = BEACON.major AND DEVICE.beacon_minor = BEACON.minor
INNER JOIN CATEGORY
ON BEACON.major = CATEGORY.major;



SELECT DEVICE.inventory_number,designation,serial_number,gurantee,note, longitude, latitude, timesstamp FROM DEVICE
INNER JOIN BEACON
ON DEVICE.beacon_major = BEACON.major AND DEVICE.beacon_minor = BEACON.minor
INNER JOIN BEACON_POSITION 
ON BEACON.major = BEACON_POSITION.major AND BEACON.minor = BEACON_POSITION.minor
INNER JOIN LOCATION
ON BEACON_POSITION.location_id = LOCATION.location_id
WHERE inventory_number = 758921
ORDER BY timesstamp DESC;


SELECT loan_day,loan_end, WORKER.name, WORKER.surname, PROJECT.name AS Baustelle, inventory_number  FROM BORROWS
INNER JOIN PROJECT
ON BORROWS.project_id = BORROWS.project_id
INNER JOIN WORKER
ON BORROWS.worker_id = WORKER.worker_id;