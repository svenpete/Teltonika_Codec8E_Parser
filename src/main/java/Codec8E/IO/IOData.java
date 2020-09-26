/** IOData
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 11.08.2020
 */



package Codec8E.IO;

import Codec8E.FieldEncoding;
import Codec8E.Reader;

import java.util.ArrayList;
import java.util.List;

import static Codec8E.Decoder.hexCode;

public class IOData {

    private int eventId;
    private int totalElementCount;

    private List<IOElement> n1Elements;
    private List<IOElement> n2Elements;
    private List<IOElement> n4Elements;
    private List<IOElement> n8Elements;
    private List<Beacon> beaconData;
    private BeaconMetaData beaconMetaData;

    private Reader reader;

    public IOData(Reader reader){
        this.reader = reader;
        setEventId();
        setTotalElementCount();
        setN1ElementValues();
        setN2ElementValues();
        setN4ElementValues();
        setN8ElementValues();
        setBeaconElement();
    }

    private void setEventId(){
        this.eventId = reader.readInt4();
    }

    /**
     * This method sets the total elementCount of all received a
     */
    private void setTotalElementCount(){
        this.totalElementCount = reader.readInt4();
    }

    /**
     * This method gets the counter for n1 elements from the hex-code and adds elements data to the n1 list.
     *  n1 elements have a id and a value.
     *  The id is always 4 bit long.
     *  The value of n2 elements is always 2 bit long.
     */
    private void setN1ElementValues(){
        int n1ElementCount = reader.readInt4();

        if (n1ElementCount > 0){
            addN1ElementsToList(n1ElementCount);
        }
    }

    /**
     * This method adds all n1 elements in the io-data to a list
     * @param counter to iterate over the received elements received by hex-code
     */
    private void addN1ElementsToList(int counter){
        n1Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            int value = reader.readInt2();

            IOElement ioElement = new IOElement(reader.readInt4(),value);
            n1Elements.add(ioElement);
        }
    }

    /**
     * This method gets the counter for n2 elements from the hex-code and adds elements data to the n2 list.
     *  n2 elements have a id and a value.
     *  The id is always 4 bit long.
     *  The value of n2 elements is always 4 bit long.
     */
    private void setN2ElementValues(){

        int n1ElementCount = reader.readInt4();

        if (n1ElementCount > 0){
            addN2ElementsToList(n1ElementCount);
        }
    }

    /**
     * This method adds all n2 elements in the io-data to a list.
     * @param counter to iterate over the received elements received by hex-code
     */
    private void addN2ElementsToList(int counter){
        n2Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            int value = reader.readInt4();

            IOElement ioElement = new IOElement(reader.readInt4(),value);
            n2Elements.add(ioElement);
        }
    }

    /**
     * This method gets the counter for n4 elements from the hex-code and adds elements data to the n4 list.
     *  n4 elements have a id and a value.
     *  The id is always 4 bit long.
     *  The value of n4 elements is always 8 bit long.
     */
    private void setN4ElementValues(){

        int n1ElementCount = reader.readInt4();

        if (n1ElementCount > 0){
            addN4ElementsToList(n1ElementCount);
        }
    }

    /**
     * This method adds all n1 elements in the io-data to a list
     * @param counter to iterate over the received elements received by hex-code
     */
    private void addN4ElementsToList(int counter){
        n4Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            int value = reader.readInt8();

            IOElement ioElement = new IOElement(reader.readInt4(), value);
            n4Elements.add(ioElement);

        }
    }

    /**
     * This method gets the counter for n8 elements from the hex-code and adds elements data to the n8 list.
     *  n1 elements have a id and a value.
     *  The id is always 4 bit long.
     *  The value of n8 elements is always 16 bit long.
     */
    private void setN8ElementValues(){
        int n1ElementCount = reader.readInt4();

        if (n1ElementCount > 0){
            addN8ElementsToList(n1ElementCount);
        }
    }

    /**
     * This method adds all n8 elements in the io-data to a list
     * @param counter to iterate over the received elements received by hex-code
     */
    private void addN8ElementsToList(int counter){
        n8Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            // gonna rework
            Long value = reader.readLong16();

            IOElement ioElement = new IOElement(reader.readInt4(), value.intValue());
            n8Elements.add(ioElement);

        }
    }

    /**
     * This method gets the counter for beacon elements from the hex-code and adds elements data to the beacon list.
     * Beacon elements have beacon-flag, uuid, minor, major and rssi value.
     * The id is always 4 bit long.
     * The value of n8 elements is always variable.
     * A 4 bit value between the id and ble-flag determines the size of the received beacon-data.
     */
    private void setBeaconElement(){
        int beaconCount = reader.readInt4();

        if (beaconCount > 0) {
            addBeaconsToList(beaconCount);
        }
    }

    /**
     * This method adds all beacon elements in the io-data to a list.
     * @param counter to iterate over the received elements received by hex-code
     */
    private void addBeaconsToList(int counter){
        this.beaconData = new ArrayList<>();
        int valueToPass = 0;


        // iterate over nx element
        for (int i = 0; i < counter; i++) {

            //gets element id to determine type of xn elements
            int elementIdToCheck = reader.readInt4();


            //get length of received xn element

            int beaconDataLength = reader.readInt4(); // -1 to fix

            if (elementIdToCheck == 385){


                this.beaconMetaData = new BeaconMetaData(reader); // get beaconmeta data



                    while (beaconDataLength - 1  > 0){




                        beaconData.add(new Beacon(reader));

                        beaconDataLength = beaconDataLength - 22;
                    }
                }

            }
        }






    public int getEventId() {
        return eventId;
    }

    public int getTotalElementCount() {
        return totalElementCount;
    }

    public List<IOElement> getN1Elements() {
        return n1Elements;
    }

    public List<IOElement> getN2Elements() {
        return n2Elements;
    }

    public List<IOElement> getN4Elements() {
        return n4Elements;
    }

    public List<IOElement> getN8Elements() {
        return n8Elements;
    }

    public List<Beacon> getBeaconData() {
        return beaconData;
    }

    public BeaconMetaData getBeaconMetaData() {
        return beaconMetaData;
    }


}
