/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *
 * @author Ryan
 */
public class HighScore {
    
    //instance variables
    private String  name;
    private int     score;
    
    //default constructor
    public HighScore()
    {
        score = 0;
        name = "Empty";
    }
    
    //constructor
    public HighScore(String name, int score)
    {
        this.name = name;
        this.score = score;
    }
    
    
    //accessor method
    public int getScore()
    {
        return score;
    }
    
    //accessor method
    public String getName()
    {
        return name;
    }
    
    
    //mutator method
    public void setScore(int score)
    {
        this.score = score;
    }
    
    //mutator method
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    @Override
    public String toString()
    {
        return String.format("Name: %-20s Score: %6d", name, score);
    }
    
    
}
