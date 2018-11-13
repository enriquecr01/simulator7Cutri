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
import Models.Device;

public class ContainerCans 
{
    private int id;
    private Date date;
    private String time;
    private Device device;
    private int cantidad;
    private int cantidadActual;

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

    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }
    
    

    public ContainerCans() 
    {
        this.id = 0;
        this.date = new Date();
        this.time = "";
        this.device = new Device();
        this.cantidad = 0;
        this.cantidadActual= 0;
    }
    
    public ContainerCans(int id, String date, Device device, int value, String time, int cantidadActual)
    {
        try
        {
            this.id = id;
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            this.time = time;
            this.device = device;
            this.cantidad = value;
            this.cantidadActual = cantidadActual;
        }
        catch(ParseException e)
        {
            
        }
        
        
    }
    
    public ContainerCans(String date, Device device, int value, String time, int cantidadActual) 
    {
        try
        {
            this.id = 0;
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            this.time = time;
            this.device = device;
            this.cantidad = value;
            this.cantidadActual = cantidadActual;
        }
        catch(ParseException e)
        {
                    
        }
    } 
   
    public boolean add(int cans, int aCans)
    {
        //connection
        Connection connection = MySqlConnection.getConnection();
        //query
        String query = "INSERT INTO container (idMac, date, time, quantity, actualQuantity) VALUES (?, ?, ?, ?, ?);";
        try
        {
            //prepare statement
            PreparedStatement command = connection.prepareStatement(query);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            String strDate = dateFormat.format(date); 
            
            command.setString(1, this.device.getId()); // set value for parameter
            command.setString(2, strDate);
            command.setString(3, this.time);
            command.setDouble(4, cans);
            command.setDouble(5, aCans);
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
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity, c.actualQuantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac\n"+
                        "ORDER BY c.idContainer desc";
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
                int actualQ = result.getInt("actualQuantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour, actualQ));
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
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity, c.actualQuantity\n" +
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
                int actualQ = result.getInt("actualQuantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour, actualQ));
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
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity, c.actualQuantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac\n" +
                        "WHERE date BETWEEN ? AND ?\n"+
                        "ORDER BY c.idContainer desc";
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
                int actualQ = result.getInt("actualQuantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour, actualQ));
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
        String query = "SELECT d.idMac, d.description, d.ipAddress, d.totalLifeCans, c.idContainer, c.date, c.time, c.quantity, c.actualQuantity\n" +
                        "FROM devices as d\n" +
                        "INNER JOIN container as c ON c.idMac = d.idMac\n" +
                        "WHERE d.idMac = ? AND c.date BETWEEN ? AND ?\n" +
                        "ORDER BY c.idContainer desc";
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
                int actualQ = result.getInt("actualQuantity");
                        
                String idDevice = result.getString("idMac");
                String description = result.getString("description");
                String ipAddress = result.getString("ipAddress");
                int totalLifeCans = result.getInt("totalLifeCans");
                Device d = new Device(idDevice, description, ipAddress, totalLifeCans);
                //add product to list
                list.add(new ContainerCans(id, date, d, value, hour, actualQ));
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
        int quantity = 0;
        String query = "SELECT c.actualQuantity \n"
                + "FROM devices as d\n "
                + "INNER JOIN container as c ON d.idMac = c.idMac \n"
                + "WHERE d.idMac = ? \n"
                + "ORDER BY c.idContainer \n"
                + "DESC LIMIT 0, 1;";
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
                quantity = result.getInt("actualQuantity");
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
