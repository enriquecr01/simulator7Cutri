package Models;

import Exception.ValueOutOfRange;
import dataAccess.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ReadingDuckFood 
{
    private int id;
    private LocalDate date;
    private String time;
    private Device device;
    private double value;

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

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public ReadingDuckFood() 
    {
        this.id = 0;
        this.date = LocalDate.now();
        this.time = "";
        this.device = new Device();
        this.value = 0;
    }
    
    public ReadingDuckFood(int id, String date, Device device, double value, String time) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateParsed;
        dateParsed = LocalDate.parse(date, formatter);
        //SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
        
        
        this.id = id;
        this.date = dateParsed;
        this.time = time;
        this.device = device;
        this.value = value;
    }
    
    public ReadingDuckFood(String date, Device device, double value, String time) throws ValueOutOfRange 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateParsed;
        dateParsed = LocalDate.parse(date, formatter);
        //SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
        
        
        this.id = 0;
        this.date = dateParsed;
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
        String query = "INSERT INTO duckfoodhistory (idMac, date, time, weight) VALUES (?, ?, ?, ?);";
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
    
    public ArrayList<ReadingDuckFood> getAll()
    {
               //list
        ArrayList<ReadingDuckFood> list = new ArrayList<ReadingDuckFood>();
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, dfh.idHistoryDog, dfh.date, dfh.time, dfh.weight\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN duckfoodhistory as dfh ON dfh.idMac = d.idMac";
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
                list.add(new ReadingDuckFood(id, date, d, value, hour));
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
