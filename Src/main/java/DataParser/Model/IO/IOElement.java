/** IOElement
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 27.09.2020
 */

package DataParser.Model.IO;

import java.util.List;

public class IOElement {

    private int eventID;
    private int propertiesCount;
    private List<IoProperty> properties;
    private List<Beacon> beacons;

    public IOElement(int eventID, int propertiesCount, List<IoProperty> properties, List<Beacon> beacons) {
        this.eventID = eventID;
        this.propertiesCount = propertiesCount;
        this.properties = properties;
        this.beacons = beacons;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getPropertiesCount() {
        return propertiesCount;
    }

    public void setPropertiesCount(int propertiesCount) {
        this.propertiesCount = propertiesCount;
    }

    public List<IoProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<IoProperty> properties) {
        this.properties = properties;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }
}
