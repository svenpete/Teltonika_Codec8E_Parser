/** Beacon
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */
package DataParser.Model.IO;

public class Beacon {

    private String uuid;
    private String major;
    private String minor;
    private int rssi;


    private String beaconType;
    private boolean signalStrengthAvaible;



    public Beacon(String beaconType, Boolean signalStrengthAvaible, String uuid, String major, String minor, int rssi) {
        this.beaconType = beaconType;
        this.signalStrengthAvaible = signalStrengthAvaible;
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

    public boolean isSignalStrengthAvaible() {
        return signalStrengthAvaible;
    }

    public void setSignalStrengthAvaible(boolean signalStrengthAvaible) {
        this.signalStrengthAvaible = signalStrengthAvaible;
    }
}
