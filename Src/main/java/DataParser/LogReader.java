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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class LogReader
{

    // gets the working directory
    private static final String currentSystemDirectory = System.getProperty("user.dir");
    private static final String projectPath = currentSystemDirectory + "/logs/beacon.log";
    private static final String timeStampFormat = "yyyy-mm-dd hh:mm:ss";

    public LogReader(){
    }

    /**
     * This method checks the status of a received hex data. If the type of an entry is received the method returns true.
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
     * This method extracts the hex data from a specific log entry.
     * @param stringToSearch a single log entry with timestamp, log info and hexcode.
     * @return the received hex data from fmb devices.
     */
    public String getHexData(String stringToSearch){

        int firstBracket = stringToSearch.indexOf('[');
        String contentOfBrackets = stringToSearch.substring(firstBracket + 1, stringToSearch.indexOf(']', firstBracket));
        return contentOfBrackets;

    }

    /** This method reads a log-file and returns all lines from that file.
     * @return a list with given strings from log file
     * @throws FileNotFoundException
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


    /** This method checks if a given timestamp is between to others or not.
     * @param lowerBound describes the lowerBound
     * @param upperBound describes the upperBound
     * @param toCheck should be between lower- and upperBound
     * @return true if toValidate is between.
     * @throws ParseException
     */
    public boolean checkTimeStamp(Timestamp lowerBound, Timestamp upperBound, Timestamp toCheck){
        if (toCheck.getTime() < upperBound.getTime() && toCheck.getTime() > lowerBound.getTime()) {
            return true;
        }
        return false;
    }



    /** This method converts a string to a timestamp.
     * @param toConvert string to be converted into timestamp.
     * @return Timestamp for given string.
     */
    public Timestamp convertToTimeStamp(String toConvert) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(timeStampFormat);
        Date parsedDate = dateFormat.parse(toConvert);

        return new Timestamp(parsedDate.getTime());
    }


    /**
     * This method checks if hex-data has a correct length.
     * Minimum length for hex data is 106 for protocol 'Codec 8 Extended'.
     * @param hexData string to check.
     * @return boolean based
     */
    public boolean checkHexDataLength(String hexData){

        boolean valid = (hexData.length() >= 106) ? true : false;
        return valid;
    }



    /**
     * This method checks if decoded hex-data are formatted in hexadecimal.
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
     * This method returns the timestamp for a specific log string.
     * @param log
     * @return Timestamp for log string.
     * @throws ParseException
     */
    public Timestamp getLogTimeStamp(String log) throws ParseException{
        String logEntryDate = log.substring(0,19);
        Timestamp timeStamp = convertToTimeStamp(logEntryDate);
        return timeStamp;
    }


    /**
     * This method returns a list with received hex-data between two timestamps.
     * @param lowerBound Timestamp
     * @param upperBound Timestamp
     * @return List<String> containing hex-data
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public List<String> getHexList(Timestamp lowerBound, Timestamp upperBound) throws FileNotFoundException, ParseException {
        // stored all log Resources
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
