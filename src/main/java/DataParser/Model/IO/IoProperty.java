/** IOProperty
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */

package DataParser.Model.IO;

/**
 * This class represents input/output property for each element.
 */
public class IoProperty {

    private int id;
    private long value;

    public IoProperty(int id, long value){
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
