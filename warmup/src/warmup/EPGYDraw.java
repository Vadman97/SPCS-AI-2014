package warmup;

import acm.graphics.*;
import acm.program.*;


/**
 * Here's an intro/review to using Java, combining many concepts and parts of SimpleGraphics.
 * 
 * Your task: build a simple drawing program that does the following:
 *  (1) Buttons that lets you choose whether to draw an ellipse, a rectangle, a line, or a rounded rect
 *  (2) When the program first starts, the main canvas should display a nice welcoming message and/or graphics.
 *  (3) As soon as the user starts drawing, this welcome message should disappear
 *  (4) To draw, click and drag the mouse. The shape should display as you are dragging.
 *  (5) When released, the shape will stay on the screen.
 *  (6) A clear button will clear everything on the screen.
 *  (7) Any other awesome stuff you can think of!
 * 
 * @author Vadim Korolik
 *
 */
@SuppressWarnings("serial")
public class EPGYDraw extends GraphicsProgram {
	public static void main(String[] args) {
		new EPGYDraw().start(args);
	}
	
	
	
	/**
	 * Put any setup code here!
	 */
	@Override
	public void init() {
		// TODO: INIT!
		setTitle("EPGYDraw!");
	}
	
	/**
	 * Put your program run code here! Most of your code should be in here.
	 */
	@Override
	public void run() {
		// TODO: The main run of the program!
		add(new GLabel("I'm a placeholder!", 10, 40));
	}
}
