package Frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuVisual 
{
    JFrame frame = new JFrame("Menu");
    JButton buttonLog = new JButton("Error Log");
    JButton buttonContainer = new JButton("Container");
    JButton buttonDuck = new JButton("Duck Food");
    JButton buttonDog = new JButton("Dog Food");
    JButton buttonDevice = new JButton("Device");
    JButton buttonExit = new JButton("Exit");
    JLabel labelCatalog = new JLabel("Select your catalog");
    JPanel panelTitle = new JPanel();
    JPanel panelButtons = new JPanel();
    
    public MenuVisual()
    {
        Container container = frame.getContentPane();
        
        panelTitle.add(labelCatalog);
        
        container.add(panelTitle, BorderLayout.PAGE_START);
        //LAYOUTS
        //ADD CONTROLS TO PANEL
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10); //margins between controls
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.WEST; //ALING TO LEFT
        c.gridy = 0; c.gridx = 0; panelButtons.add(buttonLog , c);
        c.gridy = 0; c.gridx = 1; panelButtons.add(buttonContainer , c);
        c.gridy = 1; c.gridx = 2; panelButtons.add(buttonDuck , c) ;
        c.gridy = 1; c.gridx = 2; panelButtons.add(buttonDog , c) ;
        c.gridy = 1; c.gridx = 3; panelButtons.add(buttonDevice , c) ;
        c.gridy = 1; c.gridx = 4; panelButtons.add(buttonDevice , c) ;
        c.gridy = 2; c.gridx = 0; panelButtons.add(buttonExit , c) ;
       

        //ADD THE PANLE TO THE CONTAINER
        container.add(panelButtons, BorderLayout.CENTER);
        
        buttonLog.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) { ErrorLogVisual elv = new ErrorLogVisual(); elv.show();}
        });
        
        buttonContainer.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) { ContainerVisual cv = new ContainerVisual(); cv.show(); }
        });
        
        buttonDuck.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) { DuckFoodVisual dfv = new DuckFoodVisual(); dfv.show(); }
        });
                
        buttonDog.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) { DogFoodVisual dfv = new DogFoodVisual(); dfv.show(); }
        });
        
        buttonDevice.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) { DevicesVisual dv = new DevicesVisual(); /*dv.show();*/ }
        });
                
        buttonExit.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) { close(); }
        });     
        
        
        // le decimos que cierre 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // tamaño
        frame.setSize(300, 200);
        // mostramos
        frame.setVisible(true);
    }
    
    private void close()
    {
        if(JOptionPane.showConfirmDialog(
            this.frame, "Exit Application? ", "Confirm", JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_NO_OPTION)
                System.exit(0);
    }
}
