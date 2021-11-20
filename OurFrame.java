import javax.swing.JFrame;
// inherit JFrame
public class OurFrame extends JFrame{
    //write Default constructor for class
    OurFrame(){ 
        OurPanel panelg = new OurPanel();// create instance of our OurPanel class
        this.add(panelg); // insert panel into frame
        this.setTitle("Francisco's Snake Game!!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // let user exit when they press the X
        this.setResizable(false); // don't let user  resize 
        this.pack(); // the pack function will take our JFrame and fit it around the components 
        this.setVisible(true); // frames are initially invisible, change it to visible
        this.setLocationRelativeTo(null); // game appears in the middle of the screen 

    }
    
}
