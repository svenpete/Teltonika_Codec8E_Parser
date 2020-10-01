package DataParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
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

    public LogReader() throws FileNotFoundException, ParseException {
    }

    /**
     * This method checks the status of a received byte string. If the type of an entry is received the method returns true.
     * Purpose is to examine the important byte strings from log Resources.
     * @param checkType to determine if the hexcode is send by fmb device or server.
     * @return true or false based on message type
     */
    public static boolean checkStatus(String checkType)
    {
        if (checkType.contains("received"))
        {
            return true;
        }
        return false;

    }

    /**
     * This method extracts the hex code from a specific log entry out of the log file.
     * @param stringToSearch a single log entry with timestamp, log info and hexcode.
     * @return the received hexcode from fmb devices.
     */
    public static String filterHexData(String stringToSearch){

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
     * @param lowerBound describes the timestamp to start
     * @param upperBound describes the timestamp to stop searching for
     * @param timeStampInBetween describes the timestamp whether it is valid or not
     * @return true if given input lays between otherwise false.
     * @throws ParseException
     */
    public boolean validateTimeStamp(Timestamp lowerBound, Timestamp upperBound, Timestamp timeStampInBetween){
        if (timeStampInBetween.getTime() < upperBound.getTime() && timeStampInBetween.getTime() > lowerBound.getTime()) {
            return true;
        }
        return false;
    }



    /** This method returns the timestamp of a specific log entry.
     *  The timestamp of a log entry must be the first entry in a given line otherwise this method wont work.
     * @param toConvert = the log entry to be get the date from.
     * @return the given timestamp for this specific log entry.
     */
    public Timestamp convertToTimeStamp(String toConvert) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(timeStampFormat);
        Date parsedDate = dateFormat.parse(toConvert);

       return new Timestamp(parsedDate.getTime());
    }


    /**
     * This method checks if a hex strings length is correct.
     * Purpose to determine the hex code for whitelisting.
     * @param hex to check
     * @return true if valid otherwise false.
     */
    public boolean checkHexLength(String hex){

       boolean valid = (hex.length() > 48) ? true : false;
        return valid;
    }

    public Timestamp getLogTimeStamp(String log) throws ParseException{
        String logEntryDate = log.substring(0,19);
        Timestamp timeStamp = convertToTimeStamp(logEntryDate);
        return timeStamp;
    }


    /**
     * This method returns a list with received hexcode Resources from the log file.
     * @end this parameter determines the timestamp value to look for .
     * @return
     */
    public List<String> getHexCode(Timestamp lowerBound, Timestamp upperBound) throws FileNotFoundException, ParseException {
        // stored all log Resources
        List<String> logData = getLogData();

        List<String> hexCodes = new ArrayList<>();

        for (int i = 0; i < logData.size(); i++)
        {
            String logEntry = logData.get(i);
            Timestamp logStamp = getLogTimeStamp(logEntry);
            boolean valid = validateTimeStamp(lowerBound, upperBound,logStamp);

            if (valid && checkStatus(logEntry) && checkHexLength(filterHexData(logEntry)))
            {
                hexCodes.add(filterHexData(logData.get(i)));
            }
        }
        return hexCodes;
    }

    public static String getProjectPath() {
        return projectPath;
    }
}
