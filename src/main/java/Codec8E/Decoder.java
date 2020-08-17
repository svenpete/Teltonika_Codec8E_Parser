package Codec8E;

import Codec8E.AvlDatapacket.Collection.AvlDataCollection;

public class Decoder {

    public static String hexCode = "00000000000000588E0200000173E8132A89000000000000000000000000000000000181000100000000000000000001018100011100000173E8134DB0000000000000000000000000000000000000000100000000000000000001014B00000200007DA7";


    public static void main (String [] args){
        AvlDataCollection avlDataPacket = new AvlDataCollection();
        System.out.println();

        /*
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getMajor());
        System.out.println(Integer.parseInt(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getMinor(),16));
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getMajor());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getMinor());

         */
        System.out.println(avlDataPacket.getNumberOfData2());

    }

    public static void setHexCode(String hexCode) {
        Decoder.hexCode = hexCode;
    }
}

