/** GpsData
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 17.08.2020
 */

package Codec8E.GPS;

import Codec8E.FieldEncoding;

import java.util.ArrayList;
import java.util.List;

import static Codec8E.Decoder.hexCode;

public class GpsData {

    private Double longitude;
    private Double latitude;
    private int altitude;

    private int angle;
    private int satellite;
    private int speed;

    private int actualPosition;
    private int internalPosition;

    public GpsData(int actualPosition){
    this.actualPosition = actualPosition;
    setLongitude();
    setLatitude();
    setAltitude();
    setAngle();
    setSatellites();
    setSpeed();
    }

    private void setLongitude(){
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        int value = getElementValue(internalPosition);
        this.longitude = value * 0.0000001;
    }

    private void setLatitude(){
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        int value = getElementValue(internalPosition);
        this.latitude = value * 0.0000001;

    }

    private void setAltitude(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.altitude = getElementValue(internalPosition);
    }

    private void setAngle(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.angle = getElementValue(internalPosition);
    }

    private void setSatellites(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.satellite = getElementValue(internalPosition);
    }

    private void setSpeed(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.speed = getElementValue(internalPosition);
    }

    private Integer getElementValue(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        Integer value = Integer.parseInt(elementHexCode,16);

        actualPosition = internalPosition;
        return value;
    }

    // this method returns a specific attributes need for location table.
    public List<Object> getGPSAttributes(){
        List<Object> attributes = new ArrayList<Object>();
        attributes.add(this.speed);
        attributes.add(this.angle);
        attributes.add(this.longitude);
        attributes.add(this.latitude);
        attributes.add(this.altitude);
        return attributes;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getAngle() {
        return angle;
    }

    public int getSatellite() {
        return satellite;
    }

    public int getSpeed() {
        return speed;
    }

    public int getActualPosition() {
        return actualPosition;
    }
}
