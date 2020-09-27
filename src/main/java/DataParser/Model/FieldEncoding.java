/** FieldEncoding
 * <p>
 *     Version 1
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package DataParser.Model;

public enum FieldEncoding {
    byte1(1),
    byte2(2),
    byte4(4),
    byte8(8),
    byte16(16),
    byte32(32);

    private int element;

    FieldEncoding (int element){
            this.element = element;
        }

        public int getElement() {
            return element;
        }


}
