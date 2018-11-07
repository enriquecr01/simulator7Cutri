package dataAccess;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlConnection 
{
    //connection parameters
    private static final String SERVER = "localhost";
    private static final String DATABASE = "rfood";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    //connection objects
    private static Connection connection = null;
    
    //get MySql Connection object
    public static Connection getConnection() {
        try {
            //connection string
            String connectionString = "jdbc:mysql://" + SERVER + "/" + DATABASE + "?user=" + USERNAME; 
            //add password to connection string if needed
            if (PASSWORD != "") connectionString += "&password=" + PASSWORD;
            //MySQL Java drivers
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //MySQL connection
            connection = DriverManager.getConnection(connectionString);
        }
        catch(ClassNotFoundException ex) {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(InstantiationException ex) {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(IllegalAccessException ex) {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(SQLException ex) {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(Exception ex) {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        finally {
            return connection;
        }   
    }
}
