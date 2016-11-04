/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import Interfaces.Drawable;
import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author Ryan
 */
public abstract class GameObject implements Drawable {
    
    //instance variables
    private int xPosition;      //represents x coordinate of GameObject
    private int yPosition;      //represents y coordinate of GameObject
    private Color color;
    
    
    //constructor
    public GameObject(int xPosition, int yPosition, Color color)
    {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
    }
    
    
    //accessor method
    public int getXPosition()
    {
        return xPosition;
    }
    
    //accessor method
    public int getYPosition()
    {
        return yPosition;
    }
    
    //accessor method
    public Color getColor()
    {
        return color;
    }
    
    //mutator method
    public void setXPosition(int x)
    {
        xPosition = x;
    }
    
    //mutator method
    public void setYPosition(int y)
    {
        yPosition = y;
    }
    
    
    //mutator method
    public void setColor( Color color)
    {
        this.color = color;
    }
    
      
    //abstract method
    public abstract Rectangle getBounds();
    
    
    
    //determines if two GameObjects are colliding
    /*
    Uses instance of Rectangle class for each object, determines whether the
    two objects are overlapping by using the intersects() method of the Rectangle
    class. Returns true if the two GameObject instances are colliding.
    */
    public boolean isColliding(GameObject other)
    {
        return this.getBounds().intersects(other.getBounds() );
    }
}
