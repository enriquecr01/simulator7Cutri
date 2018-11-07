package socketclient;

import java.net.Socket;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SocketClient 
{
    public static void main(String[] args) 
    {
       // declare objects
        Socket s; 
        PrintStream ps;
        InputStreamReader isr;
        BufferedReader br;
        Random r = new Random(); //random number generator
        ////////////////////////////////////////////////
        int dispensador = 10000;
        int dispensar = 0;
        String message = ""; // outgoing message
        String response = ""; // incoming response from server
        String stationId = "00:17:4F:08:5F:69"; // station id
        //String formattedUnixDate = ""; // formated timestamp
              
        
        try
        {
            // send dates
            do{
                Date date = Calendar.getInstance().getTime();  
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
                DateFormat hourFormat = new SimpleDateFormat("kk:mm");  
                String strDate = dateFormat.format(date); 
                String strHour = hourFormat.format(date);
                char date1 = strHour.charAt(0);                
                char date2 = strHour.charAt(1);
                char date3 = strHour.charAt(3);                
                char date4 = strHour.charAt(4);
                String horaS = date1 + "" + date2 + "." + date3 + date4;
                double horaD = Double.parseDouble(horaS);
                if(horaD >= 10.00 && horaD <= 23.30)
                {
                    System.out.println("Esta Abierto");
                    int latas = 4 + r.nextInt(4);
                    System.out.println(strHour);                    
                    System.out.println("latas son: " + latas);
                    switch(latas){
                        case 4: dispensar = 20;
                        if(dispensador >= dispensar)
                        {                            
                            dispensador = dispensador - dispensar;
                            break;                            
                        }
                        else{
                            
                            System.out.println("Dispensador solo cuenta con: " + dispensador + " gramos");
                            break;
                        }
                        case 5: dispensar = 40;
                            if(dispensador >= dispensar)
                            {                                
                                dispensador = dispensador - dispensar;
                                break; 
                            }
                            else{
                                System.out.println("Dispensador solo cuenta con: " + dispensador + " gramos");
                                break;
                            }
                        case 6: dispensar = 60;
                            if(dispensador >= dispensar)
                            {                                
                                dispensador = dispensador - dispensar;
                                break; 
                            }
                            else{
                                System.out.println("Dispensador solo cuenta con: " + dispensador + " gramos");
                                break;
                            }
                        case 7: dispensar = 80;
                            if(dispensador >= dispensar)
                            {                                
                                dispensador = dispensador - dispensar;
                                break; 
                            }
                            else{
                                System.out.println("Dispensador solo cuenta con: " + dispensador + " gramos");
                                break;
                            }
                        case 8: dispensar = 100;
                            if(dispensador >= dispensar)
                            {                                
                                dispensador = dispensador - dispensar;
                                break; 
                            }
                            else{
                                System.out.println("Dispensador solo cuenta con: " + dispensador + " gramos");
                                break;
                            }
                    }
                    System.out.println("El numero de latas es: " + latas);
                    System.out.println("Dispensando... " + dispensar + " gramos");                    
                    System.out.println("Dispensador cuenta con: " + dispensador);
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
                                " \"val\" : \"" + dispensador +"\"," +
                                " \"dod\" : \"" + 1 +"\"" +
                          "}";
                    ps.println(message); 
                    response = br.readLine();
                    System.out.println(response);                    
                    System.out.println(message);
                    s.close();//close socket
                    try {
                      //Thread.sleep(latas * 60 * 1000); 
                      Thread.sleep(1000); 
                        //el tiempo que tardara en que vuelvan a hecharle latas, va ser igual al numero de latas en minutos
                    } catch(InterruptedException e) {
                      System.out.println(e.getMessage());
                    }
                    if(dispensador <= 0){
                        try {                        
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
                                        " \"val\" : \"" + 0 +"\"," +
                                        " \"dod\" : \"" + 1 +"\"" +
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
              
                else{
                    try {                        
                        System.out.println("Esta Cerrado");
                        Thread.sleep(300000);
                    } catch(InterruptedException e) {
                      System.out.println(e.getMessage());
                    }
                }
            }while(dispensador != 0);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
}
