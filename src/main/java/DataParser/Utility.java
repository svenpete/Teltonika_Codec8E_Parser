package DataParser;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utility {

    public static String getWorkingDir(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println(s);
        return s;
    }
}
