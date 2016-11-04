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
public class Bullet extends MovingGameObject {
    
    //instance variable
    private int diameter;       //diameter of the bullet
    
    //constructor
    public Bullet(Ship ship, int diameter, int xVelocity, int yVelocity, Color color)
    {
        super(0, 0, xVelocity, yVelocity, color);

        //xPosition of Bullet corresponds with middle of Ship
        //yPosition correponds with front of Ship
        int xPosition = ship.getXPosition() + ship.getXLength() / 2 ;
        int yPosition = ship.getYPosition() - diameter;
        
        
        super.setXPosition(xPosition);
        super.setYPosition(yPosition);
        
        
        
        
        this.diameter = diameter;
    }
    
    
    //accessor method
    public int getDiameter()
    {
        return diameter;
    }
    
    
    //draws Bullet object
    @Override
    public void draw(Graphics g)
    {
        g.setColor( super.getColor() );
        g.fillOval( super.getXPosition(), super.getYPosition(), diameter, diameter);
    }
    
    
    //returns Rectangle object representing a bounding box for the Bullet object
    @Override
    public Rectangle getBounds()
    {
        return new Rectangle(super.getXPosition(), super.getYPosition(), diameter, diameter); 
    }
    
    
    @Override
    public boolean detectOutOfFrame(int frameWidth, int frameHeight)
    {
        
        if(super.getXPosition() < 0 || super.getXPosition() + diameter > frameWidth)
        {
            return true;
        }
        if(super.getYPosition() < 0 || super.getYPosition() + diameter > frameHeight)
        {
            return true;
        }
        return false;
    }
    
    
    @Override
    public boolean detectFrameCollision(int frameWidth, int frameHeight)
    {
        if(super.getXPosition() == 0 || super.getXPosition() + diameter == frameWidth)
        {
            return true;
        }
        if(super.getYPosition() == 0 || super.getYPosition() + diameter == frameHeight)
        {
            return true;
        }
        return false;
    }
}
