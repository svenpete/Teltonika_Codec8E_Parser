/** CodecProtocolException
 * <p>
 *     Version 1
 * </p>
 * Change of date: 28.05.2020
 */

package Codec8E.Exceptions;

public class CodecProtocolException extends Exception {

    int codecProtocolID;

    public CodecProtocolException(int id){
        this.codecProtocolID = id;
    }

    @Override
    public String toString() {
        return "The received protocol format isn't supported. Supported format is codec 8 extended. Received " +
                "format is: " + codecProtocolID + " .";
    }
}
