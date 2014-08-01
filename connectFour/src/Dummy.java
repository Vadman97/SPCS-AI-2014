import java.util.*;

/** 
 *  NOTHING FOR YOU TO DO HERE.
 *  
 *  A Solver that chooses moves randomly. */
public class Dummy implements Solver {

	private Board.Player myColor;

	public Dummy(Board.Player color) {
		myColor= color;
	}
	
	/** See Solver.getMoves for the specification. */
    public @Override Move[] getMoves(Board b) {
    	Random rand= new Random();
		int column= rand.nextInt(Board.NUM_COLS);
		while (b.getTile(0, column) != null) {
			column= rand.nextInt(Board.NUM_COLS);
		}
		Move[] move= { new Move(myColor, column) };
		return move;
	}
	
}
