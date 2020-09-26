package Codec8E;

import static Codec8E.Decoder.hexCode;

public class Reader {
    private int actualPosition;
    private int internalPosition;

    public Reader(){
        this.actualPosition = 0;
        this.internalPosition = 0;
    }

    public int readInt1(){
        return getIntegerValue(FieldEncoding.byte1.getElement());
    }

    public int readInt2(){
        return getIntegerValue(FieldEncoding.byte2.getElement());
    }

    public int readInt4(){
        return getIntegerValue(FieldEncoding.byte4.getElement());
    }

    public int readInt8(){
        return getIntegerValue(FieldEncoding.byte8.getElement());
    }


    public String readString4(){
        return getStringValue(FieldEncoding.byte4.getElement());
    }

    public String readString32(){
        return getStringValue(FieldEncoding.byte32.getElement());
    }

    public Long readLong8(){
        return getLongValue(FieldEncoding.byte8.getElement());
    }

    public Long readLong16(){
        return getLongValue(FieldEncoding.byte16.getElement());
    }

    public String readString(int steps){ return  getElement(steps);}

    public void setInternalPosition(int internalPosition) {
        this.internalPosition = internalPosition;
    }

    private Integer getIntegerValue(int steps){
        return Integer.parseInt(getElement(steps),16);
    }

    //decode
    private String getElement(int steps){

        internalPosition = actualPosition + steps;
        String elementHexCode = hexCode.substring(actualPosition, internalPosition );
        actualPosition = internalPosition;
        return elementHexCode;
    }



    private Long getLongValue(int steps) {


        return Long.parseLong(getElement(steps), 16);
    }

    private String getStringValue(int steps){

        return getElement(steps);
    }

    public void setActualPosition(int actualPosition) {
        this.actualPosition = actualPosition;
    }

}
