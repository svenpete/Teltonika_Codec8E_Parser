package JDBC;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Select {
    public static void getAllWorkers(Connection con) throws SQLException {
        String sql = "SELECT * FROM DEVICE";
        Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = result.getMetaData();
        int count = resultSetMetaData.getColumnCount();
        Map<String, Object> map = new HashMap<>();
        while (result.next()){
            for (int i = 1; i <= count; i++) {

                System.out.print(resultSetMetaData.getColumnLabel(i) + ": ");
                System.out.println(result.getObject(i));
                map.put(resultSetMetaData.getColumnLabel(i),result.getObject(i)); // Hashmap ersetzt alte values somit diese in eine List<Hashmap> nach der forschleife adden
            }
        }
        for (Map.Entry e :
                map.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }
    }

    public static HashMap<String, Object> createHashmap(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int count = resultSetMetaData.getColumnCount();
        return null;
    }

    public static void getSpecificWorker(Connection con, String e_mail, String password) throws SQLException {
        String sql = "SELECT * FROM DEVICE WHERE e_mail = ? AND password = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, e_mail);
        stmt.setString(2, password);
        ResultSet result = stmt.executeQuery(sql);
        while (result.next())
            System.out.println(result.getString("name"));
    }

    public static void getSpecificWorker(Connection con) throws SQLException {
        String sql = "SELECT DEVICE.inventory_number,designation,serial_number,gurantee,note,category, TUEV.timestamp AS NEXT_TUEV, UVV.timestamp AS NEXT_UVV  FROM DEVICE" +
                "INNER JOIN BEACON" +
                "ON DEVICE.beacon_major = BEACON.major AND DEVICE.beacon_minor = BEACON.minor\n" +
                "INNER JOIN CATEGORY" +
                "ON BEACON.major = CATEGORY.major" +
                "INNER JOIN DEVICE_STATUS" +
                "ON DEVICE.device_status = DEVICE_STATUS.device_status_id" +
                "INNER JOIN TUEV" +
                "ON DEVICE.inventory_number = TUEV.inventory_number" +
                "INNER JOIN UVV" +
                "ON DEVICE.inventory_number = UVV.inventory_number;";

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet result = stmt.executeQuery(sql);
        while (result.next())
            System.out.println(result.getString("name"));
    }

}
