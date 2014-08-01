package vad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import robocode.AdvancedRobot;

public class BulletSimulator
{
	private Random				rand	= new Random();
	private double				enemyStartX, enemyStartY;
	private double				bulletStartX, bulletStartY;
	private double				enemyVelX, enemyVelY;
	private double				bulletVel, bulletVelX, bulletVelY;
	private double				bulletHeading;
	private Rectangle			enemyRobot;
	private static Graphics2D	g;

	public BulletSimulator(double bulletSpeed, double absEnemyX, double absEnemyY, double avrEVelX, double avrEVelY, double bulletX, double bulletY, double bulletHeading, double enemyHeadingRads,
			Graphics2D g)
	{
		this.g = g;
		this.bulletVel = bulletSpeed;
		this.enemyStartX = absEnemyX;
		this.enemyStartY = absEnemyY;
		this.enemyVelX = avrEVelX;
		this.enemyVelY = avrEVelY;
		this.bulletHeading = bulletHeading;
		this.bulletStartX = bulletX;
		this.bulletStartY = bulletY;
		this.bulletVelX = Math.sin(Math.toRadians(this.bulletHeading)) * this.bulletVel;
		this.bulletVelY = Math.cos(Math.toRadians(this.bulletHeading)) * this.bulletVel;
		this.enemyRobot = new Rectangle();
	}

	public double[] getFlightTimeSimulation()
	{
		int numSimulations = 0;
		double bulletCurrentX = this.bulletStartX;
		double bulletCurrentY = this.bulletStartY;
		double enemyCurrentX = this.enemyStartX;
		double enemyCurrentY = this.enemyStartY;
		double testBulletHeading = this.bulletHeading;
		int constant = 0;

		while (true)
		{
			int flightTime = 0;
			double testingBulletVelX = Math.sin(Math.toRadians(testBulletHeading)) * this.bulletVel;
			double testingBulletVelY = Math.cos(Math.toRadians(testBulletHeading)) * this.bulletVel;
			enemyRobot.setRect(enemyCurrentX - constant, enemyCurrentY - constant, 36, 36);

			while (true)
			{
				bulletCurrentX += testingBulletVelX;
				bulletCurrentY += testingBulletVelY;
				enemyCurrentX += this.enemyVelX;
				enemyCurrentY += this.enemyVelY;

				enemyRobot.setRect(enemyCurrentX - constant, enemyCurrentY - constant, 36, 36);
				if (g != null)
				{
					g.setColor(Color.cyan);
					g.fillOval((int) bulletCurrentX - 2, (int) bulletCurrentY - 2, 4, 4);
					// g.setColor(new Color(rand.nextInt(255),
					// rand.nextInt(255), rand.nextInt(255)));
					// g.drawRect((int) this.enemyRobot.getX(), (int)
					// this.enemyRobot.getY(), (int) this.enemyRobot.getWidth(),
					// (int) this.enemyRobot.getHeight());
				}
				flightTime++;

				double[] arr = new double[2];
				arr[0] = flightTime;
				arr[1] = testBulletHeading;

				if (this.enemyRobot.contains(bulletCurrentX, bulletCurrentY)) 
				{ 
					if (g != null)
					{
						g.setColor(Color.red);
						g.fillRect((int) enemyCurrentX, (int) bulletCurrentY, (int) this.enemyRobot.getWidth(), (int) this.enemyRobot.getHeight());
					}
					return arr; 
				}
				if (flightTime >= 100) break;
			}
			if (g != null)
			{
				//g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
				//g.fillRect((int) this.enemyRobot.getX(), (int) this.enemyRobot.getY(), (int) this.enemyRobot.getWidth(), (int) this.enemyRobot.getHeight());
			}

			testBulletHeading += .1; // in both directions, -= as well
			numSimulations++;
			if (numSimulations >= 200 || Math.abs(this.bulletHeading - testBulletHeading) > 90) return null;
		}
		// return flightTime;
	}
}
