/** An instance represents a player and a column number, so that when this move
 *  is applied to a Board object, this Move's player will place a piece in
 *  this Move's column in the Board. */
public class Move {
    /** A Move array of length 0 */
    public static final Move[] length0= {};

    private Board.Player player;  // player is playing
    private int column;           // in this column

    /** Constructor: an instance with player p playing in column c.
     * Precondition p != null and c in 0..Board.NUM_COLS-1. */
    public Move(Board.Player p, int c) {
        if (p == null) {
            throw new IllegalArgumentException("Cannot create a Move with a null player");
        }
        if (c < 0 || Board.NUM_COLS <= c) {
            throw new IllegalArgumentException("Cannot create a Move with column that " +
                    "is not in 0..Board.NUM_COLS-1");
        }
        column= c;
        player= p;
    }

    /** Return the player of this Move. */
    public Board.Player getPlayer() {
        return player;
    }

    /** Return the column of this Move. */
    public int getColumn() {
        return column;
    }

    /** Return a representation of this Move. */
    public @Override String toString() {
        return player + " put a piece in column " + column;
    }

}
