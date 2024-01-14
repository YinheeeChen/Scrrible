package scribble;

import java.io.File;
import java.util.ArrayList;

import java.io.*;

public class Dictionary {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED =  "\u001B[31m";

    public Dictionary() {
    }

    /**Read the inputted word then add it into an arraylist
     * @param file store the inputted word
     * @return inputWord the arraylist that store the input of player
     */
    public static ArrayList readInputWord(File file) {
        BufferedReader wordsReader = null;
        ArrayList<String> inputWord = new ArrayList<String>();
        try {
            wordsReader = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = wordsReader.readLine()) != null) {
                String[] wordsArr1 = str.split("[^a-zA-Z]");
                for (String word : wordsArr1) {
                    if (word.length() != 0) {
                        inputWord.add(word.toLowerCase());
                    }
                }
            }
            wordsReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputWord;
    }
    /**
     * Put the inputted word into a file, create arraylist.
     * check the dictionary file and the new file to see whether the word exists
     * if so, count score. If not, count 0.
     * @param inputWord a file which
     * @param dictionary
     * @return
     */
    public static int dictionaryCheck(ArrayList inputWord, ArrayList dictionary, int time) {
        int score = 0;
        for (int i = 0; i < inputWord.size(); i++) {
            if (dictionary.contains(inputWord.get(i))) {
                score = wordScore((String) inputWord.get(i));
                score *= time;
            } else {
                System.out.println(ANSI_RED+"The word you entered does not exist."+ANSI_RESET);
            }
        }
        return score;
    }

    /**
     * Count the score
     * @param word
     * @return
     */
    public static int wordScore(String word){
        int score = 0;
        for (int i = 0; i < word.length(); i++){
            score += charPoint(word.substring(i, i+1));
        }
        return score;
    }

    /**
     * Convert the inputted word into corresponding chars
     * @param everyLetter
     * @return
     */
    public static int charPoint(String everyLetter){

        if (everyLetter.matches("[aeioulnstr]")){
            return 1;
        } else if (everyLetter.matches("[dg]")){
            return 2;
        } else if (everyLetter.matches("[bcmp]")){
            return 3;
        } else if (everyLetter.matches("[fhvwy]")){
            return 4;
        } else if (everyLetter.matches("[k]")){
            return 5;
        } else if (everyLetter.matches("[jx]")){
            return 8;
        } else if (everyLetter.matches("[qz]")){
            return 10;
        } else return 0;
    }
}

