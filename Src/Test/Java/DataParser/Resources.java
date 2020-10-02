package DataParser;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Resources   {

    private String path = "Src/Test/DataParser.Resources/";
    private String fileToSearch = "DatabaseConfig.properties";



    @Test
    public void searchPropertiesFile(){

    File resourcesDir = new File(path);
        File[] listFiles = resourcesDir.listFiles();
        String fileToFind = fileToSearch;

        for (File file :
                listFiles) {

            String fileToCheck = file.getName();

            if ( fileToFind.equals(fileToCheck) )
                Assert.assertEquals(fileToFind, fileToCheck);
        }
    }

    @Test
    public void testReadProperties(){


        File resourcesDir = new File(path);
        File[] listFiles = resourcesDir.listFiles();


        for (File file :
                listFiles) {

            if ( file.canRead() )
                Assert.assertTrue(file.canRead());
        }
    }

    @Test
    public void testWriteProperties(){

        File resourcesDir = new File(path);
        File[] listFiles = resourcesDir.listFiles();

        for (File file :
                listFiles) {

            if ( file.canWrite() )
                Assert.assertTrue(file.canWrite());
        }
    }


    @Test
    public void testPropertieValues(){

        java.util.Properties properties = new java.util.Properties();

        try {
            properties.load(new FileInputStream(path + fileToSearch));

            for (String key : properties.stringPropertyNames()) {

                String value = properties.getProperty(key);

                if ( value == null || value == "")
                    Assert.fail("DataParser.Resources are sent correctly.");
            }

        } catch (FileNotFoundException e ){
            e.printStackTrace();
            Assert.fail("DataParser.Resources are sent correctly.");
        } catch (IOException e) {
            Assert.fail("DataParser.Resources are sent correctly.");
            e.printStackTrace();
        }
    }


}
