/** IOElement
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package Codec8E.IO;

public class IOElement {

   private int id;
   private int value;

    IOElement(int id, int value){
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}
