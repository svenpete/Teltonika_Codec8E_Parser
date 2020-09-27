/** ReceivedDataException
 * <p>
 *     Version 1
 * </p>
 * Change of date: 28.05.2020
 */

package DataParser.Exceptions;

public class ReceivedDataException extends Exception{

    int receivedData, dataToCheck;

    public ReceivedDataException(int x, int y){
        this.receivedData = x;
        this.dataToCheck = y;
    }

    @Override
    public String toString() {
        return "The value of received avldata and the checksum of received avldata doesn't match!" +
                " avldata = " + receivedData + " checksum for avldata = " + dataToCheck;
    }
}
