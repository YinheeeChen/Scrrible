package scribble;


import java.io.*;
import java.util.ArrayList;

public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    public static char[][] newChessBoard() {
        char[][] chessBoard = new char[15][15];
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[i].length; j++) {
                chessBoard[i][j] = ' ';
            }
        }
        return chessBoard;
    }

    /**
     * Display a chessBoard
     *
     * @param chessBoard Saved chess board
     */
    public static void displayBoard(char[][] chessBoard) {
        System.out.println(ANSI_BLUE + "      0     1     2     3     4     5     6     7     8     9    10    11    12    13    14" + ANSI_RESET);
        for (int i = 0; i < chessBoard.length; i++) {
            System.out.println("   |-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|");
            System.out.printf(ANSI_BLUE + "%3d" + ANSI_RESET, i);
            for (int j = 0; j < chessBoard[i].length; j++) {
                if ((i == 0 && (j == 0 || j == 14)) || (i == 2 && (j == 2 || j == 12)) || (i == 12 && (j == 2 || j == 12)) || (i == 14 && (j == 0 || j==14))) {
                System.out.print("|"+ ANSI_GREEN_BACKGROUND+ "  " +ANSI_RESET + ANSI_YELLOW+chessBoard[i][j]+ANSI_RESET+"  ");
             } else  if ((i == 4 && (j == 4 || j == 10)) || (i == 10 && (j == 4 || j == 10)) || (i == 7 && (j == 0|| j == 14)) ) {
                    System.out.print("|"+ ANSI_PURPLE_BACKGROUND+ "  " +ANSI_RESET + ANSI_YELLOW+chessBoard[i][j]+ANSI_RESET+"  ");
                }
                 else System.out.print("|"+"  " + ANSI_YELLOW+chessBoard[i][j]+ANSI_RESET+"  ");
            }
            System.out.println("|");
        }
        System.out.println("   |-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|");
    }

    /** Load a saved chess board
     * @return saved chess board
     */
    public static char[][] loadSavedChessBoard(ArrayList<Player> playerList) {
        BufferedReader gameReader = null;
        int playerNumber = 0;
        char[][] savedChessBoard = new char[15][15];
        try {
            File gameFile = new File("savedBoard.txt");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(gameFile));
            gameReader = new BufferedReader(isr);
            for (int i = 0; i < savedChessBoard.length; i++) {
                for (int j = 0; j < savedChessBoard[i].length; j++) {
                    savedChessBoard[i][j] = gameReader.readLine().charAt(0);
                }
            }

            playerNumber = Integer.parseInt(gameReader.readLine());
            for (int n = 0; n < playerNumber; n++) {
                Player player = new Player();
                playerList.add(player);
            }
            for (int m = 0; m < playerNumber; m++) {
                playerList.get(m).setPlayerName(gameReader.readLine());
                playerList.get(m).setScore(Integer.parseInt(gameReader.readLine()));
            }
            Game.setScoreLimit(Integer.parseInt(gameReader.readLine()));
            Player.setMoveCounter(Integer.parseInt(gameReader.readLine()));
            isr.close();
            gameReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No saved board available!" + "\nwhy not start a new game!\n");
            System.out.println("============================================================");
            char[][] chessBoard = new char[15][15];
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard[i].length; j++) {
                    chessBoard[i][j] = ' ';
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
              throw e;
        }
        return savedChessBoard;
    }


}




