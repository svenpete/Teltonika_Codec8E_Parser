/** Beacon
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Model;

/**
 * This class represents beacon data sent by FMB devices.
 */
public class Beacon {

    private String uuid;
    private String major;
    private String minor;
    private String beaconType;
    private int rssi;
    private boolean signalStrengthAvailable;


    public Beacon(String beaconType, Boolean signalStrengthAvailable, String uuid, String major, String minor, int rssi) {
        this.beaconType = beaconType;
        this.signalStrengthAvailable = signalStrengthAvailable;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getBeaconType() {
        return beaconType;
    }

    public void setBeaconType(String beaconType) {
        this.beaconType = beaconType;
    }

    public boolean isSignalStrengthAvailable() {
        return signalStrengthAvailable;
    }

    public void setSignalStrengthAvailable(boolean signalStrengthAvailable) {
        this.signalStrengthAvailable = signalStrengthAvailable;
    }
}

