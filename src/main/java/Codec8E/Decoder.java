package Codec8E;
import Codec8E.Collection.AvlDataCollection;
import Codec8E.Exceptions.PreAmbleLengthException;
import Logger.ReadLogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Decoder {

    public static String hexCode = "";

    private List<AvlDataCollection> decodedData;

    Decoder() throws PreAmbleLengthException {
        setAvlCollectionList();
    }

    public static void main (String [] args) {
        try {
            Decoder decoder = new Decoder();
            System.out.println();
        } catch (PreAmbleLengthException e){
            System.out.println(e);
        }





    }

    public void setAvlCollectionList() throws PreAmbleLengthException {
        decodedData = new ArrayList<>();
        List<String> byteStrings = ReadLogs.getByteList();
        for (int i = 0; i < byteStrings.size(); i++) {
            setHexCode(byteStrings.get(i));
            decodedData.add(new AvlDataCollection());
        }
        removeDeadBeaconEntries();
        removeAvlCollection();
    }

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
    
    private void removeAvlCollection(){
        Iterator<AvlDataCollection> it = decodedData.iterator();

        while(it.hasNext()){
            AvlDataCollection toCheck = it.next();
            if (toCheck.getAvlDataList().size() == 0 )
                it.remove();
        }
    }


    public static void checkBeaconData(){}

    public static void setHexCode(String hexCode) {
        Decoder.hexCode = hexCode;
    }
}

