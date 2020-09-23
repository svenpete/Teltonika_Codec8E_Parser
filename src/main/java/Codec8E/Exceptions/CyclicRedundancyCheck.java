/** CyclicRedundancyCheck
 * <p>
 *     Version 1
 * </p>
 * Change of date: 23.09.2020
 */


package Codec8E.Exceptions;

public class CyclicRedundancyCheck extends Exception{
    private int checkPolynom, calculatedPolynom;

    public CyclicRedundancyCheck(int checkPolynom, int calculatedPolynom){
        this.checkPolynom = checkPolynom;
        this.calculatedPolynom = calculatedPolynom;
    }

    @Override
    public String toString() {
        return "Cyclic redundancy check failed. Your value was " + calculatedPolynom + " it should be " + checkPolynom;
    }
}
