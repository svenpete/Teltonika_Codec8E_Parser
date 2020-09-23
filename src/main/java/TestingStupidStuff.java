import Codec8E.Decoder;
import org.apache.log4j.Logger;

public class TestingStupidStuff {

    static {
        System.setProperty("logPath",System.getProperty("user.dir"));
    }


    static Logger log = Logger.getLogger(Decoder.class.getName());


    public static void main (String [] args){

        String s = System.getProperty("user.dir");
        System.out.println(s);

    }
}
