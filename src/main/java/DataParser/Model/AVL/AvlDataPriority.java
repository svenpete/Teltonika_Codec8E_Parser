/** AvlData
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 27.09.2020
 */

package DataParser.Model.AVL;

public enum AvlDataPriority {
    LOW(0),
    HIGH(1),
    PANIC(2),
    SECURITY(3);


    private int priority;

    AvlDataPriority (int element){
        this.priority = element;
    }

    public int getPriority() {
        return priority;
    }


}
