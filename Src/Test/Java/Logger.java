
import org.apache.log4j.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Logger {

    private static org.apache.log4j.Logger logger = null;

    @BeforeClass
    public static void setLogger()
    {
        System.setProperty("logPath",System.getProperty("user.dir"));
        logger = LogManager.getLogger(Logger.class.getName());
    }

    @Test
    public void testOne()
    {

        int counter = getLineSize();


            logger.debug("Debug Message Logged !!!");
            logger.info("Info Message Logged !!!");

        int afterInsert  = getLineSize();

        Assert.assertEquals(afterInsert,counter+2);


    }

    public static int getLineSize(){

        File file = new File("Src/Test/Resources/debug.log");

        if (!file.canRead() || !file.isFile())
            System.exit(0);

        BufferedReader in = null;

        try {
            int count = 0;
            in = new BufferedReader(new FileReader("Src/Test/Resources/debug.log"));

            String zeile = null;
            List<String> debugLog = new ArrayList<>();

            while ((zeile = in.readLine()) != null) {
                System.out.println("Gelesene Zeile: " + zeile);
                debugLog.add(zeile);
                count ++;
            }
            return  count;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }

}
