import java.io.*;

/** An instance represents the state of a game of Connect Four. */
public class State implements Comparable<Object> {
    /** A State array of length 0. */
    public final static State[] length0= {};
    
    /** It is player's turn to play. */
    private Board.Player player;

    /** The current Board layout. */
    private Board board;

    /** The most recent move taken. */
    private Move lastMove;

    /** All possible game States that can result from the next player's Move.
     *  The length of the array equals the number of States.
     *  It is an array of length 0 if there are no possible moves
     *  (once it has been set; initially, it is an array of length0) */
    private State[] children= length0;

    /** How desirable this State is. */
    private int value;

    /** Constructor: a game State consisting of board b, with player player
     *  to play next; lm is the last Move made on this game --null
     *  if the user does not care what the last move made was. */
    public State(Board.Player p, Board b, Move lm) {
        player= p;
        board= b;
        value= 0;
        lastMove= lm;
    }

    /** Return the player whose turn it is. */
    public Board.Player getPlayer() {
        return player;
    }

    /** Return this State's Board. */
    public Board getBoard() {
        return board;
    }

    /** Return the last move made. */
    public Move getLastMove() {
        return lastMove;
    }

    /** Return this State's children. */
    public State[] getChildren() {
        return children;
    }
    
    /** Set this State's children to c. */
    public void setChildren(State[] c) {
    	children= c;
    }

    /** Return this State's value. */
    public int getValue() {
        return value;
    }

    /** Set this State's value to v. */
    public void setValue(int v) {
        value= v;
    }

    /** 
     *  Retrieves the possible moves and initializes this State's children.
     *  The result is that this State's children reflect the possible
     *  States that can exist after the next move. Remember, in the
     *  children it is the opposite player's turn. This method
     *  initializes only this State's children; it does not recursively
     *  initialize all descendants. 
     */
    public void initializeChildren() { 
        // TODO
    }

    /** Write this State to a file called "output.txt", including its
     *  children, their children, etc.. This method allows the State to
     *  be viewed in a file even when it is too large to print to console.
     *  Beep when printing is done. */
    public void writeToFile() {
        PrintWriter writer= null;
        try {
            writer= new PrintWriter("output.txt", "UTF-8");
            writer.println(this);
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /** Return a representation of this State. */
    public @Override String toString() {
//        System.out.println("State.toString printing");
        return toStringHelper(0,"");
    }

    /** Return a string that contains a representation of this board indented
        with string ind (expected to be a string of blank characters) followed
        by a similar representation of all its children,
        indented an additional ind characters. d is the depth of this state. */
    private String toStringHelper(int d, String ind) {
        String str= ind + player + " to play\n";
        str= str + ind + "Value: " + value + "\n";
        str= str + board.toString(ind) + "\n";
        if (children != null && children.length > 0) {
            str= str + ind + "Children at depth "+ (d+1) + ":\n" +
                    ind + "----------------\n";

            for (State s : children) {
                str= str + s.toStringHelper(d+1,ind + "   ");
            }
        }
        return str;
    }

    /** = -1, 0, or 1 depending on whether this object is <, = , or > than ob */
    public @Override int compareTo(Object ob) {
        // NOTE: this does not work! If you wish to use any sorting
        // method from the Java API, you must implement this.
        return 0;
    }
}
