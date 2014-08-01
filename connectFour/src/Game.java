/** Runs the Connect Four game. */
public class Game {

    private Solver player1;   // The first player
    private Solver player2;  // The second player
    private Board board;     // the board
    private Solver activePlayer;  // The possible moves to the player whose turn it is
    private GUI gui;
    private Board.Player winner;  // null

    //Change this if you would like a delay between plays
    private static final long SLEEP_INTERVAL= 0; //in milliseconds

    /** Have the computer play against itself, putting output int the default
     * Java console.
     * You can also have your new AI player play from here, with no GUI output.
     * It may be useful for testing.
     * If you want a human to play, use GUI.main.
     */
    public static void main(String[] args) {
        /* p1 is the first player, p2 is the second player. This is set to
         * have to Dummy players--playing randomly.
         * To play the game with your own AI object, use assignments like you
         * see in the comments after these two assignments. In those assignments,
         * the second argument of the constructor is the depth to which AI
         * searches the game space. */
        Solver p1= new Dummy(Board.Player.RED);
        Solver p2= new Dummy(Board.Player.YELLOW); 
         
        //Solver p1= new AI(Board.Player.RED, 5);
        //Solver p2= new AI(Board.Player.YELLOW, 5);
        
        Game game= new Game(p1, p2);
        game.runGame();
        
        /* When testing, you may want to comment out all the above statements
         */
        
        /* -------------------------------------------------------------- */

        /* ABOUT TESTING.
         * Testing is difficult using a JUnit testing class because it is difficult to
         * get at many of the fields and methods from that class. So you may want to
         * put some testing methods in this class Game.
         * 
         * In testing various methods, you may want to use boards with certain
         * layouts of your choosing. For example, a board with column 0 all filled.
         * To do this, you can
         * 
         *     1. Create a board b
         *     2. Call makeMove several times to put pieces where you want them.
         *     3. Play the game.
         *     
         * For example, you can write the following to set up an initial board and
         * then play the game
         * 
         *      Board b= new Board();
         *      b.makeMove(new Move(Board.Player.RED, 4));
         *      b.makeMove(new Move(Board.Player.YELLOW, 3));
         *      b.makeMove(new Move(Board.Player.RED, 5));
         *      Game game= new Game(p1, p2, b, false);
         * 
         * This code places a red piece in column 4, a yellow piece in column 3,
         * and a red piece in column 5. Then it runs the game.
         * 
         * We give you also procedure fillColumn at the end of this file to help
         * out in initializing a board. Study it. Note that it is static.
         * 
         * Suppose you want to test a method that your wrote, like Board.getPossibleMoves.
         * Thus, you want to do the following.
         * 
         *     1. Create a board b
         *     2. Call makeMove several times to put pieces where you want them.
         *     3. Call the method you want to test.
         *     4. Check the result, if any.
         *     
         * You can check the result by using println statements to print out things and
         * looking at the output. You are testing by eyeballing the output. This is OK as
         * long as you are careful. Here is how you could print out the results of a
         * call to getPossibleMoves:
         * 
         *      Board b= new Board();
         *      fillColumn(b, Board.Player.RED, 0);  // fill column 0
         *      Move[] moves= b.getPossibleMoves(Board.Player.RED);
         *      for (Move m : moves) {
         *           System.out.println(m);
         *
         * If you are having real trouble, the above may not help. Here is what you
         * can do to test the very basics of Board.getPossibleMoves:
         * 
         *      Board b= new Board();
         *      fillColumn(b, Board.Player.RED, 0);  // fill column 0
         *      Move[] moves= b.getPossibleMoves(Board.Player.RED);
         *      if (moves.length != Board.NUM_COLS-1) {
         *          System.out.println("Error in getPossibleMoves with 1 col filled. array size is wrong: " +  moves.length);
         *      }
         *      if (moves[0].getColumn() != 1) {
         *          System.out.println(s + "First col is filled, second isn't but moves[0] is " + moves[0]);
         *      } 
         *      
         * We suggest you write a static method in class main to test getPossibleMoves 
         * (and perhaps other methods for other tests).
         * Make it a method so that you can call it or not from method main, depending on
         * your needs. It doen't have to test ALL possible cases, but it has to check
         * enough that so you are positive the method works.
         * 
         * 
        }
         * 
         * */

        /* ********* Put any testing methods that you write here.  *******
         * *** We will not see them because you don't submit class Game ***/
    }
    
    
    
    /** ************** Do not change anything below here ***************/

    /** Construct a new Game with p1 as the first player, p2 as the second player,
     *  with b as the current Board state, and with it being p's turn to play
     *  true means player 1, false means player 2). */
    public Game(Solver p1, Solver p2, Board b, boolean p) {
        this(p1, p2);
        board= b;
        activePlayer= (p ? player1 : player2);
    }

    /** Constructor: a new Game with p1 as the first player and p2 as the second player.
     * It is p1's turn to play. 
     *  Precondition: p1 and p2 are different */
    public Game(Solver p1, Solver p2) {
        player1= p1;
        player2= p2;
        board= new Board();
        activePlayer= player1;
    }

    /** Attach GUI gui to this Game model. */
    public void setGUI(GUI gui) {
        this.gui= gui;
    }

    /** Notify this Game that column col has been clicked by a user. */
    public void columnClicked(int col) {
        if (activePlayer instanceof Human) {
            ((Human) activePlayer).columnClicked(col);
        }
    }

    /** Run the game until finished. If GUI is not initialized, the output will be 
     *  sent to the console. */
    public void runGame() {
        while (!isGameOver()) {
            //Checking to see that the move can be made (not overflowing a column)
            boolean moveIsSafe= false;
            Move nextMove= null;
            while (!moveIsSafe) {
                Move[] bestMoves= activePlayer.getMoves(board);
                if (bestMoves.length == 0) {
                    gui.setMsg("Game cannot continue until a Move is produced.");
                    continue;
                } else {
                    nextMove= bestMoves[0];
                }
                if (board.getTile(0,nextMove.getColumn()) == null) {
                    moveIsSafe= true; 
                } else {
                    gui.setMsg("Illegal Move: Cannot place disc in full column. Try again.");
                }
            }

            board.makeMove(nextMove);
            if (gui == null) {
                System.out.println(nextMove);
                System.out.println(board);
            } else {
                gui.updateGUI(board, nextMove);
            }
            activePlayer= (activePlayer == player1 ? player2 : player1);

            // The following code causes a delay so that you can easily view the plays
            // being made by the AIs
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (gui == null) {
            if (winner == null) {
                System.out.println("Tie game!");
            } else {
                System.out.println(winner + " won the game!!!");
            }
        } else {
            gui.notifyGameOver(winner);
        }
    }

    /** Return true iff this game is over. If the game
     *  is over, set the winner field to the winner; if no winner
     *  set the winner to null. */
    public boolean isGameOver() {
        winner= board.hasConnectFour();
        if (winner != null) return true;

        // if these is an unfilled tile, return false;
        for (int r= 0; r < Board.NUM_ROWS; r= r+1) {
            for (int c= 0; c < Board.NUM_COLS; c= c+1) {
                if (board.getTile(r, c) == null)
                    return false;
            }
        }

        return true;
    }

}
