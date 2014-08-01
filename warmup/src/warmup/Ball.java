package warmup;

import java.awt.Color;
import java.awt.Rectangle;

public class Ball 
{
	public double x, y;
	public double velX, velY;
	public Color bCol = Color.BLUE;
	
	public Ball(double x, double y, double velX, double velY)
	{
		this.x = (double)x;
		this.y = (double)y;
		this. velX = (double)velX;
		this.velY = (double)velY;
	}
	
	public Ball()
	{
		x = 0.;
		y = 0.;
		velX = 0.;
		velY = 0.;
	}
	
	public void simulate(double accX, double accY, double timeDelta)
	{
		//accel is distance / sec ^ 2
		velX += accX * timeDelta;
		velY += accY * timeDelta;
		
		x += velX * timeDelta;
		y += velY * timeDelta;
	}
	
	public void simulate(double accX, double accY, double timeDelta, Rectangle bounds)
	{
		//accel is distance / sec ^ 2
		velX += accX * timeDelta;
		velY += accY * timeDelta;
		
		if (y >= bounds.getMaxY())
		{
			velY = - velY;
			y = bounds.getMaxY() - 1.;
		}
		if (x >= bounds.getMaxX())
		{
			velX = - velX;
			x = bounds.getMaxX() - 1.;
		}
		if (y <= 0.)
		{
			velY = - velY;
			y = 1.;
		}
		if (x <= 0.)
		{
			velX = - velX;
			x = 1.;
		}
		
		x += velX * timeDelta;
		y += velY * timeDelta;
	}

}
