package DataParser;

import DataParser.Codec.Codec8E;
import DataParser.HexReader;
import DataParser.LogReader;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class Codec8ETest {

    LogReader logReader;
    HexReader hexReader;
    Codec8E codec8E;


    @Before
    public void setUp(){
        logReader = new DataParser.LogReader(System.getProperty("beaconPath"));

    }


    @Test
    public void decodeAvlDataCollection() {
        

        Timestamp lowerBound = null;
        try {
            lowerBound = logReader.convertToTimeStamp("2020-08-20 17:18:03");
            Timestamp upperBound = logReader.convertToTimeStamp("2020-08-20 17:55:03");
           // List<String> hexCode = logReader.getHexList(lowerBound, upperBound);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}