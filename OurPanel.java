import javax.swing.*;
import java.awt.event.*; 
import java.awt.*;
import java.util.Random; 
public class OurPanel  extends JPanel implements ActionListener {
    /**create fields for screen dimensions, scaling factor for units, 
    number of units, and the delay to pass onto timer instance*/ 
    static final int SCREEN_W=600; 
    static final int SCREEN_H=600;
    static final int UNIT_SIZE=25; 
    static final int GAME_UNITS= (SCREEN_H*SCREEN_W)/ UNIT_SIZE; 
    static final int DELAY=80; 
    // two arrays to hold coordinates of snake
    final int[] x= new int [GAME_UNITS] ; 
    final int[] y = new int [GAME_UNITS];
    // snake starts with size 2, create variables for keeping count & apple coordinates, not running at start
    int body=2; 
    int applesgotten, appleX, appleY; 
    char direction = 'R'; 
    boolean running = false; 
    Timer timer; 
    Random random; 
    /**
     * Default Constructor for my Panel. 
     * Configure panel, allow it to be focused on, add action event listener for keys
     * Event/Key listener is now an instance of MyKeyAdpater which extends KeyAdapter
     */
    OurPanel() { 
        random=new Random(); 
        this.setPreferredSize(new Dimension(SCREEN_W, SCREEN_H)); 
        this.setBackground(Color.black); 
        this.setFocusable(true);  
        this.addKeyListener(new MyKeyAdapter());
        startGame(); 
    }
    /**
     * Method call to start the game 
     * Creates and gets apple coordinates randomly
     * Methods depedning on a running=true are allowed to execute
     * Timer instance acts on Panel constructor, an 80ms delay is passed on, and started
     */
    public void startGame() { 
        giveApple(); 
        running=true; 
        timer= new Timer(DELAY,this); 
        timer.start(); 
    } 
    /**
     * Override paint component
     * Calls the parent paintComponent method and calls draw method
     */
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);
        draw(g); 
    }
    public void draw(Graphics g) { 
        if(running) { 
            // loop to create grid lines
            for (int i =0; i<SCREEN_H/UNIT_SIZE; i++ ) { 
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_H);
                g.drawLine(0, i* UNIT_SIZE, SCREEN_W, i* UNIT_SIZE); 
            }
            // set to red then configure 
            g.setColor(Color.red); 
            // fill with the coordinates of the apple then make sure the fill is as large as a single unit
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
            // loop to fill out the snake
            for(int i=0; i<body; i++) { 
                if(i==0){ // for the head
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    else { // rest of body give a different shade of green
                        g.setColor(new Color (121,207,92) );
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            // visual for the score keeping
            g.setColor(Color.red); 
            g.setFont(new Font ("Times New Roman",Font.BOLD,40)); // TNR, make it bold and 40 pixels
            FontMetrics metrics = getFontMetrics(g.getFont());
            // draw the string in the top middle 
            g.drawString("Score:"+applesgotten, (SCREEN_W-metrics.stringWidth("Score:"+applesgotten))/2, g.getFont().getSize());
        } else { 
            // send to game over screen
            gameOver(g);
        }
    }
    /**
     * Generate new apple when we eat one or start game
     */
    public void giveApple(){ 
        appleX=random.nextInt((int)(SCREEN_W/UNIT_SIZE))*UNIT_SIZE;
        appleY=random.nextInt((int)(SCREEN_H/UNIT_SIZE))*UNIT_SIZE; 
    }
    /**
     * To move the coordinates of snake around by loop and switch structure
     */
    
    public void move() { 
        for (int i=body;i>0;i--) { // push last element up the chain 
            x[i]=x[i-1]; 
            y[i]=y[i-1]; 
        }
        // origin starts at upper-left corner
        switch (direction) { // to move direction of the head, additional restrictions in subclass
            case 'U': 
            y[0]=y[0]-UNIT_SIZE;
            break;
            case 'D': 
            y[0]=y[0]+UNIT_SIZE; 
            break; 
            case 'L': 
            x[0]=x[0]-UNIT_SIZE; 
            break; 
            case 'R': 
            x[0]=x[0]+UNIT_SIZE;
            break; 
        }

    }
     /**
     * To increase counter, increase length and replace an apple when snake head reaches apple
     */
    public void checkApple() { 
        if ((x[0]==appleX)&& (y[0]==appleY)) { 
            body++; 
            applesgotten++; 
            giveApple();
        }
        
    }
     /**
     * Method to check if head hit panel's borders or itself
     */
    public void checkCollision () { 
    for (int i=body;i>0;i-- ){ 
        if((x[0]==x[i])&& (y[0]==y[i])) {
                running=false; // flag as false to start the game over screen
        }
    }
        if (x[0]<0) { // for left border
            running=false; 
        }
        if (x[0]>SCREEN_W) { // for right border
            running=false; 
        }
        if (y[0]<0) { // for top border
            running=false; 
        }
        if (y[0]>SCREEN_H) { // for bottom border
            running=false; 
        }
        if (!running) { 
            timer.stop(); // new panel instance is not created
        }

    }
    /**
     * Creates and configures the GamevOver screen. 
     * Prints out the score and a GameOver message.
     */
    public void gameOver(Graphics g) { 
        g.setColor(Color.red); 
        g.setFont(new Font ("Times New Roman",Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score:"+applesgotten, (SCREEN_W-metrics1.stringWidth("Score:"+applesgotten))/2, g.getFont().getSize());
        g.setColor(Color.red); 
        g.setFont(new Font ("Times New Roman",Font.BOLD,80));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        // puts the stuff in the very center of the screen 
        g.drawString("Game Over", (SCREEN_W-metrics2.stringWidth("Game Over"))/2, SCREEN_H/2);
    }
    /**
     * Makes calls to these three methods continously as our grame is running 
     * Once these changes are made,calls the repaint method
     */
    @Override
    public void actionPerformed(ActionEvent e) { 
        if (running) { 
            move(); 
            checkApple();
            checkCollision();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter { 
        /**
         * Speacializing keyPressed method for my use
         * Prevents player from going in the opposite direction that they are currently going in 
         */
        @Override 
        public void keyPressed(KeyEvent e) { 
            // matches keycode to four discrete cases
            switch (e.getKeyCode()) { 
                case KeyEvent.VK_LEFT: 
                    if (direction!='R') { 
                        direction='L';
                    }
                    break; 
                case KeyEvent.VK_RIGHT: 
                    if (direction !='L') { 
                        direction = 'R'; 
                    }
                    break; 
                case KeyEvent.VK_UP: 
                    if (direction!='D') { 
                        direction= 'U';
                    }
                    break; 
                case KeyEvent.VK_DOWN: 
                    if (direction!='U') { 
                    direction='D';
                    }
                    break;    
            }
    
        }
    
    }
    
}
