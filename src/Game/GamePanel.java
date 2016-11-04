/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Controller.KeyboardController;
import GameObjects.Beam;
import GameObjects.Bullet;
import GameObjects.Enemy;
import GameObjects.GameObject;
import GameObjects.MovingGameObject;
import GameObjects.Shield;
import GameObjects.Ship;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author araderma
 */
public class GamePanel extends JPanel
{
    // Required components. Do not remove!
    private Timer                       gameTimer; 
    private KeyboardController          controller; 
    
    // Controls size of game window and framerate
    private final int                   gameWidth = 800; 
    private final int                   gameHeight = 720; 
    private final int                   framesPerSecond = 60; 
    
    // instance variables added
    private int                         eventFrameNumber;
    private int                         playerLives;
    private int                         playerScore;
    private boolean                     gamePaused;
    private HighScoreList               list1;
    
    private Ship                        ship1;
    private ArrayList<Enemy>            enemyList;
    private int []                      closestEnemyPerColumn;
    private ArrayList<Shield>           shieldList;
    private ArrayList<MovingGameObject> projectileList;
    
    private GameFrame                   container;                  //added to create a way to delete GameFrame after each game
                                
    
    
    public final void setupGame()
    {
        // Initialize field values here
        
        playerLives     = 5;
        playerScore     = 0;
        gamePaused      = false;
        list1           = new HighScoreList();
        
        
        ship1           = new Ship( gameWidth /2, gameHeight - 100, Color.RED, controller );
        enemyList       = new ArrayList(0);
        shieldList      = new ArrayList(0);
        projectileList  = new ArrayList(0);
        
        //creates a platoon of Enemy objects, first number is rows, second number is columns
        createEnemies(5, 5);
        
        //creates three walls to protect the player, made up of Shield objects
        //argument controls the number of rows of Shield objects the walls have
        createWalls(4);
        
        
//        populateHighScores();
        
        
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        // Draw GameObjects here
        super.paint(g);
        
        //displays current lives and score
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + playerLives, 10, 30);
        g.drawString("Score: " + playerScore, 10, 50);
        
        g.setColor(Color.GRAY);
        g.drawString("Press ENTER to pause.   Press ESC to quit.", 10, gameHeight-15);
        
        
        
        ship1.draw(g);
        
        
        
        for(Enemy enemy : enemyList)                
        {
            enemy.draw(g);
        }
        
        
        for(Shield shield: shieldList)
        {
            shield.draw(g);
        }
        
        
        for( MovingGameObject object : projectileList)
        {
            object.draw(g);
        }  
        
              
    }
    
    
    public void updateGameState(int frameNumber)
    {      
        // Move GameObjects and check for collisions here 
        ship1.move();
        
        
        //moves Enemy ArrayList based on a time limitation
        if( enemyList.size() > 0 && frameNumber % 20 == 0)
        {
            checkEnemyPosition();
            moveEnemies(); 
        }
        
        
        
        //instantiates Bullet object based on conditions and assigns it to an ArrayList
        //limits number of Bullets that can be created based on time
        if( isSpaceBarPressed() && isBulletReady(frameNumber)  )
        {
            projectileList.add(new Bullet(ship1,4,0,-2,Color.YELLOW));
            
            controller.resetController();
            this.eventFrameNumber = frameNumber;
        }
        

        
        //instantiates Beam objects based on conditions and assigns them to an ArrayList
        //allows Enemy to fire after a short introduction non-firing time
        //allows Enemy to fire only intermittedly
        if(enemyList.size() > 0)
        {
            if( frameNumber > 250 && frameNumber % 100 == 0)
            {
                determineEnemyBeams();
            } 
        }
        
        
        
        if(projectileList.size() > 0)
        {
            moveProjectiles();
        }
        
        
        
        detectOutOfFrame();
        detectCollisionWithEnemy();
        detectCollisionWithShip();
        detectCollisionWithShield();
        checkGameStatus();
        
        //removes collided objects from game
        removeObjectsFromGame();
        
   
    }
    
    
    //private helper method
    //instantiates a platoon of Enemies and assigns them to an ArrayList
    private void createEnemies(int rows, int columns)
    {
        //instantiates int array with length equal to number of columns
        //sets each value initially to number of rows
        //keeps track of closest Enemy to Ship in each row for firing purposes
        closestEnemyPerColumn = new int[columns];
        for(int i = 0; i < closestEnemyPerColumn.length; i++)
        {
            closestEnemyPerColumn[i] = rows-1;
        }
        
        
        //Color possibilities for the Enemy objects
        Color[] colorList = {Color.ORANGE, Color.GREEN, Color.CYAN, Color.YELLOW, 
                             Color.MAGENTA, Color.WHITE, Color.RED, Color.BLUE};
        
        //Amount of space between Enemy objects
        int enemySpacing = 10;
        
        //Height of Enemy object is determined by this algorithm
        //Enemy rows intended to fit within the upper half of the game frame
        //Rows start 50 from the top, to maintain a border
        int enemyHeight = (gameHeight/2 - 50 - (rows - 1) * enemySpacing ) / rows;
        
        //Width of Enemy object is the same as its height, to maintain square shape of each Enemy object
        int enemyWidth = enemyHeight;
        
        //Starting coordinate of the first Enemy object
        //Centers the Enemy columns in the game frame
        int enemyStartXCoordinate = (gameWidth - (columns - 1) * enemySpacing - columns * enemyWidth) / 2;
        
        
        //Assigns Enemy objects to an ArrayList
        //xPosition depends on column number
        //yPosition depends on row number
        //color depends on row number so that all columns in a single row have the same color
        for( int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                enemyList.add( new Enemy(enemyStartXCoordinate + i*(enemyWidth + enemySpacing),  
                                         50 + j*(enemyHeight + enemySpacing), 
                                         enemyWidth, enemyHeight, 10, 0, j, i, colorList[j])  );
            }
        }
    }
    
    

    //private helper method
    //instantiates three walls made of Shield objects
    private void createWalls(int strength)
    {
        //wall width depends on game frame width and spacing desired between walls
        int wallWidth = (gameWidth - 401) / 3;
        
        //width of Shield object is 25% wall width
        int shieldWidth = wallWidth / 4;
        
        //height of Shield object is manually set
        int shieldHeight = 4;
        
        //Colors of each row of the wall
        Color [] colorList = {Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.DARK_GRAY};
        
        
        
        //repeats each row of Shield objects required in the wall
        for(int k = 0; k < strength; k++)
        {
            
            //builds three separate walls in a line, each consisting of a single row of Shield objects
            for(int i = 0; i < 3; i++)
            {
                
                //instantiates each Shield object in a single wall in a single row
                for(int j = 0; j < 5; j++)
                {
                    
                    //xPosition depends on wall 1, 2, or 3 and current column in that wall 
                    int startXCoordinate = 100 + (shieldWidth*4 + 100)*i + shieldWidth*j;
                    
                    //yPosition is set an arbitrary distance from the bottom of the game frame
                    //yPosition depends on current row of Shield objects required
                    int startYCoordinate = (gameHeight - 150) + shieldHeight*k;

                    //instantiates new Shield and assigns it to ArrayList
                    shieldList.add( new Shield( startXCoordinate, startYCoordinate, shieldWidth, shieldHeight, colorList[k])  );
                }
            }   
        }       
    }    


    
    //private helper method
    //if detect Enemy has collided with game frame or is out of frame
    //changes xVelocity to opposite its current value and changes yPosition 
    //to keep Enemies on the screen and moving toward the Player
    private void checkEnemyPosition()
    {
        //check each Enemy object in ArrayList
        for( Enemy enemy : enemyList)
        {
            
            //if one Enemy contacts the frame of the game or is out of frame, 
            //reverses direction for all Enemy objects and moves all Enemy objects down
            //performs this function exactly once per check
            if(enemy.detectFrameCollision(gameWidth, gameHeight) || enemy.detectOutOfFrame(gameWidth, gameHeight))
            {
                
                
                for(Enemy object : enemyList)
                {
                    object.setXVelocity( object.getXVelocity() * -1);
                    object.setYPosition( object.getYPosition() +  20);
                    
                }
                break;   
            }                     
        }
    }    
    
    
    
    //private helper method
    //moves Enemy objects
    private void moveEnemies()
    {
        for( Enemy enemy: enemyList)
        {
            enemy.move();
        }
    }
    
    
    
    //private helper method
    //returns true if spacebar is currently pressed
    private boolean isSpaceBarPressed()
    {
        return controller.getKeyStatus(KeyEvent.VK_SPACE);   
    }
    
    
    
    //private helper method
    //returns true if currentFrame is greater than frameNumber of last Bullet created
    //and some additional time
    private boolean isBulletReady(int currentFrame )
    {
        return currentFrame > eventFrameNumber + 60;       
    }
    
    
    
    //private helper method
    //instantiates a set number of Beam objects per cycle
    //assigns the Beam objects to an ArrayList
    private void determineEnemyBeams()                                                      
    {
        Random rand = new Random();
        ArrayList<Enemy> temp = determineFirstEnemyInEachColumn(enemyList);
        
        //creates arrayList for index positions that have been called already
        int [] selectedIndexes = {-1, -1, -1};
        boolean alreadyAdded = false;
        
        //randomly selects 3 Enemy objects that are the first Enemy in their column to fire a beam
        //if the number of enemies remaining is less than 3, only randomly selects the number remaining
        //each Enemy can only be added to list once
        for(int i = 0; i < 3; i++)
        {
            if(i > temp.size() )
            {
                break;
            }
            
            //determines random index position based on list of all first Enemy object in a given column
            int index = rand.nextInt( temp.size() );
            
            
            //checks whether index position is in selectedIndexes int array 
            for(int j : selectedIndexes)
            {
                if(j == index)
                {
                    alreadyAdded = true;
                }
            }
            
            //if index position hasn't already been added
            if( !alreadyAdded)
            {
               //instantiates Beam object in front of each Enemy selected
                //assigns objects to ArrayList
                projectileList.add(new Beam(temp.get(index), 0, 2, 4, 10,Color.WHITE)); 
                selectedIndexes[i] = index;
                
                //resets boolean variable for next iteration
                alreadyAdded = false;
            }    
        }   
    }    

    
     
    //private helper method
    //Creates a temporary ArrayList of the first Enemy object in each column
    private ArrayList<Enemy> determineFirstEnemyInEachColumn(ArrayList<Enemy> list)
    {
        
        ArrayList<Enemy> temp = new ArrayList<Enemy> (0);
        
        //checks the current row position for closest Enemy in that row
        //checks against the row position of each Enemy, based on column position of Enemy
        //Enemy objects that are the first Enemy in their row are added to a temporary ArrayList
        for(Enemy enemy: list)
        {
            //prevents error due to int array value being less than 0
            if (   closestEnemyPerColumn[enemy.getObjectColumn()] >= 0 &&
                   closestEnemyPerColumn[enemy.getObjectColumn()] == enemy.getObjectRow()  )
            {
                temp.add(enemy);
            }
        }
        
        return temp;
    }
    
    
    //private helper method
    //moves all objects in projectileList and updates 
    private void moveProjectiles()
    {
        for( MovingGameObject object : projectileList)
        {
            object.move();   
        } 
    }

    
    //private helper method
    //detects whether MovingGameObjects are out of frame and sets ShouldBeRemoved variable to true
    private void detectOutOfFrame()
    {
        for(MovingGameObject object: projectileList)
        {
            if(object.detectOutOfFrame(gameWidth, gameHeight))
            {
                object.setShouldBeRemoved(true);
            }
        }
    }
    
    
    //private helper method
    //determines whether Bullet hits the Enemy                                              //issue if Enemy moves into own Beam??
    //sets ShouldBeRemoved variable accordingly
    private void detectCollisionWithEnemy()                                     
    {
        //iterates through projectile list and Enemy list
        for(MovingGameObject object : projectileList)
        {
            for(Enemy enemy : enemyList)
            {
                //if projectile intersects enemy, both projectile and enemy are to be destroyed
                if( object.getBounds().intersects( enemy.getBounds() )    )
                {
                    enemy.setShouldBeRemoved(true);
                    object.setShouldBeRemoved(true);
                    playerScore += 50;
                    
                }
            }
        }   
    }    
    
    
    //private helper method
    //determines whether Beam hits Ship
    //sets ShouldBeRemoved variable and playerLives accordingly                                 
    private void detectCollisionWithShip()
    {
        //iterates through projectile list and Enemy list
        for(MovingGameObject object : projectileList)
        {         
            //if object intersects Ship, object is destroyed and player loses a life
            if( object.getBounds().intersects( ship1.getBounds() )    )
            {
                object.setShouldBeRemoved(true);
                playerLives -= 1;
            }  
        }
    } 
    
    
    
    //private helper method
    //determines whether Beam hits Shield and whether Bullet hits Shield
    //sets shouldBeRemoved variable accordingly
    private void detectCollisionWithShield()
    {
        //checks if Beam hits Shield
        for(MovingGameObject object : projectileList)
        {    
            for(Shield shield : shieldList)
            {
                
                if( object.getBounds().intersects( shield.getBounds() )    )
                {
                    shield.setShouldBeRemoved(true);
                    object.setShouldBeRemoved(true);
                } 
            }             
        }    
    }    
    
    
    //private helper method
    //determines whether Enemy is too far advanced and should end the game                      //how to end game??
    private boolean detectMovementTooClose()       
    {
        for(Enemy enemy : enemyList)
        {
            if( enemy.getYPosition() + enemy.getYLength() > gameHeight - 150)
            {
                enemy.setColor(Color.PINK);
                return true;
            }
        }
        return false;
    }
    
    
    //private helper method
    //determines whether each GameObject should be removed from game
    //adds object to temporary ArrayList
    //removes GameObjects from their respective lists
    private void removeObjectsFromGame()
    {
        ArrayList<GameObject> listToRemove = new ArrayList(0);        
    
        //enhanced for loop iterates through projectileList and adds objects that
        //should be removed to second ArrayList
        for( MovingGameObject object : projectileList)
        {
            if(object.getShouldBeRemoved() )
            {
                listToRemove.add(object);
            }
        }
        
        
        //removes Objects from ArrayList, then adjusts size accordingly
        projectileList.removeAll(listToRemove);
        projectileList.trimToSize();
        
        
        
        //enhanced for loop iterates through enemyList and adds objects that
        //should be removed to second ArrayList
        //updates closestEnemyPerColumn int array to reflect changes to current closest
        //enemy in that row
        for(Enemy enemy: enemyList)
        {
            if(enemy.getShouldBeRemoved() )
            {
                listToRemove.add(enemy);
                closestEnemyPerColumn[enemy.getObjectColumn()]--;
            }
        }
        
        //removes Objects from ArrayList, then adjusts size accordingly
        enemyList.removeAll(listToRemove);
        enemyList.trimToSize();
        
        

        
        //enhanced for loop iterates through shieldList and adds objects that
        //should be removed to second ArrayList
        for(Shield shield: shieldList)
        {
            if(shield.getShouldBeRemoved() )
            {
                listToRemove.add(shield);
            }
        }
        
        //removes Objects from ArrayList, then adjusts size accordingly
        shieldList.removeAll(listToRemove);
        shieldList.trimToSize();
        
    }
    
    

    //private helper method
    //determines whether the quit key has been pressed
    private void detectQuitKey()
    {
        if( controller.getKeyStatus(KeyEvent.VK_ESCAPE)  )
        {
            controller.resetController();
            gamePaused = true;
            
            StartScreen newScreen = new StartScreen();
            newScreen.setVisible(true);
            container.hook();                               //added to create a way to delete GameFrame after each game
            
        }
    }
    
    
    
     //private helper method
    //determines whether the pause key has been pressed
    private void detectPauseKey()
    {
         
        if( controller.getKeyStatus(KeyEvent.VK_ENTER)   )
        {
            gamePaused = !gamePaused;
            controller.resetController();
            
//            if (gamePaused == false)
//            {
//                
//                gamePaused = true;
//            }
//            else
//            {
//                gamePaused = false;
//            }
        }
    }
    
    
    
    //private helper method
    //quits game if player runs out of lives, runs out of enemies, or enemies get too close
    //checks whether player made the High Scores list and initiates dialog if necessary
    private void checkGameStatus()
    {
        
        
        if(detectMovementTooClose() || playerLives < 1)
        {
            
            gamePaused = true;
            JOptionPane.showMessageDialog(null, "Game Over. You lose." );
            list1.compareScore(playerScore);                                                
            
            
            StartScreen newScreen = new StartScreen();                                      
            newScreen.setVisible(true); 
            container.hook();                           //added to create a way to delete GameFrame after each game
           
            
        }

        
        if(enemyList.size() == 0)
        {
            gamePaused = true;
            JOptionPane.showMessageDialog(null, "Game Over. You win!" );
            list1.compareScore(playerScore);
            
            
            StartScreen newScreen = new StartScreen();
            newScreen.setVisible(true);     
            container.hook();                       //added to create a way to delete GameFrame after each game
        }
        
        
        
    }
  
    
    
    
    /**
     * Constructor method for GamePanel class.
     * It is not necessary for you to modify this code at all
     */
    public GamePanel(GameFrame container)           //added container argument to create a way to delete GameFrame after each game
    {
        this.container = container; //added to create a way to delete GameFrame after each game
        // Set the size of the Panel
        this.setSize(gameWidth, gameHeight);
        this.setPreferredSize(new Dimension(gameWidth, gameHeight));
        
        this.setBackground(Color.BLACK);
        
        // Register KeyboardController as KeyListener
        controller = new KeyboardController(); 
        this.addKeyListener(controller); 
        
        // Call setupGame to initialize fields
        this.setupGame(); 
        
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    
    /**
     * Method to start the Timer that drives the animation for the game.
     * It is not necessary for you to modify this code unless you need to in 
     *  order to add some functionality. 
     */
    public void start()
    {
        // Set up a new Timer to repeat every 20 milliseconds (50 FPS)
        gameTimer = new Timer(1000 / framesPerSecond, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            private int frameNumber = 0; 
            
            
                        
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                detectPauseKey();
                detectQuitKey();
                
                if(!gamePaused)
                {
                    // Update the game's state and repaint the screen
                    updateGameState(frameNumber++);
                    repaint(); 
                }      
            }
        });
        
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    
    
}
