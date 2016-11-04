/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import Interfaces.Moveable;
import java.awt.Color;

/**
 *
 * @author Ryan
 */
public abstract class MovingGameObject extends GameObject implements Moveable {
    
    
    //instance variables
    private int xVelocity;              //represents horizontal speed of instance
    private int yVelocity;              //represents vertical speed of instance
    private boolean shouldBeRemoved;
    
    
    
    //constructor
    public MovingGameObject(int xPosition, int yPosition, int xVelocity, int yVelocity, Color color)
    {
        super(xPosition, yPosition, color);
        
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        shouldBeRemoved = false;
    }
    
    
    //accessor method
    public int getXVelocity()
    {
        return xVelocity;
    }
    
    //accessor method
    public int getYVelocity()
    {
        return yVelocity;
    }
    
    //accessor method
    public boolean getShouldBeRemoved()
    {
        return shouldBeRemoved;
    }
    
    
    //mutator method
    public void setXVelocity(int xVelocity)
    {
        this.xVelocity = xVelocity;
    }
    
    
    //mutator method
    public void setYVelocity(int yVelocity)
    {
        this.yVelocity = yVelocity;
    }
    
    
    //mutator method
    public void setShouldBeRemoved(boolean b)
    {
        shouldBeRemoved = b;
    }
    
    
    //updates isOutOfFrame variable based on whether position of Object is out of frame
    public abstract boolean detectOutOfFrame(int frameWidth, int frameHeight);
    
    
    
    public abstract boolean detectFrameCollision(int frameWidth, int frameHeight);
    
    
    
     
    //adjusts position of MovingGameObject based on xVelocity and yVelocity values
    @Override
    public void move()
    {
        super.setXPosition(  super.getXPosition() + xVelocity  );
        super.setYPosition(  super.getYPosition() + yVelocity  );
        
                
    }
    
}


