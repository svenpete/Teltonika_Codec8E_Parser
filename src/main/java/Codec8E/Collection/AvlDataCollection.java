/** AvlDataCollection
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */


package Codec8E.Collection;

import Codec8E.AVL.AvlData;
import Codec8E.Exceptions.CodecProtocolException;
import Codec8E.Exceptions.PreAmbleLengthException;
import Codec8E.Exceptions.ReceivedDataException;
import Codec8E.FieldEncoding;

import java.util.ArrayList;
import java.util.List;

import static Codec8E.Decoder.hexCode;


public class AvlDataCollection {



    private int preAmble;
    private int dataFieldLength;
    private int codecId;

    private int receivedAmountOfData;
    private int receivedAmountOfDataCheck;

    private List<AvlData> avlDataList;

    private int actualPosition;
    private int internalPosition;



   public AvlDataCollection() throws PreAmbleLengthException, CodecProtocolException, ReceivedDataException {
        actualPosition = 0;
        setPreAmble();
        setDataFieldLength();
        setCodecID();
        setReceivedAmountOfData();
        setAvlDataList();
        setReceivedAmountOfDataCheck();

    }



    private void setAvlDataList(){
       avlDataList = new ArrayList<>();
        for (int i = 0; i < receivedAmountOfData; i++) {

            if (i > 0)
              this.actualPosition = avlDataList.get(avlDataList.size() - 1).getIoData().getActualPosition();

        avlDataList.add(new AvlData(actualPosition));
        }
   }


    private void setReceivedAmountOfDataCheck() throws ReceivedDataException {
        actualPosition = avlDataList.get(avlDataList.size() - 1).getIoData().getActualPosition();
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();

        int receivedAmountOfDataCheck = getElementValue(internalPosition);
        checkReceivedNumberOfData(receivedAmountOfData,receivedAmountOfDataCheck);
        this.receivedAmountOfDataCheck = receivedAmountOfDataCheck;
   }

    // preamble length is a 8 bit long value which contains just zeros 
    private void setPreAmble() throws PreAmbleLengthException {
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        int preAmble = getElementValue(internalPosition);
        checkPreAmble(preAmble);
        this.preAmble = preAmble;

    }

    private void setDataFieldLength(){
        internalPosition = actualPosition + FieldEncoding.byte8.getElement();
        this.dataFieldLength = getElementValue(internalPosition);
    }

    private void setCodecID() throws CodecProtocolException {
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        int codecId = getElementValue(internalPosition);
        checkCodecProtocol(codecId);
        this.codecId = codecId;
   }

    private void setReceivedAmountOfData(){
        internalPosition = actualPosition + FieldEncoding.byte2.getElement();
        this.receivedAmountOfData = getElementValue(internalPosition);
    }

    private Integer getElementValue(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        Integer value = Integer.parseInt(elementHexCode,16);

        actualPosition = internalPosition;
        return value;
    }

    /**
     *  This method check if the received preamble has the correct value.
     *  If the received preamble is incorrect an exception will be thrown and the parse process will be interrupted
     * @param preAmble 
     * @throws PreAmbleLengthException incorrect format exception
     */
    private void checkPreAmble(int preAmble) throws PreAmbleLengthException {
        if (preAmble != 0)
            throw new PreAmbleLengthException(preAmble);
    }

    /**
     *  This method checks if the received amount of data is equal to it's check value by the hexcode.
     * @throws ReceivedDataException
     */
    private void checkReceivedNumberOfData(int receivedAmountOfData, int receivedAmountOfDataCheck) throws ReceivedDataException {
        if (receivedAmountOfData != receivedAmountOfDataCheck)
            throw new ReceivedDataException(receivedAmountOfData, receivedAmountOfDataCheck);
    }

    /**
     * This method checks if the received protocol has the right format. The right format is Code 8 Extended.
     * @throws CodecProtocolException
     */
    private void checkCodecProtocol(int codecId) throws CodecProtocolException {
        if (codecId != 142)  // decoded value for codec 8 extended protocol
            throw new CodecProtocolException(codecId);
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

    public int getReceivedAmountOfData() {
        return receivedAmountOfData;
    }

    public int getReceivedAmountOfDataCheck() {
        return receivedAmountOfDataCheck;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public int getInternalPosition() {
        return internalPosition;
    }

    public List<AvlData> getAvlDataList() {
        return avlDataList;
    }
}
