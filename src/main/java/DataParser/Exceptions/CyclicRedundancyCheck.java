/** CyclicRedundancyCheck
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Exceptions;

public class CyclicRedundancyCheck extends Exception{

    public CyclicRedundancyCheck(int checkPolynom, int calculatedPolynom){
       super("Cyclic redundancy check failed. Your value was " + calculatedPolynom + " it should be " + checkPolynom);
    }


}
