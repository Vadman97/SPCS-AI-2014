import java.util.concurrent.Semaphore;

/**  NOTHING FOR YOU TO DO HERE. */

 /**  A Solver that takes human input from a GUI to determine moves. */
public class Human implements Solver {

	private Board.Player player; // The player
	private Semaphore waitSema;  // Used to wait for a move by the human
	private int nextColumn;      // Place to put next piece

	/** Construct a Human Solver for player p. */
	public Human(Board.Player p) {
		player= p;
		waitSema= new Semaphore(0);
	}

	/** See Solver.getMoves for the specification. */
    public @Override Move[] getMoves(Board b) {
    	try {
			waitSema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Move[]{ new Move(player, nextColumn) };
	}
	
	/** Signal to this Human that the user wants to place
	 *  a piece in column c. */
	public void columnClicked(int c) {
		nextColumn= c;
		waitSema.release();
	}

}
