package DataParser;

import org.junit.Assert;
import org.junit.Test;

public class CrcTest {


    @Test
    public void testCrc(){

        String test = "14ABC92341";
        Crc toCheck = new Crc(test);
        int crcToCheck = toCheck.calculateCrc();

        String test2 = "14ABC92341";
        Crc toCheck2 = new Crc(test2);
        int crcToCheck2 = toCheck2.calculateCrc();

        Assert.assertEquals(crcToCheck,crcToCheck2);


    }

    @Test
    public void testCrc2(){

        String test = "14ABC92341";
        Crc toCheck = new Crc(test);
        int crcToCheck = toCheck.calculateCrc();

        String test2 = "14ABC92388";
        Crc toCheck2 = new Crc(test2);
        int crcToCheck2 = toCheck2.calculateCrc();

        Assert.assertNotSame(crcToCheck,crcToCheck2);


    }
}
