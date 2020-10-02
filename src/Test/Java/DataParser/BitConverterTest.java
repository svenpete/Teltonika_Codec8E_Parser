package DataParser;


import org.junit.Assert;
import org.junit.Test;

public class BitConverterTest {

    @Test
    public void testTwoComplement(){
        String binary = Integer.toBinaryString(Integer.parseInt("BF",16));
        StringBuffer strBuffer = new StringBuffer(binary);

        String convertedBinary = BitConverter.findTwoscomplement(strBuffer);
        int parsedComplement = Integer.parseInt(convertedBinary,2);
        Assert.assertEquals(65, parsedComplement);
    }

}