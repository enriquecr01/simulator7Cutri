package Models;

import dataAccess.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import exception.RecordNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Device 
{
    private String id;
    private String description;
    private String ipAddress;
    private int totalLifeCans;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public int getTotalLifeCans() { return totalLifeCans; }
    public void setTotalLifeCans(int totalLifeCans) { this.totalLifeCans = totalLifeCans;}

    public Device() 
    {
        this.id = "00:00:00:00:00";
        this.description = "";
        this.ipAddress = "0.0.0.0";
        this.totalLifeCans = 0;
    }
    
    public Device(String id) throws RecordNotFoundException
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT idMac, description, ipAddress, totalLifeCans FROM devices WHERE idMac = ?";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, id); // set value for parameter
            // execute query
            ResultSet result = command.executeQuery();
            //check if record was found
            if (result.next())
            {   
                this.id = result.getString("idMac");
                this.description = result.getString("description");
                this.ipAddress = result.getString("ipAddress");
                this.totalLifeCans = result.getInt("totalLifeCans");   
            }
            else
                throw new RecordNotFoundException(id);
            connection.close();
        }
        catch(SQLException ex)
        {
        }
    }
    
    public Device(String id, String description, String ipAddress, int totalLifeCans) 
    {
        this.id = id;
        this.description = description;
        this.ipAddress = ipAddress;
        this.totalLifeCans = totalLifeCans;
    }
    
    public boolean add()
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "INSERT INTO devices (idMac, description, ipAddress) VALUES (?, ?, ?);";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, this.id); // set value for parameter
            command.setString(2, this.description);
            command.setString(3, this.ipAddress);
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
    
    public boolean edit()
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "UPDATE devices SET description = ?, ipAddress = ?, totalLifeCans = ? WHERE idMac = ?;";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, this.description);
            command.setString(2, this.ipAddress);
            command.setInt(3, this.totalLifeCans);
            command.setString(4, this.id);
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
    
    public boolean delete()
    {
        // Metodo de insertar a la base de datos
        return true;
    }
    
    public ArrayList<ReadingDogFood> getAllDog()
    {
               //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, dfh.date, dfh.date, dfh.time, dfh.weight, dfh.actualWeight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac"
                        + "WHERE d.idMac = ?";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, this.id); // set value for parameter
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idHistoryDog");
                String date = result.getString("date");
                String hour = result.getString("time");
                double value = result.getDouble("weight");                
                double aValue = result.getDouble("actualWeight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour, aValue));
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
    
        public static ArrayList<Device> getAll()
    {
        //list
        ArrayList<Device> list = new ArrayList<Device>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT idMac, description, ipAddress, totalLifeCans " +
                        "FROM devices";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                //add product to list
                list.add(new Device(idDevice, description, ipAddress, totalLifeCans));
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
    
    public ArrayList<ReadingDogFood> getAllDog(Date date1)
    {
        //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, DATE_FORMAT(dfh.date, '%d/%m/%Y') as date, dfh.time, dfh.weight, dfh.actualWeight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac\n"
                        + "WHERE d.idMac = ? AND dfh.date = ?";
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
            //to convert Date to String, use format method of SimpleDateFormat class.
            String stringDate1 = dateFormat.format(date1);
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, this.id); // set value for parameter
            command.setString(2, stringDate1);
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idHistoryDog");
                String date = result.getString("date");
                String hour = result.getString("time");
                double value = result.getDouble("weight");
                double aValue = result.getDouble("actualWeight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour, aValue));
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
    
    public ArrayList<ReadingDogFood> getAllDog(Date date1, Date date2)
    {
        //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, DATE_FORMAT(dfh.date, '%d/%m/%Y') as date, dfh.time, dfh.weight, dfh.actualWeight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac\n"
                        + "WHERE d.idMac = ? AND dfh.date BETWEEN ? AND ?";
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
            //to convert Date to String, use format method of SimpleDateFormat class.
            String stringDate1 = dateFormat.format(date1);
            String stringDate2 = dateFormat.format(date2);
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, this.id); // set value for parameter
            command.setString(2, stringDate1); 
            command.setString(3, stringDate2); 
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idHistoryDog");
                String date = result.getString("date");
                String hour = result.getString("time");
                double value = result.getDouble("weight");
                double aValue = result.getDouble("actualWeight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour, aValue));
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
