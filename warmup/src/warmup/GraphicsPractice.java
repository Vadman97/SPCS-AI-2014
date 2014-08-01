package warmup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;

import acm.graphics.*;
import acm.program.*;

/**
 * Some practice with ACM Graphics and objects.
 * 
 * @author Vadim Korolik
 *
 */
@SuppressWarnings("unused")
public class GraphicsPractice extends GraphicsProgram 
{
	
	private static final long serialVersionUID = 6455019434829221613L;
	public static final int APPLICATION_HEIGHT = 720;
	public static final int APPLICATION_WIDTH = 1280;
	private static final String BUTTON_1_TEXT = "Clear Screen";
	public static Random rand = new Random();
	
	public static Ball b1;
	
	Graphics bg;
    Image offscreen;
    Dimension dim;
		
	public static void main(String[] args) 
	{
		new GraphicsPractice().start(args);
	}
	
	public void actionPerformed(ActionEvent e) {
	    if (e.getActionCommand().equals(BUTTON_1_TEXT)) {
	        System.out.println("Button was pressed!");
	        removeAll();
	    }
	}
	
	public void mouseClicked(MouseEvent e) 
	{
	    /*G3DRect rect = new G3DRect(rand.nextInt(100), rand.nextInt(100), rand.nextInt(1000), rand.nextInt(1000), true);
	    rect.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
	    rect.setFilled(true);*/
		//add(rect);
	}
	public void mouseMoved(MouseEvent e)
	{
		/*GOval oval = new GOval(e.getX(), e.getY(), 25 + rand.nextDouble(), 25 + rand.nextDouble());
		oval.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
	    oval.setFilled(true);*/
		//add(oval);
	}
	
	@Override
	public void init() {
		setTitle("Vadim's Graphics Practice");		
		
		dim = getSize();
        offscreen = createImage(dim.width, dim.height);
        bg = offscreen.getGraphics();
		
		JLabel specialLabel = new JLabel("Interactors!");
		JButton button = new JButton(BUTTON_1_TEXT);
		button.addActionListener(this);
		
		//add(specialLabel, SOUTH);
		add(button, WEST);
		
		/*addMouseListeners();		
		addKeyListeners();*/
	}
	
	@Override
	public void run() 
	{
		/*
		GRect rectTemp = new GRect(rand.nextInt(APPLICATION_WIDTH), rand.nextInt(APPLICATION_HEIGHT), rand.nextInt(100), rand.nextInt(100));
		rectTemp.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		rectTemp.setFilled(true);
		add(rectTemp);
		*/
		
		//Queue<String> peopleInLine = new Queue<String>();
		
		ArrayList<String> test = new ArrayList<String>();
		ArrayList<String> test2 = new ArrayList<String>(test.subList(5, 5));
		
		Collections.sort(test);
		
		GLabel label = new GLabel("Vadim Korolik", 500, 500);
		label.setColor(Color.BLUE);
		label.setFont(new Font("SansSerif", Font.BOLD, 100));
		//add(label);
		
		b1 = new Ball(500, 500, 100., -400.);
		GOval ball = new GOval(b1.x, b1.y, 20., 20.);
		ball.setColor(b1.bCol);
		ball.setFilled(true);
		add(ball);
		
		long sysTimeS = System.nanoTime(), sysTimeE = System.nanoTime(), delta = 0;
		
		while (isActive())
		{
			ball.setLocation(b1.x, b1.y);
			delta = sysTimeE - sysTimeS;
			sysTimeS = System.nanoTime();
			b1.simulate(-100., 350., (delta / 100000000.), getBounds()); 
			sysTimeE = System.nanoTime();
		}
		
	}
}
