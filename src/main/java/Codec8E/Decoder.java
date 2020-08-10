package Codec8E;

import Codec8E.AvlDatapacket.Collection.AvlDataPacket;

public class Decoder {

    public static String hexCode = "000000000000005A8E010000016B69B0C9510000000000000000000000000000000001810001000000000000000000010181002D11216B817F8A274D4FBDB62D33E1842F8DF8014D022BBF21A579723675064DC396A7C3520129F61900000000BF0100003E5D";

    public Decoder( ) {
    }


    public static void main (String [] args){
        AvlDataPacket avlDataPacket = new AvlDataPacket();
        System.out.println(avlDataPacket.getAvlData().getIoData().getBeaconData().get(0).getUuid());
    }





}

