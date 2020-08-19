package Codec8E.Exceptions;

public class CodecProtocolException extends Exception {

    int codecProtocolID;

    public CodecProtocolException(int id){
        this.codecProtocolID = id;
    }

    @Override
    public String toString() {
        return "The received protocol format isn't supported. Supported format is codec 8 extended received " +
                "format is: " + codecProtocolID + " .";
    }
}
