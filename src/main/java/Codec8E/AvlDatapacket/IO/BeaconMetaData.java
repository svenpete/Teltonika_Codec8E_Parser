/** BeaconMetaData
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package Codec8E.AvlDatapacket.IO;

import Codec8E.AvlDatapacket.FieldEncoding;

import static Codec8E.Decoder.hexCode;

public class BeaconMetaData {

    private int recordCount;
    private int totalRecord;
    private String beaconDataPart;

    private int actualPosition;
    private int internalPosition;

    BeaconMetaData(int actualPosition){
        this.actualPosition = actualPosition;

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

    public int getActualPosition() {
        return actualPosition;
    }

    public int getInternalPosition() {
        return internalPosition;
    }
}
