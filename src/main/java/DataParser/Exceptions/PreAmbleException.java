/** PreAmbleException
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Exceptions;

public class PreAmbleException extends Exception {

    /**
     * Class constructor specifying preamble value.
     * @param preAmble send preamble by FMB device.
     */
    public PreAmbleException(int preAmble){
        super("Unable to decode. Missing or false package prefix. Preamble value is " +
                preAmble + " it should be 00000000.");
    }


}
