/** LogReader
 * <p>
 *     Version 5
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */
package DataParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogReader
{

    private static final String TIMESTAMP_FORMAT = "yyyy-mm-dd hh:mm:ss";
    private static String projectPath;



    public LogReader(String path){
    projectPath = path;
    }

    /**
     * The checkStatus method checks the status of a received hex data. If the type of an entry is received the method returns true.
     * Purpose is to examine the received hex data from log file.
     * @param checkType to determine if the hex data is send by fmb device or server.
     * @return true or false based on message type
     */
    public boolean checkStatus(String checkType)
    {
        if (checkType.contains("received"))
        {
            return true;
        }
        return false;
    }

    /**
     * The getHexData method extracts the hex data from a specific log entry.
     * @param entry a single log entry with timestamp, log info and hexcode.
     * @return the received hex data from fmb devices.
     */
    public String getHexData(String entry){

        int firstBracket = entry.indexOf('[');
        String contentOfBrackets = entry.substring(firstBracket + 1, entry.indexOf(']', firstBracket));
        return contentOfBrackets;

    }

    /**
     * The getLogData method reads a log-file and returns all lines from that file.
     * @return a list with given strings from log file
     * @throws FileNotFoundException if file is not there.
     */
    public List<String> getLogData() throws FileNotFoundException {
        String line = "";

        ArrayList<String> logList = new ArrayList<>();

        Scanner logScanner = new Scanner(new File(projectPath));

        while (logScanner.hasNextLine())
        {
            line = logScanner.nextLine();
            logList.add(line);
        }
        return logList;
    }


    /**
     * The checkTimeStamp method checks if a given timestamp is between to others or not.
     * @param lowerBound describes the lowerBound
     * @param upperBound describes the upperBound
     * @param toCheck should be between lower- and upperBound
     * @return true if toValidate is between.
     */
    public boolean checkTimeStamp(Timestamp lowerBound, Timestamp upperBound, Timestamp toCheck){
        if (toCheck.getTime() < upperBound.getTime() && toCheck.getTime() > lowerBound.getTime()) {
            return true;
        }
        return false;
    }



    /**
     * The convertToTimeStamp method converts a string to a timestamp.
     * @param toConvert string to be converted into timestamp.
     * @return Timestamp for given string.
     */
    public Timestamp convertToTimeStamp(String toConvert) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date parsedDate = dateFormat.parse(toConvert);

        return new Timestamp(parsedDate.getTime());
    }


    /**
     * The checkHexDataLength method checks if hex-data has a correct length.
     * Minimum length for hex data is 106 for protocol 'Codec 8 Extended'.
     * @param hexData string to check.
     * @return boolean based
     */
    public boolean checkHexDataLength(String hexData){

        boolean valid = (hexData.length() >= 106) ? true : false;
        return valid;
    }



    /**
     * The checkHexFormat method checks if decoded hex-data are formatted in hexadecimal.
     * [0-9A-Fa-f]     Character class: Any character in 0 to 9, or in A to F.
     * +               Quantifier: One or more of the above.
     * @param hexData to check format.
     * @return true if correctly formatted.
     */
    public boolean checkHexFormat(String hexData)
    {
        boolean isHex = hexData.matches("[0-9A-Fa-f]+");
        return isHex;
    }


    /**
     * The getLogTimeStamp method returns the timestamp for a specific log string.
     * @param log entry containing timestamp and hex data.
     * @return Timestamp for log string.
     * @throws ParseException
     */
    public Timestamp getLogTimeStamp(String log) throws ParseException{
        String logEntryDate = log.substring(0,19);
        Timestamp timeStamp = convertToTimeStamp(logEntryDate);
        return timeStamp;
    }


    /**
     * The getHexList method returns a list with received hex-data between two timestamps.
     * @param lowerBound Timestamp for upper bound.
     * @param upperBound Timestamp for lower bound.
     * @return List String containing hex-data
     * @throws FileNotFoundException if file is not found.
     * @throws ParseException if wrong data has wrong format.
     */
    public List<String> getHexList(Timestamp lowerBound, Timestamp upperBound) throws FileNotFoundException, ParseException {


        List<String> logData = getLogData();

        List<String> hexCodes = new ArrayList<>();

        for (int i = 0; i < logData.size(); i++)
        {
            //get timestamp from log entry
            String logEntry = logData.get(i);
            Timestamp logStamp = getLogTimeStamp(logEntry);

            boolean valid = checkTimeStamp(lowerBound, upperBound,logStamp);

            if (valid && checkStatus(logEntry))
            {
                String hexValue = getHexData(logEntry);

                if (  checkHexFormat(hexValue) && checkHexDataLength(hexValue))
                hexCodes.add(hexValue);

            }
        }
        return hexCodes;
    }

    public static String getProjectPath() {
        return projectPath;
    }
}
