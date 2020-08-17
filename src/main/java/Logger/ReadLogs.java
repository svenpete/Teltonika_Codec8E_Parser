package Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReadLogs
{
    // gets the working directory
    private static String currentDirectory = System.getProperty("user.dir");

    private static final String path = currentDirectory + "/logs/log.log";


    private static boolean checkStatus(String checkType)
    {
        if (checkType.contains("received"))
        {
            return true;
        }
        return false;

    }

    private static String getBytes(String stringToSearch){

        int firstBracket = stringToSearch.indexOf('[');
        String contentOfBrackets = stringToSearch.substring(firstBracket + 1, stringToSearch.indexOf(']', firstBracket));
        return contentOfBrackets;

    }

    /** This method reads a log-file and returns all lines from that file.
     * @return a list with given strings from log file
     * @throws FileNotFoundException
     */
    private static List<String> getLogData() throws FileNotFoundException {
        String line = "";

        ArrayList<String> logList = new ArrayList<>();

        Scanner logScanner = new Scanner(new File(path));

        while (logScanner.hasNextLine())
        {
            line = logScanner.nextLine();
            logList.add(line);
        }
        return logList;
    }

    /** This method checks if a given timestamp is between to others or not.
     * @param timeStampFrom describes the timestamp to start
     * @param timeStampTo describes the timestamp to stop searching for
     * @param timeStampInBetween describes the timestamp between the others
     * @return true if given input lays between otherwise false.
     * @throws ParseException
     */
    private static boolean getDateBetween(String timeStampFrom, String timeStampTo, String timeStampInBetween) throws ParseException {
        Timestamp lowerBound = getTimeStamp(timeStampFrom);
        Timestamp upperBound = getTimeStamp(timeStampTo);
        Timestamp toCheck = getTimeStamp(timeStampInBetween);

        if (toCheck.getTime() < upperBound.getTime() && toCheck.getTime() > lowerBound.getTime()) {
            return true;
        }

        return false;
    }

    /** This method returns the timestamp of a specific log entry.
     *  The timestamp of a log entry must be the first entry in a given line otherwise this method wont work.
     * @param toConvert = the log entry to be get the date from.
     * @return the given timestamp for this specific log entry.
     */
    private static Timestamp getTimeStamp(String toConvert) throws ParseException
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(toConvert);
        Timestamp timeStamp = new Timestamp(parsedDate.getTime());

       return timeStamp;
    }

    /**
     * This method returns a list with byte strings to decode.
     * @return
     */
    public static List<String> getByteList() {

        try{
            // stored all log data
            List<String> logList = getLogData();

            List<String> byteList = new ArrayList<>();

            for (int i = 0; i < logList.size(); i++)
            {
                boolean valid = getDateBetween("2020-08-13 15:57:27", "2020-08-13 16:00:00",logList.get(i));
                if (valid && checkStatus(logList.get(i)))
                {
                    byteList.add(getBytes(logList.get(i)));
                }
            }
            return byteList;
        } catch (ParseException e){
            e.printStackTrace();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

}
