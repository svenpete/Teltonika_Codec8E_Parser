/** TcpDataPacket
 * <p>
 *     Version 2
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 27.09.2020
 */
package DataParser.Model;

import DataParser.Model.AVL.AvlPacket;

/**
 * This class represents the received data-packet for tcp/ip connection for FMB-Devices.
 */
public class TcpDataPacket {
    private int preAmble;
    private int length;
    private int crc;
    private AvlPacket avlPacket;

    public TcpDataPacket(int preAmble, int length, int crc, AvlPacket avlPacket) {
        this.preAmble = preAmble;
        this.length = length;
        this.crc = crc;
        this.avlPacket = avlPacket;
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

    public AvlPacket getAvlPacket() {
        return avlPacket;
    }
}
