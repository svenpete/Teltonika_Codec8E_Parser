/** Crc
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change of date: 25.09.2020
 */
package DataParser;
import java.math.BigInteger;


/**
 * This class handles the calculation of a crc checksum.
 */
public class Crc {

    private String polynom;


    public Crc(String polynom){
        this.polynom = polynom;
    }


    public int calculateCrc(){

        return calculateCrc(new BigInteger(polynom,16).toByteArray());
    }


    /**
     * The calculateCrc method calculates a checksum based on CRC-16/IBM.
     * CRC-Polynomial x16+x15+x2+1 (0x8005), initial value 0x0000, low bit first, high bit after, result is XOR0
     * 0xA001 is the result of 0x8005 bitwise reversal
     * @param buffer the hexcode from codecic until avl Resources checksum.
     * @return int based on crc caluclation.
     */
    private static int calculateCrc(byte[] buffer) {

        int crcValue = 0x0000;
        int crcPolynom = 0xa001;

        //calc crc for every byte
        for (byte byteBuffer : buffer) {

            //define 0xff for an int literal for desired value
            crcValue ^= ((int) byteBuffer & 0x00ff);

            for (int j = 0; j < 8; j++) {

                if ( (crcValue & 0x0001) != 0 ) {
                    //right shift
                    crcValue >>= 1;

                    //bitwise xor
                    crcValue ^= crcPolynom;

                } else {
                    crcValue >>= 1;
                }
            }
        }
        return crcValue ^= 0x0000;
    }
}