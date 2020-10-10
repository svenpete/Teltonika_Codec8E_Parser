/** CyclicRedundancyCheck
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Exceptions;

public class CyclicRedundancyCheck extends Exception{

    /**
     * Class constructor specifying received crc value and calculated crc value.
     * @param checkPolynomial send by FMB device
     * @param calculatedPolynomial calculated by Crc object.
     */
    public CyclicRedundancyCheck(int checkPolynomial, int calculatedPolynomial){
       super("Cyclic redundancy check failed. Your value was " + calculatedPolynomial + " it should be " + checkPolynomial);
    }


}
