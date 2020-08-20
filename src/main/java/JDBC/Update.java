/** Update
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.08.2020
 */

package JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Update {

    public static void updateData(List<Object> dataToUpdate, Connection connection, String tableName, String schemaName)
            throws SQLException {
        List<String> columnNames = JDBC.getColums(connection, tableName, schemaName);
    }


}
