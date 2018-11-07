package Models;

import Exception.ValueOutOfRange;
import dataAccess.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReadingDogFood 
{
    private int id;
    private Date date;
    private String time;
    private Device device;
    private double value;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDate() { return this.date; }
    public void setDate(String date) 
    {
        try
        {
            this.date = new SimpleDateFormat("dd/MM/yyyy").parse(date); 
        }
        catch(ParseException e) { }
        
    }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public Device getDevice() { return device; }
    public void setDevice(Device station) { this.device = station; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public ReadingDogFood() 
    {
        this.id = 0;
        this.date = new Date();
        this.time = "";
        this.device = new Device();
        this.value = 0;
    }
    
    public ReadingDogFood(int id, String date, Device device, double value, String time) 
    {
        this.id = id;
        try { this.date = this.date = new SimpleDateFormat("dd/MM/yyyy").parse(date);}
        catch(ParseException e) { }
        this.time = time;
        this.device = device;
        this.value = value;
    }
    
    public ReadingDogFood(String date, Device device, double value, String time) throws ValueOutOfRange 
    {
        this.id = 0;
        try { this.date = this.date = new SimpleDateFormat("dd/MM/yyyy").parse(date);}
        catch(ParseException e) { }
        this.time = time;
        this.device = device;
        if(value < 12000)
        {
            this.value = value;
        }
        else
            throw new ValueOutOfRange(value);
    } 
   
    public boolean add()
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "INSERT INTO dogfoodhistory (idMac, date, time, weight) VALUES (?, ?, ?, ?);";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            String dateString = this.date.toString();
            System.out.println(dateString);
            
            command.setString(1, this.device.getId()); // set value for parameter
            command.setString(2, dateString);
            command.setString(3, this.time);
            command.setDouble(4, this.value);
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
    
    public static ArrayList<ReadingDogFood> getAll()
    {
               //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, dfh.date, dfh.time, dfh.weight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac";
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
                int id = result.getInt("idHistoryDog");
                String date = result.getString("date");
                String hour = result.getString("time");
                double value = result.getDouble("weight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour));
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
    
    
    public static ArrayList<ReadingDogFood> getAll(String idMac)
    {
        //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, dfh.date, dfh.time, dfh.weight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac" +
                        "WHERE d.idMac = ?\n" +
                        "ORDER BY dfh.idContainer desc";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            
            command.setString(1, idMac);
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                //read fields
                int id = result.getInt("idHistoryDog");
                String date = result.getString("date");
                String hour = result.getString("time");
                double value = result.getInt("weight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour));
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
    
    public static ArrayList<ReadingDogFood> getAll(Date date1, Date date2)
    {
        //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, dfh.date, dfh.time, dfh.weight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac" +
                        "WHERE dfh.date BETWEEN ? AND ?";
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
                int id = result.getInt("idHistoryDog");
                String date = result.getString("date");
                String hour = result.getString("time");
                double value = result.getInt("weight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour));
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
    
    public static ArrayList<ReadingDogFood> getAll(Date date1, Date date2, String idMac)
    {
        //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, dfh.date, dfh.time, dfh.weight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN dogfoodhistory as dfh ON dfh.idMac = d.idMac" +
                        "WHERE d.idMac = ? AND dfh.date BETWEEN ? AND ?";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
            //to convert Date to String, use format method of SimpleDateFormat class.
            String stringDate1 = dateFormat.format(date1);
            String stringDate2 = dateFormat.format(date2);
            
            command.setString(1, idMac);
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
                double value = result.getInt("weight");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ReadingDogFood(id, date, d, value, hour));
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
