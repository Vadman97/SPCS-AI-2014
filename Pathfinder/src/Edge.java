import java.awt.Color;

import acm.graphics.GLine;

/**
 * A representation of a graph node.
 * 
 * @author Lekan Wang (lekan@lekanwang.com)
 *
 */
public class Edge {
	
	private Node n1, n2;
	private double distBetween;
	private GLine drawnLine;
	public static Color lineColor = Color.BLACK;
	// TODO You will probably want to add more stuff here.
	
	/**
	 * Creates an edge that connects nodes n1 and n2.
	 */
	public Edge(Node n1, Node n2) 
	{
		System.out.println("Creating edge");
		System.out.println(n1);
		System.out.println(n2);
		
		this.n1 = n1;
		this.n2 = n2;
		
		double distX = n2.getX() - n1.getX();
		double distY = n2.getY() - n1.getY();
		drawnLine = new GLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
		drawnLine.setColor(lineColor);
		
		setDistBetween(Math.sqrt((distX * distX) + (distY * distY)));
	}
	
	/**
	 * Gets both nodes as a Pair<Node> object. You can access
	 * the individual Nodes by saying pair.left, and pair.right
	 * once you have the pair.
	 * 
	 * This method does not guarantee any ordering on the nodes.
	 * Hence, this could be useful when working with undirected
	 * graphs. (*hint hint*)
	 * 
	 * @return
	 */
	public Pair<Node> getNodes() {
		return new Pair<Node>(n1, n2);
	}
	
	/**
	 * Returns the first node. Because this guarantees an order,
	 * it could be useful when working with directed graphs.
	 * 
	 * @return
	 */
	public Node getNode1() {
		if (this.n1 == null)
			System.out.println("NODE 1 IS NULLL!!!!!!");
		return this.n1;
	}
	
	/**
	 * Returns the second node. Because this guarantees an order,
	 * it could be useful when working with directed graphs.
	 * 
	 * @return
	 */
	public Node getNode2() {
		if (this.n2 == null)
			System.out.println("NODE 2 IS NULLL!!!!!!");
		return this.n2;
	}

	public double getDistBetween() {
		return distBetween;
	}

	private void setDistBetween(double distBetween) {
		this.distBetween = distBetween;
	}

	public GLine getDrawnLine() {
		return drawnLine;
	}
}
