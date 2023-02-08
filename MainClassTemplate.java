// Import statements go here.  For example,
// import java.awt.Color;
import java.util.ArrayList;
// import java.util.Random;
import java.util.TreeMap;
import java.util.Comparator;

/**
 *  Lab (or Programming Project) X: Name of Lab or PP<br>
 *
 *  The <code>MainClassTemplate</code> class provides a main method
 *  for a program that does X.
 *
 *  A more detailed description goes here, if necessary.
 *
 *  @author [your name]
 *  @author [your partner's name]
 *  @author [with assistance from ... (including instructor/TAs)]
 *  @author [working side-by-side with ...]
 *  @version [last_modification_date]
 */
public class MainClassTemplate
{
    /**
     *  The main function initiates execution of this program.
     *    @param    String[] args not used in this program
     *              (but main methods always need this parameter)
     **/
    public static void main(String[] args)
    {  
        printLineInfo("1661-0.txt");
    }//end main
       
    public static void printLineInfo(String fileName) {
        var reader = new WordReader(fileName);
        int startLineNumber = 35;
        int charCountTotal = 0;
        int wordCountTotal = 0;
        int lineCount = 0;
        int wordLengthTotal = 0;
        String line;
        
        for (line = reader.getNextLine(); line != null; line = reader.getNextLine()) {        
            var charCount = line.length();
            charCountTotal += charCount;
            var lineWords = reader.breakIntoWords(line);
            var wordCount = lineWords.size();
            wordCountTotal += wordCount;
            lineCount++;
            
            for (var word: lineWords) {
                wordLengthTotal += word.length();
            }
            
            int bodyLineNumber = lineCount - startLineNumber + 1;
                       
            if (lineCount >= startLineNumber && lineCount <= (startLineNumber + 20)) {
                System.out.println("Line " + bodyLineNumber + ": " + line + " (Characters=" + charCount + ")" + "(Words=" + wordCount + ")");
            }
            
            if (lineCount == (startLineNumber + 20)) {
                System.out.println("On average first 21 lines has " + (float)charCountTotal / lineCount + " characters per line.");
                System.out.println("On average first 21 lines has " + (float)wordCountTotal / lineCount + " words per line.");                
            }
        }
        
        System.out.println("File " + fileName + " has " + wordCountTotal + " words across " + lineCount + " lines.");
        System.out.println("File " + fileName + " has " + reader.getFullWordList().size() + " unique words across " + lineCount + " lines.");        
        System.out.println("On average " + fileName + " has " + ((float)charCountTotal / lineCount) + " characters per line.");
        System.out.println("On average " + fileName + " has " + ((float)wordCountTotal / lineCount) + " words per line.");
        System.out.println("On average " + fileName + " has words of length " + ((float)wordLengthTotal / wordCountTotal));
        

        var wordsLength14 = new ArrayList<String>();
        var oneTimeWordCount = 0;
        for (var word: reader.getFullWordList()) {
            if (reader.getCountFor(word) == 1) {
                oneTimeWordCount++;
            }
            if (word.length() >= 14) {
                wordsLength14.add(word);
            }
        }
        System.out.println("File " + fileName + " has " + oneTimeWordCount + " words that appear once.");

        var wordsLength14Message = "Words more than 14 characters long are: ";        
        for (var word: wordsLength14) {
            wordsLength14Message += (word + "; ");
        }
        System.out.println(wordsLength14Message); 
        System.out.println("File " + fileName + " has " + wordsLength14.size() + " words that more than 14 characters long.");
        
        var base = "house";
        var containsMessage = "Words containing " + base + ": ";
        var containsResults = getWordsContaining(reader, base);        
        for (var word: containsResults) {
            containsMessage += (word + "(" + reader.getCountFor(word) + "); ");
        }
        System.out.println(containsMessage);
        
        
        wordFrequency1(reader);
    }
    
    public static ArrayList<String> getWordsContaining(WordReader reader, String base) {
        var results = new ArrayList<String>();
        for (var word: reader.getFullWordList()) {
            if (word.contains(base)) {
                results.add(word);
            }
        }
        return results;
    }

    public static void wordFrequency1(WordReader reader) {
        var topCountMax = 10;
        var upperBound = Integer.MAX_VALUE;
        for (var topCount = 0; topCount < topCountMax; topCount++) {
            
            var topWordCount = -1;
            for (var word: reader.getFullWordList()) {            
                var count = reader.getCountFor(word);
                
                if (count > topWordCount && count < upperBound) {
                    topWordCount = count;
                }
            }
    
            var topWords = new ArrayList<String>();
            for (var word: reader.getFullWordList()) {
                var count = reader.getCountFor(word);            
                if (count == topWordCount) {
                    topWords.add(word);
                }
            }
            
            System.out.println(String.join(",", topWords) + " (" + topWordCount + ")");
            
            upperBound = topWordCount;
        }
    }
    
    public static void wordFrequency2(WordReader reader) {
        var wordsSortedByFrequency = new TreeMap<Integer, ArrayList<String>>(Comparator.reverseOrder());
        for (var word: reader.getFullWordList()) {            
            var count = reader.getCountFor(word);
            
            var wordsForFreq = wordsSortedByFrequency.get(count);
            if (wordsForFreq == null) {
                wordsForFreq = new ArrayList<String>();
                wordsSortedByFrequency.put(count, wordsForFreq);
            }
            wordsForFreq.add(word);            
        }
        
        var topWordsMessage = "The most frequently used word(s) are: ";
        var topEntry = wordsSortedByFrequency.firstEntry();
        topWordsMessage += String.join(",", topEntry.getValue());
        topWordsMessage += " (" + topEntry.getKey() + ")";
        System.out.println(topWordsMessage);
        
        var bottomWordsMessage = "The least frequently used word(s) are: ";
        var bottomEntry = wordsSortedByFrequency.lastEntry();
        bottomWordsMessage += String.join(",", bottomEntry.getValue());
        bottomWordsMessage += " (" + bottomEntry.getKey() + ")";
        System.out.println(bottomWordsMessage);

        
        int topCountMax = 10;
        int topCount = 0;
        System.out.println("Top " + topCountMax + " Words:");
        for (var entry: wordsSortedByFrequency.entrySet()) {
            System.out.println(String.join(",", entry.getValue()) + " (" + entry.getKey() + ")" );
            topCount++;
            if (topCount == topCountMax) {
                break;
            }
        }       
    }
}//end class
