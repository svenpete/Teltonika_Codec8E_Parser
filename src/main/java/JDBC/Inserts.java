/** Inserts
 * <p>
 *     Version 3
 * </p>
 * Author: Sven Petersen
 * Change date: 11.09.2020
 */
package JDBC;
import DataParser.Model.AvlData;
import DataParser.Model.GpsData;
import DataParser.Model.Beacon;
import DataParser.Model.TcpDataPacket;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Inserts {


    /**
     * This method inserts decoded gps data into the database and returns the generated id for
     * the inserted position:
     * @param conn
     * @param avlData contains timestamp and gps data.
     * @return generated id for inserted location. If insert fails null will be returned.
     * @throws SQLException
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


        int rowAffected = pstmt.executeUpdate();

        // try to get the inserted id from auto column if insert was successful
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
     * This method insert decoded beacon data to the database and connects them with a location.
     * @param connection
     * @param beacon to be inserted
     * @param location_id to link a beacon with a given location.
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
     * This method initializes the insert of decoded location and beacon data
     * @param
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static void insertTcpData(Connection conn, TcpDataPacket tcpDataPacket) throws SQLException{

        int avlCount = tcpDataPacket.getAvlPacket().getData().size();


            for (int i = 0; i < avlCount; i++) {


                AvlData AvlData = tcpDataPacket.getAvlPacket().getData().get(i);

                // store generated id from inserted location for beacons
                int insertedLocationID = Inserts.insertLocation(conn,AvlData);


                int beaconCount = AvlData.getIoElement().getBeacons().size();
                for (int j = 0; j < beaconCount; j++) {

                    Beacon beaconToInsert = AvlData.getIoElement().getBeacons().get(j);

                    Integer rowsAffected = insertBeaconPosition(conn,beaconToInsert, insertedLocationID);

                    System.out.println(rowsAffected); // logger einbinden


                    if (rowsAffected == 1 ){
                        int deviceAffected = updateDeviceLatestPosition(conn,insertedLocationID, beaconToInsert);

                        if (deviceAffected == 1){
                            JDBC.log.info("UPDATED " + deviceAffected + " DEVICE SUCCESSFUL");
                        } else {
                            JDBC.log.warn("UPDATED FAILED FOR BEACON WITH MAJOR: " + beaconToInsert.getMajor()
                                    + "AND MINOR: " + beaconToInsert.getMinor());
                        }
                    }

                }
            }


    }


    /**
     * This method updates the latest position a device were registered by a FMB devices.
     * @param conn
     * @param position to update
     * @param beacon device to search for
     * @return 1 if suc
     * @throws SQLException
     */
    public static int updateDeviceLatestPosition(Connection conn, int position,
                                                 Beacon beacon) throws SQLException {

        String sql = "UPDATE DEVICE SET latest_position = ?  WHERE beacon_major = ? AND beacon_minor = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, position);
        pstmt.setString(2,beacon.getMajor());
        pstmt.setString(3, beacon.getMinor());

        int tupleAffected = pstmt.executeUpdate();
        return tupleAffected;
    }


    public static List<Object> createInsertData(Object... objects) {
        List<Object> data = new ArrayList<>();

        for (Object o : objects) {
            data.add(o);
        }

        return data;
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
