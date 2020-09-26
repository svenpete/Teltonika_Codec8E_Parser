/** BeaconMetaData
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package Codec8E.IO;

import Codec8E.FieldEncoding;
import Codec8E.Reader;

import static Codec8E.Decoder.hexCode;

public class BeaconMetaData {

    private int recordCount;
    private int totalRecord;
    private String beaconDataPart;

    private Reader reader;

    BeaconMetaData(Reader reader){
        this.reader = reader;

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
        this.recordCount = reader.readInt1();
    }

    private void setTotalRecord(){
        this.totalRecord = reader.readInt1();
    }


}
