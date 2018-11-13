package Frames;

import Models.Device;
import Models.ReadingDuckFood;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DuckFoodVisual 
{
    // titulos
    private final static String nombresDeColumna[] = { "MAC Device", "Description device", "Date", "Time", "Actual Weight", "Weight" };
    
    JFrame frame = new JFrame("Duck Food History");
    
    JPanel panelControls = new JPanel();
    
    JPanel panelGrid = new JPanel();
    
    JLabel labelFirstDate = new JLabel("First date:");
    JTextField textFirstDate = new  JTextField(10);
    
    JLabel labelLastDate = new JLabel("Last date:");
    JTextField textLastDate = new JTextField(10);


    JPanel panelButtons = new JPanel();
    JButton buttonOk = new JButton("Ok");
    JButton buttonCancel  = new JButton("Cancel");
    
    JComboBox devicesList/* = new JComboBox()*/;
    
    JTable table = new JTable();
    DefaultTableModel modelo = new DefaultTableModel();
    JScrollPane scrollPane;

    public DuckFoodVisual() 
    {
        
        ArrayList<Device> devices = Device.getAll();
        int sizeArrayList = devices.size();
        String[] devicesMAC = new String[sizeArrayList + 1];
        devicesMAC[0] = "All devices";
        
        for (int i = 0; i < sizeArrayList; i++) 
        {
            devicesMAC[i+1] = devices.get(i).getId();
        }
        
        devicesList = new JComboBox(devicesMAC);
        
        ArrayList<ReadingDuckFood> duckReading = new ArrayList<ReadingDuckFood>();
        duckReading = ReadingDuckFood.getAll();
        
        modelo = new DefaultTableModel(null, nombresDeColumna); 
        
        String[] fila = new String[6];

        for (ReadingDuckFood dfh : duckReading) 
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = dateFormat.format(dfh.getDate());

            fila[0] = dfh.getDevice().getId();
            fila[1] = dfh.getDevice().getDescription();
            fila[2] = stringDate;
            fila[3] = dfh.getTime();
            fila[4] = Double.toString(dfh.getActualValue());
            fila[5] = Double.toString(dfh.getValue());

            modelo.addRow(fila);
        }
        
        // creamos la Table basados en el modelo de datos que hemos creado
        JTable table = new JTable(modelo);

        // ordenacion de filas (por defecto, al ser tipos primitivos)
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modelo);
        table.setRowSorter(sorter);

        // creamos un scroll y le añadomos la tabla
        scrollPane = new JScrollPane(table);
        
        panelGrid.add(scrollPane, BorderLayout.CENTER);
        
        Container container = frame.getContentPane();
        //LAYOUTS
        //ADD CONTROLS TO PANEL
        GridBagConstraints c= new GridBagConstraints();
        c.insets = new Insets(10,10,0,0); //margins between controls
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.WEST; //ALING TO LEFT
        c.gridy = 0; c.gridx = 0; panelControls.add(labelFirstDate , c);
        c.gridy = 0; c.gridx = 1; panelControls.add(textFirstDate , c);
        c.gridy = 1; c.gridx = 0; panelControls.add(labelLastDate , c) ;
        c.gridy = 1; c.gridx = 0; panelControls.add(textLastDate , c) ;
        c.gridy = 2; c.gridx = 0; panelControls.add(devicesList , c) ;

        //ADD THE PANLE TO THE CONTAINER
        container.add(panelControls, BorderLayout.PAGE_START);
        
        //c.gridy = 3; c.gridx = 0; panelControls.add(buttonOk , c) ;
        c.gridy = 3; c.gridx = 0; panelControls.add(buttonCancel , c) ;
        
        
        devicesList.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                String sDate1 = textFirstDate.getText();  
                String sDate2 = textLastDate.getText();
                
                System.out.println(sDate1);
                System.out.println(sDate2);
                
                if(sDate1.length()==0 && sDate2.length()==0)
                {
                    selectReadingMac();
                }
                else
                {
                    selectMacWithDates();
                }
                
            }
        });
        
        
        buttonCancel.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                close();
            }
        });
        
        //buttonOk.addActionListener(new java.awt.event.ActionListener() 
        //{
            //public void actionPerformed(java.awt.event.ActionEvent evt) 
            //{  
                //getDatesAllTypes();
            //}
        //});
        
        
        
        // le decimos que cierre 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // añadimos la tabla
        frame.add(scrollPane);
        // tamaño
        frame.setSize(700, 300);
        // mostramos
        //frame.setVisible(true);
    }
    
    public void fillTable(ArrayList<ReadingDuckFood> duckReadings)
    {
        int sizeModel = modelo.getRowCount();

        for (int i = 0; i < sizeModel ; i ++) { modelo.removeRow(0); }

        String[] fila = new String[6];

        for (ReadingDuckFood dfh : duckReadings) 
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = dateFormat.format(dfh.getDate());

            fila[0] = dfh.getDevice().getId();
            fila[1] = dfh.getDevice().getDescription();
            fila[2] = stringDate;
            fila[3] = dfh.getTime();
            fila[4] = Double.toString(dfh.getActualValue());
            fila[5] = Double.toString(dfh.getValue());

            modelo.addRow(fila);
        }
        
        modelo.fireTableDataChanged();   
    }
    
    public void selectReadingMac()
    {
        if (devicesList.getSelectedItem() != null) 
        {
            String mac = devicesList.getSelectedItem().toString();
            int type;
            ArrayList<ReadingDuckFood> duckReadings = new ArrayList<ReadingDuckFood>();
            switch(mac)
            {
                case "All devices": 
                    type = 0; 
                    duckReadings = ReadingDuckFood.getAll();
                    break;
                default: 
                    duckReadings = ReadingDuckFood.getAll(mac);
                    break;
            }
                    
            fillTable(duckReadings);
        }
    }
    
    public void selectMacWithDates()
    {
        try
        {
            String mac = devicesList.getSelectedItem().toString();
            ArrayList<ReadingDuckFood> duckReadings = new ArrayList<ReadingDuckFood>();
                    
            String sDate1 = textFirstDate.getText();  
            String sDate2 = textLastDate.getText();
            
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                
            if(mac == "All devices" && sDate1.length()!=0 && sDate2.length()!=0 )
            {
                duckReadings = ReadingDuckFood.getAll(date1, date2);
            }
            else
            {
                duckReadings = ReadingDuckFood.getAll(date1, date2, mac);
            }
            
            int sizeArrayList = duckReadings.size();
                    
            if(sizeArrayList == 0) { JOptionPane.showMessageDialog(frame, "No se encontraron lecturas"); }
            else { fillTable(duckReadings); }
                    
        } 
        catch (ParseException ex) 
        {
            JOptionPane.showMessageDialog(frame, "Asegurese que la fecha ingresada sea dia/mes/año Ejemplo:(01/08/2018)");
        }
    }

    
    public void show(){
            this.frame.setVisible(true);
    }//public void show
        
    private void close(){
            if(JOptionPane.showConfirmDialog(
                    this.frame, "Exit Application? ", "Confirm", JOptionPane.YES_NO_OPTION )
                    == JOptionPane.YES_NO_OPTION)
                System.exit(0);
           
        }
}