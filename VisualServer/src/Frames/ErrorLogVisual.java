package Frames;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import Models.ErrorLog;
import Models.ErrorLogCode;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


/*Mandar esto a Raul
https://es.stackoverflow.com/questions/16721/como-puedo-crear-un-grid-en-java-que-me-permita-autofiltrar-los-datos-como-en
https://www.youtube.com/watch?v=mx5EyT1eEdk
https://www.daniweb.com/programming/software-development/threads/322792/netbeans-jtable-insert-row
*/

public class ErrorLogVisual 
{
    // titulos
    private final static String nombresDeColumna[] = { "Fecha", "Tipo de error", "Mensaje" };
    private final static String errores[] = {  "All type", "InvalidJSONFormat", "ValueOutOfRange", "RecordNotFound"};
    
    JFrame frame = new JFrame("Error log");
    
    JPanel panelControls = new JPanel();
    
    JPanel panelGrid = new JPanel();
    
    JLabel labelFirstDate = new JLabel("First date:");
    JTextField textFirstDate = new  JTextField(10);
    
    JLabel labelLastDate = new JLabel("Last date:");
    JTextField textLastDate = new JTextField(10);


    JPanel panelButtons = new JPanel();
    JButton buttonOk = new JButton("Ok");
    JButton buttonCancel  = new JButton("Cancel");
    
    JComboBox errorList = new JComboBox(errores);
    
    JTable table = new JTable();
    DefaultTableModel modelo = new DefaultTableModel();
    JScrollPane scrollPane;

    public ErrorLogVisual() 
    {
        ErrorLog el = new ErrorLog();
        ArrayList<ErrorLog> errors = new ArrayList<ErrorLog>();
        errors = el.getAll();
        
        modelo = new DefaultTableModel(null, nombresDeColumna); 
        
        String[] fila = new String[3];

        for (ErrorLog elo : errors) 
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String stringDate = dateFormat.format(elo.getDate());

            fila[0] = stringDate;
            fila[1] = elo.getEnumCode().toString();
            fila[2] = elo.getMessage();

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
        c.gridy = 2; c.gridx = 0; panelControls.add(errorList , c) ;

        //ADD THE PANLE TO THE CONTAINER
        container.add(panelControls, BorderLayout.PAGE_START);
        
        //c.gridy = 3; c.gridx = 0; panelControls.add(buttonOk , c) ;
        c.gridy = 3; c.gridx = 0; panelControls.add(buttonCancel , c) ;
        
        
        errorList.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                String sDate1 = textFirstDate.getText();  
                String sDate2 = textLastDate.getText();
                
                System.out.println(sDate1);
                System.out.println(sDate2);
                
                if(sDate1.length()==0 && sDate2.length()==0)
                {
                    selectErrorType();
                }
                else
                {
                    selectTypeWithDates();
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
    
    public void getDatesAllTypes()
    {
        try
        {
            ErrorLog el = new ErrorLog();
                    
            String sDate1 = textFirstDate.getText();  
            String sDate2 = textLastDate.getText();
                    
                    
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                    
            ArrayList<ErrorLog> errors = el.getAll(date1, date2);
                    
            int sizeArrayList = errors.size();
                    
            if(sizeArrayList == 0) { JOptionPane.showMessageDialog(frame, "No se encontraron errores entre esas fechas, ¡eso es bueno!"); }
            else { fillTable(errors); }
                    
        } 
        catch (ParseException ex) 
        {
            JOptionPane.showMessageDialog(frame, "Asegurese que la fecha ingresada sea dia/mes/año Ejemplo:(01/08/2018)");
        }
    }
    
    public void fillTable(ArrayList<ErrorLog> errors)
    {
        int sizeModel = modelo.getRowCount();

        for (int i = 0; i < sizeModel ; i ++) { modelo.removeRow(0); }

        String[] fila = new String[3];

        for (ErrorLog elo : errors) 
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            
            String stringDate = dateFormat.format(elo.getDate());
                            
            fila[0] = stringDate;
            fila[1] = elo.getEnumCode().toString();
            fila[2] = elo.getMessage();

            modelo.addRow(fila);
        }
        
        modelo.fireTableDataChanged();   
    }
    
    public void selectErrorType()
    {
        if (errorList.getSelectedItem() != null) 
        {
            String type = errorList.getSelectedItem().toString();
            int errorType;
            ArrayList<ErrorLog> errors = new ArrayList<ErrorLog>();
            switch(type)
            {
                case "InvalidJSONFormat": errorType = 0; break;
                case "ValueOutOfRange": errorType = 1; break;
                case "RecordNotFound": errorType = 2; break;
                default: 
                    errorType = 3;
                    break;
            }
            if(errorType == 3)
            {
                ErrorLog el = new ErrorLog();
                errors = el.getAll();
            }
            else { errors = ErrorLog.getAll(errorType); }
                    
            fillTable(errors);
        }
    }
    
    public void selectTypeWithDates()
    {
        try
        {
            String type = errorList.getSelectedItem().toString();
            ErrorLog el = new ErrorLog();
            int errorType;
            ArrayList<ErrorLog> errors = new ArrayList<ErrorLog>();
                    
            String sDate1 = textFirstDate.getText();  
            String sDate2 = textLastDate.getText();
            
            switch(type)
            {
                case "InvalidJSONFormat": errorType = 0; break;
                case "ValueOutOfRange": errorType = 1; break;
                case "RecordNotFound": errorType = 2; break;
                default: 
                    errorType = 3;
                    break;
            }
            
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
            
            if (errorType == 3 && sDate1 == "" && sDate2 == "")
            {
                errors = el.getAll();
            }
            else if (errorType == 3 && sDate1 != "" && sDate2 != "")
            {
                errors = el.getAll(date1, date2);
            }
            else { errors = ErrorLog.getAll(date1, date2, errorType); }
                    
            fillTable(errors);
            
            int sizeArrayList = errors.size();
                    
            if(sizeArrayList == 0) { JOptionPane.showMessageDialog(frame, "No se encontraron errores entre esas fechas, ¡eso es bueno!"); }
            else { fillTable(errors); }
                    
        } 
        catch (ParseException ex) 
        {
            JOptionPane.showMessageDialog(frame, "Asegurese que la fecha ingresada sea dia/mes/año Ejemplo:(01/08/2018)");
        }
    }
    
    public void show()
    {
        this.frame.setVisible(true);
    }//public void show
        
    private void close()
    {
        if(JOptionPane.showConfirmDialog(
            this.frame, "Exit Application? ", "Confirm", JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_NO_OPTION)
                System.exit(0);
    }
    
}
