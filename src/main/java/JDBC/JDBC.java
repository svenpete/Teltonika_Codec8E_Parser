/** JDBC
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 11.09.2020
 */
package JDBC;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import DataParser.DataDecoder;
import DataParser.Exceptions.CodecProtocolException;
import DataParser.Exceptions.CyclicRedundancyCheck;
import DataParser.Exceptions.PreAmbleException;
import DataParser.LogReader;
import DataParser.HexReader;
import DataParser.Model.AvlData;
import DataParser.Model.TcpDataPacket;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBC {

    private static String db_port = "";
    private static String db_user = "";
    private static String db_password = "";
    private static String db_database = "";
    private static String db_serverTimezone = "";
    private static String db_host = "";
    private static String uri = "";
    public static Logger log = Logger.getLogger(JDBC.class.getName());
    // set up system variables
    static {

        Properties props = new Properties();
        try {
            CodeSource codeSource = JDBC.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getParentFile().getParentFile().getPath();

            InputStream input = new FileInputStream(jarDir +"/PathConfig.properties");
            props.load(input);
            String beaconPath = (String) props.get("beaconPath");
            System.setProperty("beaconPath",beaconPath);

            String logPath = (String) props.get("logPath");
            System.setProperty("logPath",logPath);

            input.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @throws IOException
     */
    public static void createUri() throws IOException {
        //checks if strings have already been set
        if (db_database.isEmpty() || db_user.isEmpty()
                || db_password.isEmpty() || db_host.isEmpty()
                || db_serverTimezone.isEmpty() || db_port.isEmpty())
            loadDatabaseConfiguration();
        else {
            log.info("Server: Configfile already loaded!");
        }
        try {
            log.info("Server: Establish DB-Connection");
            uri = "jdbc:mysql://"
                    + db_host
                    + ":" + db_port
                    + "/" + db_database
                    + "?serverTimezone="
                    + db_serverTimezone;
        } catch (Exception e) {
            log.info("services.Database-Connector Exception: " + e.getMessage() + e);
        }


    }

    /**
     * This method read a properties file and store its properties into given variables.     *
     * @throws IOException
     */
    public static void loadDatabaseConfiguration() throws IOException {

        Properties props = new Properties();
        InputStream inputStream = JDBC.class.getClassLoader()
                .getResourceAsStream("DatabaseConfig.properties");
        props.load(inputStream);


        db_port = props.getProperty("db.port");
        db_user = (String) props.get("db.user");
        db_password = props.getProperty("db.password");
        db_database = props.getProperty("db.database");
        db_serverTimezone = props.getProperty("db.serverTimezone");
        db_host = props.getProperty("db.host");
        inputStream.close();
    }

    /** Method for database connection.
     * @return Connection object for database operations
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (uri.isEmpty())
            createUri();
        Connection connection = DriverManager.getConnection(uri, db_user, db_password);
        System.out.println("Connection Established");
        return connection;
    }


    public static int updateStatement(Connection connection, String sqlStatement) throws SQLException {
        Statement statement = connection.createStatement();
        int tuplesUpdate = statement.executeUpdate(sqlStatement);
        System.out.println("SQL update executed: " + sqlStatement);
        return tuplesUpdate;
    }

    public static void main(String[] args) {
        try {
            LogReader logReader = new LogReader(System.getProperty("beaconPath"));

            // setting limit for hexCode input
            Timestamp upperBound = new Timestamp(System.currentTimeMillis());
            Timestamp lowerBound = new Timestamp(System.currentTimeMillis() - 300000);

            List<String> hex = logReader.getHexList(lowerBound,upperBound);

            for (int i = 0; i < hex.size(); i++) {

                try{

                HexReader hexReader = new HexReader(hex.get(i));
                DataDecoder decoder = new DataDecoder(hexReader);

                TcpDataPacket tcpDataPacket = decoder.decodeTcpData();
                int count = tcpDataPacket.getAvlPacket().getData().size();

                for (int j = 0; j < count; j++) {
                    AvlData toCheck = tcpDataPacket.getAvlPacket().getData().get(j);
                    int beaconCount = toCheck.getIoElement().getBeacons().size();
                    if ( beaconCount >= 1 ) {
                        Inserts.insertTcpData(getConnection(), tcpDataPacket);
                    }
                }

                }   catch (PreAmbleException e) {
                    e.printStackTrace();
                    log.info(e);
                } catch (CyclicRedundancyCheck e) {
                    e.printStackTrace();
                    log.info(e);
                } catch (CodecProtocolException e) {
                    e.printStackTrace();
                    log.info(e);
                } catch (SQLException e) {

                    System.out.println("Error Message: " + e.getMessage());
                    log.debug("Error Message: " + e.getMessage());

                    System.out.println("SQL State: " + e.getSQLState());
                    log.info("SQL State: " + e.getSQLState());
                    e.printStackTrace();

                }

            }

        } catch (IOException e) {

            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();

            log.debug("Invalid Input: ", e);

        } catch (ParseException e){

            System.out.println("Error Message: Unable to decode. Missing or false package prefix. ");
            log.debug("Error Message: ", e);

        }


    }

    public static void createSchema(Connection connection, String schemaName) throws SQLException {
        Schema schema = new Schema(schemaName);
        updateStatement(connection, schema.use());
    }

    public static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        Table rights = new Table("RIGHTS")
                .addPrimaryKey("role", Type.VARCHAR)
                .addAttr("booking_device", Type.BOOLEAN)
                .addAttr("edit_device", Type.BOOLEAN)
                .addAttr("add_device", Type.BOOLEAN)
                .addAttr("view_device", Type.BOOLEAN)
                .addAttr("delete_device", Type.BOOLEAN)
                .addAttr("add_user", Type.BOOLEAN)
                .addAttr("delete_user", Type.BOOLEAN)
                .addAttr("edit_user", Type.BOOLEAN)
                .addAttr("delete_booking", Type.BOOLEAN)
                .addAttr("edit_booking", Type.BOOLEAN)
                .addAttr("picking",Type.BOOLEAN);
        stmt.addBatch(rights.create());


        Table worker = new Table("WORKER")
                .addPrimaryKey("worker_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("password", Type.VARCHAR)
                .addAttr("e_mail", Type.VARCHAR)
                .addAttr("name", Type.VARCHAR)
                .addAttr("surname", Type.VARCHAR)
                .addForeignKey("role", Type.VARCHAR, rights, rights.attributes.get(0),
                         Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(worker.create());


        Table project = new Table("PROJECT")
                .addPrimaryKey("project_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("name", Type.VARCHAR)
                .addAttr("street", Type.VARCHAR)
                .addAttr("postcode", Type.VARCHAR)
                .addAttr("city", Type.VARCHAR);
        stmt.addBatch(project.create());


        Table deviceStatus = new Table("DEVICE_STATUS")
                .addPrimaryKey("device_status_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("description", Type.VARCHAR);
        stmt.addBatch(deviceStatus.create());



        Table location = new Table("LOCATION")
                .addPrimaryKey("location_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("speed", Type.INTEGER)
                .addAttr("angle", Type.INTEGER)
                .addAttr("longitude", Type.FLOAT)
                .addAttr("latitude", Type.FLOAT)
                .addAttr("altitude", Type.FLOAT)
                .addAttr("timestamp", Type.TIMESTAMP.Default("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"));
        stmt.addBatch(location.create());


        Table beacon = new Table("BEACON")
                .addPrimaryKey("major", Type.TINY_VARCHAR)
                .addAttr("minor", Type.TINY_VARCHAR)
                .addAttr("uuid", Type.VARCHAR.Default("DA95206921ED84C1ED97EA92306C5A7F"));
        stmt.addBatch(beacon.create("minor"));


        Table beaconPosition = new Table("BEACON_POSITION")
                .addForeignKey("major", Type.TINY_VARCHAR, beacon, beacon.attributes.get(0),
                         Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("minor", Type.TINY_VARCHAR, beacon, beacon.attributes.get(2),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("location_id", Type.INTEGER, location, location.attributes.get(0),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addAttr("rssi",Type.INTEGER);
        stmt.addBatch(beaconPosition.create());




        Table device = new Table("DEVICE")
                .addPrimaryKey("inventory_number", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("serial_number", Type.VARCHAR)
                .addAttr("gurantee", Type.VARCHAR)
                .addAttr("note", Type.DATE)
                .addForeignKey("device_status", Type.INTEGER, deviceStatus, deviceStatus.attributes.get(0),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("beacon_major", Type.TINY_VARCHAR,beacon,beacon.attributes.get(0),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE )
                .addForeignKey("beacon_minor", Type.TINY_VARCHAR,beacon,beacon.attributes.get(1),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE );
        stmt.addBatch(device.create());


        Table borrows = new Table("BORROWS")
                .addPrimaryKey("loan_day", Type.DATE)
                .addAttr("loan_end", Type.DATE)
                .addAttr("loan_period", Type.DATE)
                .addForeignKey("worker_id", Type.INTEGER, rights, rights.attributes.get(0),
                         Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("inventory_number", Type.INTEGER, device, device.attributes.get(0),
                         Constraint.ON_DELETE_SET_NULL, Constraint.UPDATE_CASCADE)
                .addForeignKey("project_id", Type.INTEGER, project, project.attributes.get(0),
                         Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(borrows.create());

        Table category = new Table("CATEGORY")
                .addForeignKey("major", Type.TINY_VARCHAR, beacon, beacon.attributes.get(0),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE )
                .addAttr("category", Type.VARCHAR);
        stmt.addBatch(category.create());


        Table uvv = new Table("UVV")
                .addPrimaryKey("uvv_id", Type.INTEGER_AUTO_INCREMENT)
                .addForeignKey("inventory_number", Type.INTEGER, device, device.attributes.get(0),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE )
                .addAttr("timestamp", Type.TIMESTAMP)
                .addAttr("state", Type.BOOLEAN);
        stmt.addBatch(uvv.create());


        Table tuev = new Table("TUEV")
                .addPrimaryKey("tuev_id", Type.INTEGER_AUTO_INCREMENT)
                .addForeignKey("inventory_number", Type.INTEGER,device, device.attributes.get(0),
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE )
                .addAttr("timestamp", Type.TIMESTAMP)
                .addAttr("state", Type.BOOLEAN);
        stmt.addBatch(tuev.create());


        Table repair = new Table("REPAIR")
                .addPrimaryKey("repair_id", Type.INTEGER_AUTO_INCREMENT)
                .addForeignKey("inventory_number", Type.INTEGER.Default("CURRENT_TIMESTAMP ON UPDATE " +
                        "CURRENT_TIMESTAMP,"), device, device.attributes.get(0), Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE )
                .addAttr("timestamp", Type.TIMESTAMP)
                .addAttr("state", Type.BOOLEAN)
                .addAttr("note",Type.VARCHAR);
        stmt.addBatch(repair.create());




        int[] recordsUpdated = stmt.executeBatch();
        int total = 0;
        for (int recordUpdated : recordsUpdated) {
            total ++;
        }
        System.out.println("total records updated by batch: " + total);


    }

    /**
     * This method gets the column count for a specific table.
     * @param connection to database
     * @param tableName  to obtain columnnames
     * @param schemaName to obtain qualifiedName
     * @return a list with populated column names depends on tableName
     * @throws SQLException
     */
    public static List<String> getColums(Connection connection, String tableName, String schemaName)
            throws SQLException {


        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        PreparedStatement preparedStatement = null;
        List<String> columnNames = null;


        String qualifiedName = (schemaName != null && !schemaName.isEmpty())
                ? (schemaName + "." + tableName) : tableName;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + qualifiedName + " WHERE 0 = 1");
            resultSet = preparedStatement.executeQuery();
            resultSetMetaData = resultSet.getMetaData();

            columnNames = new ArrayList<String>();


            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                columnNames.add(resultSetMetaData.getColumnLabel(i));
            }

        } catch (SQLException e) {

            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw e;
                }
                if (preparedStatement != null)
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

            }

        }
        return columnNames;
    }

    /**
     * The getPrimaryKey method returns the primary key for a given table.
     * @param connection to database.
     * @param tableName to get primary key from.
     * @param schemaName where table is located.
     * @throws SQLException
     * @return String with primary key attribute from given table.
     */
    public static String getPrimaryKey(Connection connection, String tableName, String schemaName)
    throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet primaryKey = databaseMetaData.getPrimaryKeys(null,schemaName,tableName);
        while (primaryKey.next()) {
            return primaryKey.getString("COLUMN_NAME");
        }

        return null;
    }

}
