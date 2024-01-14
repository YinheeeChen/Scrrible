package scribble;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Player {
    class playerTask extends TimerTask {
        int timeCountDown = 30;
        public int getTimeCountDown() {
            return timeCountDown;
        }
        @Override
        public void run() {
            timeCountDown--;
        }
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED =  "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static int playerNumber;
    private static int moveCounter = 1;
    private int time;
    private int score = 0;
    private int direction;
    private String playerName;
    private File wordStorage;
    private boolean flagPosition = true;
    private boolean flagEnterWord = true;
    private ArrayList<String>  wordList = new ArrayList<>();

    /** Default constructor without parameters
     * */
    public Player(){
    }

    /** Constructor with all parameters
     * */
    public Player(int time, int moveCount, int playerNumber, boolean flagPosition, int score, boolean flagEnterWord, String playerName) {
        this.time = time;
        this.score = score;
        this.flagPosition = flagPosition;
        this.flagEnterWord = flagEnterWord;
        this.playerName = playerName;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public File getWordStorage() {
        return wordStorage;
    }

    public void setWordStorage(File wordStorage) {
        this.wordStorage = wordStorage;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public void setWordList(ArrayList<String> wordList) {
        this.wordList = wordList;
    }

    public static int getPlayerNumber() {
        return playerNumber;
    }

    public static int getMoveCounter() {
        return moveCounter;
    }

    public int getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isFlagPosition() {
        return flagPosition;
    }

    public boolean isFlagEnterWord() {
        return flagEnterWord;
    }

    public static void setPlayerNumber(int playerNumber) {
        Player.playerNumber = playerNumber;
    }

    public static void setMoveCounter(int moveCounter) {
        Player.moveCounter = moveCounter;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setFlagPosition(boolean flagPosition) {
        this.flagPosition = flagPosition;
    }

    public void setFlagEnterWord(boolean flagEnterWord) {
        this.flagEnterWord = flagEnterWord;
    }


    /** Players place a word by entering row position and column position. After checking the word, players get corresponding scores.
     * @param chessboard The board of a game
     */
    public void gamerPlay(char[][] chessboard) {
        Scanner scan = new Scanner(System.in);
        String word = "";
        char firstLetter;
        int columnPosition;
        int rowPosition;
        int time = 1;
        boolean wordValid;
        Timer playerTimer = new Timer();
        playerTask task = new playerTask();
        System.out.println(ANSI_YELLOW+"\nThis is "+playerName+"'s turn.\n"+ANSI_RESET
        +"                        Hint: "+ANSI_GREEN_BACKGROUND+"Green  square"+ANSI_RESET+" triples the word score!!\n"+"                              "+ANSI_PURPLE_BACKGROUND+"Purple square"+ANSI_RESET+" doubles the word score!!\n"+ANSI_RESET);
        System.out.println(ANSI_RED+"Now a 30s count down starts!."+ANSI_RESET);
        playerTimer.schedule(task,0,1000);
        while (flagPosition) {
            flagEnterWord = true;
            try {
                rowPosition = rowEnter();
                switch (rowPosition) {
                    case -1 -> {
                        flagPosition = true;
                        continue;
                    }
                    case -2 -> {
                        flagPosition = false;
                        continue;
                    }
                }
                columnPosition = columnEnter();
                switch (columnPosition) {
                    case -1 -> {
                        flagPosition = true;
                        continue;
                    }
                }
                if (chessboard[rowPosition][columnPosition] == ' ' && moveCounter != 1) {
                    System.out.println(ANSI_RED + "You can not place your word here, it should be connected to words that are already on the board!");
                    System.out.println("**********************************\n" + ANSI_RESET);
                    flagPosition = true;
                    continue;
                }
                while (flagEnterWord) {
                    System.out.println("Please enter the word you want to place:");
                    System.out.println("(Enter "+ANSI_RED+"\"-1\""+ANSI_RESET+" to stop if you do not want to place a word here now)");
                    word = scan.nextLine();
                    if (word.equals("-1")) {
                        System.out.println("**********************************\n");
                        flagEnterWord = false;
                        flagPosition = true;
                        continue;
                    }
                    wordValid = checkWordValid(word);
                    if (!wordValid) {
                        break;
                    }
                    wordStorage = readWordAndStore(word,playerName);
                    word = word.toUpperCase();
                    firstLetter = word.charAt(0);
                    if (!checkMoveValid(firstLetter, chessboard, rowPosition, columnPosition) && moveCounter != 1) {
                        flagPosition = true;
                        System.out.println(ANSI_RED + "The initial letter of the word should be the same as the letter at this position");//这该怎么说捏
                        System.out.println("**********************************\n" + ANSI_RESET);
                        break;
                    }
                    direction = placeWord(chessboard, rowPosition, columnPosition, word);
                    if (direction == 0) {
                        System.out.println(ANSI_RED + "Position had been occupied. Please choose a new position or word!");
                        System.out.println("-----------------------------------------------\n" + ANSI_RESET);
                        flagPosition = true;
                        flagEnterWord = false;
                        break;
                    }
                    wordList = Dictionary.readInputWord(wordStorage);
                    time = checkTime(word, rowPosition, columnPosition);
                    if (Dictionary.dictionaryCheck(wordList, Game.getDictionaryList(), time) == 0) {
                        deleteWord(moveCounter, rowPosition, columnPosition, chessboard, word);
                        moveCounter--;
                        break;
                    }
                    if (task.getTimeCountDown() <= 0) {
                        System.out.println(ANSI_RED+"Time Over！"+"Sorry, you placed the word after time countdown equals zero!!\n"+"Your score does not count!"+ANSI_RESET);
                        deleteWord(moveCounter, rowPosition, columnPosition, chessboard, word);
                        moveCounter--;
                        break;
                    }
                    score += Dictionary.dictionaryCheck(wordList, Game.getDictionaryList(), time);
                }
            }
            catch(InputMismatchException e)  {
                System.out.println(ANSI_RED+"Invalid input! Try again!");
                System.out.println("**********************************\n"+ANSI_RESET);
            } catch(NumberFormatException e) {
                System.out.println(ANSI_RED+"Please enter a integer number that is between 0-14 for colum or row position here! Try again!");
                System.out.println("**********************************\n"+ANSI_RESET);
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println(ANSI_RED+"Your word is out of the bound of the board. Please select a proper position and word!");
                System.out.println("**********************************\n"+ANSI_RESET);
            }
            if (!flagEnterWord && !flagPosition){
                Board.displayBoard(chessboard);
                moveCounter++;
            }
        }
        flagPosition = true;
    }


    /**Allows the players to choose the row position where they want to place their words or pass to next player.
     *
     * @return rowPosition The valid row position.
     */
    public int rowEnter() {
        String row;
        int rowPosition;
        Scanner scan = new Scanner(System.in);
        if (moveCounter == 1) {
            System.out.println(ANSI_YELLOW+"The first player must place his word on the center of the board at the first round!");
            System.out.println("Please enter '7' for row position and '7' for column position!"+ANSI_RESET);
        }
        System.out.println("Please enter the index of the row where you want to place your word:");
        System.out.println("( Or enter"+ANSI_RED+" \"pass\" "+ANSI_RESET+"to turn to next player!)");
        row = scan.nextLine();
        if(row == null) {
            System.out.println(ANSI_RED+"You can not enter a blank! Please enter a integer that range 0-14! Try again!");
            System.out.println("**********************************\n"+ANSI_RESET);
            return -1;
        }
        if(row.equals("pass")) {
            flagEnterWord = false;
            return -2;
        }
        rowPosition = Integer.parseInt(row);
        if (rowPosition != 7 && moveCounter == 1){
            System.out.println(ANSI_RED+"Sorry！You have to enter '7' for row position at the first round");
            System.out.println("Try again!"+ANSI_RESET);
            return -1;
        }
        while(rowPosition < 0 || rowPosition > 14) {
            System.out.println(ANSI_RED+"Wrong row position! It should be a integer that ranges 0-14! Try again!");
            System.out.println("**********************************\n"+ANSI_RESET);
            System.out.println("Please enter the index of the row where you want to place your word:");
            System.out.println("( Or enter \"pass\" to turn to next player!)");
            row = scan.nextLine();
            rowPosition = Integer.parseInt(row);
            flagPosition = true;
        }
        return rowPosition;
    }

    /**Allows players to choose the column position where their place their words.
     *
     * @return column position where players want to place their words
     */
    public int columnEnter() {
        int columnPosition;
        String column;
        Scanner scan = new Scanner(System.in);
        if (moveCounter == 1) {
            System.out.println(ANSI_YELLOW+"The first player must place his word on the center of the board!");
            System.out.println("Please enter '7' for row position and '7' for column position!"+ANSI_RESET);
        }
        System.out.println("Please enter the index of the column where you want to place your word:");
        System.out.println("( Or enter "+ANSI_RED+"\"backwards\""+ANSI_RESET+" to choose a row position again!)");
        column = scan.next();
        if(column == null) {
            System.out.println(ANSI_RED+"You can not enter a blank! Please enter a integer that range 0-14! Try again!");
            System.out.println("**********************************\n"+ANSI_RESET);
            return -1;
        }
        if(column.equals("backwards")) {
            System.out.println(ANSI_RED+"You want to choose a new row position!");
            System.out.println("**********************************\n"+ANSI_RESET);
            return -1;
        }
        columnPosition = Integer.parseInt(column);
        if (columnPosition != 7 && moveCounter == 1){
            System.out.println(ANSI_RED+"Sorry！You have to enter '7' for column position at the first round");
            System.out.println("Try again!"+ANSI_RESET);
            return -1;
        }
        if(columnPosition < 0 || columnPosition > 14) {
            flagPosition = true;
            System.out.println(ANSI_RED+"Wrong colum position! It should be a integer that ranges 0-14! Try again!");
            System.out.println("**********************************\n"+ANSI_RESET);
            return -1;
        }
        return columnPosition;
    }


    /** This method checks if the words players enters exist in the dictionary.
     *
     * @param firstLetter The first letter of the word player enters.
     * @param chessboard The board of the game.
     * @param rowPosition row position where players want to place their words
     * @param columPosition column position where players want to place their words
     * @return
     */
    public boolean checkMoveValid(char firstLetter, char chessboard[][], int rowPosition, int columPosition) {
        int check = Character.compare(firstLetter,chessboard[rowPosition][columPosition]);
        if (check == 0) return true;
        return false;
    }

    /** This method check if the word player enters is a valid English word
     *
     * @param word The word player enters
     * @return if the word entered a valid word
     */
    public boolean checkWordValid(String word) {
        char everyLetter;
        for(int i = 0; i<word.length(); i++) {
            everyLetter = word.charAt(i);
            if(!Character.isAlphabetic(everyLetter)){
                System.out.println(ANSI_RED+"You should enter a word here. (Numerals or other symbols are not allowed)"+ANSI_RESET);
                return false;
            }
        }
        if(word.isBlank()){
            System.out.println(ANSI_RED+"You should not enter blank"+ANSI_RESET);
            return false;
        }
        return true;
    }

    /** This method allows players place word on board and check if it is valid to place word at this position.
     *
     * @param chessboard The board of the game
     * @param rowPosition The row position where players want to place their words
     * @param columPosition The column position where players want to place their words
     * @param word The word plays enter
     * @return The direction players want to place their words.
     */
    public int placeWord(char[][] chessboard, int rowPosition, int columPosition, String word) {
        int length = word.length()-1;
        Scanner scan = new Scanner(System.in);
        System.out.println("How do you like to place your letter?");
        System.out.println("1.Vertical");
        System.out.println("2.Horizontal");
        direction = scan.nextInt();
        while(direction != 1 && direction != 2) {
            System.out.println(ANSI_RED+"Wrong operation! Please enter 1 or 2 to place your word."+ANSI_RESET);
            direction = scan.nextInt();
        }
        for (int i = 0; i < length; i++) {
            if (direction == 1 && chessboard[rowPosition+i+1][columPosition] != ' ') {
                return 0;
            }
            if (direction == 2 && chessboard[rowPosition][columPosition+i+1] != ' ') {
                return 0;
            }
        }
        if(direction == 1) {
            if (rowPosition+length > 14) {
                System.out.println(ANSI_RED+"Your word is out of the bound of the board. Please select a proper position and word!");
                System.out.println("**********************************\n"+ANSI_RESET);
            }
        }
        if(direction == 2) {
            if (columPosition+length > 14) {
                System.out.println(ANSI_RED+"Your word is out of the bound of the board. Please select a proper position and word!");
                System.out.println("**********************************\n"+ANSI_RESET);
            }
        }
        switch(direction) {
            case 1-> {
                for (int i = 0;i < word.length(); i++) {
                    chessboard[rowPosition][columPosition] = word.charAt(i);
                    rowPosition++;
                }
                flagPosition = false;
                flagEnterWord = false;
            }
            case 2-> {
                for(int j = 0;j < word.length();j++ ) {
                    chessboard[rowPosition][columPosition] = word.charAt(j);
                    columPosition++;
                }
                flagPosition = false;
                flagEnterWord = false;
            }
        }
        return direction;
    }



    /** This method reads the word player enters and stores them in a file.
     *
     * @param word The word player enters
     * @return The file that stores words
     */
    public static File readWordAndStore(String word, String playerName) {
        FileOutputStream wordOutputStream = null;
        PrintWriter wordWriter = null;
        String pathName = playerName + "wordStorage.txt";
        File wordStorage = new File(pathName);
        try {
            wordOutputStream = new FileOutputStream(pathName);
            wordWriter = new PrintWriter(wordOutputStream);
            wordWriter.println(word);
        }
        catch (IOException e) {
            System.out.println("/t" + e);
        }
        finally {
            if (wordWriter != null)
                wordWriter.close();
        }
        return wordStorage;
    }

    /** This method checks if the score needs to be timed.
     *
     * @param word The word player enters
     * @param rowPosition The row position where players want to place their words
     * @param columnPosition The column position where players want to place their words
     * @return
     */
    public static int checkTime(String word, int rowPosition, int columnPosition) {
        int time = 1;
        if ((rowPosition == 4 && (columnPosition == 10 || columnPosition == 4)) || (rowPosition == 10 && (columnPosition == 4 || columnPosition ==10)) || (rowPosition == 7 && (columnPosition == 0 || columnPosition == 14 ))){
            time = 2;
        }
        if ((rowPosition == 0 && (columnPosition == 14 || columnPosition == 0)) || (rowPosition == 14 && (columnPosition == 0 || columnPosition ==14)) || (rowPosition == 2 && (columnPosition == 2 || columnPosition == 12)) || (rowPosition == 12 && (columnPosition == 12 || columnPosition == 2))) {
            time = 3;
        }
        return time;
    }

    public void deleteWord(int moveCounter, int rowPosition, int columnPosition, char[][] chessboard, String word) {
        if (direction == 1) {
            if (moveCounter == 1) {
                for (int i = 0; i < word.length(); i++) {
                    chessboard[rowPosition][columnPosition] = ' ';
                    rowPosition++;
                }
            } else {
                for (int i = 0; i < word.length() - 1; i++) {
                    chessboard[rowPosition + 1][columnPosition] = ' ';
                    rowPosition++;
                }
            }
            flagEnterWord = false;
            flagPosition = false;
        } else if (direction == 2) {
            if (moveCounter == 1) {
                for (int j = 0; j < word.length(); j++) {
                    chessboard[rowPosition][columnPosition] = ' ';
                    columnPosition++;
                }
            } else {
                for (int j = 0; j < word.length() - 1; j++) {
                    chessboard[rowPosition][columnPosition + 1] = ' ';
                    columnPosition++;
                }
            }
            flagEnterWord = false;
            flagPosition = false;
        }
    }

}
