package Codec8E;
import Codec8E.AVL.AvlData;
import Codec8E.Collection.AvlDataCollection;
import Codec8E.Exceptions.CodecProtocolException;
import Codec8E.Exceptions.PreAmbleLengthException;
import Codec8E.Exceptions.ReceivedDataException;
import Codec8E.GPS.GpsData;
import Codec8E.IO.IOData;
import Logger.ReadLogs;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Decoder {

    public static String hexCode = "00000000000004D48E0B000001740C2CA6D80000000000000000000000000000000000000011000400500100150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DA0012FFDD0013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2CB679000000000000000000000000000000000181000100000000000000000001018100431121DA95203921ED84C1ED97EA92306C5A7F00016643BC21DA95206921ED84C1ED97EA92306C5A7F00020DBAB921DA95206921ED84C1ED97EA92306C5A7F00020C38BA000001740C2CCDE80000000000000000000000000000000000000011000400500100150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DA0012FFDD0013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2CF4F80000000000000000000000000000000000000011000400500100150500C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DE0012FFDD0013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D1C080000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100DE0012FFDD0013FC30000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D2BA9000000000000000000000000000000000181000100000000000000000001018100431121DA95203921ED84C1ED97EA92306C5A7F00016643BA21DA95206921ED84C1ED97EA92306C5A7F00020DBAB921DA95206921ED84C1ED97EA92306C5A7F00020C38B9000001740C2D43180000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100D60012FFD90013FC34000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D6A280000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100BB0012FFD60013FC1C000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2D91380000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100B70012FFCA0013FC05000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B0000000001740C2DA0D90000000000000000000000000000000001810001000000000000000000010181002D1121DA95203921ED84C1ED97EA92306C5A7F00016643BE21DA95206921ED84C1ED97EA92306C5A7F00020DBAB9000001740C2DB8480000000000000000000000000000000000000011000400500100150400C800004502000900B5000000B600000018000000CD3BD500CE3929000D0000001100A70012FFCA0013FC30000100F1000066590002000B0000000215673560000E000000005E9F7FB00001014B00000B0000937D";

    private List<AvlDataCollection> decodedData;

    Decoder() throws CodecProtocolException, ReceivedDataException, PreAmbleLengthException, FileNotFoundException, ParseException {
        setAvlCollectionList();
    }

    public static void main (String [] args) {
        try {
            Decoder decoder = new Decoder();
            System.out.println();
        } catch (PreAmbleLengthException preAmbleLengthException){
            System.out.println(preAmbleLengthException);
        } catch (FileNotFoundException fileNotFoundException){
            System.out.println(fileNotFoundException);
        } catch (ParseException parseException){
            System.out.println(parseException);
        } catch (CodecProtocolException codecProtocolException)
        {
            System.out.println(codecProtocolException);
        } catch (ReceivedDataException receivedDataException){
            System.out.println(receivedDataException);
        }





    }

    public void objectForJDBCInsert(){
        for (int i = 0; i < decodedData.size(); i++) {
            for (int j = 0; j < decodedData.get(i).getAvlDataList().size(); j++) {
                AvlData avlData = decodedData.get(i).getAvlDataList().get(j);
                System.out.println(avlData.getTimeStamp());
                GpsData gpsData = decodedData.get(i).getAvlDataList().get(j).getGpsData();
                System.out.println(gpsData.getLongitude());
                System.out.println(gpsData.getLatitude());
                System.out.println(gpsData.getAltitude());
                System.out.println(gpsData.getAngle());

                IOData ioData = decodedData.get(i).getAvlDataList().get(j).getIoData();
                for (int k = 0; k < ioData.getBeaconData().size(); k++) {

                }
            }
        }

    }


    public void setAvlCollectionList() throws PreAmbleLengthException, FileNotFoundException, ParseException,
            ReceivedDataException, CodecProtocolException {
        decodedData = new ArrayList<>();
        ReadLogs logreader = new ReadLogs();
        List<String> byteStrings = logreader.hexCodes;
        for (int i = 0; i < byteStrings.size(); i++) {
            setHexCode(byteStrings.get(i));
            decodedData.add(new AvlDataCollection());
        }
        removeDeadBeaconEntries();
        removeAvlCollection();
    }

    /**
     *  This mehthod deletes avl-data entries which doenst have stored any beacon informations.
     */
    private void removeDeadBeaconEntries(){
        for (int i = 0; i < decodedData.size(); i++) {
            int avlDataSize = decodedData.get(i).getAvlDataList().size();

            for (int j = 0; j < avlDataSize; j++) {

                int receivedBeaconSize = decodedData.get(i).getAvlDataList().get(j).getIoData().getBeaconData().size();

                if (receivedBeaconSize == 0)
                    decodedData.get(i).getAvlDataList().remove(j);
            }

        }
    }

    /**
     * Removes an avl collection which doesn't contain any entry
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
}

