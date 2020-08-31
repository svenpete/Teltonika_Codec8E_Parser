/** JDBC
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBC {
    //creates debug information at database
    static Logger log = Logger.getLogger(JDBC.class.getName());
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
        FileInputStream fileInputStream = new FileInputStream("src/main/java/Resources/database_config.properties");
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

            Connection connection = getConnection();
            Select.getAllWorkers(connection);
            /*
            getPrimaryKey(connection,"Worker","dallmann_am2020");
            Delete.deleteData(connection,"Worker","dallmann_am2020");

            createTables(connection);

            // Testing
            List<Object> sampleDataRight = createInsertData(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
            Inserts.insertData(sampleDataRight, connection, "Rights", "dallmann_am2020");

            List<Object> sampleDataWorker = createInsertData(1, "Kralle", "test@dallmann.de", "WasDas",
                    "Detlef","Tomsen", 1);
            Inserts.insertData(sampleDataWorker, connection, "Worker", "dallmann_am2020");

            List<Object> sampleDataProject = createInsertData(1, "AssetManagement", "Untergang", "66666",
                    "Lingen");
            Inserts.insertData(sampleDataProject, connection, "Project", "dallmann_am2020");

            List<Object> sampleDataDevice = createInsertData(1, false, "Makita", "00000001", "2020-04-01",
                    "Motorflex", "Das hier ist ein Test", true);
            Inserts.insertData(sampleDataDevice, connection, "Device", "dallmann_am2020");

            List<Object> sampleDataBorrow = createInsertData("2020-04-24", "2020-04-28", "1999-11-27", 1, 1, 1);
            Inserts.insertData(sampleDataBorrow, connection, "Borrows", "dallmann_am2020");

            List<Object> sampleDataDeviceDocumentation = createInsertData(1, 1);
            Inserts.insertData(sampleDataDeviceDocumentation, connection, "Device_documentation",
                    "dallmann_am2020");

            List<Object> sampleDataTuev = createInsertData("15:32:00", "2020-01-10", true, 1);
            Inserts.insertData(sampleDataTuev, connection, "Tuev", "dallmann_am2020");

            List<Object> sampleDataUVV = createInsertData("14:24:00", "2020-01-01", true, 1);
            Inserts.insertData(sampleDataUVV, connection, "Uvv", "dallmann_am2020");

            List<Object> sampleDataRepair = createInsertData("12:20:00", "2020-04-05", "This is a test", 1);
            Inserts.insertData(sampleDataRepair, connection, "Repair", "dallmann_am2020");

            List<Object> sampleDataLocation = createInsertData("12:20:20", "2019-01-01", "691610 5334765", 1);
            Inserts.insertData(sampleDataLocation, connection, "Location", "dallmann_am2020");

             */
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void createSchema(Connection connection, String schemaName) throws SQLException {
        Schema schema = new Schema(schemaName);
        updateStatement(connection, schema.use());
    }

    public static void showAllTables(Connection connection) throws SQLException {
        String catalog = null;
        String schemaPattern = "dallmann_am2020";
        String tableNamePattern = null;
        String[] types = null;


        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet result = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);

        while (result.next()) {
            String tableName = result.getString(3);
            System.out.println(tableName);
        }

        result.close();
    }

    public static void createBeaconTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        Table Location = new Table("Location")
                .addPrimaryKey("id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("speed", Type.FLOAT)
                .addAttr("angle",Type.FLOAT)
                .addAttr("longitude", Type.FLOAT)
                .addAttr("latitude", Type.FLOAT)
                .addAttr("altitude", Type.FLOAT)
                .addAttr("timestamp", Type.FLOAT);
        stmt.addBatch(Location.create());

        Table Category = new Table("Category")
                .addPrimaryKey("major", Type.VARCHAR)
                .addAttr("category", Type.VARCHAR);
        stmt.addBatch(Category.create());

        Table Beacon = new Table("Beacon")
                .addPrimaryKey("uuid",Type.VARCHAR)
                .addAttr("minor", Type.VARCHAR)
                .addForeignKey("major", Type.VARCHAR, Category, Category.attributes.get(0),
                true, Constraint.DELETE_RESTRICT,Constraint.UPDATE_CASCADE)
                .addAttr("rssi", Type.INTEGER)
                .addForeignKey("location_id",Type.INTEGER, Location, Location.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Beacon.create("minor"));

        System.out.println(Beacon.create("minor"));
        Table Device = new Table("Device")
                .addPrimaryKey("inventory_number", Type.INTEGER)
                .addAttr("designation", Type.VARCHAR)
                .addAttr("serial_number", Type.VARCHAR)
                .addAttr("guarantee", Type.VARCHAR)
                .addAttr("note", Type.VARCHAR)
                .addAttr("reservation_status", Type.BOOLEAN)
                .addForeignKey("beacon_major", Type.VARCHAR,Beacon,Beacon.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("beacon_minor", Type.VARCHAR,Beacon,Beacon.attributes.get(0),
                        true,Constraint.DELETE_RESTRICT,Constraint.UPDATE_CASCADE);
        stmt.addBatch(Device.create());


        stmt.executeBatch();
        connection.close();


    }


    public static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        Table Rights = new Table("Rights")
                .addPrimaryKey("role", Type.INTEGER_AUTO_INCREMENT)
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
        stmt.addBatch(Rights.create());


        Table Worker = new Table("Worker")
                .addPrimaryKey("worker_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("password", Type.VARCHAR)
                .addAttr("e_mail", Type.VARCHAR)
                .addAttr("user_identification", Type.VARCHAR)
                .addAttr("name", Type.VARCHAR)
                .addAttr("surname", Type.VARCHAR)
                .addForeignKey("role", Type.INTEGER, Rights, Rights.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Worker.create());


        Table Project = new Table("Project")
                .addPrimaryKey("project_id", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("name", Type.VARCHAR)
                .addAttr("street", Type.VARCHAR)
                .addAttr("postcode", Type.VARCHAR)
                .addAttr("city", Type.VARCHAR);
        stmt.addBatch(Project.create());

        //adding enum
        Table Device = new Table("Device")
                .addPrimaryKey("inventory_number", Type.INTEGER_AUTO_INCREMENT)
                .addAttr("status", Type.BOOLEAN)
                .addAttr("designation", Type.VARCHAR)
                .addAttr("serial_number", Type.VARCHAR)
                .addAttr("gurantee", Type.DATE)
                .addAttr("category", Type.ENUM_CATEGORY)
                .addAttr("note", Type.TEXT)
                .addAttr("reservation_status", Type.BOOLEAN);
        stmt.addBatch(Device.create());

        Table Borrows = new Table("Borrows")
                .addPrimaryKey("loan_day", Type.DATE)
                .addAttr("loan_end", Type.DATE)
                .addAttr("loan_period", Type.DATE)
                .addForeignKey("worker_id", Type.INTEGER, Rights, Rights.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("inventory_number", Type.INTEGER, Device, Device.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE)
                .addForeignKey("project_id", Type.INTEGER, Project, Project.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Borrows.create());

        Table Device_Documentation = new Table("Device_documentation")
                .addPrimaryKey("device_doc_number", Type.INTEGER_AUTO_INCREMENT)
                .addForeignKey("inventory_number", Type.INTEGER, Device, Device.attributes.get(0),
                        true, Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Device_Documentation.create());


        Table Location = new Table("Location")
                .addPrimaryKey("time", Type.TIME)
                .addAttr("date", Type.DATE)
                .addAttr("gps", Type.VARCHAR)
                .addAttr("device_doc_number", Type.VARCHAR);
        stmt.addBatch(Location.create());

        Table Uvv = new Table("Uvv")
                .addPrimaryKey("time", Type.TIME)
                .addPrimaryKey("date", Type.DATE)
                .addAttr("status", Type.BOOLEAN)
                .addForeignKey("device_doc_number", Type.INTEGER, Device_Documentation,
                        Device_Documentation.attributes.get(0), true,
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Uvv.create());

        Table Tuev = new Table("Tuev")
                .addPrimaryKey("time", Type.TIME)
                .addPrimaryKey("date", Type.DATE)
                .addAttr("status", Type.BOOLEAN)
                .addForeignKey("device_doc_number", Type.INTEGER, Device_Documentation,
                        Device_Documentation.attributes.get(0), true,
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Tuev.create());

        Table Repair = new Table("Repair")
                .addPrimaryKey("time", Type.TIME)
                .addPrimaryKey("date", Type.DATE)
                .addAttr("comment", Type.VARCHAR)
                .addForeignKey("device_doc_number", Type.INTEGER, Device_Documentation,
                        Device_Documentation.attributes.get(0), true,
                        Constraint.DELETE_RESTRICT, Constraint.UPDATE_CASCADE);
        stmt.addBatch(Repair.create());




        int[] recordsUpdated = stmt.executeBatch();
        int total = 0;
        for (int recordUpdated : recordsUpdated) {
            total ++;
        }
        System.out.println("total records updated by batch: " + total);


    }

    /**
     * Method will also work when schemaName is null
     *
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
