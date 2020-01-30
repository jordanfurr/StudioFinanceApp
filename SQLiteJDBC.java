import java.sql.*;

public class SQLiteJDBC {
    public static void main(String args[]) {
        Connection c = null;

        System.out.println("hello");

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:vesselDB.db");
        } catch(Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}