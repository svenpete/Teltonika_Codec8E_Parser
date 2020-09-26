/** GpsData
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 17.08.2020
 */

package Codec8E.GPS;

import Codec8E.FieldEncoding;
import Codec8E.Reader;

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

   private Reader reader;

    public GpsData(Reader reader){
    this.reader = reader;
    setLongitude();
    setLatitude();
    setAltitude();
    setAngle();
    setSatellites();
    setSpeed();
    }

    private void setLongitude(){
        int value = reader.readInt8();
        this.longitude = value * 0.0000001;
    }

    private void setLatitude(){
        int value = reader.readInt8();
        this.latitude = value * 0.0000001;

    }

    private void setAltitude(){
        this.altitude = reader.readInt4();
    }

    private void setAngle(){
        this.angle = reader.readInt4();
    }

    private void setSatellites(){
        this.satellite = reader.readInt2();
    }

    private void setSpeed(){
        this.speed = reader.readInt4();
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

}
