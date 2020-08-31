package Logger;

import JDBC.JDBC;
import org.apache.log4j.Logger;


public class WriteLogs
{
    static Logger log = Logger.getLogger(JDBC.class.getName());
    public static void main(String [] args)
    {
        log.info("Moin");


    }
}
