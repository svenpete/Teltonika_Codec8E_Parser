package JDBC;

import java.io.IOException;
import java.sql.SQLException;

public class main {

    public static void main(String [] args){
        try {
            JDBC.createTables(JDBC.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
