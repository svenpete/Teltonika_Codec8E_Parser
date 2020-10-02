/** JDBC
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;


import DataParser.DataDecoder;
import DataParser.Exceptions.CodecProtocolException;
import DataParser.Exceptions.CyclicRedundancyCheck;
import DataParser.Exceptions.PreAmbleException;
import DataParser.LogReader;
import DataParser.HexReader;
import DataParser.Model.AVL.AvlData;
import DataParser.Model.TcpDataPacket;
import org.apache.log4j.Logger;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBC {
    //creates debug information at database

    private static String db_port = "";
    private static String db_user = "";
    private static String db_password = "";
    private static String db_database = "";
    private static String db_serverTimezone = "";
    private static String db_host = "";
    private static String uri = "";

    /**
     * checks if config was loaded and
     * creates uri for database connection
     * and stores information in log
     *
     * @throws IOException
     */

    // to use logger dynamic we need to initialise the path before a logger instance is generated therefore we use static block
    static {
        System.setProperty("logPath",System.getProperty("user.dir"));
    }

    static Logger log = Logger.getLogger(JDBC.class.getName());



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
     * Loads database configuration from a property file into declared Strings
     *
     * @throws IOException
     */
    public static void loadDatabaseConfiguration() throws IOException {

        Properties props = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/Resources/DatabaseConfig.properties");
        props.load(fileInputStream);
        fileInputStream.close();

        db_port = props.getProperty("db.port");
        db_user = (String) props.get("db.user");
        db_password = props.getProperty("db.password");
        db_database = props.getProperty("db.database");
        db_serverTimezone = props.getProperty("db.serverTimezone");
        db_host = props.getProperty("db.host");
    }

    /**
     * @return Connection object for database operations
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
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

            Connection conn = getConnection();
            LogReader logReader = new LogReader();

            // setting limit for hexCode input
            Timestamp upperBound = new Timestamp(System.currentTimeMillis());
            Timestamp lowerBound = new Timestamp(System.currentTimeMillis() - 300000);

            List<String> hex = logReader.getHexList(lowerBound,upperBound);

            for (int i = 0; i < hex.size(); i++) {

                HexReader hexReader = new HexReader(hex.get(i));
                DataDecoder decoder = new DataDecoder(hexReader);

                TcpDataPacket tcpDataPacket = decoder.decodeTcpData();
                int count = tcpDataPacket.getAvlPacket().getData().size();

                for (int j = 0; j < count ; j++) {
                    AvlData toCheck = tcpDataPacket.getAvlPacket().getData().get(j);
                    int beaconCount = toCheck.getIoElement().getBeacons().size();
                    if ( beaconCount >= 1 ) {
                        Inserts.insertTcpData(conn,tcpDataPacket);
                    }

                }

            }


        } catch (IOException e) {

            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();

            log.debug("Invalid Input: ", e);

        } catch (ParseException e){

            System.out.println("Error Message: Unable to decode. Missing or false package prefix. ");
            log.debug("Error Message: ", e);

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());

            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            log.debug("Error Message: " + e.getMessage());
            log.info("SQL State: " + e.getSQLState());

        } catch (ClassNotFoundException e) {

            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            log.debug(e.getMessage());
        } catch (PreAmbleException e) {
            e.printStackTrace();
        } catch (CyclicRedundancyCheck cyclicRedundancyCheck) {
            cyclicRedundancyCheck.printStackTrace();
        } catch (CodecProtocolException e) {
            e.printStackTrace();
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
                .addAttr("edit_booking", Type.BOOLEAN);
        stmt.addBatch(rights.create());


        Table worker = new Table("WORKER")
                .addPrimaryKey("worker_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("password", Type.VARCHAR)
                .addAttr("e_mail", Type.VARCHAR)
                .addAttr("name", Type.VARCHAR)
                .addAttr("surname", Type.VARCHAR)
                .addForeignKey("role", Type.VARCHAR, rights, rights.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
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

        // Integer gucken ob nicht doch float
        Table location = new Table("LOCATION")
                .addPrimaryKey("location_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("speed", Type.INTEGER)
                .addAttr("angle", Type.INTEGER)
                .addAttr("longitude", Type.FLOAT)
                .addAttr("latitude", Type.FLOAT)
                .addAttr("altitude", Type.FLOAT)
                .addAttr("timestamp", Type.TIMESTAMP);
        stmt.addBatch(location.create());

        /*
        Table beacon = new Table("Beacon")
                .addPrimaryKey("uuid",Type.VARCHAR)
                .addAttr("minor", Type.VARCHAR)
                .addForeignKey("major", Type.VARCHAR, Category, Category.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT,Constraint.UPDATE_CASCADE)
                .addAttr("rssi", Type.INTEGER)
                .addForeignKey("location_id",Type.INTEGER, Location, Location.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(beacon.create("minor"));
         */

        Table beacon = new Table("BEACON")
                .addPrimaryKey("major", Type.TINY_VARCHAR)
                .addAttr("minor", Type.TINY_VARCHAR)
                .addAttr("uuid", Type.VARCHAR);
        stmt.addBatch(beacon.create("minor"));

        Table beacon_position = new Table("BEACON_POSITION")
                .addForeignKey("major",Type.TINY_VARCHAR,beacon,beacon.attributes.get(0),
                        true,Constraint.DELETE_RESTRICT,Constraint.UPDATE_CASCADE);

        Table Category = new Table("Category")
                .addPrimaryKey("major", Type.VARCHAR)
                .addAttr("category", Type.VARCHAR);
        stmt.addBatch(Category.create());

        //adding enum
        Table device = new Table("DEVICE")
                .addPrimaryKey("inventory_number", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("designation", Type.VARCHAR)
                .addAttr("serial_number", Type.VARCHAR)
                .addAttr("gurantee", Type.DATE)
                .addAttr("note", Type.TEXT)
                .addAttr("device_status", Type.INTEGER)
                .addForeignKey("beacon_major", Type.TINY_VARCHAR, beacon, beacon.attributes.get(0),
                        true,Constraint.DELETE_RESTRICT,Constraint.UPDATE_CASCADE)
                .addForeignKey("beacon_minor", Type.TINY_VARCHAR, beacon, beacon.attributes.get(1),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(device.create());

        Table borrows = new Table("BORROWS")
                .addPrimaryKey("loan_day", Type.DATE)
                .addAttr("loan_end", Type.DATE)
                .addAttr("loan_period", Type.DATE)
                .addForeignKey("worker_id", Type.INTEGER, rights, rights.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("inventory_number", Type.INTEGER, device, device.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("project_id", Type.INTEGER, project, project.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(borrows.create());


        Table Uvv = new Table("Uvv")
                .addPrimaryKey("time", Type.TIME)
                .addPrimaryKey("date", Type.DATE)
                .addAttr("status", Type.BOOLEAN);
        stmt.addBatch(Uvv.create());

        Table Tuev = new Table("Tuev")
                .addPrimaryKey("time", Type.TIME)
                .addPrimaryKey("date", Type.DATE)
                .addAttr("status", Type.BOOLEAN);
        stmt.addBatch(Tuev.create());

        Table Repair = new Table("Repair")
                .addPrimaryKey("time", Type.TIME)
                .addPrimaryKey("date", Type.DATE)
                .addAttr("comment", Type.VARCHAR);
        stmt.addBatch(Repair.create());




        int[] recordsUpdated = stmt.executeBatch();
        int total = 0;
        for (int recordUpdated : recordsUpdated) {
            total ++;
        }
        System.out.println("total records updated by batch: " + total);


    }

    /**
     * This method gets the column count for a specific table.
     * @param connection
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

    /** This method returns the primary key for a given table.
     *
     * @author: Sven Petersen
     * @param connection
     * @param tableName
     * @param schemaName
     * @throws SQLException
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

    public static List<Object> createInsertData(Object... objects) {
        List<Object> data = new ArrayList<>();

        for (Object o : objects) {
            data.add(o);
        }

        return data;
    }

    public static String getDb_port() {
        return db_port;
    }

    public static String getDb_user() {
        return db_user;
    }

    public static String getDb_password() {
        return db_password;
    }

    public static String getDb_database() {
        return db_database;
    }

    public static String getDb_serverTimezone() {
        return db_serverTimezone;
    }

    public static String getDb_host() {
        return db_host;
    }

    public static String getUri() {
        return uri;
    }
}
