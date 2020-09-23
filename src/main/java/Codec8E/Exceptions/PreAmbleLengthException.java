/** PreAmbleLengthException
 * <p>
 *     Version 1
 * </p>
 * Change of date: 28.05.2020
 */

package Codec8E.Exceptions;

public class PreAmbleLengthException extends Exception {

    int preAmbleValue;

    public PreAmbleLengthException(int x){
        this.preAmbleValue = x;
    }

    @Override
    public String toString() {
        return "Unable to decode. Missing or false package prefix. Preamble value is " +
                preAmbleValue + " it should be 00000000.";
    }
}
