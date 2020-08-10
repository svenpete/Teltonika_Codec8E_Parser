package Logger;

import org.apache.log4j.Logger;


public class WriteLogs
{
   public static final Logger logger = Logger.getLogger(WriteLogs.class);

    public static void main(String [] args)
    {
        logger.info("Test");



    }

}
