import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static String[][] adminBoard = new String[11][11];
    public static String[][] userBoard = new String[11][11]; // inside the startUserBoard method I use the userBoard global array to add "-" to every index
    public static int bombCount = 20;
    public static boolean gameWon = false;

    public static void main(String[] args) {
        getGame("game5.txt");
        startUserBoard();
        System.out.println("Get ready for a game of BattleShip!");
        usersBattle();
    }

    public static void getGame(String fileName){
        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);

            // filling up the admin board using the inputtted file line by line
            for (int row = 0; row < adminBoard.length && reader.hasNextLine(); row++) {
                String line = reader.nextLine();
                Scanner lineScanner = new Scanner(line);

                // each value in the line goes into the board
                for(int col = 0; col < adminBoard[row].length && lineScanner.hasNext(); col++){
                    adminBoard[row][col] = lineScanner.next();
                }
                lineScanner.close();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        }
    }

    public static void startUserBoard(){
        // filling the user's board with "-" so that it looks empty at the start
        for (int row = 1; row < userBoard.length; row++) {
            for (int col = 1; col < userBoard[row].length; col++) {
                userBoard[row][col] = "-";
            }
        }
    }

    public static void userGameBoard(String[][] board){ // inside the parameters of this method I put the userBoard array and call the method in the printGame
                                                        // and userBattle methods.
        // printing the top row of column numbers 0-9
        System.out.print("  ");
        for (int col = 1; col < board[0].length; col++) {
            System.out.print((col - 1) + " ");
        }
        System.out.println();

        // printing each row with the left side of numbers 0-9
        for (int row = 1; row < board.length; row++) {
            System.out.print((row - 1) + " ");
            for (int col = 1; col < board[row].length; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void checkTry(int row, int col){
        // only do something if the user hasn’t already tried this spot
        if (userBoard[row][col].equals("-")) {
            if (adminBoard[row][col].equals("S")) {
                userBoard[row][col] = "X"; 
                System.out.println("Hit!");
            } else {
                userBoard[row][col] = "O";
                System.out.println("Miss!");
            }
            bombCount--;
        } else {
            // if the user already guessed this spot, still take away a bomb
            System.out.println("Already guessed there!");
            bombCount--;
        }
    }

    public static boolean isGameWon(){
        // checking if any ships are still hidden
        for (int row = 1; row < adminBoard.length; row++) {
            for (int col = 1; col < adminBoard[row].length; col++) {
                if (adminBoard[row][col].equals("S") && userBoard[row][col].equals("-")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void printGame() {
        System.out.println("\nYour Final Game Board:");
        userGameBoard(userBoard);
    }

    public static void usersBattle() {
        Scanner userInput = new Scanner(System.in);

        while (bombCount > 0 && !gameWon) {
            userGameBoard(userBoard);            
            gameWon = isGameWon();

            if (!gameWon) {
                // asking the user for coordinates to guess
                System.out.print("Enter row (0-9): ");
                int userRow = userInput.nextInt();
                System.out.print("Enter column (0-9): ");
                int userCol = userInput.nextInt();

                // making sure their input is actually on the board
                boolean validRow = (userRow >= 0 && userRow <= 9);
                boolean validCol = (userCol >= 0 && userCol <= 9);
                
                if (validRow && validCol) {
                    // +1 because we're skipping index 0 in the board setup
                    checkTry(userRow + 1, userCol + 1);
                    System.out.println("Bombs remaining: " + bombCount);
                } else {
                    System.out.println("Invalid coordinates. Try again.");
                }
            }
        }

        // printing the final result based on how the game ended
        if (gameWon) {
            System.out.println("You sunk all the ships! You win!");
            printGame();
        } else {
            System.out.println("Out of tries! Game over.");
            printGame();
        }
    }
}

