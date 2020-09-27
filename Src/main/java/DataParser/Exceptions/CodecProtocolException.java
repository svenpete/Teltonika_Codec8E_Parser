/** CodecProtocolException
 * <p>
 *     Version 1
 * </p>
 * Change of date: 28.05.2020
 */

package DataParser.Exceptions;

public class CodecProtocolException extends Exception {


    public CodecProtocolException(int id){
        super("The received protocol format isn't supported. Supported format is codec 8 extended. Received " +
                "format is: " + id + ".");
    }


}
