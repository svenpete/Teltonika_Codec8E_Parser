/** AvlData
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package Codec8E.AVL;
import Codec8E.GPS.GpsData;
import Codec8E.IO.IOData;
import Codec8E.Reader;
import java.sql.Timestamp;

public class AvlData {

    private Timestamp timeStamp;
    private int priority;
    private GpsData gpsData;
    private IOData ioData;

    private Reader reader;

    public AvlData(Reader reader){
        this.reader = reader;
        setTimeStamp();
        setPriority();
        setGpsData();
        setIoData();

    }

    private void setGpsData(){
        gpsData = new GpsData(reader);
    }

    private void setIoData(){
        ioData = new IOData(reader);
    }

    private void setTimeStamp(){
        Long milliSeconds = reader.readLong16();
        this.timeStamp = calculateTimeStamp( milliSeconds);
    }

    /** This method calculates the timestamp based on the unix time.
     *
     * @param milliSeconds from which the timestamp will be generated.
     * @return the timestamp calculated from the milliseconds
     */
    private Timestamp calculateTimeStamp(Long milliSeconds){
        // adding 2 hours because of timezone difference
        int timeToCorrect = 7200000;
        long correctedMilliSeconds = milliSeconds - timeToCorrect;

        Timestamp timestamp = new Timestamp(correctedMilliSeconds);
        return  timestamp;
    }

    private void setPriority() {
        this.priority = reader.readInt2();
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public int getPriority() {
        return priority;
    }

    public GpsData getGpsData() {
        return gpsData;
    }

    public IOData getIoData() {
        return ioData;
    }
}
