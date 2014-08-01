package vad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import robocode.AdvancedRobot;
//import java.awt.Color;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.StatusEvent;
import robocode.util.Utils;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * VadBot - a robot by Vadim Korolik
 * 
 * @author Vadim Korolik
 * @author Help from James
 */
public class VadBot extends AdvancedRobot
{
	public Random							rand			= new Random();
	public int								numOtherPlayers	= 0;
	public ArrayList<ContinuousEnemyData>	constEnemyData	= new ArrayList<ContinuousEnemyData>();
	public double							fieldHeight		= Double.NaN;
	public double							fieldWidth		= Double.NaN;
	public double							robotWidth		= Double.NaN;
	public double							robotHeight		= Double.NaN;
	public double							robotX			= Double.NaN;
	public double							robotY			= Double.NaN;
	public double							firePower		= 3;
	public double							bulletSpeed		= 20 - (3 * this.firePower);
	public Rectangle						screenBounds	= null;
	public static Graphics2D				paintGraphics	= null;

	// /TODO^^^ make this count robotWidth Height to know where dobot can go/not
	// go
	// TODO for some reason, robot leads either too much or too little?or out of
	// sync, check physics/prediction
	@Override
	public void onBulletHit(BulletHitEvent event)
	{

	}

	@Override
	public void onBulletHitBullet(BulletHitBulletEvent event)
	{

	}

	@Override
	public void onBulletMissed(BulletMissedEvent event)
	{

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	@Override
	public void onHitByBullet(HitByBulletEvent e)
	{
		// Replace the next line with any behavior you would like
		this.setTurnRight(35 + this.rand.nextInt(20));
		this.setBack(200);
	}

	@Override
	public void onHitRobot(HitRobotEvent e)
	{
		this.fire(this.firePower);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	@Override
	public void onHitWall(HitWallEvent e)
	{
		// Replace the next line with any behavior you would like
		this.setTurnRight(25);
		this.setBack(100);
	}

	@Override
	public void onPaint(Graphics2D g)
	{
		if (paintGraphics == null)
			paintGraphics = g;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e)
	{
		for (ContinuousEnemyData ed : this.constEnemyData)
		{
			if (ed.getRobotName() == null)
			{
				boolean goingToUpdate = true;
				for (ContinuousEnemyData ed2 : this.constEnemyData)
				{
					System.out.println("Checking if already created such robot");
					if (ed2.getRobotName() == e.getName())
					{
						goingToUpdate = false;
					}
				}
				if (goingToUpdate)
				{
					System.out.println("Found new robot! " + e.getName());
					ed.setRobotName(e.getName());
					ed.addData(e);
				}
			} else
			{
				// System.out.println("Adding data! " + "Last scan time: " +
				// ed.getLastData(1).get(0).getEnemyScanTime() +
				// " Current time: " + this.getTime());
				ed.addData(e);
			}
		}

		if (this.singleEnemy())
		{
			double radarTurn = (this.getHeadingRadians() + e.getBearingRadians()) - this.getRadarHeadingRadians();
			this.setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
			this.execute();
		}
		if (this.getX() <= this.screenBounds.getMinX())
		{
			this.turnRight(45);
			this.ahead(100);
		}
		if (this.getX() >= this.screenBounds.getMaxX())
		{
			this.turnLeft(45);
			this.ahead(100);
		}
		if (this.getY() <= this.screenBounds.getMinY())
		{
			this.turnRight(135);
			this.ahead(100);
		}
		if (this.getY() >= this.screenBounds.getMaxY())
		{
			this.turnLeft(135);
			this.ahead(100);
		}

		this.turnTurretAtEnemy(paintGraphics);
	}

	@Override
	public void onSkippedTurn(SkippedTurnEvent event)
	{
		System.out.println("CRITICAL Skipped turn num: " + event.getSkippedTurn());
	}

	/*
	 * This method is called every turn in a battle round in order to provide
	 * the robot status as a complete snapshot of the robot's current state at
	 * that specific time.
	 */
	@Override
	public void onStatus(StatusEvent e)
	{
	}

	@Override
	public void run()
	{
		this.numOtherPlayers = this.getOthers();
		this.setAdjustRadarForGunTurn(true);
		this.setAdjustRadarForRobotTurn(true);
		this.setAdjustGunForRobotTurn(true);

		this.robotX = this.getX();
		this.robotY = this.getY();
		this.fieldHeight = this.getBattleFieldHeight();
		this.fieldWidth = this.getBattleFieldWidth();
		this.robotWidth = this.getWidth();
		this.robotHeight = this.getHeight();

		this.screenBounds = new Rectangle((int) this.robotWidth, (int) this.robotHeight, (int) (this.fieldWidth - this.robotWidth), (int) (this.fieldHeight - this.robotHeight));

		System.out.println("Num other players: " + this.numOtherPlayers);

		for (int i = 0; i < this.numOtherPlayers; i++)
		{
			this.constEnemyData.add(new ContinuousEnemyData(i));
		}

		this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		this.execute();
		this.scan();
		while (true)
		{
			this.execute();
			for (ContinuousEnemyData ed : this.constEnemyData)
			// in case of scan loss, track again
			{
				if (!ed.recentlyUpdated(2))
				{
					this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
				}
			}
		}
	}

	public boolean singleEnemy()
	{
		return (this.numOtherPlayers == 1);
	}

	public void turnTurretAtEnemy(Graphics2D g)
	{
		double gunTurnAmt = 0.;
		int numDataSets = 1;

		if (this.singleEnemy())
		{
			ArrayList<InstantaneousEnemyData> temp;

			temp = this.constEnemyData.get(0).getLastData(numDataSets);
			if (temp == null)
			{
				System.out.println("Not enough data yet!");
				return;
			}

			double avrEBearing = 0.;
			double avrEHeading = 0.;
			double avrEDistance = 0.;
			double avrEVel = 0.;

			for (InstantaneousEnemyData ied : temp)
			{
				avrEBearing += ied.getEnemyBearing();
				avrEHeading += ied.getEnemyAbsHeading();
				avrEVel += ied.getEnemyVelocity();
				avrEDistance += ied.getEnemyDistance();
			}
			avrEBearing = avrEBearing / numDataSets;
			avrEHeading = avrEHeading / numDataSets;
			avrEDistance = avrEDistance / numDataSets;
			avrEVel = avrEVel / numDataSets;
			avrEBearing = Utils.normalRelativeAngleDegrees(avrEBearing);

			double absB = avrEBearing + this.getHeading();
			double enx = Math.sin(Math.toRadians(absB)) * avrEDistance;
			double eny = Math.cos(Math.toRadians(absB)) * avrEDistance;
			double absEnemyX = this.getX() + enx + 5;
			double absEnemyY = this.getY() + eny + 5;
			if (g != null)
			{
				g.setColor(Color.blue);
				g.fillRect((int)absEnemyX - 20, (int)absEnemyY - 20, 40, 40);
			}

			double avrEVelX = avrEVel * Math.sin(Math.toRadians(avrEHeading));
			double avrEVelY = avrEVel * Math.cos(Math.toRadians(avrEHeading));
			double timeToBulletHit = avrEDistance / this.bulletSpeed;
			// INCORRECT^^^
			System.out.println("Simply calculated time: " + timeToBulletHit);
			BulletSimulator bs = new BulletSimulator(this.bulletSpeed, absEnemyX, absEnemyY, avrEVelX, avrEVelY, this.getX(), this.getY(), this.getGunHeading(), this.getHeadingRadians(), g); // abs
			timeToBulletHit = bs.getFlightTimeSimulation()[0];
			System.out.println("Calculated time: " + timeToBulletHit);
			/*
			 * double x1 = this.getX(); double y1 = this.getY(); double x2 =
			 * absEnemX; double y2 = absEnemY; double term1 = (x1 * x1 - (2* x1
			 * * x2) + x2 * x2 + (y1 - y2) * (y1 - y2) * this.bulletSpeed *
			 * this.bulletSpeed); double term2 = (x1 * x1 * dy * dy); double
			 * term3 = (2 * x1 * (x2 * dy + dx * (y1 - y2)) * dy); double term4
			 * = (x2 * x2 * dy * dy); double term5 = (2 * x2 * dx * (y1-y2) *
			 * dy); double term6 = (dx * dx * (y1 - y2) * (y1 - y2)); double
			 * term7 = (x1 * dx); double term8 = ();
			 * 
			 * double superSwag = (Math.sqrt(term1 - term2 + term3 - term4 -
			 * term5 - term6 - term7 + (x2 * dx) - term8));
			 * 
			 * System.out.println(superSwag);
			 */

			double predictionBulletHitTravelDistanceX = avrEVelX * timeToBulletHit;
			double predictionBulletHitTravelDistanceY = avrEVelY * timeToBulletHit;
			double predictedAbsEnemX = absEnemyX + predictionBulletHitTravelDistanceX;
			double predictedAbsEnemY = absEnemyY + predictionBulletHitTravelDistanceY;
			if (predictedAbsEnemX < 0)
			{
				predictedAbsEnemX = 10;
			}
			if (predictedAbsEnemY < 0)
			{
				predictedAbsEnemY = 10;
			}

			// predictedAbsEnemX = 0;
			// predictedAbsEnemY = 0;

			// double enemyAbsB = Math.toDegrees(Math.atan2(predictedAbsEnemY,
			// predictedAbsEnemX));
			double enemyRelB = Utils.normalRelativeAngleDegrees((90 + this.getHeading()) - (Math.toDegrees(Math.atan2(this.getY() - predictedAbsEnemY, this.getX() - predictedAbsEnemX))));

			gunTurnAmt = Utils.normalRelativeAngleDegrees(-this.getGunHeading() + Math.toDegrees(Math.atan2(predictedAbsEnemX - this.getX(), predictedAbsEnemY - this.getY())));

			System.out.println(avrEVelX);
			System.out.println(avrEVelY);
			System.out.println(absEnemyX);
			System.out.println(absEnemyY);
		}

		/*
		 * if (Math.abs(gunTurnAmt) < 2) { if (this.getGunHeat() == 0)
		 * fireBullet(firePower); }
		 */
		if (gunTurnAmt < 0)
		{
			this.turnGunLeft(-gunTurnAmt);
		} else
		{
			this.turnGunRight(gunTurnAmt);
		}
		if (this.getGunHeat() == 0)
		{
			this.fireBullet(this.firePower);
		}
		if (this.constEnemyData.get(0).getLastEnergyDelta() < 0.)
		{
			this.setTurnRightRadians(this.rand.nextDouble());
			this.setAhead(150);
		}
	}
}
