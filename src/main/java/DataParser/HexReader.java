/** DataParser
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser;
import DataParser.Model.FieldEncoding;

/**
 * This class handles the reading of hex code.
 */
public class HexReader {
    private final String HEXCODE;
    private int position;


    public HexReader(String HEXCODE){
        this.HEXCODE = HEXCODE;
        this.position = 0;
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

    private Integer getIntegerValue(int steps){
        return Integer.parseInt(getElement(steps),16);
    }

    /**
     * The getElement method returns a string based on the reader position and given input parameter.
     * @param steps
     * @return string
     */
    private String getElement(int steps){

        int internalPosition = position + steps;
        String elementHexCode = HEXCODE.substring(position, internalPosition );
        position = internalPosition;
        return elementHexCode;
    }

    /**
     * The getLongValue method returns a long value based on reader position and given input steps.
     * @param steps
     * @return long value
     */
    private Long getLongValue(int steps) {


        return Long.parseLong(getElement(steps), 16);
    }

    /**
     * The getStringValue method returns a string based on reader position and given input steps.
     * @param steps size of element
     * @return Sting value
     */
    private String getStringValue(int steps){

        return getElement(steps);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHEXCODE() {
        return HEXCODE;
    }



}
