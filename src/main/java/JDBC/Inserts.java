/** Inserts
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class Inserts {

    /**
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
