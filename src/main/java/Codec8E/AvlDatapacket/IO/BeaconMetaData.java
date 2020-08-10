package Codec8E.AvlDatapacket.IO;

import static Codec8E.Codec8.hexCode;

public class BeaconMetaData {

    private int recordCount;
    private int totalRecord;
    private String beaconDataPart;
    private int beaconLength;


    private int actualPosition;
    private int internalPosition;

    BeaconMetaData(int actualPosition){
        this.actualPosition = actualPosition;
        setBeaconLength();
        setRecordCount();
        setTotalRecord();
        setBeaconDataPart();
    }



    private void setBeaconDataPart(){
        String s = String.valueOf(recordCount);
        String t = String.valueOf(totalRecord);
        this.beaconDataPart = s + t;
    }

    private void setRecordCount(){
        internalPosition = actualPosition + FieldEncoding.byte1.getElement();
        this.recordCount = getElementValue(internalPosition);
    }

    private void setTotalRecord(){
        internalPosition = actualPosition + FieldEncoding.byte1.getElement();
        this.totalRecord = getElementValue(internalPosition);
    }


    private Integer getElementValue(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        Integer value = Integer.parseInt(elementHexCode,16);

        actualPosition = internalPosition;
        return value;
    }

    private void setBeaconLength(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.beaconLength = getElementValue(internalPosition);
    }

    public int getRecordCount() {
        return recordCount;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public String getBeaconDataPart() {
        return beaconDataPart;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public int getInternalPosition() {
        return internalPosition;
    }

    public int getBeaconLength() {
        return beaconLength;
    }
}
