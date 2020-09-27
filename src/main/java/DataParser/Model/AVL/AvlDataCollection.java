/** AvlDataCollection
 * <p>
 *     Version 3
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 12.08.2020
 */

package DataParser.Model.AVL;


import java.util.List;

public class AvlDataCollection {

    private int codecId;
    private int dataCount;
    private List<AvlData> data;


    public AvlDataCollection(int codecId, int dataCount, List<AvlData> data) {

        this.codecId = codecId;
        this.dataCount = dataCount;
        this.data = data;

    }

    public int getCodecId() {
        return codecId;
    }

    public int getDataCount() {
        return dataCount;
    }

    public List<AvlData> getData() {
        return data;
    }

}
