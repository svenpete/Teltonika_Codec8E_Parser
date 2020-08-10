package Codec8E.AvlDatapacket.IO;

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

    private void setSignalStrengthAvaible(){
        String beaconFlagBinary = Integer.toBinaryString(Integer.parseInt(bleBeaconFlag));
        if(beaconFlagBinary.indexOf(0) == 1) {
            this.signalStrengthAvaible = true;
        } else {
            this.signalStrengthAvaible = false;
        }
    }




    private void setBeaconType(){
        String beaconTypeBinary = Integer.toBinaryString(Integer.parseInt(bleBeaconFlag));
        if (beaconTypeBinary.indexOf(5) == 1) {
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

    private void setRssi(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        String binary = convertToBinary(Integer.parseInt(getElement(internalPosition),16));

        StringBuffer stringBuffer = new StringBuffer(binary);
        this.rssi = -1 * Integer.parseInt(findTwoscomplement(stringBuffer),2); // temp solution
    }

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

    private String getElement(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        actualPosition = internalPosition;
        return elementHexCode;
    }

}
