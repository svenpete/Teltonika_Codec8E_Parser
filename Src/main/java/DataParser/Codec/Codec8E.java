/**
 * DataParser
 * <p>
 * Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 28.09.2020
 */


package DataParser.Codec;

import DataParser.Exceptions.CodecProtocolException;
import DataParser.Model.AVL.AvlData;
import DataParser.Model.AVL.AvlDataCollection;
import DataParser.Model.AVL.AvlDataPriority;
import DataParser.Model.GPS.GpsData;
import DataParser.Model.IO.Beacon;
import DataParser.Model.IO.IOElement;
import DataParser.Model.IO.IoProperty;
import DataParser.HexReader;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Codec8E {
    private HexReader hexReader;


    public Codec8E(HexReader hexReader) {
        this.hexReader = hexReader;

    }

    static String findTwoscomplement(StringBuffer str) {
        int n = str.length();

        // Traverse the string to get first '1' from
        // the last of string
        int i;
        for (i = n - 1; i >= 0; i--)
            if ( str.charAt(i) == '1' )
                break;

        // If there exists no '1' concat 1 at the
        // starting of string
        if ( i == -1 )
            return "1" + str;

        // Continue traversal after the position of
        // first '1'
        for (int k = i - 1; k >= 0; k--) {
            //Just flip the values
            if ( str.charAt(k) == '1' )
                str.replace(k, k + 1, "0");
            else
                str.replace(k, k + 1, "1");
        }

        // return the modified string
        return str.toString();
    }

    /**
     * This method decodes the hex String and returns the an instance of avldatacollection. An avldatacollection can have
     * multiple store multiply avldata.
     * @return AvlDataCollection with decoded Data.
     * @throws CodecProtocolException if wrong protocol is used.
     */
    public AvlDataCollection decodeAvlDataCollection() throws CodecProtocolException {

        int codecId = hexReader.readInt2();
        int dataCount = hexReader.readInt2();
        List<AvlData> data = new ArrayList<>();

        for (int i = 0; i < dataCount; i++) {
            AvlData avlData = decodeAvlData();
            data.add(avlData);
        }

        return new AvlDataCollection(codecId, dataCount, data);
    }

    /**
     * This method decodes the avldata and handels the decoding for gps, ioelement and beacons which belong to the avldata.
     * @return avldata with decoded information.
     */
    private AvlData decodeAvlData() {
        Timestamp timestamp = calculateTimeStamp(hexReader.readLong16());

        String priority = decodePriority(hexReader.readInt2());


        // GPS element decoding
        GpsData gpsElement = decodeGpsElement();


        // IO Element decoding
        int eventId = hexReader.readInt4();
        int propertiesCount = hexReader.readInt4();


        // IO Element Properties decoding
        List<IoProperty> ioProperties = decodeIoProperties();
        List<Beacon> beacons = decodeBeacon();

        IOElement ioElement = new IOElement(eventId, propertiesCount, ioProperties, beacons);

        return new AvlData(priority, timestamp, gpsElement, ioElement);

    }


    private String decodePriority(int priority){

        for (AvlDataPriority prior :
                AvlDataPriority.values()) {

            if ( prior.getPriority() == priority ) return String.valueOf(prior);
        }

        return null;
    }


    /**
     * This method decodes gpsdata.
     * @return gpsdata with decoded informations about longi, lati, alti, speed, angle and satelelites.
     */
    private GpsData decodeGpsElement() {
        double longitude = hexReader.readLong8();
        double latitude = hexReader.readLong8();
        int altitude = hexReader.readInt4();
        int angle = hexReader.readInt4();
        int satellites = hexReader.readInt2();
        int speed = hexReader.readInt4();

        return new GpsData(longitude, latitude, altitude, speed, angle, satellites);
    }

    /**
     * This method decodes all ioproperties which the hex string contains except beacon information.
     * @return List<IoProperty> contained by the hex strong
     */
    private List<IoProperty> decodeIoProperties() {
        List<IoProperty> result = new ArrayList<>();
        // total number of I/O properties which length is 1 byte

        int ioCountInt8 = hexReader.readInt4();
        for (int i = 0; i < ioCountInt8; i++) {
            int propertyId = hexReader.readInt4();
            int value = hexReader.readInt2();

            result.add(new IoProperty(propertyId, value));
        }


        // total number of I/O properties which length is 2 bytes
        int ioCountInt16 = hexReader.readInt4();
        for (int i = 0; i < ioCountInt16; i++) {
            int propertyId = hexReader.readInt4();
            int value = hexReader.readInt4();

            result.add(new IoProperty(propertyId, value));
        }


        // total number of I/O properties which length is 4 bytes
        int ioCountInt32 = hexReader.readInt4();
        for (int i = 0; i < ioCountInt32; i++) {
            int propertyId = hexReader.readInt4();
            int value = hexReader.readInt8();

            result.add(new IoProperty(propertyId, value));
        }


        // total number of I/O properties which length is 8 bytes
        int ioCountInt64 = hexReader.readInt4();
        for (int i = 0; i < ioCountInt64; i++) {
            int propertyId = hexReader.readInt4();
            Long value = hexReader.readLong16();

            result.add(new IoProperty(propertyId, value));
        }

        return result;
    }


    /**
     * This method decodes the hex-string and look for beacons to insert.
     * @return List<Beacon> a
     */
    private List<Beacon> decodeBeacon() {

        List<Beacon> beacons = new ArrayList<>();

        int ioCountX = hexReader.readInt4();
        for (int i = 0; i < ioCountX; i++) {
            int propertyId = hexReader.readInt4();
            int elementLength = hexReader.readInt4();

            // propertyId  385 are beacons sent to the Server so just continue if id is equal to 385.
            if ( propertyId == 385 ) {
                //hex don't have a value which tells how many beacons are stored therefore we need beaconSize and Length
                int beaconSize = 22;

                int receivedBeacons = (elementLength - 1) / beaconSize;


                String dataPart = hexReader.readString(2);

                for (int j = 0; j < receivedBeacons; j++) {

                    int bleFlag = hexReader.readInt2();
                    boolean signalAvaible = decodeSignalAvaible(bleFlag);
                    String type = decodeBeaconType(bleFlag);

                    String uuid = hexReader.readString32();
                    String major = hexReader.readString4();
                    String minor = hexReader.readString4();
                    int rssi = hexReader.readInt2();
                    Beacon beacon = new Beacon(type, signalAvaible, uuid, major, minor, decodeRssi(rssi));
                    beacons.add(beacon);
                }

            }

        }

        return beacons;
    }

    /**
     * This method parses the received beacon-flag to binary and checks afterwards the first character of the received
     * binary string.
     * 1 = signal strength available
     * 0 = signal strength not available
     */
    private boolean decodeSignalAvaible(int bleFlag) {
        String beaconFlagBinary = Integer.toBinaryString(bleFlag);
        return beaconFlagBinary.indexOf(0) == 1;
    }

    /**
     * This method determines of which type the beacon is.
     * If fifth index from binary string is 1 the beacon type will be iBeacon otherwise it is Eddystone.
     */
    private String decodeBeaconType(int bleFlag) {
        String beaconTypeBinary = Integer.toBinaryString(bleFlag);
        char toCheck = beaconTypeBinary.charAt(5);
        if ( toCheck == '1' ) {
            return "iBeacon";
        } else {
            return "Eddystone";
        }
    }

    /**
     * This method sets the received signal strength indication (=rssi).
     * The received value from the hex code needs to be parsed in binary 2 complement and then be parsed to decimal.
     */
    private int decodeRssi(int decode) {

        String binary = Integer.toBinaryString(decode);

        StringBuffer stringBuffer = new StringBuffer(binary);
        int rssi = -1 * Integer.parseInt(findTwoscomplement(stringBuffer), 2); // temp solution
        return rssi;
    }

    /**
     * This method calculates the timestamp based on the unix time
     *
     * @param milliSeconds from which the timestamp will be generated.
     * @return the timestamp calculated from the milliseconds
     */
    private Timestamp calculateTimeStamp(Long milliSeconds) {
        // adding 2 hours because of timezone difference
        int timeToCorrect = 7200000;
        long correctedMilliSeconds = milliSeconds - timeToCorrect;

        Timestamp timestamp = new Timestamp(correctedMilliSeconds);
        return timestamp;
    }

}
