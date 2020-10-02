/** LogReaderTest
 * <p>
 *     Version 2
 * </p>
 * Author: Sven Petersen
 * Change date: 01.10.2020
 */
package DataParser;
import DataParser.Exceptions.CodecProtocolException;
import DataParser.Exceptions.CyclicRedundancyCheck;
import DataParser.Exceptions.PreAmbleException;
import DataParser.Model.TcpDataPacket;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class DataDecoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @Test
    public void testPreAmbleException() throws CyclicRedundancyCheck, PreAmbleException, CodecProtocolException {
        String hex = "00000100000000778E01000001740C72EB200004CE44F51F3C78BE0000000000000000000011000400500000150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100750012FFF10013FC1C000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B00000100004E49";
        HexReader reader = new HexReader(hex);

        int preAmble = reader.readInt8();
        reader.setPosition(0);
        thrown.expect(PreAmbleException.class);
        thrown.expectMessage("Unable to decode. Missing or false package prefix. Preamble value is " +
                preAmble + " it should be 00000000.");

        DataParser.DataDecoder decoder = new DataParser.DataDecoder(reader);
        decoder.decodeTcpData();

    }

    @Test
    public void testCrcException() throws CyclicRedundancyCheck, PreAmbleException, CodecProtocolException {
        String hex = "00000000000000778E01000001740C72EB202004CE44F51F3C78BE0000000000000000000011000400500000150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100750012FFF10013FC1C000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B00000100004E49";
        HexReader reader = new HexReader(hex);

        int preAmble = reader.readInt8();

        int datalength = reader.readInt8();

        String polynom = reader.readString(datalength * 2);

        Crc crc16 = new Crc(polynom);
        int toCheck = crc16.calculateCrc();
        int givenCrC =  reader.readInt8();
        reader.setPosition(0);


       thrown.expectMessage( "Cyclic redundancy check failed. Your value was " + toCheck + " it should be " + givenCrC);

        DataParser.DataDecoder decoder = new DataParser.DataDecoder(reader);
        decoder.decodeTcpData();
        thrown.expect(DataParser.Exceptions.CyclicRedundancyCheck.class);

    }



    @Test
    public void codecProtocolException() throws CyclicRedundancyCheck, PreAmbleException, CodecProtocolException {
        String hex = "000000000000003608010000016B40D8EA30010000000000000000000000000000000105021503010101425E0F01F10000601A014E0000000000000000010000C7CF";
        HexReader reader = new HexReader(hex);

        reader.setPosition(16);
        int codec = reader.readInt2();
        reader.setPosition(0);

       thrown.expect(CodecProtocolException.class);
       thrown.expectMessage( "The received protocol format isn't supported. Supported format is codec 8 extended. Received format is: 8.");

        DataParser.DataDecoder decoder = new DataParser.DataDecoder(reader);
        decoder.decodeTcpData();

    }

    // test with empty String
    @Test
    public void decode() throws CyclicRedundancyCheck, PreAmbleException, CodecProtocolException {
        String hex = "";
        HexReader reader = new HexReader(hex);
        DataParser.DataDecoder decoder = new DataParser.DataDecoder(reader);
        TcpDataPacket tcpDataPacket = decoder.decodeTcpData();
        Assert.assertEquals(null, tcpDataPacket);
    }





}
