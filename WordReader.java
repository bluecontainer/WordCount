// Class: WordReader
//
// Author: Alyce Brady
//
// Created on 9 April 2020

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class contains code to read line from a file and to
 * break those lines up into individual words.
 * 
 * @author Alyce Brady
 * @version 9 April 2020
 */
public class WordReader 
{
    private String filename;
    private BufferedReader reader;
    private int lineCount;
    private HashMap<String, Integer> wordCountMap;

    /** Constructs an object that can read lines from a file, or can
     *  report on word frequencies for every word in the file.  It uses
     *  the following regular expression to parse words: ("\\P{L}+")
     *  (Thanks to Pierre C on Stack Overflow.)
     *      @param filename  the name of the file to open for reading
     */
    public WordReader(String filename) 
    {
        this.filename = filename;
        this.lineCount = 0;
        this.wordCountMap = new HashMap<String, Integer>();

        // Get two readers: one for reading line-by-line, and one to
        // populate the word count map.
        BufferedReader mapReader = null;
        try
        {
            this.reader = new BufferedReader(new FileReader(filename));
            mapReader = new BufferedReader(new FileReader(filename));
        } 
        catch (IOException e)
        {
            // If you get this error, probably the filename was wrong
            //    (in wrong folder, typo in name, missing .txt extension, etc.)
            throw new IOError(new Error("Cannot open file " + filename
                + " for reading"));
        }

        populateMap(mapReader);
    }

    /** Reads the next non-empty line from the file provided to the constructor.
     *  Precondition: the constructor must have successfully opened the file for
     *                reading
     *      @return String  contains the contents of the line that was just read
     */
    public String getNextLine()
    {
        String nextLine = null;
        try 
        {
            // Read lines until hitting a null or blank line.
            while ( (nextLine = this.reader.readLine()) != null  )
            {
                nextLine = nextLine.trim();
                if ( nextLine.length() != 0 && ! nextLine.equals("") )
                {
                    lineCount++;
                    return nextLine;
                }
            }

            // End-of-file
            return null;

            
        } 
        catch (IOException e) 
        {
            System.err.println("Could not read file " + filename);
            return null;
        }      
    }

    /** Takes a line of text containing multiple words and returns an
     *  ArrayList of the individual words that were in the line.  It uses
     *  the following regular expression to parse words: ("\\P{L}+")
     *  (Thanks to Pierre C on Stack Overflow.)
     *  Note that this method considers hyphens, apostrophes, and other
     *  punctuation to be word separators, so "don't" or "fast-forward" would
     *  each break into two words.
     *      @param lineOfText   a string containing 1 or more words
     *      @return  a list of the words that were in the string
     */
    public ArrayList<String> breakIntoWords(String lineOfText)
    {
        // Regular expression below is from Pierre C on Stack Overflow:
        String[] wordsInCurrentLine = splitLine(lineOfText);
        return new ArrayList<String>(Arrays.asList(wordsInCurrentLine));
    }

    /** Gets a list of all the distinct words in this book (each word occurs
     *  only once in the list).  Words have all been converted to lower case
     *  to avoid duplication based on capitalization; for example, "They" and
     *  "they" will both appear as "they" in the word list.  This method can
     *  called anytime after this object is constructed; it does not depend on
     *  calls to the getNextLine method.
     *      @return       the list of words in the book
     */
    public ArrayList<String> getFullWordList()
    {
        return new ArrayList<>(wordCountMap.keySet());
    }

    /** Returns how many times the given word occurs in this book
     *  (ignoring capitalization).  For example, if this method is called
     *  with either "They" or "they" as a parameter, it will return how many
     *  times the word occurs with any capitalization (including "THEY").
     *  This method can called anytime after this object is constructed; it
     *  does not depend on calls to the getNextLine method.
     *      @param word   the word to count
     *      @return       the number of times <code>word</code> occurs in
     *                      this book
     */
    public int getCountFor(String word)
    {
        if ( word != null )
        {
            // If word is not in map, need to avoid null pointer exception
            // from auto-unboxing Integer null to int type.
            Integer count = wordCountMap.get(word.toLowerCase());
            if ( count != null )
                return count;
        }
        return 0;
    }

    /** Populates a map of word-count pairs used by the getFullWordList
     *  and getCountFor methods.
     *      @return void
     */
    private void populateMap(BufferedReader mapReader)
    {
        // Read in each line, parse into words, and add words to the map.
        String nextLine = null;
        try 
        {
            // Read lines until hitting a null or blank line.
            while ( ((nextLine = mapReader.readLine()) != null) )
            {
                // Add all the words from this line to the overall countMap.
                String[] wordsInCurrentLine = splitLine(nextLine);
                for ( String word : wordsInCurrentLine )
                {
                    if ( word.length() > 0 )
                    {
                        String normalizedWord = word.toLowerCase(); // or toUpperCase?
                        // countMap.compute(word, 
                        //  (k, v) -> (v == null) ? 1 : v + 1);
                        // would I need to use new Integer(v.intValue + 1)?
                        Integer count = wordCountMap.get(normalizedWord);
                        wordCountMap.put(normalizedWord,
                                    (count == null) ? 1 : count + 1);
                    }
                }
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Could not read file " + filename);
        }
    }

    /** Splits the given line into an array of individual words.
     *  Works with accents (at least from European countries).
     *      @param lineOfText a line of text read in using this WordReader
     *      @return an array of the words that were in the line
     */
    private String[] splitLine(String lineOfText)
    {
        return lineOfText.split("\\P{L}+");
    }

}
