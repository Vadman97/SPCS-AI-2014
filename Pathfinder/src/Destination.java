import java.awt.Color;



public class Destination extends Node
{
	private String name;
	public static Color defaultColor = Color.GREEN;
	public static Color highlightedColor = Color.CYAN;
	
	public Destination(double x, double y, String name, Color ovalColor)
	{
		super(x, y, ovalColor);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
