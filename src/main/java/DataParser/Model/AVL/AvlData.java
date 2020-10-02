/** AvlData
 * <p>
 *     Version 3
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Model.AVL;
import DataParser.Model.GPS.GpsData;
import DataParser.Model.IO.IOElement;
import java.sql.Timestamp;

/**
 * This class handels in
 */
public class AvlData {

    private String priority;
    private Timestamp timeStamp;
    private GpsData gpsData;
    private IOElement ioElement;


    public AvlData(String priority, Timestamp timeStamp, GpsData gpsData, IOElement ioElement) {
        this.timeStamp = timeStamp;
        this.gpsData = gpsData;
        this.ioElement = ioElement;
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public GpsData getGpsData() {
        return gpsData;
    }

    public void setGpsData(GpsData gpsData) {
        this.gpsData = gpsData;
    }

    public IOElement getIoElement() {
        return ioElement;
    }

    public void setIoElement(IOElement ioElement) {
        this.ioElement = ioElement;
    }
}
