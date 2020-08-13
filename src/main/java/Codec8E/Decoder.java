package Codec8E;

import Codec8E.AvlDatapacket.Collection.AvlDataPacket;

public class Decoder {

    public static String hexCode = "00000000000000708E0100000173DE018F59000000000000000000000000000000000181000100000000000000000001018100431121FDA50693A4E24FB1AFCFC6EB07647825271B271BC221DA95206921ED84C1ED97EA92306C5A7F00020C38B621DA95203921ED84C1ED97EA92306C5A7F00016643BD0100003806";


    public static void main (String [] args){
        AvlDataPacket avlDataPacket = new AvlDataPacket();
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getMajor());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getMinor());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(2).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getBleBeaconFlag());
        System.out.println(avlDataPacket.getNumberOfData2());
    }

    public static void setHexCode(String hexCode) {
        Decoder.hexCode = hexCode;
    }
}

