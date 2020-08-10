package Codec8E.AvlDatapacket.IO;

public enum FieldEncoding {
    byte1(1),
    byte2(2),
    byte4(4),
    byte8(8),
    byte16(16),
    byte32(32);

    private int element;

    FieldEncoding (int element){
            this.element = element;
        }

        public int getElement() {
            return element;
        }


}
