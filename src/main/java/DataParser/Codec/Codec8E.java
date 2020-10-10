/**
 * DataParser
 * <p>
 * Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 28.09.2020
 */
package DataParser.Codec;
import DataParser.BitConverter;
import DataParser.Model.AvlData;
import DataParser.Model.AvlPacket;
import DataParser.Model.AvlDataPriority;
import DataParser.Model.GpsData;
import DataParser.Model.Beacon;
import DataParser.Model.IOElement;
import DataParser.Model.IoProperty;
import DataParser.HexReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * This class handle's the decoding of the 'Codec8 Extended Protocol' from Teltonika.
 */
public class Codec8E {

    private HexReader hexReader;


    /**
     * Class constructor specifying an object of HexReader for decoding process.
     * @param hexReader contains the data for decoding process.
     */
    public Codec8E(HexReader hexReader) {
        this.hexReader = hexReader;

    }



    /**
     * The decodeAvlPacket method decodes the hex data and returns an instance of AvlPacket.
     * @return AvlPacket with decoded Data.
     */
    public AvlPacket decodeAvlPacket(){

        int codecId = hexReader.readInt2();
        int dataCount = hexReader.readInt2();
        List<AvlData> data = new ArrayList<>();

        for (int i = 0; i < dataCount; i++) {
            AvlData avlData = decodeAvlData();
            data.add(avlData);
        }

        return new AvlPacket(codecId, dataCount, data);
    }

    /**
     * The decodeAvlData method decodes the avl-data and handels the decoding for gps, i/o-element and beacons which
     * belong to the avl-data.
     * @return avl-data
     */
    private AvlData decodeAvlData() {
        Timestamp timestamp = decodeTimestamp(hexReader.readLong16());

        String priority = decodePriority(hexReader.readInt2());


        // GPS element decoding
        GpsData gpsElement = decodeGpsElement();


        // IO Element decoding
        int eventId = hexReader.readInt4();
        int propertiesCount = hexReader.readInt4();


        // IO Element Properties decoding
        List<IoProperty> ioProperties = decodeIoProperties();
        List<Beacon> beacons = decodeNxProperties();

        IOElement ioElement = new IOElement(eventId, propertiesCount, ioProperties, beacons);

        return new AvlData(priority, timestamp, gpsElement, ioElement);

    }

    /**
     * The decodePriority method decodes the received data priority.
     * @param priority the data priority
     * @return the decoded data priority
     */
    private String decodePriority(int priority){

        for (AvlDataPriority prior :
                AvlDataPriority.values()) {

            if ( prior.getPriority() == priority ) return String.valueOf(prior);
        }

        return null;
    }


    /**
     * The decodeGpsElement method decodes gps-data and returns an GpsData object.
     * @return object of GpsData with decoded data.
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
     * The decodeIoProperties method decodes input/output properties which the hex string contains except beacon information.
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
     * The decodeNxProperties method decodes the nx-properties containing multiplebeacon data.
     * NX-properties do have variable lengths therefore these properties contain a length element.
     * @return List<Beacon>
     */
    private List<Beacon> decodeNxProperties() {

        List<Beacon> beacons = new ArrayList<>();


        int ioCountX = hexReader.readInt4();

        for (int i = 0; i < ioCountX; i++) {
            int propertyId = hexReader.readInt4();
            int elementLength = hexReader.readInt4();

            // propertyId  385 are beacons sent to the server so just continue if id is equal to 385.
            if ( propertyId == 385 ) {
                //hex don't have a value which tells how many beacons are stored therefore we need beaconSize and Length
                int beaconSize = 22;

                int receivedBeacons = (elementLength - 1) / beaconSize;


                String dataPart = hexReader.readString(2);

                for (int j = 0; j < receivedBeacons; j++) {
                    Beacon beacon = decodeBeacon();

                    beacons.add(beacon);
                }

            } else {
                //reposition reader to continue without bugs.
                hexReader.readString(elementLength);

            }
        }

        return beacons;
    }

    private Beacon decodeBeacon(){

        int bleFlag = hexReader.readInt2();
        boolean signalAvailable = decodeSignalAvailable(bleFlag);
        String type = decodeBeaconType(bleFlag);

        String uuid = hexReader.readString32();
        String major = hexReader.readString4();
        String minor = hexReader.readString4();
        int rssi = hexReader.readInt2();

        return new Beacon(type, signalAvailable, uuid, major, minor, decodeRssi(rssi));
    }

    /**
     * The decodeSignalAvailable method parses the received beacon-flag to binary and checks afterwards the first
     * character of the received binary string.
     * 1 = signal strength available
     * 0 = signal strength not available
     * @param bleFlag data containing beacon metadata.
     * @return  true or false based on the
     */
    private boolean decodeSignalAvailable(int bleFlag) {
        String beaconFlagBinary = Integer.toBinaryString(bleFlag);
        return beaconFlagBinary.indexOf(0) == 1 ? true : false;
    }

    /**
     * The decodeBeaconType method determines the beacon protocol format.
     * If fifth index from binary string is 1 the beacon type will be iBeacon otherwise it is Eddystone.
     * @param  bleFlag data containing beacon metadata
     * @return String containing beacon format.
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
     * The decodeRssi method sets the received signal strength indication (rssi).
     * The received value from the hex code needs to be parsed in binary 2 complement and then be parsed to decimal.
     * @param  hexRssi to be converted.
     * @return rssi value.
     */
    private int decodeRssi(int  hexRssi) {

        String binary = Integer.toBinaryString( hexRssi);

        StringBuffer stringBuffer = new StringBuffer(binary);
        int rssi = -1 * Integer.parseInt(BitConverter.findTwoscomplement(stringBuffer), 2); // temp solution
        return rssi;
    }

    /**
     * The decodeTimestamp method calculates the timestamp based on unix time1.
     * @param milliSeconds from which the timestamp will be generated.
     * @return the timestamp calculated from the milliseconds
     */
    private Timestamp decodeTimestamp(Long milliSeconds) {
        // adding 2 hours because of timezone difference
        int timeToCorrect = 7200000;
        long correctedMilliSeconds = milliSeconds - timeToCorrect;

        Timestamp timestamp = new Timestamp(correctedMilliSeconds);
        return timestamp;
    }



}
