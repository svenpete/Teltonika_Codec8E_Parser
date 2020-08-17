package Codec8E;

import Codec8E.Collection.AvlDataCollection;
import Logger.ReadLogs;

import java.util.ArrayList;
import java.util.List;

public class Decoder {

    public static String hexCode = "000000000000002D8E0100000173E81CEAA0000000000000000000000000000000000000000100000000000000000001014B0000010000DFAC";

    private List<AvlDataCollection> avlDataCollections;

    public static void main (String [] args){
        Decoder decoder = new Decoder();
        decoder.avlDataCollections = new ArrayList<>();

        List<String> byteStrings = ReadLogs.getByteList();
        for (int i = 0; i < byteStrings.size(); i++) {
            setHexCode(byteStrings.get(i));
            decoder.avlDataCollections.add(new AvlDataCollection());
        }
        System.out.println();
        /*
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getMajor());
        System.out.println(Integer.parseInt(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getMinor(),16));
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getMajor());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getMinor());

         */


    }

    public static List<AvlDataCollection> getAvlCollectionList(){
        return null;
    }

    public static void setHexCode(String hexCode) {
        Decoder.hexCode = hexCode;
    }
}

