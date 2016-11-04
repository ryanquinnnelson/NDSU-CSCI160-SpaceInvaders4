/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import Controller.KeyboardController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author Ryan
 */
public class Ship extends ControlledGameObject{
    
    //instance variables
    private int xLength = 40;
    private int yLength = 40;
    private int xVelocity = 10;
    
    
    //constructor
    public Ship(int xPosition, int yPosition, Color color, KeyboardController control)
    {
        super(xPosition, yPosition, color, control);
    }

    
    
    //accessor method
    public int getXLength()
    {
        return xLength;
    }
    
    //accessor method
    public int getYLength()
    {
        return yLength;
    }
    
    
    //draws the Ship
    @Override
    public void draw(Graphics g)
    {
        
        
        //bounding box
        g.setColor( super.getColor()  );
        //g.fillRect( super.getXPosition() , super.getYPosition(), xLength, yLength);
        
        //base wing
        g.fillRect(super.getXPosition(), super.getYPosition() + (int) (0.5*yLength), xLength, (int) (0.25*yLength) );
        
        //Side guns
        g.fillRect(super.getXPosition(), super.getYPosition() + (int) (0.25*yLength),
                            (int) (xLength*0.05), yLength - (int) (0.5*yLength));
        
        g.fillRect(super.getXPosition() + (xLength - (int) (xLength*0.05)) ,
                   super.getYPosition() + (int) (0.25*yLength) , (int) (xLength*0.05), yLength - (int) (0.5*yLength));
        
        
        //engines
        g.fillRect(super.getXPosition() + (int) (xLength*0.25), super.getYPosition() + (int) (0.75*yLength) , 
                            (int) (xLength*0.2), (int) (0.25*yLength));
        
        g.fillRect(super.getXPosition() + (int) (xLength*0.55), super.getYPosition() + (int) (0.75*yLength) , 
                            (int) (xLength*0.2), (int) (0.25*yLength));
        
        //rear body
        g.fillRect(super.getXPosition() + (int) (xLength*0.25), super.getYPosition() + (int) (0.75*yLength), 
                            (int) (xLength*0.5), (int) (0.15*yLength));

        

        

        
        //Triangle body
        Polygon body = new Polygon();
        body.addPoint(super.getXPosition() + xLength / 2, super.getYPosition());
        body.addPoint(super.getXPosition() + (int) (xLength*0.25) , super.getYPosition() + (int) (yLength*0.75) );
        body.addPoint(super.getXPosition() + (int) (xLength*0.75),  super.getYPosition() + (int) (yLength*0.75) );
        g.fillPolygon(body);
        
        
        
        
    }
    
    
    //returns Rectangle object representing a bounding box for the Ship
    @Override
    public Rectangle getBounds()
    {
        return new Rectangle( super.getXPosition(), super.getYPosition(), xLength, yLength);
    }
    
    
    //moves the Ship
    /*
    Method interacts with KeyboardController field to determine if any movement is necessary
    Checks key status of keyboard keys to see if they are currently being held down
    Adjust the position of the ship based on those results
    */
    @Override
    public void move()
    {
        //checks if key is pressed
        boolean leftArrowIsPressed;
        boolean rightArrowIsPressed;
        
        leftArrowIsPressed  = super.getKeyboardController().getKeyStatus(KeyEvent.VK_LEFT);
        rightArrowIsPressed = super.getKeyboardController().getKeyStatus(KeyEvent.VK_RIGHT);
        
        
        //moves Ship if key is pressed
        if( leftArrowIsPressed && !detectLeftEdge() )
        {
            super.setXPosition(  super.getXPosition() - xVelocity);
        }
        
        if( rightArrowIsPressed && !detectRightEdge() )
        {
            super.setXPosition(  super.getXPosition() + xVelocity);    
        } 
        
    }
    
    
    //helper method
    //determines if Ship has reached right edge of GamePanel
    private boolean detectRightEdge()
    {
        if( super.getXPosition() + xLength >= 800)
        {
           return true; 
        }
        
        return false;
    }
    
    //helper method
    //determines if Ship has reached left edge of GamePanel
    private boolean detectLeftEdge()
    {
        if( super.getXPosition() <= 0)
            {
                return true;
            }
        
        return false;
    }
    
    
}
