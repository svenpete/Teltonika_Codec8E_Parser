package Codec8E.Exceptions;

public class PreAmbleLengthException extends Exception {

    int preAmbleValue;

    public PreAmbleLengthException(int x){
        this.preAmbleValue = x;
    }

    @Override
    public String toString() {
        return "Unable to decode. Missing or false package prefix. Preamble value is " +
                preAmbleValue + " it should be 0.";
    }
}
