import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;


/**
 * NOTHING FOR YOU TO DO HERE.
 */
 /** An instance represents the game panel. */
public class GamePanel extends JPanel {
    /** The colors for various parts of the panel. The name explains it. */
	private final Color emptyColor= new Color(208,208,208);
	private final Color backColor= new Color(0,123,255);
	private final Color sepColor= new Color(0,123,255);
	private final Color yellowColor= new Color(255,255,0);
	private final Color redColor= new Color(255,0,0);

	private final int cellSize= 62;
	private final int sepSize= 3;	

	/** The width of the panel (in pixels). */
	private int width;
	/** The height of the panel (in pixels). */
	private int height;
	/** The board. */
	private Board board;

	private GUI window;    
	private Semaphore waitSema;

	// Animation
	private Timer timer;
	public boolean animation;
	private Color animationColor;
	private final int animDelay= 75; //milliseconds 
	private int colIndex;
	private int rowIndex;
	private int stopRow;

	/** An instance is the JPanel that represents the Connect Four board. */
	public GamePanel(GUI window, Board b) {
		this.window= window;
		
		board= new Board(b);

		width= Board.NUM_COLS*cellSize + (Board.NUM_COLS+1)*sepSize;
		height= Board.NUM_ROWS*cellSize + (Board.NUM_ROWS+1)*sepSize;
		waitSema= new Semaphore(0);

	}

	/** Start the animation at column col
	 *  and release the blocked thread that called this method.*/
	private void startAnimation(int col) {
		colIndex= col;
		rowIndex= -1;
		animation= true;
		
		stopRow= 0; //the first occurrence of a non-null tile
		while (!(stopRow >= Board.NUM_ROWS || board.getTile(stopRow, colIndex) != null)) {
			stopRow++;
		}

		// Store in takPerformer an anonymous listener for animation events
		ActionListener taskPerformer= new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!animation) return;
				
				if (rowIndex+1 < stopRow) {
					rowIndex++;
					repaint();
					return;
				}
				repaint();
				animation= false;
				waitSema.release();
				timer.stop();
			}
		};
		timer= new Timer(animDelay,taskPerformer);
		timer.start();
	}

	/** Start the animation for player p using column col and block the running thread. */
	public void playColumn(Board.Player p, int col) {
		window.setMsg("Run playColumn with column " + col + ".");
		animationColor=  p == Board.Player.YELLOW ? yellowColor : redColor;
		startAnimation(col);
		try {
			waitSema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** Save a copy of board b.
	 *  A copy is needed so that when the board from class Game is updated,
	 *  it does not show up on the Game panel just yet. There needs to be some
	 *  time for the Game Panel to show the animation. */
	public void updateBoard(Board b) {
		board= new Board(b);
	}
	
	/** Return the board. */
	public Board getBoard() {
		return board;
	}

	/** Return the Dimension of the panel. */
	public Dimension getPreferredSize() {
		return new Dimension(width,height);
	}

	/** Paint the entire game panel, painting whatever is represented in this
	 *  object's version of the board and as well as the animation's falling tiles. */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d= (Graphics2D) g;
		
		// Paint with the background color.
		g2d.setColor(backColor);
		g2d.fillRect(0, 0, width, height);
		//Draw the lines.
		g2d.setColor(sepColor);
		//Draw the horizontal lines.
		for (int y= 0, i= 0; i <= Board.NUM_ROWS; i= i+1) {
			g2d.fillRect(0,y,width,sepSize);
			y= y + sepSize + cellSize;
		}
		//Draw the vertical lines;
		for (int x = 0, i = 0; i <= Board.NUM_COLS; i= i+1) {
			g2d.fillRect(x, 0, sepSize, height);
			x= x + sepSize + cellSize;
		}
		
		//Paint occupied tiles
		for (int x= sepSize, i= 0; i < Board.NUM_COLS; i++) {
			for (int y= sepSize, j = 0; j < Board.NUM_ROWS; j++) {
				if (board.getTile(j,i) == Board.Player.YELLOW) {
					g2d.setColor(yellowColor);
					g2d.fillOval(x, y, cellSize, cellSize);
				} else if (board.getTile(j,i) == Board.Player.RED) {
					g2d.setColor(redColor);
					g2d.fillOval(x, y, cellSize, cellSize);
				} else {
					g2d.setColor(emptyColor);
					g2d.fillOval(x, y, cellSize, cellSize);
				}
				y= y + sepSize + cellSize;
			}
			x= x + sepSize + cellSize;
		}
		
		//This code  paints the falling tiles of the animation
		if (animation) {
			g2d.setColor(animationColor);
			int xLoc= colIndex*(sepSize+cellSize) + sepSize;
			int yLoc= rowIndex*(sepSize+cellSize) + sepSize;
			g2d.fillOval(xLoc,yLoc,cellSize,cellSize);
		}
	}
}
