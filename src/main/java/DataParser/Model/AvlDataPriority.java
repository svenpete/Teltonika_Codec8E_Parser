/** AvlDataPriority
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser.Model;

/**
 * This class represents different priorities which avlData can have depending on their configuration in FMB Devices.
 */
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
