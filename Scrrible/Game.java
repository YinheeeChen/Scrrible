package scribble;


import java.util.*;
import java.io.*;

public class Game {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED =  "\u001B[31m";
    private static int scoreLimit = 0;
    private static boolean endGame = true;
    private static File dictionary = new File("Vocabulary.txt");
    private static ArrayList<String> dictionaryList = new ArrayList<>();
    private static ArrayList<Integer> playerScoreList = new ArrayList<Integer>();
    public static ArrayList<String> getDictionaryList() {
        return dictionaryList;
    }

    public Game() {
    }

    public static void setDictionaryList(ArrayList<String> dictionaryList) {
        Game.dictionaryList = dictionaryList;
    }

    public static int getScoreLimit() {
        return scoreLimit;
    }

    public static void setScoreLimit(int scoreLimit) {
        Game.scoreLimit = scoreLimit;
    }

    public static boolean isEndGame() {
        return endGame;
    }

    public static void setEndGame(boolean endGame) {
        Game.endGame = endGame;
    }

    public static ArrayList<Integer> getPlayerScoreList() {
        return playerScoreList;
    }

    public static void setPlayerScoreList(ArrayList<Integer> playerScoreList) {
        Game.playerScoreList = playerScoreList;
    }
    public static File getDictionary() {
        return dictionary;
    }

    public static void setDictionary(File dictionary) {
        Game.dictionary = dictionary;
    }


    public static void main(String[] args) {
        dictionaryList = Dictionary.readInputWord(dictionary);
        operation();
    }



    public static ArrayList<Player> gameSet() {
        ArrayList<Player> playerList = playerSet();
        setScore();
        return playerList;
    }

    /**Set some basic information for players
     *
     * @return A list of all players
     */
    public static ArrayList<Player> playerSet() {
        ArrayList<Player> playerList = new ArrayList<>();
        Scanner scan;
        int playerNumber = 0;
        while (true) {
            try {
                scan = new Scanner(System.in);
                System.out.println("Please enter the number of players! 2-4 players!");
                playerNumber = scan.nextInt();
                if (playerNumber >= 2 && playerNumber <= 4) {
                    Player.setPlayerNumber(playerNumber);
                    break;
                } else {
                    System.out.println(ANSI_RED+"Invalid player number! Only for 2-4 players. Try again!");
                    System.out.println("------------------------------------------\n"+ANSI_RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED+"Need a integer number for player number here");
                System.out.println("------------------------------------------\n"+ANSI_RESET);
            }
        }
        System.out.println(Player.getPlayerNumber() + " players playing.......");
        for (int i = 0; i < Player.getPlayerNumber(); i++) {
            String playerName;
            System.out.println("This is player No." + (i + 1));
            Player newPlayer = new Player();
            System.out.println("Please enter the name of the player" + (i + 1) + "(Name should be English letters or numbers)");
            playerName = scan.next();
            if (playerName.length() > 5) {
                System.out.println(ANSI_RED+"Sorry your name is longer than 5 characters!"+ANSI_RESET);
                playerName = playerName.substring(0,5);
                System.out.println("This is your new name: "+playerName);
            }
            newPlayer.setPlayerName(playerName);
            playerList.add(newPlayer);
            System.out.println("-----------------------------------");
        }
        playerRank(playerList);
        return playerList;
    }

    /** The entrance of the game scribble
     *
     */
    public static void operation() {
        boolean flagOperation = true;
        while (flagOperation) {
            try {
                Scanner scan = new Scanner(System.in);
                Menu.displayMenu();
                int choice = scan.nextInt();
                while (choice < 1 || choice > 4) {
                    System.out.println(ANSI_RED+"Wrong operation enter, please enter a integer that ranges 0-4!");
                    System.out.println("------------------------------------------\n"+ANSI_RESET);
                    Menu.displayMenu();
                    choice = scan.nextInt();
                }
                switch (choice) {
                    case 1 -> {
                        System.out.println("You want to start a new game!");
                        ArrayList<Player> playerList = gameSet();
                        char[][] chessBoard = Board.newChessBoard();
                        gamePlay(playerList, chessBoard);
                        exitGame(chessBoard,playerList);
                        flagOperation = false;
                    }
                    case 2 -> {
                        System.out.println("You want to play a saved game!");
                        ArrayList<Player> savedPlayerList = new ArrayList<>();
                        char[][] savedGame = Board.loadSavedChessBoard(savedPlayerList);
                        if (savedPlayerList.size() == 0) {
                            flagOperation = true;
                            continue;
                        }
                        System.out.println(savedPlayerList.size()+" players playing.....");
                        System.out.println("The score limit is: "+scoreLimit+"\n");
                        playerRank(savedPlayerList);
                        gamePlay(savedPlayerList, savedGame);
                        exitGame(savedGame,savedPlayerList);
                        flagOperation = false;
                    }
                    case 3 -> {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    case 4 -> {
                        helpFile();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED+"Invalid operation here!");
                System.out.println("Enter again!");
                System.out.println("------------------------------------------\n"+ANSI_RESET);
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED+"A integer number that ranges 0-4 is needed here! Try again!");
                System.out.println("------------------------------------------\n"+ANSI_RESET);
            }
        }
    }


    /** This method sorts players according to their scores, and displays players and their scores in a table.
     * @param playerList The list of all players
     **/
    public static void playerRank(ArrayList<Player> playerList) {
        System.out.println("This is player rank:");
        System.out.println("    NAME       score  ");
        for (int i = playerList.size()-1; i < 0; i--) {
            for (int j = playerList.size()-1; j < 0; j--) {
                if (playerList.get(j).getScore() > playerList.get(j - 1).getScore()) {
                    Collections.swap(playerList, (j - 1), j );
                }
            }
        }
        for (int m = 0; m < playerList.size(); m++) {
            System.out.println("+-----------+--------+");
            System.out.print("|  ");
            System.out.printf(ANSI_YELLOW+"%-6s"+ANSI_RESET, playerList.get(m).getPlayerName());
            System.out.println("   |  " + playerList.get(m).getScore() + "     |");
            System.out.println("+-----------+--------+");
        }
    }

    /**This method controls the process of playing game
     *
     * @param playerList The list of all players
     * @param chessBoard The board of scribble
     */
    public static void gamePlay(ArrayList<Player> playerList, char[][] chessBoard) {
        int loop = 0;
        while (endGame) {
            Board.displayBoard(chessBoard);
            playerList.get(loop).gamerPlay(chessBoard);
            loop++;
            playerRank(playerList);
            for (int j = 0; j < playerList.size(); j++) {
                if (playerList.get(j).getScore() >= scoreLimit) {
                    System.out.println(playerList.get(j).getPlayerName() + " reached the score limit! ");
                    System.out.println(playerList.get(j).getPlayerName() + " wins!");
                    System.exit(0);
                }
            }
            exitGame(chessBoard,playerList);
            if (loop == playerList.size()) {
                loop = 0;
            }
        }
    }

    /**This method allows players to set their score limit and check if the score limit valid.
     *
     */
    public static void setScore() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Please enter the score limit you want:  (10-100!)");
                scoreLimit = scan.nextInt();
                System.out.println("Your score limit is: " + scoreLimit);
                if (scoreLimit > 10 && scoreLimit < 100) {
                    break;
                } else {
                    System.out.println(ANSI_RED+"Your score limit is not in the ranges 10-100! Enter again!");
                    System.out.println("------------------------------------------\n"+ANSI_RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED+"Please enter a integer for score limit here.");
                System.out.println("------------------------------------------\n"+ANSI_RESET);
                String garbage = scan.next();
            }
        }
    }

    /**This method allows players to exit the game after each player finish his turn.
     *
     * @param chessBoard
     */
    public static void exitGame(char[][] chessBoard,ArrayList<Player> playerList) {
        String exit;
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Do you want to exit now?" + " (You game will be stored!)");
            System.out.println("Enter anything to continue! Or enter "+ ANSI_RESET+ "\"yes\""+ANSI_RESET+ " to exit!");
            exit = scan.next();
            if (exit.equals("yes")) {
                System.out.println("This is player rank:");
                playerRank(playerList);
                System.out.println("This game is stored!");
                System.out.println("Goodbye!");
                saveGame(chessBoard, playerList);
                System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println(ANSI_RED+"Wrong operation here! Please "+ANSI_RED+"\"yes\""+ANSI_RESET +" or anything else!");
            throw new RuntimeException();
        }
    }

    /** This method saves player information and the board after game ends
     *
     * @param chessBoardSave
     * @param playerList
     */
    public static void saveGame(char[][] chessBoardSave, ArrayList<Player> playerList) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("savedBoard.txt"));
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    bw.write(String.valueOf(chessBoardSave[i][j]));
                    bw.newLine();
                }
                bw.flush();
            }
            bw.write(String.valueOf(playerList.size()));
            bw.newLine();
            for (int i = 0; i < playerList.size(); i++) {
                bw.write(String.valueOf(playerList.get(i).getPlayerName()));
                bw.newLine();
                bw.write(String.valueOf(playerList.get(i).getScore()));
                bw.newLine();
            }
            bw.write(String.valueOf(scoreLimit));
            bw.newLine();
            bw.write(String.valueOf(Player.getMoveCounter()));
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** This method print a help file of the game scribble which includes the rules and other background information.
     *
     */
    public static void helpFile() {
        BufferedReader br = null;
        File f = new File("d:\\help.txt");
        try {
            InputStreamReader ipr = new InputStreamReader(new FileInputStream(f));
            br = new BufferedReader(ipr);
            String line;
            while ((line = br.readLine() )!=null){
            System.out.println(line);
            }
            ipr.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------\n");

        Menu.displayMenu();

    }




}


