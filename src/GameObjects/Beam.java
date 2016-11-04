/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Ryan
 */

public class Beam extends MovingGameObject {

    //instance variables
    int width;
    int height;
    
    
    //constructor
    public Beam( Enemy enemy, int xVelocity, int yVelocity, int width, int height, Color color)
    {
        super(0, 0, xVelocity, yVelocity, color);
        this.width = width;
        this.height = height; 
        
        
        //xPosition of Beam corresponds with middle of Enemy
        //yPosition correponds with front of Enemy
        int xPosition = enemy.getXPosition() +  enemy.getXLength() / 2 ;
        int yPosition = enemy.getYPosition() + enemy.getYLength();
        
        
        super.setXPosition(xPosition);
        super.setYPosition(yPosition);
        
    }
    
    
    @Override
    public boolean detectOutOfFrame(int frameWidth, int frameHeight) 
    {
        
        if(super.getXPosition() < 0 || super.getXPosition() + width > frameWidth)
        {
            return true;
        }
        if(super.getYPosition() < 0 || super.getYPosition() + height > frameHeight)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean detectFrameCollision(int frameWidth, int frameHeight) 
    {
        if(super.getXPosition() == 0 || super.getXPosition() + width == frameWidth)
        {
            return true;
        }
        if(super.getYPosition() == 0 || super.getYPosition() + height == frameHeight)
        {
            return true;
        }
        return false;
    }
    
    

    @Override
    public Rectangle getBounds() 
    {
        return new Rectangle( super.getXPosition(), super.getYPosition(), width, height);
        
    }

    @Override
    public void draw(Graphics g) 
    {
        g.setColor(super.getColor() );
        g.fillRect(super.getXPosition(), super.getYPosition(), width, height);
    }
    
    
}
