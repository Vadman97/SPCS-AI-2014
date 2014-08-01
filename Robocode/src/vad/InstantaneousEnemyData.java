package vad;

import robocode.ScannedRobotEvent;

public class InstantaneousEnemyData
{
	private int		robotIndex;
	private double	enemyAbsHeading;
	private double	enemyBearing;
	private double	enemyDistance;
	private double	enemyEnergy;
	private String	enemyName;
	private double	enemyVelocity;
	private long	enemyScanTime;
	private long	updateTimeThreshhold	= 1;
	private boolean	hasBeenUpdated			= false;

	public InstantaneousEnemyData(int robotIndex)
	{
		this.robotIndex = robotIndex;
		this.enemyAbsHeading = Double.NaN;
		this.enemyBearing = Double.NaN;
		this.enemyDistance = Double.NaN;
		this.enemyEnergy = Double.NaN;
		this.enemyName = null;
		this.enemyVelocity = Double.NaN;
		this.enemyScanTime = Long.MAX_VALUE;
	}
	public InstantaneousEnemyData(int robotIndex, double heading, double bearing, double distance, double energy, String name, double velocity, long scanTime)
	{
		this.robotIndex = robotIndex;
		this.enemyAbsHeading = heading;
		this.enemyBearing = bearing;
		this.enemyDistance = distance;
		this.enemyEnergy = energy;
		this.enemyName = name;
		this.enemyVelocity = velocity;
		this.enemyScanTime = scanTime;
	}

	public InstantaneousEnemyData(int robotIndex, ScannedRobotEvent e)
	{
		this(robotIndex);
		this.updateData(e);
	}

	public double getEnemyAbsHeading()
	{
		return this.enemyAbsHeading;
	}

	public double getEnemyBearing()
	{
		return this.enemyBearing;
	}

	public double getEnemyDistance()
	{
		return this.enemyDistance;
	}

	public double getEnemyEnergy()
	{
		return this.enemyEnergy;
	}

	public String getEnemyName()
	{
		return this.enemyName;
	}

	public long getEnemyScanTime()
	{
		return this.enemyScanTime;
	}

	public double getEnemyVelocity()
	{
		return this.enemyVelocity;
	}

	public int getRobotIndex()
	{
		return this.robotIndex;
	}

	public long getUpdateTimeThreshhold()
	{
		return this.updateTimeThreshhold;
	}

	public boolean hasRealData()
	{
		return this.hasBeenUpdated;
	}

	public boolean isName(String name)
	{
		return (this.enemyName == name);
	}

	public boolean recentlyUpdated(long currentTime)
	{
		if (Math.abs(currentTime - this.enemyScanTime) < this.updateTimeThreshhold) return true;
		return false;
	}

	public void updateData(ScannedRobotEvent e)
	{
		this.enemyBearing = e.getBearing();
		this.enemyAbsHeading = e.getHeading();
		this.enemyDistance = e.getDistance();
		this.enemyEnergy = e.getEnergy();
		this.enemyName = e.getName();
		this.enemyVelocity = e.getVelocity();
		this.enemyScanTime = e.getTime();
		this.hasBeenUpdated = true;
		//System.out.println("Updated data for enemy: " + this.enemyName);
	}
}
