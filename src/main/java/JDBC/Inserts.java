/** Inserts
 * <p>
 *     Version 3
 * </p>
 * Author: Sven Petersen
 * Change date: 27.09.2020
 */

package JDBC;


import DataParser.Model.AVL.AvlData;
import DataParser.Model.GPS.GpsData;
import DataParser.Model.IO.Beacon;
import DataParser.Model.TcpDataPacket;
import java.io.IOException;
import java.sql.*;
import java.util.List;


public class Inserts {



    /**
     * This method inserts decoded location Resources from fmb device into the database and returns the generated id for
     * the inserted position
     * @param avlData values need to be in the right order otherwise values in the database are mixed up.
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Integer insertLocation(Connection conn, AvlData avlData) throws SQLException{

        String sql = "INSERT INTO LOCATION (speed, angle, longitude, latitude, altitude, timesstamp) VALUES (?,?,?,?,?,?);";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        Timestamp gpsTimeStamp = avlData.getTimeStamp();
        GpsData gpsData = avlData.getGpsData();

        pstmt.setObject(1 , gpsData.getSpeed());
        pstmt.setObject(2 , gpsData.getAngle());
        pstmt.setObject(3 , gpsData.getLongitude());
        pstmt.setObject(4 , gpsData.getLatitude());
        pstmt.setObject(5 , gpsData.getAltitude() );
        pstmt.setObject(6 , gpsTimeStamp);

        // to check if  value was added successfully
        int rowAffected = pstmt.executeUpdate();

        // try to pull the inserted id from auto column when insert was successfully
        if (rowAffected == 1 ){
            ResultSet insertedID = pstmt.getGeneratedKeys();

            if (insertedID.next()){
                int insertedLocationID = insertedID.getInt(1);

                return insertedLocationID;
            }

        }
        return null;
    }

    /**
     * This method insert beacon Resources and the latest inserted positionid to link them in the right table.
     * @param connection
     * @param beacon
     * @param location_id
     * @throws SQLException
     */
    public static Integer insertBeaconPosition(Connection connection, Beacon beacon, Integer location_id) throws SQLException {

        String sql ="INSERT INTO BEACON_POSITION (major, minor, location_id, rssi) VALUES (?,?,?,?);";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setObject(1,beacon.getMajor());
        pstmt.setObject(2,beacon.getMinor());
        pstmt.setObject(3,location_id);
        pstmt.setObject(4,beacon.getRssi());

        int rowsAffected =  pstmt.executeUpdate();
        return rowsAffected;

    }

    /**
     * This method initializes the insert of location and beacon Resources for all decoded information.
     * @param
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static void insertTcpData(Connection conn, TcpDataPacket tcpDataPacket) throws SQLException, IOException, ClassNotFoundException {

        int avlCount = tcpDataPacket.getAvlPacket().getData().size();
            // iterate over all received avl Resources in avl collection

            for (int i = 0; i < avlCount; i++) {

                // get values for database insert
                AvlData AvlData = tcpDataPacket.getAvlPacket().getData().get(i);

                // store generated id from inserted location for beacons
                int insertedLocationID = Inserts.insertLocation(conn,AvlData);

                // get io-element it has the most variables in it
                int beaconCount = AvlData.getIoElement().getBeacons().size();
                for (int j = 0; j < beaconCount; j++) {

                    Beacon beaconToInsert = AvlData.getIoElement().getBeacons().get(j);

                    Integer rowsAffected = insertBeaconPosition(conn,beaconToInsert, insertedLocationID);
                    System.out.println(rowsAffected); // logger einbinden

                    // just update a device position if this device
                    if (rowsAffected == 1 ){
                        int deviceAffected = updateDeviceLatestPosition(conn,insertedLocationID,
                                beaconToInsert.getMajor(),beaconToInsert.getMinor());
                        if (deviceAffected == 1){
                            JDBC.log.info("UPDATED " + deviceAffected + " DEVICE SUCCESSFUL");
                        } else {
                            JDBC.log.warn("UPDATED FAILED FOR BEACON WITH MAJOR: " + beaconToInsert.getMajor() + "AND MINOR: "
                                + beaconToInsert.getMinor());
                        }
                    }

                }
            }


    }


    // gonna use after beacon information is stored inside the beacon_position table for histroy reasons.
    public static int updateDeviceLatestPosition(Connection conn, int position, String major, String minor) throws SQLException {
        String sql = "UPDATE DEVICE SET latest_position = ?  WHERE beacon_major = ? AND beacon_minor = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, position);
        pstmt.setString(2,major);
        pstmt.setString(3, minor);

        int tupleAffected = pstmt.executeUpdate();
        return tupleAffected;
    }


    /** This method can be used for all tables to store data into the database.
     * @param data
     * @param connection
     * @param tableName
     * @param schemaName
     * @throws SQLException
     */
    public static void insertData(List<Object> data, Connection connection, String tableName, String schemaName)
            throws SQLException {
        List<String> columnNames = JDBC.getColums(connection, tableName, schemaName);

        String insertColumns = "";
        String insertValues = "";


        if (columnNames != null && columnNames.size() > 0) {
            insertColumns += columnNames.get(0);
            insertValues += "?";
        }


        for (int i = 1; i < columnNames.size(); i++) {
            insertColumns += ", " + columnNames.get(i);
            insertValues += ",?";
        }

        String insertSql = "INSERT INTO " + tableName + " ( " + insertColumns + " ) VALUES (" + insertValues + ")";

        try {

            PreparedStatement pStmt = connection.prepareStatement(insertSql);


            for (int i = 1; i <= data.size(); i++) {
                pStmt.setObject(i, data.get(i - 1));
            }

            pStmt.execute();

        } catch (SQLException e) {
            throw e;
        }
    }


}
