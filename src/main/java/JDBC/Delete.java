/** Delete
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.08.2020
 */

package JDBC;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Delete {


    // search for column which is auto incremented to determine which value is the primary key to get it dynamicly working for all tables
    public static void deleteData( List<Object> data, Connection connection, String tableName, String schemaName)
            throws SQLException {
        List <String> columnNames = JDBC.getColums(connection, tableName, schemaName);

        String primaryKey = JDBC.getPrimaryKey(connection, tableName, schemaName);
        System.out.println(primaryKey);
        String deleteSql = "DELETE FROM ? WHERE ? = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(deleteSql);

            for (int i = 0; i < data.size(); i++) {
                pstmt.setObject(1, tableName);
                pstmt.setObject(2, primaryKey);
                pstmt.setObject(3,data.get(i));
                pstmt.addBatch();
            }
            int[] recordsDelted = pstmt.executeBatch();
            System.out.println("Records Delted: " + recordsDelted.length);
        } catch (SQLException exception ) {
            throw exception;
        }

    }
}
