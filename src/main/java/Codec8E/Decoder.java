package Codec8E;

import Codec8E.AvlDatapacket.Collection.AvlDataPacket;

public class Decoder {

    public static String hexCode = "00000000000000708E0100000173BAF73D01000000000000000000000000000000000181000100000000000000000001018100431121FDA50693A4E24FB1AFCFC6EB07647825271B271BAC21DA95206921ED84C1ED97EA92306C5A7F00020C38AB21DA95203921ED84C1ED97EA92306C5A7F00016643C10100003799";

    public Decoder(String hexCode ) {

    }


    public static void main (String [] args){
        AvlDataPacket avlDataPacket = new AvlDataPacket();
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(1).getUuid());
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getBleBeaconFlag());
        System.out.println(avlDataPacket.getNumberOfData2());
    }





}

