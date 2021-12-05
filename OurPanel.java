/**
 * <h1> Panel for the Snake Game </h1?
 * Extends JPanel and implements ActionListener 
 * Creates the Panel to place in OurFrame class.
 * Has draw and move methods that are drawn every 60ms.
 * Faster move rate to make it more challenging as I tweaked functionatily 
 * so that when you collide with an edge you appear on the opposite side.
 * Includes a GameOver scree.
 * 
 * 
 * <p>
 * @author Francisco Javier Serrano Jr
 * @version 1.1
 * @since 11/2021
 * </p>
 */


import javax.swing.*;

import java.awt.event.*; 
import java.awt.*;

import java.util.Random; 
public class OurPanel  extends JPanel implements ActionListener { 
    /**
     * Gives the width dimension 
     */
    static final int SCREEN_W=600; 
     /**
     * Gives the height dimension 
     */
    static final int SCREEN_H=600;
     /**
     * Gives the scaling factor for the units
     */
    static final int UNIT_SIZE=25; 
     /**
     * Gives the amount of units 
     */
    static final int GAME_UNITS= (SCREEN_H*SCREEN_W)/ UNIT_SIZE; 
     /**
     * Holds delay for timer 
     */
    static final int DELAY=60; 
     /**
     * Hold x-coordinates of snake 
     */
    final int[] x= new int [GAME_UNITS] ; 
    /**
     * Hold y-coordinates of snake 
     */
    final int[] y = new int [GAME_UNITS];
   // Start snake with length of 2 units
    int body=2; 
    int applesgotten, appleX, appleY; 
    // start direction to the right
    char direction = 'R'; 
    boolean running = false; 
    Timer timer; 
    Random random; 
    /**
     * Default Constructor for my Panel. 
     * Configure panel
     * Adds a key event listener which is an instance of a subclass of the KeyAdapter class
     * Make call to method to use a timer instance 
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
     * Starts the process of drawing and updating location of snake and apple 
     * Creates and gets apple coordinates at random 
     * Timer instance acts on Panel constructor, a delay is passed on, and started
     */
    public void startGame() { 
        giveApple(); 
        running=true; 
        timer= new Timer(DELAY,this); 
        timer.start(); 
    } 
    /**
     * Override paint component, call parent paintComponent method
     * and all your own draw method
     * Called by repaint() method when an action event is performed 
     */
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);
        draw(g); 
    }
    /**
     * All of my drawing will take place here except End Screen.
     * Creates grid-lines in first for loop, draws apple, 
     * colors the head of snake a different color, and colors rest of snake body.
     * Lets player keep track of score
     * Uses if/else to send to EndScreen with a GameOver method call
     */
    public void draw(Graphics g) { 
        if(running) { 

            for (int i =0; i<SCREEN_H/UNIT_SIZE; i++ ) { 
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_H);
                g.drawLine(0, i* UNIT_SIZE, SCREEN_W, i* UNIT_SIZE); 
            }
            g.setColor(Color.red); 
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
            for(int i=0; i<body; i++) { 
                if(i==0){ 
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); 
                    }
                    else { 
                        g.setColor(new Color (121,207,92) );
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            g.setColor(Color.red); 
            g.setFont(new Font ("Times New Roman",Font.BOLD,40)); 
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:"+applesgotten, (SCREEN_W-metrics.stringWidth("Score:"+applesgotten))/2, g.getFont().getSize());
        } else { 
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
     * To move the coordinates of snake around.
     * Reacts to change of direction from keyPressed method when called in actionPerformed method
     * Movement is from tail-end pushing up toward the head of snake
     * Switch decision structure to change direction of head
     *
     */
    public void move() { 
        for (int i=body;i>0;i--) { // push last element up the chain 
            x[i]=x[i-1]; 
            y[i]=y[i-1]; 
        }
        switch (direction) { // recall that origin starts at upper-left corner
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
     * To check if snake has hit itself
     * Changed my version to let you go from one side of screen to the other
     */
    public void checkCollision () { 
    for (int i=body;i>0;i-- ){ 
        if((x[0]==x[i])&& (y[0]==y[i])) {
                running=false; 
        }
    }
        if (x[0]<0) { 
            x[0]=SCREEN_W; 
        }
        if (x[0]>SCREEN_W) { 
            x[0]=0; 
        }
        if (y[0]<0) { 
            y[0]=SCREEN_H; 

        }
        if (y[0]>SCREEN_H) { 
            y[0]=0; 
        }
        if (!running) { 
            timer.stop(); 
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
        g.drawString("Game Over", (SCREEN_W-metrics2.stringWidth("Game Over"))/2, SCREEN_H/2);
    }
    /**
     * When player touched keys and game is running then 
     * move, checkApple and checkCollision methods are called
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
