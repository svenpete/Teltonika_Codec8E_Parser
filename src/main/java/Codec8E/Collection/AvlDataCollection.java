/** AvlDataCollection
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */


package Codec8E.Collection;

import Codec8E.AVL.AvlData;
import Codec8E.FieldEncoding;

import java.util.ArrayList;
import java.util.List;

import static Codec8E.Decoder.hexCode;


public class AvlDataCollection {



    private int preAmble;
    private int dataFieldLength;
    private int codecId;

    private int numberOfData1;
    private int numberOfData2;

    private List<AvlData> avlDataList;

    private int actualPosition;
    private int internalPosition;



   public AvlDataCollection(){
        actualPosition = 0;
        setPreAmble();
        setDataFieldLength();
        setCodecID();
        setNumberOfData1();
        setAvlDataList();
        setNumberOfData2();

    }

    private void setAvlDataList(){
       avlDataList = new ArrayList<>();
        for (int i = 0; i < numberOfData1; i++) {

            if (i > 0)
              this.actualPosition = avlDataList.get(avlDataList.size() - 1).getIoData().getActualPosition();

        avlDataList.add(new AvlData(actualPosition));
        }
   }

    private void setNumberOfData2(){
        actualPosition = avlDataList.get(avlDataList.size() - 1).getIoData().getActualPosition();
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.numberOfData2 = getElementValue(internalPosition);
    }

    // preamble length is a fix value normaly start at 8 because of zero we start with
    private void setPreAmble(){
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        this.preAmble = getElementValue(internalPosition);
    }

    private void setDataFieldLength(){
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        this.dataFieldLength = getElementValue(internalPosition);
    }

    private void setCodecID(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.codecId = getElementValue(internalPosition);
    }

    private void setNumberOfData1(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.numberOfData1 = getElementValue(internalPosition);
    }

    private Integer getElementValue(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        Integer value = Integer.parseInt(elementHexCode,16);

        actualPosition = internalPosition;
        return value;
    }

    public int getPreAmble() {
        return preAmble;
    }

    public int getDataFieldLength() {
        return dataFieldLength;
    }

    public int getCodecId() {
        return codecId;
    }

    public int getNumberOfData1() {
        return numberOfData1;
    }

    public int getNumberOfData2() {
        return numberOfData2;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public int getInternalPosition() {
        return internalPosition;
    }

}
