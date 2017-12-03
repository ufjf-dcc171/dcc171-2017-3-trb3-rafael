package ufjf.dcc171;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDAO {
    private static Connection conn = null;
    
    public static Connection connection() throws Exception {
        if (conn == null) {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String driverUrl = "jdbc:derby://localhost:1527/dcc171";
            conn = DriverManager.getConnection(driverUrl, "dcc171", "dcc171");
        } 

        return conn;        
    }
}
