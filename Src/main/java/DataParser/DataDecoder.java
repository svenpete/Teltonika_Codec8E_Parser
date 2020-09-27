/** DataParser
 * <p>
 *     Version 2
 * </p>
 * Autor: Sven Petersen
 * Änderungsdatum 27.09.2020
 */

package DataParser;

import DataParser.Exceptions.PreAmbleException;
import DataParser.Model.AVL.AvlData;
import DataParser.Codec.Codec8E;
import DataParser.Model.AVL.AvlDataCollection;
import DataParser.Exceptions.CodecProtocolException;
import DataParser.Exceptions.CyclicRedundancyCheck;
import DataParser.Model.IO.IOElement;
import DataParser.Model.TcpDataPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataDecoder {

    private HexReader hexReader;

    public DataDecoder(HexReader hexReader){

        this.hexReader = hexReader;
    }

    public List<TcpDataPacket> decodeHex(List<String> hex) throws CyclicRedundancyCheck, PreAmbleException, CodecProtocolException {

        List<TcpDataPacket> decodedData = new ArrayList<>();
        for (int i = 0; i < hex.size(); i++) {
            HexReader hexReader = new HexReader(hex.get(i));
            DataDecoder dataDecoder = new DataDecoder(hexReader);
            TcpDataPacket tcpDataPacket =  dataDecoder.decodeTcpData();
            decodedData.add(tcpDataPacket);
            deleteDataWithoutBeacon(decodedData.get(i));
        }
        deletePacketWithoutData(decodedData);


        return decodedData;
    }




    public static List<TcpDataPacket> deletePacketWithoutData(List<TcpDataPacket> tcpDataPacket){

        Iterator<TcpDataPacket> iterator = tcpDataPacket.iterator();

        while (iterator.hasNext()){
            TcpDataPacket tcpDataPacket1 = iterator.next();
            if ( tcpDataPacket1.getAvlDataCollection().getData().size() == 0 )    iterator.remove();
        }
        return tcpDataPacket;


    }




    public static TcpDataPacket deleteDataWithoutBeacon(TcpDataPacket tcpDataPacket) {

        AvlDataCollection avlDataCollection = tcpDataPacket.getAvlDataCollection();
        int count = avlDataCollection.getData().size();

        Iterator<AvlData> iterator = avlDataCollection.getData().iterator();

        while (iterator.hasNext()){
            AvlData avlData = iterator.next();

            IOElement ioElement = avlData.getIoElement();
            int beaconSize =  ioElement.getBeacons().size();

            if ( beaconSize == 0 ) iterator.remove();
        }

        return tcpDataPacket;

    }

    public boolean checkHexLength(){
        if ( hexReader.getHexCode() == null )
            return false;

        int length = hexReader.getHexCode().length();
        boolean valid = length > 62 ? true : false;
        return valid;
    }

    /**
     * This method decodes the received tcp Resources and is the entry point for hex decoding .
     * @return
     */
    public TcpDataPacket decodeTcpData() throws PreAmbleException, CyclicRedundancyCheck, CodecProtocolException {

        //try {
            if ( checkHexLength() && hexReader.isValidHexaCode(hexReader.getHexCode())) {


                int preAmble = hexReader.readInt8();
                int length = hexReader.readInt8() * 2;
                int codecId = hexReader.readInt2();

                hexReader.setActualPosition(16);


                String data = hexReader.readString(length);
                Integer crc = hexReader.readInt8();

                hexReader.setActualPosition(16);

                // is the prefix of each hex code sent over tcp ip
                if ( preAmble != 0 ) {

                    throw new PreAmbleException(preAmble);

                }

                Crc test = new Crc(data);
                //check if Resources was manipulated
                if ( crc != test.calculateCrc() ) throw new CyclicRedundancyCheck(crc, test.calculateCrc());


                AvlDataCollection avlDataCollection;

                //checking for right teltonika protocol encoding
                if ( codecId == 142 ) {
                    avlDataCollection = new Codec8E(hexReader).decodeAvlDataCollection();

                } else {
                    throw new CodecProtocolException(codecId);
                }


                return new TcpDataPacket(preAmble, length, crc, avlDataCollection);
            } else {
                return null;
            }

       /* } catch (PreAmbleException e) {
            e.printStackTrace();
        } catch (CodecProtocolException e) {
            e.printStackTrace();
        } catch (CyclicRedundancyCheck cyclicRedundancyCheck) {
            cyclicRedundancyCheck.printStackTrace();
        }
        */
    //   return null;
    }
}
