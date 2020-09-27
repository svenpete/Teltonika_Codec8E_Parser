/** GpsData
 * <p>
 *     Version 3
 * </p>
 * Änderungsdatum 27.09.2020
 */

package DataParser.Model.GPS;

public class GpsData {

    private Double longitude;
    private Double latitude;
    private int altitude;

    private int angle;
    private int satellite;
    private int speed;



    public GpsData(Double longitude, Double latitude, int alti, int angle, int satellite, int speed ){

        setLongitude(longitude);
        setLatitude(latitude);
        this.altitude = alti;
        this.angle = angle;
        this.satellite = satellite;
        this.speed = speed;
    }

    private void setLongitude(Double longitude){
        this.longitude = longitude * 0.0000001;
    }

    private void setLatitude(Double latitude){
        this.latitude = latitude * 0.0000001;
    }

    public static boolean isLongiValid(Double longitude){
        return -180 <= longitude && longitude <= 180;
    }


    public static boolean isLatiValid(Double latitude){
        return  -90 <= latitude && latitude <= 90;
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
