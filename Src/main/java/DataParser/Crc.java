/** Crc
 * <p>
 *     Version 1
 * </p>
 * Change of date: 23.09.2020
 */


package DataParser;

import java.math.BigInteger;



public class Crc {

    private String polynom;


    public Crc(String polynom){

        this.polynom = polynom;
    }


    public int calculateCrc(){

        return calculateCrc(new BigInteger(polynom,16).toByteArray());
    }


    /**
     * This method calculates a checksum based on CRC-16/IBM. With this you can check if hex code are sent without Resources lose.
     * DataParser.CRC-16
     * CRC-Polynomial x16+x15+x2+1 (0x8005), initial value 0x0000, low bit first, high bit after, result is XOR0
     * 0xA001 is the result of 0x8005 bitwise reversal
     *
     * @param buffer the hexcode from codecic until avl Resources checksum.
     * @return
     */
    public static int calculateCrc(byte[] buffer) {

        int wCRCin = 0x0000;
        int wCPoly = 0xa001;

        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);

            for (int j = 0; j < 8; j++) {

                if ( (wCRCin & 0x0001) != 0 ) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;

                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x0000;
    }

}