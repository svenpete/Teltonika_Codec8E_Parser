/** Querys
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Querys {

    public static void select(Connection connection, String sql, ReseultSetHandler handler) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                handler.handle(rs);
            }
        }

    }

    public static List<Object> select(Connection connection, String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        try (ResultSet rs = stmt.executeQuery(sql)) {
            List<Object> results = new ArrayList<>();
            int i = 1;
            while (rs.next()) {
                results.add(rs.getObject(i));
                i++;
            }
            return results;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }




}
