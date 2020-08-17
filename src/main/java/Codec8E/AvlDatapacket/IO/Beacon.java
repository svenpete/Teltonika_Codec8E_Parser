/** Beacon
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */
package Codec8E.AvlDatapacket.IO;

import Codec8E.AvlDatapacket.FieldEncoding;

import static Codec8E.Decoder.hexCode;

public class Beacon {





    private String uuid;
    private String major;
    private String minor;
    private String signalStrength;

    private int internalPosition;
    private int actualPosition;

    private String bleBeaconFlag;
    private String BeaconType;
    private boolean signalStrengthAvaible;
    private int rssi;

    private int beaconDataLength;


    Beacon(int actualPosition){
            this.actualPosition = actualPosition;


            setBleBeaconFlag();
            setSignalStrengthAvaible();
            setBeaconType();
            setUUID();
            setMajor();
            setMinor();
            setRssi();

    }

    /**
     *  This method parses the received beacon-flag to binary and checks afterwards the first character of the received
     *  binary string.
     *  1 = signal strength available
     *  0 = signal strength not available
     */
    private void setSignalStrengthAvaible(){
        String beaconFlagBinary = Integer.toBinaryString(Integer.parseInt(bleBeaconFlag));
        if(beaconFlagBinary.indexOf(0) == 1) {
            this.signalStrengthAvaible = true;
        } else {
            this.signalStrengthAvaible = false;
        }
    }



    /** This method determines of which type the beacon is.
     *  If fifth index from binary string is 1 the beacon type will be iBeacon otherwise it is Eddystone.
     */
    private void setBeaconType(){
        String beaconTypeBinary = Integer.toBinaryString(Integer.parseInt(bleBeaconFlag,16));
        char toCheck = beaconTypeBinary.charAt(5);
        if ( toCheck == '1') {
            this.BeaconType = "iBeacon";
        } else {
            this.BeaconType = "Eddystone";
        }
    }



    private void setUUID(){
        internalPosition = actualPosition + FieldEncoding.byte32.getElement();
        this.uuid = getElement(internalPosition);
    }

    private void setMajor(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.major = getElement(internalPosition);
    }

    private void setMinor(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.minor = getElement(internalPosition);
    }

    /**
     *  This method sets the received signal strength indication (=rssi).
     *  The received value from the hex code needs to be parsed in binary 2 complement and then be parsed to decimal.
     */
    private void setRssi(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        String binary = convertToBinary(Integer.parseInt(getElement(internalPosition),16));

        StringBuffer stringBuffer = new StringBuffer(binary);
        this.rssi = -1 * Integer.parseInt(findTwoscomplement(stringBuffer),2); // temp solution
    }

    /**
     *  This method sets the received beacon-flag from the hex code.
     *  The beacon-flag owns information about the beacon-type is
     */
    private void setBleBeaconFlag(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.bleBeaconFlag = hexCode.substring(actualPosition, internalPosition);
        actualPosition = internalPosition;
    }





    private String convertToBinary(int toConvert){
        return Integer.toBinaryString(toConvert);
    }

    public String getUuid(){
        return this.uuid;
    }

    public String getMajor(){
        return this.major;
    }

    public String getMinor(){
        return this.minor;
    }

    public String getSignalStrength(){
        return this.signalStrength;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public int getRssi() {
        return rssi;
    }

    static String findTwoscomplement(StringBuffer str)
    {
        int n = str.length();

        // Traverse the string to get first '1' from
        // the last of string
        int i;
        for (i = n-1 ; i >= 0 ; i--)
            if (str.charAt(i) == '1')
                break;

        // If there exists no '1' concat 1 at the
        // starting of string
        if (i == -1)
            return "1" + str;

        // Continue traversal after the position of
        // first '1'
        for (int k = i-1 ; k >= 0; k--)
        {
            //Just flip the values
            if (str.charAt(k) == '1')
                str.replace(k, k+1, "0");
            else
                str.replace(k, k+1, "1");
        }

        // return the modified string
        return str.toString();
    }

    public int getInternalPosition() {
        return internalPosition;
    }

    public String getBleBeaconFlag() {
        return bleBeaconFlag;
    }

    public String getBeaconType() {
        return BeaconType;
    }

    public boolean isSignalStrengthAvaible() {
        return signalStrengthAvaible;
    }

    private String getElement(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        actualPosition = internalPosition;
        return elementHexCode;
    }

    private Integer getElementValue(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        Integer value = Integer.parseInt(elementHexCode,16);

        actualPosition = internalPosition;
        return value;
    }


}
