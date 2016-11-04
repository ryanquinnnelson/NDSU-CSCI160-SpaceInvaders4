/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Ryan
 */
public class HighScoreList {
    
    //instance variables
    private HighScore [] list = new HighScore [10];
    
    
    //constructor
    public HighScoreList()
    {
        populateHighScores();
        
        
        buildHighScoreArray(   readHighScoreFile()   );
        
    }
    
    
    
    //accessor method
    public HighScore[] getHighScoreArray()
    {
        HighScore[] temp = new HighScore [list.length];
        
        for(int i = 0; i < list.length; i++)
        {
            temp[i] = list[i];
        }
        
        return temp;
    }
 
    
    
    
//    //temp for testing purposes
//    protected void setHighScoreList()
//    {
//        list[2] = new HighScore("Jim", 25);
//        list[3] = new HighScore("John", 25);
//        list[4] = new HighScore("Carol", 25);
//        list[5] = new HighScore("Pete", 25);
//        list[6] = new HighScore("Sarah", 25);
//        list[7] = new HighScore("Rocky", 25);
//        list[8] = new HighScore("Bob", 25);
//    }
    
    
    //checks playerScore against highScores array
    //prompts Player to enter name if score is among high scores
    //places player HighScore object in correct position in HighScore array
    public void compareScore(int score)
    {
        int indexToReplace = -1;
        String name = "";
        
        
        //checks playerScore against each HighScore object in array
        //determines index position to replace if playerScore is higher than a high score
        for(int i = 0; i < list.length; i++)
        {
            if( score > list[i].getScore())
            {
                indexToReplace = i;
                
                name = JOptionPane.showInputDialog(null, "Congratulations! You made the High Score list." +         
                                                    "\nPlease enter your name for the record.");
                
                //if player presses cancel, the String stored will be null
                //check to avoid NullPointerException
                if(name == null)
                {
                    name = "Player 1";    
                }
                else
                {
                    //checks whether input is only whitespace characters
                    String nameTemp = new String(name);
                    if(nameTemp.replaceAll(" ", "").isEmpty() )
                    {
                        name = "Player 1";
                    }
                }
                
                //once it finds the first indexToReplace, stops looking
                break;
            }
        }
        
        
        
        
        //replaces last element in array
        if(indexToReplace == 9)      
        {
            list[indexToReplace] = new HighScore(name, score);
        }
        
        //replaces element other than last element in array
        else if (indexToReplace >= 0 && indexToReplace <9)
        {
            //moves second to last element to last index position
            //moves each adjacent element to next highest position
            //stops once it has moved the element in the index position that is to be replaced
            for(int i = 8; i >= indexToReplace; i--)
            {
                list[i+1] = list[i];
            }
            
            //places element in the index position that is to be replaced
            list[indexToReplace] = new HighScore(name, score);       
        }  
        
        saveHighScoreArray();
    }
    
    
    public void clearHighScores()
    {
        for(int i = 0; i < list.length; i++)
        {
            list[i] = new HighScore();
        }
        
        saveHighScoreArray();
    }
    
    
    @Override
    public String toString()
    {
        String message = "";
        
        for(HighScore score: list)
        {
            message += score + "\n" ;
        }
        
        return message;
    }
    
    
    
    //****************************************************************************
    
    
    //private helper method
    //sets initial values for all elements in highScores array to default
    private void populateHighScores()
    {
        for(int i = 0; i < list.length; i++)
        {
            list[i] = new HighScore();
        }
    }
     
    

    
    //private helper method
    //reads in data from file line by line and stores in an ArrayList
    private ArrayList<String> readHighScoreFile()
    {
        ArrayList<String> temp = new ArrayList<String> (0);
        
        
        
        try
        {
            File hs = new File("SpaceInvaderHighScores.txt");
            Scanner scan = new Scanner(hs);
            
               
            //reads each token, which in this case is a line (to /n character)
            //each token consists of <String>name comma <String>score
            //adds that token to ArrayList
            while(scan.hasNext()  )
            {
                String stringRead = scan.nextLine();
                
                
                temp.add(stringRead);
    
            }
            
            scan.close();
        }
        catch (FileNotFoundException fnfe)
        {

            System.out.println("Unable to find file.");
        }
        
        catch( Exception e)
        {
            e.printStackTrace();
        }
        
        //returns value no matter whether there was an exception
        finally
        {
            if(temp.size() == 0)
            {
                return null;
            }
            else
            {
                return temp;
            }
        }
        
    }
    
    
    
    //private helper method
    //converts ArrayList of String objects into HighScore array
    //requires ArrayList object to consist of a single String containing:
    //a String as a name, a comma without spaces, and a second String that 
    //can be parsed as an int value
    private void buildHighScoreArray(ArrayList<String> stringArray)
    {
        //sets all array elements to default HighScore objects
        populateHighScores();
        
        try
        {
              //sets individual array elements if ArrayList contains objects
              if(stringArray != null && !stringArray.isEmpty())
              {
                  //for each String object in ArrayList, constructs new HighScore object                                 
                  for(int i = 0; i < stringArray.size(); i++)
                  {
                      //checks whether HighScore array contains index position before continuing 
                      if(i < list.length)
                      {
                          String  name;
                          int     score;

                          //parses into a <String> name and an <int> score
                          Scanner parse = new Scanner(stringArray.get(i)  );
                          parse.useDelimiter(",");

                          name = parse.next();
                          score = Integer.parseInt(   parse.next() );                             

                          //constructs new HighScore object based on input and assigns to current index position
                          list[i]= new HighScore(name, score); 
                      }   
                  }         
              }   
        }
        catch(InputMismatchException ime)
        {
            System.out.println("Error: InputMismatchException. HighScore list set as default values.");
        }       
    }
    
   
    //prints the HighScore array to file
    private void saveHighScoreArray()
    {
        try 
        {
            FileOutputStream os = new FileOutputStream("SpaceInvaderHighScores.txt", false);
            
            PrintWriter pw = new PrintWriter(os);
            
            
            for(int i = 0; i < list.length; i++)
            {
                pw.println(list[i].getName() + "," + list[i].getScore());
            }
   
            pw.close();
        }
            
        catch(FileNotFoundException fnfe)
        {
            System.out.println("File not found. Exiting...");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        
        
    }
    
    

    //unused
    //creates file in case of file not existing
    private void buildFile()
    {
        try 
        {
           FileOutputStream os = new FileOutputStream("SpaceInvaderHighScores.txt", false); 
           PrintWriter pw = new PrintWriter(os);
           
           pw.close();
                
        }
        catch (FileNotFoundException  fnfe) 
        {
            System.out.println("Error: File cannot be created!");
        }
        
    }

    
   //**************************************************************************** 
   
    
    
    
    
    
    
    
    
    
//    //testing purposes only
//    public static void main( String[] args)
//    {
//        HighScoreList list1 = new HighScoreList();
//
//        
//
//        
//        
////        System.out.println(list1);
//
//        
////        list1.compareScore(50);
////        list1.saveHighScoreArray();
//        
//    }
}
