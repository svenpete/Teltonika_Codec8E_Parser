package Codec8E;
import Codec8E.AVL.AvlData;
import Codec8E.Collection.AvlDataCollection;
import Codec8E.Exceptions.CodecProtocolException;
import Codec8E.Exceptions.PreAmbleLengthException;
import Codec8E.Exceptions.ReceivedDataException;
import Codec8E.IO.Beacon;
import Codec8E.IO.IOData;
import JDBC.Inserts;
import JDBC.JDBC;
import Logger.ReadLogs;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static JDBC.Inserts.insertBeaconPosition;

public class Decoder {

    public static String hexCode = "00000000000004D48E0B000001740C2CA6D80000000000000000000000000000000000000011000400500100150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DA0012FFDD0013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2CB679000000000000000000000000000000000181000100000000000000000001018100431121DA95203921ED84C1ED97EA92306C5A7F00016643BC21DA95206921ED84C1ED97EA92306C5A7F00020DBAB921DA95206921ED84C1ED97EA92306C5A7F00020C38BA000001740C2CCDE80000000000000000000000000000000000000011000400500100150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DA0012FFDD0013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2CF4F80000000000000000000000000000000000000011000400500100150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DE0012FFDD0013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D1C080000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DE0012FFDD0013FC30000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D2BA9000000000000000000000000000000000181000100000000000000000001018100431121DA95203921ED84C1ED97EA92306C5A7F00016643BA21DA95206921ED84C1ED97EA92306C5A7F00020DBAB921DA95206921ED84C1ED97EA92306C5A7F00020C38B9000001740C2D43180000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100D60012FFD90013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D6A280000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100BB0012FFD60013FC1C000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D91380000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100B70012FFCA0013FC05000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2DA0D90000000000000000000000000000000001810001000000000000000000010181002D1121DA95203921ED84C1ED97EA92306C5A7F00016643BE21DA95206921ED84C1ED97EA92306C5A7F00020DBAB9000001740C2DB8480000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100A70012FFCA0013FC30000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B00000B0000937D";
    static Logger log = Logger.getLogger(Decoder.class.getName());

    private List<AvlDataCollection> decodedData;

    public Decoder() throws CodecProtocolException, ReceivedDataException, PreAmbleLengthException, FileNotFoundException, ParseException {
        setAvlCollectionList();
    }

    /**
     * This method returns a list with all necessary data for table position from GpsData class and AvlData class.
     * in die klasse von gps verlagern und später nur noch diese liste ziehen und dann den timestamp dran hängen.
     * @param avlData
     * @return
     */
    public List<Object> getLocationAttributes(AvlData avlData){
        List<Object> locationAttributes = avlData.getGpsData().getGPSAttributes();
        locationAttributes.add(avlData.getTimeStamp());
        return locationAttributes;
    }

    public void setAvlCollectionList() throws PreAmbleLengthException, FileNotFoundException, ParseException,
            ReceivedDataException, CodecProtocolException {

        decodedData = new ArrayList<>();

        ReadLogs reader = new ReadLogs();
        List<String> byteStrings = reader.getHexCodes();

        for (int i = 0; i < byteStrings.size(); i++) {
            setHexCode(byteStrings.get(i));
            decodedData.add(new AvlDataCollection());
        }

        removeDeadBeaconEntries();
        removeAvlCollection();
    }

    /**
     *  This method deletes an avl-data class which don't have any beacon information stored.
     */
    private void removeDeadBeaconEntries(){
        for (int i = 0; i < decodedData.size(); i++) {
            int avlDataSize = decodedData.get(i).getAvlDataList().size();


            while (avlDataSize > 0) {
                int j = avlDataSize - 1;
                int receivedBeaconSize = decodedData.get(i).getAvlDataList().get(j).getIoData().getBeaconData().size();

                if ( receivedBeaconSize == 0 )
                    decodedData.get(i).getAvlDataList().remove(j);
                avlDataSize--;
            }


        }
    }

    /**
     * Removes an avl-collection class which doesn't contain any information about the.
     */
    private void removeAvlCollection(){
        Iterator<AvlDataCollection> it = decodedData.iterator();

        while(it.hasNext()){
            AvlDataCollection toCheck = it.next();
            if (toCheck.getAvlDataList().size() == 0 )
                it.remove();
        }
    }

    public static void setHexCode(String hexCode) {
        Decoder.hexCode = hexCode;
    }

    public List<AvlDataCollection> getDecodedData() {
        return decodedData;
    }
}

