package Frames;

import Models.ContainerCans;
import Models.Device;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DevicesVisual 
{
    // titulos
    //private final static String nombresDeColumna[] = { "MAC Device", "Description", "Ip Address", "Total Cans Collected"};
    
    JFrame frame = new JFrame("Devices");
    
    JPanel panelControls = new JPanel();
    
    JPanel panelGrid = new JPanel();
    
    JLabel labelMAC = new JLabel("MAC of Device:");
    JTextField textMAC = new  JTextField(10);
    
    JLabel labelDescription = new JLabel("Description of device:");
    JTextField textDescription = new JTextField(20);
    
    JLabel labelIpAddress = new JLabel("Ip Address of device:");
    JTextField textIpAddress = new JTextField(10);

    JPanel panelButtons = new JPanel();
    JButton buttonOk = new JButton("Ok");
    JButton buttonNew = new JButton("Nuevo");
    JButton buttonCancel  = new JButton("Cancel");
    
    //JComboBox devicesList/* = new JComboBox()*/;
    
    JTable table/* = new JTable()*/;
    DefaultTableModel modelo = new DefaultTableModel();
    JScrollPane scrollPane;

    public DevicesVisual() 
    {
        JTable table = new JTable(modelo);
        
        
        modelo.addColumn("Mac Address");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Ip Address");
        modelo.addColumn("Total cans collected");

        ArrayList<Device> devices = Device.getAll();
        
        //modelo = new DefaultTableModel(null, nombresDeColumna); 
        
        String[] fila = new String[4];

        for (Device d : devices) 
        {
            fila[0] = d.getId();
            fila[1] = d.getDescription();
            fila[2] = d.getIpAddress();
            fila[3] = Integer.toString(d.getTotalLifeCans());

            modelo.addRow(fila);
        }
        
        table.setModel(modelo);

        // ordenacion de filas (por defecto, al ser tipos primitivos)
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modelo);
        table.setRowSorter(sorter);

        // creamos un scroll y le añadomos la tabla
        scrollPane = new JScrollPane(table);
        
        panelGrid.add(scrollPane);
        
        Container container = frame.getContentPane();
        //LAYOUTS
        //ADD CONTROLS TO PANEL
        GridBagConstraints c= new GridBagConstraints();
        c.insets = new Insets(10,10,0,0); //margins between controls
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.WEST; //ALING TO LEFT
        c.gridy = 0; c.gridx = 0; panelControls.add(labelMAC , c);
        c.gridy = 0; c.gridx = 1; panelControls.add(textMAC , c);
        c.gridy = 1; c.gridx = 0; panelControls.add(labelDescription , c) ;
        c.gridy = 1; c.gridx = 1; panelControls.add(textDescription , c) ;
        c.gridy = 2; c.gridx = 0; panelControls.add(labelIpAddress , c) ;
        c.gridy = 2; c.gridx = 1; panelControls.add(textIpAddress , c) ;
        c.gridy = 3; c.gridx = 0; panelControls.add(buttonOk , c) ;
        c.gridy = 3; c.gridx = 1; panelControls.add(buttonNew , c) ;
        c.gridy = 3; c.gridx = 2; panelControls.add(buttonCancel , c) ;

        //ADD THE PANLE TO THE CONTAINER
        container.add(panelControls, BorderLayout.NORTH);
        
        container.add(panelGrid, BorderLayout.SOUTH);
        
        //c.gridy = 3; c.gridx = 0; panelControls.add(buttonOk , c) ;
        
       // table.addMouseListener(new java.awt.event.MouseAdapter() {
         //   @Override
           // public void mouseClicked(java.awt.event.MouseEvent evt) 
            //{
               // JTableMouseClicked(evt);
            //}
        //});
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {
            // do some actions here, for example
            // print first column value from selected row
            textMAC.setEnabled(false);
            textMAC.setText((String) table.getValueAt(table.getSelectedRow(), 0));
            textDescription.setText((String) table.getValueAt(table.getSelectedRow(), 1));
            textIpAddress.setText((String) table.getValueAt(table.getSelectedRow(), 2));
            
            System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
            }
        });
        
        buttonNew.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                textMAC.setEnabled(true);
                textMAC.setText("");
                textDescription.setText("");
                textIpAddress.setText("");
            }
        });
        
        buttonOk.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                
                String mac = textMAC.getText();
                String des = textDescription.getText();
                String ip = textIpAddress.getText();
                if(!mac.equals("") || !des.equals("") || !ip.equals(""))
                {
                    Device d = new Device();
                    d.setId(mac);
                    d.setDescription(des);
                    d.setIpAddress(ip);
                    d.add();
                    ArrayList<Device> devices = Device.getAll();
                    fillTable(devices);
                    JOptionPane.showMessageDialog(frame, "Se ha añadido exitosamente el dispositivo");
                    textMAC.setText("");
                    textDescription.setText("");
                    textIpAddress.setText("");
                }
                else
                {
                    JOptionPane.showMessageDialog(frame, "Falta llenar algun campo");
                }
            }
        });
        
        buttonCancel.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                //close();
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
        frame.setSize(1100, 300);
        // mostramos
        frame.setVisible(true);
    }
    
    public void fillTable(ArrayList<Device> devices)
    {
        int sizeModel = modelo.getRowCount();

        for (int i = 0; i < sizeModel ; i ++) { modelo.removeRow(0); }

        String[] fila = new String[4];

        for (Device d : devices) 
        {
            fila[0] = d.getId();
            fila[1] = d.getDescription();
            fila[2] = d.getIpAddress();
            fila[3] = Integer.toString(d.getTotalLifeCans());

            modelo.addRow(fila);
        }
        
        modelo.fireTableDataChanged();   
    }
    
    private void JTableMouseClicked(java.awt.event.MouseEvent evt)
    {
        if (evt.getButton() == 1)
        {
            int fila = table.getSelectedRow();
            System.out.println(fila);
            try
            {
                textMAC.setText((String) table.getValueAt(fila, 0));
                textDescription.setText((String) table.getValueAt(fila, 1));
                textIpAddress.setText((String) table.getValueAt(fila, 2));
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}
