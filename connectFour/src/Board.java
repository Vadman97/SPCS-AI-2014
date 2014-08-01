import java.util.*;

/** An instance represents a grid of pieces from two opposing
 *  players in a game of Connect Four. The grid is 0-indexed first by rows
 *  starting at the top, then by columns 0-indexed starting at the left. */
public class Board {
    /** The number of rows on the Connect Four board. */
    public static final int NUM_ROWS= 6;
    /** The number of columns on the Connect Four board. */
    public static final int NUM_COLS= 7;

    /** vertical, horizontal, uphill, downhill, directions from any position */
    private static final int[][] deltas= {{1, 0}, {0, 1}, {-1, 1}, {1, 1}}; 
    
    /** The two opposing players. A null Player value in board
     *  indicates an empty tile. */
    public enum Player {
        RED, YELLOW;

        /** Return the opponent of this player. */
        public Player opponent() {
            return this == RED ? YELLOW : RED;
        }
        
        /** Return the Player as a String */
        public String toString() {
            return this == RED ? "RED" : "YELLOW";
        }
    }

    /** The grid of Player pieces. */
    private Player[][] board;

    /** Constructor: an empty Board. */
    public Board() {
        board= new Player[NUM_ROWS][NUM_COLS];
    }

    /** Constructor: a duplicate of Board b. */
    public Board(Board b) {
        board= new Player[NUM_ROWS][NUM_COLS];
        for (int r= 0; r < NUM_ROWS; r++) {
            for (int c= 0; c < NUM_COLS; c++) {
                board[r][c]= b.board[r][c];
            }
        }
    }

    /** Return the element in row r col c.
     * Precondition: r and c give a position on the board */
    public Player getPlayer(int r, int c) {
        assert 0 <= r  &&  r < NUM_ROWS  &&  0 <= c  &&  c < NUM_COLS;
        return board[r][c];
    }

    /** Constructor: a Board constructed by duplicating b and
     *  applying nextMove to the new Board. */
    public Board(Board b, Move nextMove) {
        this(b);
        makeMove(nextMove);
    }

    /** Return the Player at board position (row, col). Rows are
     *  0-indexed starting at the top and columns are 0-indexed starting
     *  at the left. A null return value indicates an empty tile. */
    public Player getTile(int row, int col) {
        return board[row][col];
    }

    /** Apply Move move to this Board by placing a piece from move's
     *  player into move's column on this Board. Throw an
     *  IllegalArgumentException if move's column is full on this Board. */
    public void makeMove(Move move) {
        // TODO
    	//Delete the following code once you've decided to start implementing
    	//throw new UnsupportedOperationException("You need to implement makeMove before running the game.");
    }

    /** Return an array of all moves that can possibly be made by Player p on this
     *  board. The moves must be in order of increasing column number.
     *  Note: The length of the array must be the number of possible moves.
     *  Note: If the board has a winner (4 things of the same color in a row), no
     *  move is possible because the game is over.
     *  Note: If the game is not over, the number of possible moves is the number
     *  of columns that are not full. Thus, if all columns are full, return an
     *  array of length 0. */
    public Move[] getPossibleMoves(Player p) {
        // TODO
    	return null;
    }

    /** Return a representation of this board */
    public @Override String toString() {
        return toString("");
    }

    /** Return the String representation of this Board with indent
     *  prepended to each line. Typically, indent contain space characters. */
    public String toString(String indent) {
        String str = "";
        for (Player[] row : board) {
            str += indent + "|";
            for (Player spot : row) {
                if (spot == null) {
                    str += " |";
                } else if (spot == Player.RED) {
                    str += "R|";
                } else {
                    str += "Y|";
                }
            }
            str += "\n";
        }
        return str;
    }

    /** Return the Player that has four in a row (or null if no player has). */
    public Player hasConnectFour() {
        for (Player[] loc : winLocations()) {
            if (loc[0] != null && loc[0] == loc[1] && loc[0] == loc[2] && loc[0] == loc[3]) {
                return loc[0];
            }
        }
        return null;
    }

    /** Return a list of all locations where it is possible to
     *  achieve connect four. In this context, a "win location" is an
     *  array of the Player pieces on this Board from four connected tiles. */
    public List<Player[]> winLocations() {
        List<Player[]> locations= new ArrayList<Player[]>();
        for (int[] delta : deltas) {
            for (int r= 0; r < NUM_ROWS; r++) {
                for (int c= 0; c < NUM_COLS; c++) {
                    Player[] loc= possibleWin(r, c, delta);
                    if (loc != null) {
                        locations.add(loc);
                    }
                }
            }
        }
        return locations;
    }

    /** If the 4 locations in a row beginning in board[r][c] going in the direction
     * given by [delta[0]][delta[1]] are on the board, return an array of them.
     * Otherwise, return null;
     * Precondition: board[r][c] is on the board and delta is one of the elements of
     * static variable deltas.
     */
    public Player[] possibleWin(int r, int c, int[] delta) {
        Player[] location = new Player[4];
        for (int i = 0; i < 4; i++) {
            int newR = r + i*delta[0];
            int newC= c + i*delta[1];
            if (!(0 <= newR  &&  newR < NUM_ROWS  &&  0 <= newC  &&  newC <  NUM_COLS)) {
                return null;
            }
            location[i]= board[newR][newC];
        }
        return location;
    }
}
