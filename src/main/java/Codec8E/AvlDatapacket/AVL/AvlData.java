/** AvlData
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package Codec8E.AvlDatapacket.AVL;
import Codec8E.AvlDatapacket.GPS.GpsData;
import Codec8E.AvlDatapacket.FieldEncoding;
import Codec8E.AvlDatapacket.IO.IOData;

import java.sql.Timestamp;

import static Codec8E.Decoder.hexCode;

public class AvlData {

    private Timestamp timeStamp;
    private int priority;
    private GpsData gpsData;
    private IOData ioData;

    private int actualPosition;
    private int internalPosition;

    public AvlData(int actualPosition){
        this.actualPosition = actualPosition;
        setTimeStamp();
        setPriority();
        setGpsData();
        setIoData();

    }

    private void setGpsData(){
        gpsData = new GpsData(actualPosition);
    }

    private void setIoData(){
        ioData = new IOData(gpsData.getActualPosition());
    }

    private void setTimeStamp(){
        internalPosition = actualPosition + FieldEncoding.byte16.getElement();
        Long milliSeconds = getTimeStampValue(internalPosition);

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

    private void setPriority(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();

        this.priority = getPriorityValue(internalPosition);
    }

    private Integer getPriorityValue(int internalPosition ){
        String getElementHex = hexCode.substring(actualPosition, internalPosition);
        Integer elementValue  = Integer.parseInt(getElementHex,16);

        actualPosition = internalPosition;
        return elementValue;
    }

    private Long getTimeStampValue(int internalPosition ){
        String getElementHex = hexCode.substring(actualPosition, internalPosition);
        Long elementValue  = Long.parseLong(getElementHex,16);

        actualPosition = internalPosition;
        return elementValue;
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

    public int getActualPosition() {
        return actualPosition;
    }

    public int getInternalPosition() {
        return internalPosition;
    }

    public IOData getIoData() {
        return ioData;
    }
}
