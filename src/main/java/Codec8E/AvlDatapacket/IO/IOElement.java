package Codec8E.AvlDatapacket.IO;

public class IOElement {

   private int id;
   private int value;

    IOElement(int id, int value){
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}
