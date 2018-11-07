package Models;

import dataAccess.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ContainerCans 
{
    private int id;
    private Date date;
    private String time;
    private Device device;
    private int cantidad;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDate() { return this.date; }
    public void setDate(String date) 
    {
        try
        {
            this.date = new SimpleDateFormat("dd/MM/yyyy").parse(date); 
        }
        catch(ParseException e)
        {
            
        }
        
    }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public Device getDevice() { return device; }
    public void setDevice(Device device) { this.device = device; }

    public double getValue() { return cantidad; }
    public void setValue(int value) { this.cantidad = value; }

    public ContainerCans() 
    {
        this.id = 0;
        this.date = new Date();
        this.time = "";
        this.device = new Device();
        this.cantidad = 0;
    }
    
    public ContainerCans(int id, String date, Device device, int value, String time)
    {
        try
        {
            this.id = id;
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            this.time = time;
            this.device = device;
            this.cantidad = value;
        }
        catch(ParseException e)
        {
            
        }
        
        
    }
    
    public ContainerCans(String date, Device device, int value, String time) 
    {
        try
        {
            this.id = 0;
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            this.time = time;
            this.device = device;
            this.cantidad = value;
        }
        catch(ParseException e)
        {
                    
        }
    } 
   
    public boolean add(int cans)
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "INSERT INTO container (idMac, date, time, quantity) VALUES (?, ?, ?, ?);";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            String dateString = this.date.toString();
            System.out.println(dateString);
            
            command.setString(1, this.device.getId()); // set value for parameter
            command.setString(2, dateString);
            command.setString(3, this.time);
            command.setDouble(4, cans);
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
    
    public static ArrayList<ContainerCans> getAll()
    {
        //list
        ArrayList<ContainerCans> list = new ArrayList<ContainerCans>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac";
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
                int id = result.getInt("idContainer");
                String date = result.getString("date");
                String hour = result.getString("time");
                int value = result.getInt("quantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour));
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
    
    public static ArrayList<ContainerCans> getAll(String idMac)
    {
        //list
        ArrayList<ContainerCans> list = new ArrayList<ContainerCans>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac\n" +
                        "WHERE d.idMac = ?\n" +
                        "ORDER BY c.idContainer desc";
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
                int id = result.getInt("idContainer");
                String date = result.getString("date");
                String hour = result.getString("time");
                int value = result.getInt("quantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour));
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
    
    public static ArrayList<ContainerCans> getAll(Date date1, Date date2)
    {
        //list
        ArrayList<ContainerCans> list = new ArrayList<ContainerCans>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac\n" +
                        "WHERE date BETWEEN ? AND ?";
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
                int id = result.getInt("idContainer");
                String date = result.getString("date");
                String hour = result.getString("time");
                int value = result.getInt("quantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour));
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
    
    public static ArrayList<ContainerCans> getAll(Date date1, Date date2, String idMac)
    {
        //list
        ArrayList<ContainerCans> list = new ArrayList<ContainerCans>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac\n" +
                        "WHERE d.idMac = ? AND c.date BETWEEN ? AND ?";
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
                int id = result.getInt("idContainer");
                String date = result.getString("date");
                String hour = result.getString("time");
                int value = result.getInt("quantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour));
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
    
    public int getLastTotalCans()
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        int quantity = -1;
        String query = "SELECT c.quantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON d.idMac = c.idMac\n" +
                        "WHERE d.idMac = ?\n" +
                        "ORDER BY c.idContainer DESC\n" +
                        "LIMIT 0, 1";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            command.setString(1, this.device.getId());
            // execute query
            ResultSet result = command.executeQuery();
            //read rows
            while (result.next())
            {
                quantity = result.getInt("quantity");
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
        return quantity;
    }
}
