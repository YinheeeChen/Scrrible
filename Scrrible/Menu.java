package scribble;

public class Menu {
    private static final int NEW_GAME = 1;
    private static final int SAVED_GAME = 2;
    private static final int EXIT = 3;
    private static final int HELP = 4;

    /** This method displays the main menu of game scribble
     *
     */
    public static void displayMenu() {
        System.out.println("Hello! Welcome come to Scribble!");
        System.out.println("-----------MENU-----------");
        System.out.println("#1.Start a new game");
        System.out.println("#2.Load a saved game");
        System.out.println("#3.Exit");
        System.out.println("#4.Help");
        System.out.println("Choose an option:");
    }
}