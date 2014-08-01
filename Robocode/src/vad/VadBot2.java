package vad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import robocode.AdvancedRobot;
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

public class VadBot2 extends AdvancedRobot
{
	public Random				rand				= new Random();
	public int					numOtherPlayers		= 0;
	public int					safetyWallDist		= 30;
	public double				fieldHeight			= Double.NaN;
	public double				fieldWidth			= Double.NaN;
	public double				robotWidth			= Double.NaN;
	public double				robotHeight			= Double.NaN;
	public double				firePower			= 3;
	public double				bulletSpeed			= 20 - (3 * this.firePower);
	public long					lastTurnScanned		= 0;
	public int					incrementMoveDist	= 20 + this.rand.nextInt(20);
	public Rectangle			screenBounds		= null;
	public static Graphics2D	paintGraphics		= null;
	public boolean				movingForward		= true;
	public ContinuousEnemyData	eData				= null;

	/**
	 * doFiring: Runs all aiming and shooting related calculations, then shoots.
	 */
	public void doFiring(int numDataIterations, ScannedRobotEvent e)
	{
		InstantaneousEnemyData avrData = this.eData.getAverageData(numDataIterations);
		double enemyHeading = avrData.getEnemyAbsHeading();
		double enemyVelX = avrData.getEnemyVelocity() * Math.sin(Math.toRadians(enemyHeading));
		double enemyVelY = avrData.getEnemyVelocity() * Math.cos(Math.toRadians(enemyHeading));

		double gunTurnDeg = Utils.normalRelativeAngleDegrees((this.getHeading() + avrData.getEnemyBearing()) - this.getGunHeading());

		//System.out.println("eVelX: " + enemyVelX);
		//System.out.println("eVelY: " + enemyVelY);

		this.setTurnGunRight(gunTurnDeg);

		double absEnemyBearing = avrData.getEnemyBearing() + this.getHeading();
		double enx = Math.sin(Math.toRadians(absEnemyBearing)) * avrData.getEnemyDistance();
		double eny = Math.cos(Math.toRadians(absEnemyBearing)) * avrData.getEnemyDistance();
		double absEnemyX = this.getX() + enx;
		double absEnemyY = this.getY() + eny;

		if (VadBot2.paintGraphics != null)
		{
			// VadBot2.paintGraphics.setColor(Color.green);
			// VadBot2.paintGraphics.fillRect((int)(absEnemyX - 15),
			// (int)(absEnemyY - 15), 30, 30);
		}

		BulletSimulator bs = new BulletSimulator(this.bulletSpeed, absEnemyX, absEnemyY, enemyVelX, enemyVelY, this.getX(), this.getY(), this.getGunHeading(), this.getHeadingRadians(),
				VadBot2.paintGraphics);
		double[] temp = bs.getFlightTimeSimulation();
		
		if (temp != null)
		{
			System.out.println("Bullet flight time: " + temp[0]);
			double gunTurnDegPredicted = Utils.normalRelativeAngleDegrees(temp[1] - this.getGunHeading());
			this.turnGunRight(gunTurnDegPredicted);
			this.fire(firePower);
		}
		//System.out.println("eAbsX: " + absEnemyX);
		//System.out.println("eAbsY: " + absEnemyY);
		// this.setFire(firePower);
	}

	/**
	 * doMovement: Does all movement/gun motion calculations and
	 */
	public void doMovement(int numDataIterations, ScannedRobotEvent e)
	{
		if (this.getX() <= this.screenBounds.getMinX())
		{
			if (this.getHeading() >= 270)
			{
				this.setTurnRight(450 - this.getHeading());
				if (!Utils.isNear(450 - this.getHeading(), 0))
					return;
				this.setAhead(100 + this.rand.nextInt(200));
			} else if (this.getHeading() >= 180)
			{
				this.setTurnLeft(this.getHeading() - 90);
				if (!Utils.isNear(this.getHeading() - 90, 0))
					return;
				this.setAhead(100 + this.rand.nextInt(200));
			}
		}
		if (this.getX() >= this.screenBounds.width)
		{
			if (this.getHeading() >= 90)
			{
				this.turnRight(270 - this.getHeading());
				this.setAhead(100 + this.rand.nextInt(200));
			} else if (this.getHeading() >= 0)
			{
				this.turnLeft(90 + this.getHeading());
				this.setAhead(100 + this.rand.nextInt(200));
			}
		}
		if (this.getY() <= this.screenBounds.getMinY())
		{
			if (this.getHeading() >= 180)
			{
				this.turnRight(360 - this.getHeading());
				this.setAhead(50 + this.rand.nextInt(200));
			} else if (this.getHeading() >= 90)
			{
				this.turnLeft(this.getHeading());
				this.setAhead(50 + this.rand.nextInt(200));
			}
		}
		if (this.getY() >= this.screenBounds.height)
		{
			if (this.getHeading() >= 270)
			{
				this.turnLeft(this.getHeading() - 180);
				this.setAhead(50 + this.rand.nextInt(200));
			} else if (this.getHeading() >= 0)
			{
				this.turnRight(180 - this.getHeading());
				this.setAhead(50 + this.rand.nextInt(200));
			}
		}
		
		double robotTurnDeg = Utils.normalRelativeAngleDegrees(this.eData.getAverageData(numDataIterations).getEnemyBearing() + 90);

		if (robotTurnDeg >= 0)
		{
			this.setTurnRight(robotTurnDeg);
		} else
		{
			this.setTurnLeft(-robotTurnDeg);
		}

		if (this.movingForward)
		{
			this.setAhead(this.incrementMoveDist);
		} else
		{
			this.setBack(this.incrementMoveDist);
		}
	}

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

	}

	@Override
	public void onPaint(Graphics2D g)
	{
		if (VadBot2.paintGraphics == null)
		{
			VadBot2.paintGraphics = g;
		}

	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e)
	{
		long startTime = System.nanoTime();
		int numIterations = 1;
		double absEnemyBearing = e.getBearingRadians() + this.getHeadingRadians();

		if (this.eData == null)
		{
			this.eData = new ContinuousEnemyData(0, e.getName());
		}
		this.eData.addData(e);

		this.doFiring(numIterations, e);
		this.doMovement(numIterations, e);

		this.setTurnRadarRightRadians(Utils.normalRelativeAngle(absEnemyBearing - this.getRadarHeadingRadians()) * 2);
		this.execute();

		//System.out.println("Scan calc time millis: " + ((System.nanoTime() - startTime) / 1000000.));
		this.lastTurnScanned = this.getTime();
		this.scan();
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

		this.fieldHeight = this.getBattleFieldHeight();
		this.fieldWidth = this.getBattleFieldWidth();
		this.robotWidth = this.getWidth();
		this.robotHeight = this.getHeight();

		this.screenBounds = new Rectangle((int) this.robotWidth + this.safetyWallDist, (int) this.robotHeight + this.safetyWallDist, (int) (this.fieldWidth - this.robotWidth - this.safetyWallDist),
				(int) (this.fieldHeight - this.robotHeight - this.safetyWallDist));

		this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		this.execute();
		this.scan();
		while (true)
		{
			if (this.getRadarTurnRemainingRadians() == 0)
			{
				this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			}
			this.execute();
		}
	}

	public boolean singleEnemy()
	{
		return (this.numOtherPlayers == 1);
	}
}
