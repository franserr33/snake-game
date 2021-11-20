import javax.swing.*;
import java.awt.event.*; 
import java.awt.*;
import java.util.Random; 
// inheriting JPanel and implementing the ActionListener interface
public class OurPanel  extends JPanel implements ActionListener {
    // create unmodifiable dimensions and delay to pass onto timer instance 
    static final int SCREEN_W=600; 
    static final int SCREEN_H=600;
    static final int UNIT_SIZE=25; // unit size is my scaling factor
    static final int GAME_UNITS= (SCREEN_H*SCREEN_W)/ UNIT_SIZE; // how many "squares" there are,not the legth of the square
    static final int DELAY=80; 
    // two arrays for grid of x&y where the number of useable spaces on the screen, bound by size of game itself
    final int[] x= new int [GAME_UNITS] ; 
    final int[] y = new int [GAME_UNITS];

    int body=2; // size of snake at start
    int applesgotten, appleX, appleY; // counter var and var for apple coordinates
    char direction = 'R'; // snakes moves right initially 
    boolean running = false; //not running at start
    // using instance of timer class and random class so creating ref variable 
    Timer timer; 
    Random random; 
    // Panel default constructor
    OurPanel() { 
        random=new Random(); // give ref var the address of newly instantiated random object
        this.setPreferredSize(new Dimension(SCREEN_W, SCREEN_H)); // size of panel to be placed in frame
        this.setBackground(Color.black); 
        this.setFocusable(true); // panel can be focsed on  
        // using an instance of MyKeyAdapter to monitor key events, extends keyadapter with overridden methods
        this.addKeyListener(new MyKeyAdapter());
        startGame(); 
    }
    public void startGame() { 
        giveApple(); // get coordinates for a randomly placed apple on screen
        running=true; // rest of prog will now run
        timer= new Timer(DELAY,this); // every 80ms the instance of the ourpanel is called 
        timer.start(); // start timer

    } 
    // my on-demand call for redrawing
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
            g.setColor(Color.red); // set to red then configure 
            // coordinates upper left corner of apple passed w scaling factors for width and heigth of apple
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
            // loop to fill out the snake
            for(int i=0; i<body; i++) { 
                if(i==0){ // for the head a shade of green
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    else { // rest of body give a different shade of green
                        g.setColor(new Color (121,207,92) );
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            // create animation for the score keeping
            g.setColor(Color.red); 
            g.setFont(new Font ("Times New Roman",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:"+applesgotten, (SCREEN_W-metrics.stringWidth("Score:"+applesgotten))/2, g.getFont().getSize());
        } else { 
            // send to game over screen
            gameOver(g);
        }
    }
    // generate new apple when we eat one or start game
    public void giveApple(){ 
        appleX=random.nextInt((int)(SCREEN_W/UNIT_SIZE))*UNIT_SIZE;
        // the division ensures that the coordinate's range would be the width divided by each little box in grid
        appleY=random.nextInt((int)(SCREEN_H/UNIT_SIZE))*UNIT_SIZE; 
    }
    // method to show animated movement
    public void move() { 
        // when not changing direction push elements up
        for (int i=body;i>0;i--) { 
            x[i]=x[i-1]; // shift x&y coordinate over 1
            y[i]=y[i-1]; // pull each segment up the chain and to follow head
        }
        switch (direction) { 
        // when going up, head's coordinate will equal 
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
// increase body length, score counter and give another apple when head's coordinates match the apples
public void checkApple() { 
if ((x[0]==appleX)&& (y[0]==appleY)) { 
    body++; 
    applesgotten++; 
    giveApple();
}
    }

    public void checkCollision () { 
        // first check to see if head of snake hits the body
        for (int i=body;i>0;i-- ){ 
            // loops through every part of body and makes sure that they dont share same coordinate with the head
            if((x[0]==x[i])&& (y[0]==y[i])) {
                running=false; // flag as false to start the game over screen
            }
        }
        // check to see if head touches left border 
        if (x[0]<0) { 
            running=false; 
        }
        // check for right border
        if (x[0]>SCREEN_W) { 
            running=false; 
        }
        // check to see if head touches top 
        if (y[0]<0) { 
            running=false; 
        }
        // check to see if they touch the bottom border
        if (y[0]>SCREEN_H) { 
            running=false; 
        }
        if (!running) { 
            timer.stop(); //stop firing of timer once user loses
        }

    }
    public void gameOver(Graphics g) { 
        // end screen set up
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

    @Override
    public void actionPerformed(ActionEvent e) { 
        if (running) { 
            move(); // 
            checkApple();
            checkCollision();

        }
        repaint();

    }
    public class MyKeyAdapter extends KeyAdapter { 
        @Override 
        public void keyPressed(KeyEvent e) { 
            // matches keycode to four discrete cases
            switch (e.getKeyCode()) { 
            // each case has an if statement to change direction if you are not already going in the direction of the key you hit
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
