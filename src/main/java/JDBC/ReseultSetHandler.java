/** ResultSetHandler
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ReseultSetHandler {
    /**
     * This method will be executed by lambda expression
     * @param resultSet
     * @throws SQLException
     */
    void handle(ResultSet resultSet) throws SQLException;
}
