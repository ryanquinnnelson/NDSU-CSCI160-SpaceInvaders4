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
public class Shield extends GameObject{
    
    //instance variables
    private int width;
    private int height;
    private boolean shouldBeRemoved;
    
    //constructor
    public Shield( int xPosition, int yPosition, int width, int height, Color color)
    {
        super(xPosition, yPosition, color);
        
        this.width = width;
        this.height = height;
        shouldBeRemoved = false;
    }
    
    
    //accessor method
    public int getWidth()
    {
        return width;
    }
    
    //accessor method
    public int getHeight()
    {
        return height;
    }
    
    //accessor method
    public boolean getShouldBeRemoved()
    {
        return shouldBeRemoved;
    }
    
    //mutator method
    public void setWidth( int width)
    {
        this.width = width; 
    }
    
    //mutator method
    public void setHeight( int height)
    {
        this.height = height;
    }
    
    //mutator method
    public void setShouldBeRemoved( boolean b)
    {
        shouldBeRemoved = b;
        
    }
    
    
    @Override
    public void draw(Graphics g)
    {
        g.setColor( super.getColor()  );
        g.fillRect( super.getXPosition(), super.getYPosition(), width, height);
        
    }
    
    
    @Override
    public Rectangle getBounds()
    {
        return new Rectangle(super.getXPosition(), super.getYPosition(), width, height);
    }
    
    
}
