package Codec8E.AvlDatapacket.IO;

import Codec8E.AvlDatapacket.FieldEncoding;

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

    private int actualPosition;
    private int internalPosition;

    public IOData(int actualPosition){
        this.actualPosition = actualPosition;
        setEventId();
        setTotalElementCount();
        setN1ElementValues();
        setN2ElementValues();
        setN4ElementValues();
        setN8ElementValues();

        setBeaconElement();
    }

    private void setEventId(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.eventId = getElementValue(internalPosition);
    }

    private void setTotalElementCount(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        this.totalElementCount = getElementValue(internalPosition);
    }

    private void setN1ElementValues(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        int n1ElementCount = getElementValue(internalPosition);

        if (n1ElementCount > 0){
            addN1ElementsToList(n1ElementCount);
        }
    }

    private void addN1ElementsToList(int counter){
        n1Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            internalPosition = actualPosition + FieldEncoding.byte2.getElement();
            int value = getElementValue(internalPosition);

            IOElement ioElement = new IOElement(getElementId(),value);
            n1Elements.add(ioElement);
        }
    }

    private void setN2ElementValues(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        int n1ElementCount = getElementValue(internalPosition);

        if (n1ElementCount > 0){
            addN2ElementsToList(n1ElementCount);
        }
    }

    private void addN2ElementsToList(int counter){
        n2Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            internalPosition = actualPosition + FieldEncoding.byte4.getElement();
            int value = getElementValue(internalPosition);

            IOElement ioElement = new IOElement(getElementId(),value);
            n2Elements.add(ioElement);
        }
    }

    private void setN4ElementValues(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        int n1ElementCount = getElementValue(internalPosition);

        if (n1ElementCount > 0){
            addN4ElementsToList(n1ElementCount);
        }
    }

    private void addN4ElementsToList(int counter){
        n4Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            internalPosition = actualPosition + FieldEncoding.byte8.getElement();
            int value = getElementValue(internalPosition);

            IOElement ioElement = new IOElement(getElementId(), value);
            n4Elements.add(ioElement);

        }
    }

    private void setN8ElementValues(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        int n1ElementCount = getElementValue(internalPosition);

        if (n1ElementCount > 0){
            addN8ElementsToList(n1ElementCount);
        }
    }

    private void addN8ElementsToList(int counter){
        n8Elements = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            internalPosition = actualPosition + FieldEncoding.byte16.getElement();
            int value = getElementValue(internalPosition);

            IOElement ioElement = new IOElement(getElementId(), value);
            n8Elements.add(ioElement);

        }
    }

    private void setBeaconElement(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        int beaconCount = getElementValue(internalPosition);

        if (beaconCount > 0) {
            addBeaconsToList(beaconCount);
        }
    }

    private void addBeaconsToList(int counter){
        this.beaconData = new ArrayList<>();
        int valueToPass = 0;
        int internalCount = 0;

        // iterate over nx element
        for (int i = 0; i < counter; i++) {
            int toCheck = getElementValue(internalPosition + FieldEncoding.byte4.getElement());
            this.beaconMetaData = new BeaconMetaData(actualPosition);

            // value to determine size of beacon data stored in
            if (i == 0)
                valueToPass = beaconMetaData.getBeaconDataLength() - 1;

            actualPosition = beaconMetaData.getActualPosition();

            if (toCheck == 385){

                if (i > 0)
                    this.actualPosition = beaconData.get(i).getActualPosition();

                    while (valueToPass - 22 >= 0){

                        if (internalCount >= 1)
                            this.actualPosition = beaconData.get(beaconData.size() - 1).getActualPosition();

                        beaconData.add(new Beacon(this.actualPosition));

                        valueToPass = valueToPass - 22;
                        internalCount++;
                    }
            }
        }
    }

    private int getElementId(){
        internalPosition = actualPosition + FieldEncoding.byte4.getElement();
        int value = getElementValue(internalPosition);
        return value;
    }

    private Integer getElementValue(Integer internalPosition){
        String elementHexCode = hexCode.substring(actualPosition, internalPosition);
        Integer value = Integer.parseInt(elementHexCode,16);

        actualPosition = internalPosition;
        return value;
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

    public int getActualPosition() {
        return actualPosition;
    }

    public int getInternalPosition() {
        return internalPosition;
    }
}
