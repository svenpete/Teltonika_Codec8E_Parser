/** CodecProtocolException
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Exceptions;

public class CodecProtocolException extends Exception {

    /**
     * Class constructor specifying codec protocol format.
     * @param id used codec protocol
     */
    public CodecProtocolException(int id){
        super("The received protocol format isn't supported. Supported format is codec 8 extended. Received " +
                "format is: " + id + ".");
    }


}
