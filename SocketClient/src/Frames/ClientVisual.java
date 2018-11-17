package Frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ClientVisual 
{
    JLabel labelCans = new JLabel("Insert cans:");
    JTextField textCans = new  JTextField(10);
    JButton buttonOk = new JButton("Ok");
    
    JFrame frame = new JFrame("Container");
    JPanel panelButtons = new JPanel();
    
    
    private int dispensadorDog = 10000;        
    private int dispensadorDuck = 10000;
    
    public void setDispensadorDog(int value)
    {
        this.dispensadorDog = value;
    }
    
    public void setDispensadorDuck(int value)
    {
        this.dispensadorDuck = value;
    }
    
    JComboBox typeFood;
    
    
    public ClientVisual()
    {
        String[] devicesMAC = {"Dog", "Duck"};
        
        typeFood = new JComboBox(devicesMAC);
        
        
        Container container = frame.getContentPane();
        //LAYOUTS
        //ADD CONTROLS TO PANEL
        GridBagConstraints c= new GridBagConstraints();
        c.insets = new Insets(10,10,0,0); //margins between controls
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.WEST; //ALING TO LEFT
        c.gridy = 0; c.gridx = 0; panelButtons.add(labelCans , c);
        c.gridy = 0; c.gridx = 1; panelButtons.add(textCans , c);
        c.gridy = 0; c.gridx = 2; panelButtons.add(typeFood , c);
        c.gridy = 1; c.gridx = 0; panelButtons.add(buttonOk , c) ;

        //ADD THE PANLE TO THE CONTAINER
        container.add(panelButtons, BorderLayout.PAGE_START);
        
        buttonOk.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                addCans();
            }
        });
        
        // le decimos que cierre 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // añadimos la tabla
        frame.add(panelButtons);
        // tamaño
        frame.setSize(350, 100);
        // mostramos
        frame.setVisible(true);
    }
    
    public void addCans()
    {
        try
        {   
            Socket s; 
            PrintStream ps;
            InputStreamReader isr;
            BufferedReader br;
            Random r = new Random(); //random number generator
            Calendar now = Calendar.getInstance();
            Date date = Calendar.getInstance().getTime();                  
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            DateFormat hourFormat = new SimpleDateFormat("kk:mm");
            System.out.println("Fecha: "+dateFormat.format(date));
            String strDate = dateFormat.format(date); 
            String strHour = hourFormat.format(date);
            int dispensar = 0;
            int dispensador = 0;
            int latasAnt = 0;
            int dispAnt = 0;
            String message = ""; // outgoing message
            String response = ""; // incoming response from server
            String stationId = "00:17:4F:08:5F:69"; // station id
            
            int latas = Integer.parseInt(textCans.getText());
            
            String select = typeFood.getSelectedItem().toString();
            System.out.println(select);
            
            int disp = 0;
            
            if(select.equals("Duck")) { disp = 1; }
            else { disp = 2; }
            
            if (disp == 1) { dispensador = this.dispensadorDog; }
            else { dispensador = this.dispensadorDuck; }
            
            dispensar = latas * 5;
            dispensador = dispensador - dispensar;
                    
            if (disp == 1) { dispensadorDog = dispensador; }
            else { dispensadorDuck = dispensador; }
            System.out.println("El numero de latas es: " + latas);
            
            
            //stablish communication with the socket
            s = new Socket("localhost",28001);
            ps = new PrintStream(s.getOutputStream());
            isr = new InputStreamReader(s.getInputStream());
            br = new BufferedReader(isr);
            //formattedUnixDate = String.valueOf(System.currentTimeMillis()/1000);
            //json message
            message = "{" +
                                " \"id\"  : \"" + stationId +"\"," +
                                " \"da\"  : \"" + strDate +"\"," + 
                                " \"ti\"  : \"" + strHour +"\"," + 
                                " \"cans\" : \"" + latas +"\"," +
                                " \"aCans\" : \"" + latasAnt +"\"," +
                                " \"val\" : \"" + dispensar +"\"," +                            
                                " \"aVal\" : \"" + dispensador +"\"," +
                                " \"dod\" : \"" + disp +"\"" +
                          "}";
                    
            ps.println(message); 
            response = br.readLine();
            System.out.println(response);                    
            System.out.println(message);
            s.close();//close socket
            if(dispensador <= 0)
            {
                try 
                {                        
                            System.out.println("Estamos rellenando dispensador...");
                            // Aqui cuando se rellena el contenedor se vacian las latas 
                            s = new Socket("localhost",28001);
                            ps = new PrintStream(s.getOutputStream());
                            isr = new InputStreamReader(s.getInputStream());
                            br = new BufferedReader(isr);
                            message = "{" +
                                        " \"id\"  : \"" + stationId +"\"," +
                                        " \"da\"  : \"" + strDate +"\"," + 
                                        " \"ti\"  : \"" + strHour +"\"," + 
                                        " \"cans\" : \"" + 0 +"\"," + 
                                        " \"aCans\" : \"" + 0 +"\"," +
                                        " \"val\" : \"" + 0 +"\"," +
                                        " \"aVal\" : \"" + 0 +"\"," +
                                        " \"dod\" : \"" + disp +"\"" +
                                  "}";
                            ps.println(message); 
                            response = br.readLine();
                            
                            Thread.sleep(1800000);//Tardara 30 minutos
                            dispensador = 1000;
                        } catch(InterruptedException e) {
                          System.out.println(e.getMessage());
                        }
                    }

        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
}
