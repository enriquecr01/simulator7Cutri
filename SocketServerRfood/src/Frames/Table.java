package Frames;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Table 
{
    String nombresDeColumna[] = { "MAC Device", "Date", "Hour", "Cans Inserted", "Weight", "Type of food" };
        
    JFrame frame = new JFrame("Container History");
    
    JPanel panelGrid = new JPanel();
    
    JTable table = new JTable();
    
    DefaultTableModel modelo = new DefaultTableModel();
    
    JScrollPane scrollPane;
    
    public Table()
    {
        modelo = new DefaultTableModel(null, nombresDeColumna); 
        
        JTable table = new JTable(modelo);
        
        scrollPane = new JScrollPane(table);
        
        panelGrid.add(scrollPane, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // añadimos la tabla
        frame.add(scrollPane);
        // tamaño
        frame.setSize(700, 300);
    }
    
    public void add(String[] newRow)
    {
        modelo.addRow(newRow);
        modelo.fireTableDataChanged();
    }
    
    public void show()
    {
        this.frame.setVisible(true);
    }
}
