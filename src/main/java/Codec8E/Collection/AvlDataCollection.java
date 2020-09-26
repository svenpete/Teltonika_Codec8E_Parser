/** AvlDataCollection
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */


package Codec8E.Collection;

import Codec8E.CRC.Crc;
import Codec8E.AVL.AvlData;
import Codec8E.Exceptions.CodecProtocolException;
import Codec8E.Exceptions.CyclicRedundancyCheck;
import Codec8E.Exceptions.PreAmbleLengthException;
import Codec8E.Exceptions.ReceivedDataException;
import Codec8E.FieldEncoding;
import Codec8E.Reader;

import java.util.ArrayList;
import java.util.List;

import static Codec8E.Decoder.hexCode;


public class AvlDataCollection {



    private int preAmble;
    private int dataFieldLength;
    private int codecId;

    private int receivedAmountOfData;
    private int receivedAmountOfDataCheck;
    private int crc;

    private List<AvlData> avlDataList;

    private Reader reader;



   public AvlDataCollection(Reader reader) throws PreAmbleLengthException, CodecProtocolException, ReceivedDataException,
           CyclicRedundancyCheck {

       this.reader = reader;

        setPreAmble();
        setDataFieldLength();
        setCodecID();
        setReceivedAmountOfData();
        setAvlDataList();
        setReceivedAmountOfDataCheck();
        setCrc();
        checkCrc();
    }

    private void setCrc() {
     this.crc = reader.readInt8();
    }



    private void setAvlDataList(){
       avlDataList = new ArrayList<>();
        for (int i = 0; i < receivedAmountOfData; i++) {

        avlDataList.add(new AvlData(reader));
        }
   }


    private void setReceivedAmountOfDataCheck() throws ReceivedDataException {

        int receivedAmountOfDataCheck = reader.readInt2();
        checkReceivedNumberOfData(receivedAmountOfData,receivedAmountOfDataCheck);
        this.receivedAmountOfDataCheck = receivedAmountOfDataCheck;
   }

    // preamble length is a 8 bit long value which contains just zeros 
    private void setPreAmble() throws PreAmbleLengthException {
        int preAmble = reader.readInt8();
        checkPreAmble(preAmble);
        this.preAmble = preAmble;

    }

    private void setDataFieldLength(){
       this.dataFieldLength = reader.readInt8();
    }

    private void setCodecID() throws CodecProtocolException {
        int codecId = reader.readInt2();
        checkCodecProtocol(codecId);
        this.codecId = codecId;
   }

    private void setReceivedAmountOfData(){
        this.receivedAmountOfData = reader.readInt2();
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

    private void checkCrc() throws CyclicRedundancyCheck {
        Crc crc = new Crc(getHexForCrc());
        int checkValue = crc.calculateCrc();
        if( this.crc != checkValue) throw new CyclicRedundancyCheck(checkValue, this.crc);

    }

    private String getHexForCrc(){
        String t =  hexCode.substring(FieldEncoding.byte16.getElement(), hexCode.length() - FieldEncoding.byte8.getElement());
        return t;
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


    public List<AvlData> getAvlDataList() {
        return avlDataList;
    }
}
