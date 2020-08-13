package Codec8E.AvlDatapacket.Collection;

import Codec8E.AvlDatapacket.AVL.AvlData;
import Codec8E.AvlDatapacket.FieldEncoding;



public class AvlDataPacket {

    public static String hexCode = "00000000000000708E0100000173DE018F59000000000000000000000000000000000181000100000000000000000001018100431121FDA50693A4E24FB1AFCFC6EB07647825271B271BC221DA95206921ED84C1ED97EA92306C5A7F00020C38B621DA95203921ED84C1ED97EA92306C5A7F00016643BD0100003806";

    private int preAmble;
    private int dataFieldLength;
    private int codecId;

    private int numberOfData1;
    private int numberOfData2;

    private int actualPosition;
    private int internalPosition;

    public AvlData avlData;

   public AvlDataPacket(){
        actualPosition = 0;
        setPreAmble();
        setDataFieldLength();
        setCodecID();
        setNumberOfData1();
        setAvlData();
        setNumberOfData2();

    }

    private void setNumberOfData2(){
        int length = avlData.getIoData().getBeaconData().size() - 1;
        this.actualPosition = avlData.getIoData().getBeaconData().get(length).getActualPosition();
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.numberOfData2 = getElementValue(internalPosition);
    }


    // preamble length is a fix value normaly start at 8 because of zero we start with
    private void setPreAmble(){
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        this.preAmble = getElementValue(internalPosition);
    }


    private void setAvlData(){
       this.avlData = new AvlData(actualPosition);
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

    public AvlData getAvlData() {
        return avlData;
    }
}
