# this trigger will insert old device information into the history table.
DELIMITER $$

CREATE TRIGGER insert_into_device_history
    BEFORE UPDATE ON DEVICE
    FOR EACH ROW
BEGIN
    IF OLD.device_status <> NEW.device_status
        OR OLD.latest_uvv <> NEW.latest_uvv
        OR OLD.latest_tuev <> NEW.latest_tuev
        OR OLD.latest_repair <> NEW.latest_repair
        OR OLD.latest_position <> NEW.latest_position
    THEN
        INSERT INTO
            DEVICE_HISTORY(inventory_number,serial_number,gurantee,note,device_status,beacon_minor,beacon_major,model,manufacturer,latest_uvv,latest_tuev,latest_repair,latest_position)
        VALUES
        (OLD.inventory_number, OLD.serial_number, OLD.gurantee, OLD.note, OLD.device_status, OLD.beacon_minor, OLD.beacon_major, OLD.model, OLD.manufacturer,
         OLD.latest_uvv, OLD.latest_tuev, OLD.latest_repair, OLD.latest_position );
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER insert_into_device_history_location
    AFTER INSERT ON BEACON_POSITION
    FOR EACH ROW
BEGIN
    UPDATE DEVICE SET latest_position = new.location_id WHERE beacon_major = NEW.major AND beacon_minor = NEW.minor;
END$$
DELIMITER $$;

DROP TRIGGER insert_into_device_history_location;

SHOW TRIGGERS;


