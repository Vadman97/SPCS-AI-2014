import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import acm.graphics.GOval;

/**
 * A representation of a graph node.
 * 
 * @author Lekan Wang (lekan@lekanwang.com)
 *
 */
public class Node {
	private List<Edge> edges;
	private GOval oval;
	private double x, y; // An example of a value stored in the node.
	public static double width = 10, height = 10;
	// TODO You will need to modify or add to this to store what you need
	
	/**
	 * Creates an empty node with no edges, and a default 0 value;
	 */
	public Node() {
		this.edges = new ArrayList<Edge>();
	}
	
	/**
	 * Creates a node with no edges, and the given value.
	 * @param value
	 */
	public Node(double x, double y) {
		this();
		this.setX(x);
		this.setY(y);
		this.oval = null;
	}
	
	public Node(double x, double y, Color ovalCol) {
		this(x, y);

		this.oval = new GOval(getX() - (getDefaultWidth()/2), getY() - (getDefaultHeight()/2), getDefaultWidth(), getDefaultHeight());
		this.setOvalColor(ovalCol);
		this.oval.setFilled(true);
	}
	
	/**
	 * Adds the given edge to this node. The edge must be not null
	 * and valid.
	 * 
	 * @param edge
	 */
	public void addEdge(Edge edge) {
		assert (edge != null);
		edges.add(edge);
	}
	
	/**
	 * Gets the list of edges as an unmodifiable list.
	 */
	public List<Edge> getEdges() {
		System.out.println("GETTING EDGES!!!!");
		if (Collections.unmodifiableList(edges) == null)
			System.out.println("BAD BAD BAD");
		return Collections.unmodifiableList(edges);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDefaultWidth() {
		return width;
	}

	public double getDefaultHeight() {
		return height;
	}
	
	public GOval getOval() {
		return oval;
	}
	
	public Color getOvalColor() {
		assert (oval != null);
		return oval.getColor();
	}
	
	public void setOvalColor(Color ovalColor) {
		assert (oval != null);
		this.oval.setColor(ovalColor);
	}
}
