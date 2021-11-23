import javax.swing.JFrame;
public class OurFrame extends JFrame{
    /**
     * Configure Frame to our needs, instantiate an OurPanel object and insert into frame
     */
    OurFrame(){ 
        // the timer class will continously create new instances of the panel class and pass references to the panelg var
        OurPanel panelg = new OurPanel();
        this.add(panelg); 
        this.setTitle("Francisco's Snake Game!!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // let user exit when they press the X
        this.setResizable(false); 
        this.pack();// will take our frame and fit it around the components 
        this.setVisible(true); // frames are initially invisible, change it to visible
        this.setLocationRelativeTo(null); // game appears in the middle of the screen 

    }
    
}
