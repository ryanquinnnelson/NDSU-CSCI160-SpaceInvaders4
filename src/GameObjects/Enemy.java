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
public class Enemy extends MovingGameObject {
    
    //instance variables
    private int xLength;
    private int yLength;
    private int objectRow;
    private int objectColumn;
    
    
    //constructor
    public Enemy(int xPosition, int yPosition, int xLength, int yLength, int xVelocity, int yVelocity, int row, int column, Color color)
    {
        super(xPosition, yPosition, xVelocity, yVelocity, color);
        this.xLength = xLength;
        this.yLength = yLength;
        objectRow = row;
        objectColumn = column;
        
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
    
    
    //accessor method
    public int getObjectRow()
    {
        return objectRow;
    }
    
    //accessor method
    public int getObjectColumn()
    {
        return objectColumn;
    }
    
    
    //mutator method
    public void setObjectRow(int row)
    {
        objectRow = row;
    }
    
    
    //mutator method
    public void setObjectColumn(int column)
    {
        objectColumn = column;
    }
    
 
    //draws an Enemy
    @Override
    public void draw(Graphics g)
    {
        int x = super.getXPosition();
        int y = super.getYPosition();
        
        g.setColor( super.getColor() );
        //g.drawRect( x , super.getYPosition(), xLength, yLength);
        
        
        
        //main body
        g.fillOval(x + (int) (xLength*0.25), y + (int) (yLength*0.25), (int) (xLength*0.5), (int) (yLength*0.5));        
        g.fillRect(x + (int) (xLength*0.25), y + yLength/2, xLength/2, yLength/4);
        
        
        //lower arms
        g.fillRect(x + (int) (xLength*0.375), y + yLength/2, (int) (xLength*0.05), yLength/2);
        g.fillRect(x + (int) (xLength*0.575), y + yLength/2, (int) (xLength*0.05), yLength/2);
        
        //lower hands
        g.fillRect(x , y + (int) (yLength*0.95), (int) (xLength*0.375), (int) (yLength*0.05) );
        g.fillRect(x + (int) (xLength*0.575), y + (int) (yLength*0.95), (int) (xLength*0.425), (int) (yLength*0.05));
        
        //eye balls
        g.drawOval(x + (int) (xLength*0.125), y + yLength/8, xLength/3, yLength/3);
        g.drawOval(x + (int) (xLength*0.545), y + yLength/8, xLength/3, yLength/3);
        
        //eye pupils
        g.fillOval(x + (int) (xLength*0.2), y + yLength/4, xLength/10, yLength/10);
        g.fillOval(x + (int) (xLength*0.695), y + yLength/4,xLength/10, yLength/10);
        
        //side arms
        g.fillRect(x, y + yLength/2, (int) (xLength*0.5), (int) (yLength*0.05)); 
        g.fillRect(x + (int) (xLength*0.5), y + yLength/2, (int) (xLength*0.5), (int) (yLength*0.05) );
        
        
        //upper arms
        g.fillRect(x, y, (int) (xLength*0.05), (int) (yLength*0.5));       
        g.fillRect(x + (int) (xLength*0.95), y,(int) (xLength*0.05), (int) (yLength*0.5) );
        
        //upper hands
        g.fillRect(x, y, (int) (xLength*0.1), (int) (yLength*0.1));       
        g.fillRect(x + (int) (xLength*0.9), y,(int) (xLength*0.1), (int) (yLength*0.1) );
        
    }
    
    
    //returns a Rectangle object representing a bounding box for the Enemy
    @Override
    public Rectangle getBounds()
    {
        return new Rectangle( super.getXPosition() , super.getYPosition(), xLength, yLength );
    }
    
    
    @Override
    public boolean detectOutOfFrame(int frameWidth, int frameHeight)
    {
        
        if(super.getXPosition() < 0 || super.getXPosition() + xLength > frameWidth)
        {
            return true;
        }
        if(super.getYPosition() < 0 || super.getYPosition() + yLength > frameHeight)
        {
            return true;
        }
        return false;
    }
    
   
    @Override
    public boolean detectFrameCollision(int frameWidth, int frameHeight)
    {
        int xVelocityAbsValue = Math.abs( super.getXVelocity()  );
        
        if(super.getXPosition() < xVelocityAbsValue || super.getXPosition() + xLength + xVelocityAbsValue  == frameWidth)
        {
            
            return true;
        }
        if(super.getYPosition() == 0 || super.getYPosition() + yLength == frameHeight)
        {
            return true;
        }
        return false;
    }
    
    
}
