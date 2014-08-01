package vad;

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
	public Rectangle						screenBounds	= null;

	// /^^^ make this count robotWidth Height to know where dobot can go/not go

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
				System.out.println("Adding data! " + "Last scan time: " + ed.getLastData(1).get(0).getEnemyScanTime() + " Current time: " + this.getTime());
				ed.addData(e);
			}
		}

		if (this.singleEnemy())
		{
			double radarTurn = (this.getHeadingRadians() + e.getBearingRadians()) - this.getRadarHeadingRadians();
			this.setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
			this.execute();
		}
		this.turnTurretAtEnemy();
		this.execute();
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

	public void turnTurretAtEnemy()
	{
		//System.out.println(this.constEnemyData.get(0).getLastData(1).get(0).getRelativeEnemyX());
		//System.out.println(this.constEnemyData.get(0).getLastData(1).get(0).getRelativeEnemyY());
		double turretTurnRad = 0;
		int numDataSets = 3;
		
		double timeToBulletHit = 1.; /////TODO Calculate bullet flight time
		
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
			double predictedEnemyBearing = 0.;
			double avrEVel = 0.;
			double avrRelX = 0.;
			double avrRelY = 0.;

			for (InstantaneousEnemyData ied : temp)
			{
				avrEBearing += ied.getEnemyBearing();
				avrEHeading += ied.getEnemyAbsHeading();
				avrEVel += ied.getEnemyVelocity();
				avrRelX += ied.getRelativeEnemyX();
				avrRelY += ied.getRelativeEnemyY();
			}
			avrEBearing = avrEBearing / numDataSets;
			avrEHeading = avrEHeading / numDataSets;
			avrEVel = avrEVel / numDataSets; //vel equals position?
			//multiply by predicted bullet flight time
			avrRelX = avrRelX / numDataSets;
			avrRelY = avrRelY / numDataSets;
			double avrEVelX = avrEVel * Math.cos(Math.toRadians(avrEHeading));
			double avrEVelY = avrEVel * Math.sin(Math.toRadians(avrEHeading));
			double predictedX = avrRelX + avrEVelX * timeToBulletHit;
			double predictedY = avrRelY + avrEVelY * timeToBulletHit;
			//double predictedVector = Math.sqrt((predictedX * predictedX) + (predictedY * predictedY));
			
			/*System.out.println(predictedX);
			System.out.println(predictedY);
			System.out.println(predictedVector);
			System.out.println(Math.toDegrees(Math.atan(predictedVector)));*/
			System.out.println(Math.atan(predictedY/predictedX));
			
			predictedEnemyBearing = Utils.normalRelativeAngle(Math.atan(predictedY/predictedX));
					
			//Utils.normalRelativeAngle(Math.toRadians(avrEBearing) + Math.atan(avrEVel));
			avrEBearing = Utils.normalRelativeAngle(Math.toRadians(avrEBearing));
			System.out.println(avrEBearing);
			
			turretTurnRad = (this.getHeadingRadians() + predictedEnemyBearing) - this.getGunHeadingRadians();
		}
		this.setTurnGunRightRadians(turretTurnRad);
	}
}
