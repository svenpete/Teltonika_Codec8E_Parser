/** PreAmbleException
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Exceptions;

public class PreAmbleException extends Exception {

    public PreAmbleException(int x){
        super("Unable to decode. Missing or false package prefix. Preamble value is " +
                x + " it should be 00000000.");
    }


}
