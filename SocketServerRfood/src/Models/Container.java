package Models;

import dataAccess.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Container 
{
    private int id;
    private LocalDate date;
    private String time;
    private Device device;
    private double cantidad;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return this.date; }
    public void setDate(String Date) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
        LocalDate dateParsed;
        dateParsed = LocalDate.parse(Date, formatter);
        this.date = dateParsed; 
    }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public Device getStation() { return device; }
    public void setStation(Device station) { this.device = station; }

    public double getValue() { return cantidad; }
    public void setValue(double value) { this.cantidad = value; }

    public Container() 
    {
        this.id = 0;
        this.date = LocalDate.now();
        this.time = "";
        this.device = new Device();
        this.cantidad = 0;
    }
    
    public Container(int id, String date, Device device, double value, String time) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateParsed;
        dateParsed = LocalDate.parse(date, formatter);
        //SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
        
        
        this.id = id;
        this.date = dateParsed;
        this.time = time;
        this.device = device;
        this.cantidad = value;
    }
    
    public Container(String date, Device device, double value, String time) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateParsed;
        dateParsed = LocalDate.parse(date, formatter);
        //SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
        
        
        this.id = 0;
        this.date = dateParsed;
        this.time = time;
        this.device = device;
        this.cantidad = value;
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
    
    public ArrayList<ReadingDogFood> getAll()
    {
        //list
        ArrayList<ReadingDogFood> list = new ArrayList<ReadingDogFood>();
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
