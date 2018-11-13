package socketserverrfood;

import Exception.ValueOutOfRange;
import Frames.Table;
import Models.ContainerCans;
import Models.ReadingDogFood;
import Models.Device;
import Models.ReadingDuckFood;
import exception.RecordNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import Models.ErrorLog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SocketServerRfood 
{

    public static void main(String[] args)
    { 
        Table t = new Table();
        t.show();
        String idMac = "";
        //try
        //{
            //Device d = new Device("00:17:4F:08:5F:69");
            //ReadingDogFood r = new ReadingDogFood(1, "01/12/2018", d, 1800, "18:40");

            //String sDate1 = "01/01/2018";  
            //String sDate2 = "20/11/2018"; 
            //Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
            //Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);  
            
            //System.out.println(date1);
            //System.out.println(date2);
            
            
            //ArrayList<ReadingDogFood> readings = new ArrayList<ReadingDogFood>();
            //readings = d.getAll(date1, date2);

            //for (ReadingDogFood re : readings) 
            //{
                //Esta parte se hace para mostrar la fehcha de esta forma 
                // dia/mes/anio
                //LocalDate localDate = re.getDate();
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                //String formattedString = localDate.format(formatter);
                //System.out.println(formattedString);

                //System.out.println(re.getId());
                //System.out.println(re.getTime());
                //System.out.println(re.getValue());
            //}
        //}
        //catch(ParseException e)
        //{
            //e.printStackTrace();   
        //}
        //catch(RecordNotFoundException e)
        //{
            //e.printStackTrace();   
        //}
        
        // object declaration
        ServerSocket ss;
        Socket s;
        InputStreamReader isr;
        BufferedReader br;
        PrintStream ps;
        
        //ErrorLog el = new ErrorLog();
        
        //ArrayList<ErrorLog> errors = new ArrayList<ErrorLog>();
        //errors = el.getAll();
        
        //for (ErrorLog elo : errors) 
        //{
            //System.out.println(elo.getId());
            //System.out.println(elo.getDate());
            //System.out.println(elo.getEnumCode());
            //System.out.println(elo.getMessage());
        //}
        
        try
        {
            ss = new ServerSocket(28001);
            System.out.println("Socket Opened: ");
            //infinte loop
            while(true)
            {
                int status = 0; // status = 0
                String response = "Message received"; // response to client
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr); // buffered reader
                String data = br.readLine(); // read data
                
                
                
                if (data != null)
                {
                    // decode json
                    try
                    {
                        JSONObject JSONdata = new JSONObject(data);
                        idMac = JSONdata.getString("id");
                        String date = JSONdata.getString("da");
                        String time = JSONdata.getString("ti");
                        double value = JSONdata.getDouble("val");
                        double aValue = JSONdata.getDouble("aVal");
                        int cans = JSONdata.getInt("cans");
                        int aCans = JSONdata.getInt("aCans");
                        int dod = JSONdata.getInt("dod");
                        Device d = new Device(idMac);
                        ContainerCans c = new ContainerCans(date, d, cans, time, aCans);
                        int quantity = 0;
                        int lastCant = 0;
                        String dogOrDuck = "Filling Both";
                        if(cans == 0 && value == 0)
                        {
                           ReadingDogFood rdog = new ReadingDogFood(date, d, aValue, time, value);
                           rdog.add();
                           ReadingDuckFood rduck = new ReadingDuckFood(date, d, aValue, time, value);
                           rduck.add();  
                           c.add(0, 0);
                           
                           dogOrDuck = "Filling Both";
                        }
                        else if(dod == 1)
                        {
                            ReadingDuckFood r = new ReadingDuckFood(date, d, aValue, time, value);
                            r.add();
                            
                            quantity = c.getLastTotalCans();
                            lastCant = quantity + cans;
                            System.out.println("Last cant " + lastCant + ", quantity " + quantity);
                            c.add(cans, lastCant);
                            
                            dogOrDuck = "Duck Food";
                        }
                        else if (dod == 2)
                        {
                            ReadingDogFood r = new ReadingDogFood(date, d, aValue, time, value);
                            r.add();
                            quantity = c.getLastTotalCans();
                            lastCant = quantity + cans;
                            System.out.println("Last cant " + lastCant + ", quantity " + quantity);
                            c.add(cans, lastCant);
                            
                            dogOrDuck = "Dog Food";
                        }
                        
                        String[] row = new String[8];

                        row[0] = idMac;
                        row[1] = date;
                        row[2] = time;
                        row[3] = Integer.toString(cans);
                        row[4] = Double.toString(aValue);                        
                        row[5] = dogOrDuck;
                        row[6] = Double.toString(value);
                        row[7] = "Ok";
                        
                        t.add(row);
                        
                        quantity = d.getTotalLifeCans();
                        lastCant = cans + quantity;
                        d.setTotalLifeCans(lastCant);
                        d.edit();
                    }
                    catch (ValueOutOfRange e)
                    {
                        String[] row = new String[8];

                        row[0] = idMac;
                        row[1] = "N/A";
                        row[2] = "N/A";
                        row[3] = "N/A";
                        row[4] = "N/A";
                        row[5] = "N/A";
                        row[6] = "N/A";
                        row[7] = e.getMessage() + "from " + idMac;
                        
                        Date date = Calendar.getInstance().getTime();  
                        ErrorLog el = new ErrorLog(date, 1, (e.getMessage() + "from " + idMac));
                        el.add();
                        status = 4;
                        response = e.getMessage();
                    }
                    catch (RecordNotFoundException e)
                    {
                        String[] row = new String[8];

                        row[0] = idMac;
                        row[1] = "N/A";
                        row[2] = "N/A";
                        row[3] = "N/A";
                        row[4] = "N/A";
                        row[5] = "N/A";
                        row[6] = "N/A";
                        row[7] = e.getMessage() + "from " + idMac;
                        
                        Date date = Calendar.getInstance().getTime();  
                        ErrorLog el = new ErrorLog(date, 2, (e.getMessage() + "from " + idMac));
                        el.add();
                        status = 3;
                        response = e.getMessage();
                    }
                    catch (JSONException e)
                    {
                        String[] row = new String[8];

                        row[0] = idMac;
                        row[1] = "N/A";
                        row[2] = "N/A";
                        row[3] = "N/A";
                        row[4] = "N/A";
                        row[5] = "N/A";
                        row[6] = "N/A";
                        row[7] = e.getMessage() + "from " + idMac;
                        
                        t.add(row);
                        
                        Date date = Calendar.getInstance().getTime();  
                        ErrorLog el = new ErrorLog(date, 0, (e.getMessage() + "from " + idMac));
                        el.add();
                        status = 2;
                        response = "Invalid JSON format";
                    }
                }
                else
                {
                    status = 1;
                    response = "Data not received";
                }
                // response to client
                ps = new PrintStream(s.getOutputStream()); // outgoing stream
                ps.println(response);
            }
        }
        catch(IOException ex)
        {
                    
        }
    }   
}
