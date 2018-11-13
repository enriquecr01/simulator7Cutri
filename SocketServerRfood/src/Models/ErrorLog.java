package Models;

import dataAccess.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ErrorLog 
{
    private int id;
    private Date date;
    private ErrorLogCode enumCode;
    private String message;

    public int getId() { return id; }
    public Date getDate() { return date; }
    public ErrorLogCode getEnumCode() { return enumCode; }
    public String getMessage() { return message; }
    
    public ErrorLog() 
    {
        this.id = 0;
        this.date = new Date();
        this.message = "";
    }
    
    public ErrorLog(Date date, int enumCode, String message) 
    {
        this.id = 0;
        this.date = date;
        this.enumCode = ErrorLogCode.valueOf(enumCode);
        this.message = message;
    }

    public ErrorLog(int id, Date date, int enumCode, String message) 
    {
        this.id = id;
        this.date = date;
        this.enumCode = ErrorLogCode.valueOf(enumCode);
        this.message = message;
    }
    
    public boolean add()
    {
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "INSERT INTO errorlog (date, errorCode, message) VALUES (?, ?, ?);";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
            String strDate = dateFormat.format(this.date);
            
            command.setString(1, strDate);
            command.setInt(2, this.enumCode.getValue());
            command.setString(3, this.message);
            // execute query
            int result = command.executeUpdate();
            //check if record was found
            System.out.println(result);
            if (result == 1) 
            { 
                connection.close();
                return true; 
            }
            else 
            { 
                connection.close();
                return false; 
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
            return false;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
            return false;
        }
    }
    
    public ArrayList<ErrorLog> getAll()
    {
        //list
        ArrayList<ErrorLog> list = new ArrayList<ErrorLog>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT idError, date, errorCode, message FROM errorlog\n" +
                    "ORDER BY idError desc";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idError");
                String date = result.getString("date");
                int enumCode = result.getInt("errorCode");
                String message = result.getString("message");
                
                 Date dateParsed = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);  
                
                //add product to list
                list.add(new ErrorLog(id, dateParsed, enumCode, message));
            }
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }       
        //return list
        return list;
    }
    
    public ArrayList<ErrorLog> getAll(Date date1, Date date2)
    {
        //list
        ArrayList<ErrorLog> list = new ArrayList<ErrorLog>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT idError, date, errorCode, message FROM errorlog WHERE date BETWEEN ? AND ?";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
            //to convert Date to String, use format method of SimpleDateFormat class.
            String stringDate1 = dateFormat.format(date1);
            String stringDate2 = dateFormat.format(date2);
            
            command.setString(1, stringDate1); 
            command.setString(2, stringDate2); 
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idError");
                String date = result.getString("date");
                int enumCode = result.getInt("errorCode");
                String message = result.getString("message");
                
                 Date dateParsed = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);  
                
                //add product to list
                list.add(new ErrorLog(id, dateParsed, enumCode, message));
            }
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }       
        //return list
        return list;
    }
    
    public static ArrayList<ErrorLog> getAll(int code)
    {
        //list
        ArrayList<ErrorLog> list = new ArrayList<ErrorLog>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT idError, date, errorCode, message FROM errorlog WHERE errorCode = ?";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            
            command.setInt(1, code);
            
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idError");
                String date = result.getString("date");
                int enumCode = result.getInt("errorCode");
                String message = result.getString("message");
                
                 Date dateParsed = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);  
                
                //add product to list
                list.add(new ErrorLog(id, dateParsed, enumCode, message));
            }
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }       
        //return list
        return list;
    }
    
    public static ArrayList<ErrorLog> getAll(Date date1, Date date2, int code)
    {
        //list
        ArrayList<ErrorLog> list = new ArrayList<ErrorLog>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT idError, date, errorCode, message FROM errorlog WHERE errorCode = ? AND date BETWEEN ? AND ?";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
            //to convert Date to String, use format method of SimpleDateFormat class.
            String stringDate1 = dateFormat.format(date1);
            String stringDate2 = dateFormat.format(date2);
            
            command.setInt(1, code);
            command.setString(2, stringDate1); 
            command.setString(3, stringDate2); 
            
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idError");
                String date = result.getString("date");
                int enumCode = result.getInt("errorCode");
                String message = result.getString("message");
                
                 Date dateParsed = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);  
                
                //add product to list
                list.add(new ErrorLog(id, dateParsed, enumCode, message));
            }
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getClass().toString() + " : " + ex.getMessage());
        }       
        //return list
        return list;
    }
    
    
    
}
