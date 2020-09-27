/** TcpDataPacket
 * <p>
 *     Version 2
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 27.09.2020
 */
package DataParser.Model;

import DataParser.Model.AVL.AvlDataCollection;

public class TcpDataPacket {
    private int preAmble;
    private int length;
    private int crc;
    private AvlDataCollection avlDataCollection;

    public TcpDataPacket(int preAmble, int length, int crc, AvlDataCollection avlDataCollection) {
        this.preAmble = preAmble;
        this.length = length;
        this.crc = crc;
        this.avlDataCollection = avlDataCollection;
    }

    public int getPreAmble() {
        return preAmble;
    }

    public int getLength() {
        return length;
    }

    public int getCrc() {
        return crc;
    }

    public AvlDataCollection getAvlDataCollection() {
        return avlDataCollection;
    }
}
