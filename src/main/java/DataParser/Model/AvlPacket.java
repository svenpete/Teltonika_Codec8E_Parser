/** AvlPacket
 * <p>
 *     Version 3
 * </p>
 * Author: Sven Petersen
 * Change date: 12.08.2020
 */

package DataParser.Model;
import java.util.List;

/**
 * This class represents
 */
public class AvlPacket {

    private int codecId;
    private int dataCount;
    private List<AvlData> data;


    public AvlPacket(int codecId, int dataCount, List<AvlData> data) {

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
