import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.management.Query;
import javax.swing.JButton;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.program.GraphicsProgram;

/**
 * Who needs Google Maps when you have Pathfinder?
 * 
 * @author Vadim Korolik
 *
 */
public class Pathfinder extends GraphicsProgram {
	private static final long serialVersionUID = -5846277133335276638L;

	public static void main(String[] args) {
		new Pathfinder().start(args);
	}
	
	// Constants
	public static final int APPLICATION_WIDTH = 1000;
	public static final int APPLICATION_HEIGHT = 655;
	public static String imgURL = "http://i.imgur.com/GendRqt.png";
	public static URL imgPath;
	public static BufferedImage mapImage;
	public static Dimension mapDim;
	public static GImage mapImgObj;
	public static Random r = new Random();
	public static boolean editMode = false;
	public static int addingWhat = 0;
	public static double snapRange = (Node.height + Node.width)/2;
	public static String editString = "Toggle Edit";
	public static String calculateRouteString = "Calculate Route!!!";
	public static String addDestString = "Add a Destination!";
	public static String addWaypointString = "Add a Waypoint!";
	public static GLabel editEnabledLabel = new GLabel("EDIT MODE ACTIVE", 25, 25);
	
	// [YOUR DATA STRUCTURES WILL GO HERE]
	public static ArrayList<Destination> destinations = new ArrayList<Destination>();
	public static ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	public static Pair<Node> selectedNodePair = new Pair<Node>(null, null);
	
	@Override
	public void init() {
		// Setup code goes here
		setTitle("Pathfinder BETA!");
		resize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		addMouseListeners();
		addKeyListeners();
		
		JButton editMode = new JButton(editString);
		editMode.addActionListener(this);
		
		JButton calculateRoute = new JButton(calculateRouteString);
		calculateRoute.addActionListener(this);

		JButton addDest = new JButton(addDestString);
		JButton addWaypoint = new JButton(addWaypointString);
		addDest.addActionListener(this);
		addWaypoint.addActionListener(this);
		add(addDest, SOUTH);
		add(editMode, SOUTH);
		add(addWaypoint, SOUTH);
		add(calculateRoute, SOUTH);
		
		editEnabledLabel.setColor(Color.RED);
		editEnabledLabel.setFont(new Font("Sans Serif", Font.BOLD, 32));
		
		try {
			imgPath = new URL(imgURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("URL Initialized");
		
		try {
			mapImage = ImageIO.read(imgPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Image initialized");
		
		mapDim = new Dimension(mapImage.getWidth(), mapImage.getHeight());
		System.out.println("Image Dimensions initialized");

		mapImgObj = new GImage(mapImage);
		System.out.println("Map GImage initialized");
		
		System.out.println("INITIALIZED!");
	}
	
	@Override
	public void run() {
		// Run code goes here
		add(mapImgObj, 0, 0);
		/*while (true)
		{
		}*/
	}
	
	public void drawEdge(ArrayList<Node> cList)
	{
		Edge newEdge = new Edge(selectedNodePair.getLeft(), selectedNodePair.getRight());
		
		selectedNodePair.getLeft().addEdge(newEdge);
		selectedNodePair.getRight().addEdge(newEdge);
		
		Color leftCol, rightCol;
		
		if (selectedNodePair.getLeft().getClass().getName() == "Destination")
			leftCol = Destination.defaultColor;
		else
			leftCol = Waypoint.defaultColor;
		if (selectedNodePair.getRight().getClass().getName() == "Destination")
			rightCol = Destination.defaultColor;
		else
			rightCol = Waypoint.defaultColor;
			
		getElementAt(selectedNodePair.getLeft().getX(), selectedNodePair.getLeft().getY()).setColor(leftCol);
		getElementAt(selectedNodePair.getRight().getX(), selectedNodePair.getRight().getY()).setColor(rightCol);
		
		newEdge.getDrawnLine().setColor(Edge.lineColor);
		add(newEdge.getDrawnLine());
		
		for (Node n: cList)
		{
			if (selectedNodePair.getLeft().equals(n))
			{
				for (Destination d: destinations)
				{
					if (d.equals(n))
					{
						d.addEdge(newEdge);
						System.out.println("Added edge: " + newEdge + " to " + d);
					}
				}
				for (Waypoint w: waypoints)
				{
					if (w.equals(n))
					{
						w.addEdge(newEdge);
						System.out.println("Added edge: " + newEdge + " to " + w);
					}
				}
			}
			if (selectedNodePair.getRight().equals(n))
			{
				for (Destination d: destinations)
				{
					if (d.equals(n))
					{
						d.addEdge(newEdge);
						System.out.println("Added edge: " + newEdge + " to " + d);
					}
				}
				for (Waypoint w: waypoints)
				{
					if (w.equals(n))
					{
						w.addEdge(newEdge);
						System.out.println("Added edge: " + newEdge + " to " + w);
					}
				}
			}
		}
		
		selectedNodePair.setLeft(null);
		selectedNodePair.setRight(null);
	}
	
	public void runLineConnection(MouseEvent e)
	{
		ArrayList<Node> combinedList = new ArrayList<Node>();
		combinedList.addAll(destinations);
		combinedList.addAll(waypoints);
		//System.out.println(combinedList.size());
		double mX = e.getX();
		double mY = e.getY();
		Node temp = null;
		
		for (Node co : combinedList)
		{
			if (getElementAt(co.getX(), co.getY()) != null)
			{
				if (Math.abs(co.getX() - mX) <= snapRange)
				{
					if (Math.abs(co.getY() - mY) <= snapRange)
					{
						temp = co;
						
						Color col;
						
						if (temp.getClass().getName() == "Destination")
							col = Destination.highlightedColor;
						else
							col = Waypoint.highlightedColor;
						
						getElementAt(co.getX(), co.getY()).setColor(col); /////TODO This causes lines to be set to the wrong color
						
						System.out.println("Found a node there: " + temp);
						
						if (selectedNodePair.getLeft() == null && selectedNodePair.getRight() == null)
						{
							selectedNodePair.setLeft(temp);
						}
						else if (selectedNodePair.getLeft() != null)
						{
							selectedNodePair.setRight(temp);
						}
					}
				}
			}
		}
		System.out.println("Left: " + selectedNodePair.getLeft());
		System.out.println("Right: " + selectedNodePair.getRight());
		
		if (selectedNodePair.getLeft() != null && selectedNodePair.getRight() != null)
			drawEdge(combinedList);
	}
	
	public ArrayList<Node> routePath(Destination start, Destination end)
	{
		Queue<Node> nodes = new LinkedList<Node>();
		HashSet<Node> visited = new HashSet<Node>();
		ArrayList<Node> chartedPath = new ArrayList<Node>();
		nodes.add(start);
		
		long counter = 0;
		while (true)
		{			
			Node temp = nodes.remove();
			visited.add(temp);
			System.out.println(temp + " " + counter + " " + nodes.size());
			if (temp == end)
			{
				chartedPath.add(temp);
				break;
			}
			for (Edge e: temp.getEdges())
			{
				if (!visited.contains(e.getNode1()))
					nodes.offer(e.getNode1());
				if (!visited.contains(e.getNode2()))
					nodes.offer(e.getNode2());
					System.out.println("Nodes size: " + nodes.size());
			}
			chartedPath.add(temp);
			counter ++;
			if (temp == end)
				break;
		}
		return chartedPath;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		remove(editEnabledLabel);
	    if (e.getActionCommand().equals(editString)) {
	    	editMode = !editMode;
	    }
	    if (e.getActionCommand().equals(addDestString)) {
	    	addingWhat = 0;
	    }
	    if (e.getActionCommand().equals(addWaypointString)) {
	    	addingWhat = 1;
	    }
	    if (e.getActionCommand().equals(calculateRouteString)) {
	    	System.out.println("SwaG");
	    	ArrayList<Node> result = routePath(destinations.get(0), destinations.get(destinations.size() - 1));
	    	System.out.println("Swag: " + result + "SwaG");
	    	for (Node n: result)
	    	{
	    		getElementAt(n.getOval().getLocation()).setColor(Color.magenta);
	    	}
	    }
	    if (editMode) {
	    	add(editEnabledLabel);
	    }
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (!editMode){runLineConnection(e); return;}
		
		if (addingWhat == 0)
		{
			Destination d = new Destination(e.getX(), e.getY(), "Test", Destination.defaultColor);
			destinations.add(d);
			add(d.getOval());
		}
		else if (addingWhat == 1)
		{
			Waypoint w = new Waypoint(e.getX(), e.getY(), Waypoint.defaultColor);
			waypoints.add(w);
			add(w.getOval());
		}
	}
}
