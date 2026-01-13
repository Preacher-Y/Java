import java.sql.Connection;
import java.sql.DriverManager;

public class DbConfig {
    //DB Data
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/lbb";

    //Conection
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
}
