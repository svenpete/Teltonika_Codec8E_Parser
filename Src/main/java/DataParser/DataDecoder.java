/** DataParser
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser;
import DataParser.Exceptions.PreAmbleException;
import DataParser.Model.AVL.AvlData;
import DataParser.Codec.Codec8E;
import DataParser.Model.AVL.AvlPacket;
import DataParser.Exceptions.CodecProtocolException;
import DataParser.Exceptions.CyclicRedundancyCheck;
import DataParser.Model.IO.IOElement;
import DataParser.Model.TcpDataPacket;
import java.util.Iterator;
import java.util.List;


public class DataDecoder {

    private HexReader hexReader;

    public DataDecoder(HexReader hexReader){

        this.hexReader = hexReader;
    }


    /**
     * This method decodes tcp-data and checks if certain tcp-values are set correctly.
     *
     * @return an instance of TcpDataPacket.
     */
    public TcpDataPacket decodeTcpData() throws CyclicRedundancyCheck, PreAmbleException, CodecProtocolException {


            if ( hexReader.getHexCode() == null || hexReader.getHexCode() == "" )
               return null;


            int preAmble = hexReader.readInt8();
            int length = hexReader.readInt8() * 2;
            int codecId = hexReader.readInt2();

            //reposition reader for crc-calc
            hexReader.setPosition(16);

            String crcBytes = hexReader.readString(length);
            Integer crc = hexReader.readInt8();

            //reposition reader for packet avl-packet decoding
            hexReader.setPosition(16);

            // is the prefix of each hex data sent over tcp/ip
            if ( preAmble != 0 ) {

                throw new PreAmbleException(preAmble);

            }

            Crc test = new Crc(crcBytes);

            //check if packet was manipulated
            if ( crc != test.calculateCrc() ) throw new CyclicRedundancyCheck(crc, test.calculateCrc());


            AvlPacket avlPacket;

            //checking for received data-protocol encoding
            if ( codecId == 142 ) {
                avlPacket = new Codec8E(hexReader).decodeAvlPacket();

            } else {
                throw new CodecProtocolException(codecId);
            }

            return new TcpDataPacket(preAmble, length, crc, avlPacket);


    }

    /**
     * This method returns a list with non-empty avl-data.
     * @param tcpDataPacket to be f
     * @return List<TcpDataPacket> without empty avl-data entries.
     */
    public static List<TcpDataPacket> deletePacketWithoutData(List<TcpDataPacket> tcpDataPacket){

        Iterator<TcpDataPacket> iterator = tcpDataPacket.iterator();

        while (iterator.hasNext()){
            TcpDataPacket toCheck = iterator.next();
            deleteDataWithoutBeacon(toCheck);
            if ( toCheck.getAvlPacket().getData().size() == 0 )    iterator.remove();
        }
        return tcpDataPacket;


    }

    /**
     * This method deletes a avl-packet without beacon information.
     * @param tcpDataPacket
     * @return
     */
    private static void deleteDataWithoutBeacon(TcpDataPacket tcpDataPacket) {

        AvlPacket avlPacket = tcpDataPacket.getAvlPacket();

        Iterator<AvlData> iterator = avlPacket.getData().iterator();

        while (iterator.hasNext()){
            AvlData avlData = iterator.next();

            IOElement ioElement = avlData.getIoElement();
            int beaconSize =  ioElement.getBeacons().size();

            //delete avlData via iterator
            if ( beaconSize == 0 ) iterator.remove();
        }

       // return tcpDataPacket;

    }



}
