/**
 * <h1> Frame for Snake Game </h1?
 * Extends JFrame
 * Creates the Frame to place the panel instance in
 * Configures frame to our needs
 * 
 * <p>
 * @author Francisco Javier Serrano Jr
 * @version 1.1
 * @since 11/2021
 * </p>
 */

import javax.swing.JFrame;

public class OurFrame extends JFrame{
    /**
     * Configure Frame to our needs, instantiate an OurPanel object and insert into frame
     * Centers the frame, fits panel to frame, does not let you resize
     */
    OurFrame(){ 
        OurPanel panelg = new OurPanel();
        this.add(panelg); 
        this.setTitle("Francisco's Snake Game!!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setResizable(false); 
        this.pack();
        this.setLocationRelativeTo(null); 
        this.setVisible(true); 
    }
    
}
