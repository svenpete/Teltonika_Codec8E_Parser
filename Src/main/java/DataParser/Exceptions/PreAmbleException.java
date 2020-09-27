/** PreAmbleException
 * <p>
 *     Version 1
 * </p>
 * Change of date: 28.05.2020
 */

package DataParser.Exceptions;

public class PreAmbleException extends Exception {

    int preAmbleValue;

    public PreAmbleException(int x){
        super("Unable to decode. Missing or false package prefix. Preamble value is " +
                x + " it should be 00000000.");
    }


}
