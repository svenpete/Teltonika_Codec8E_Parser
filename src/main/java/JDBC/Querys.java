/** Querys
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 11.07.2020
 */

package JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Querys {


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
