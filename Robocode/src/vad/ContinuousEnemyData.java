package vad;

import java.util.ArrayList;
import java.util.HashMap;

import robocode.ScannedRobotEvent;

public class ContinuousEnemyData
{
	private String										robotName;
	private int											robotIndex;
	private int											dataIndex	= 0;
	private HashMap<Integer, InstantaneousEnemyData>	rollingData;
	private double lastEnergyDelta = 0.;

	public ContinuousEnemyData(int robotIndex)
	{
		this.robotName = null;
		this.robotIndex = robotIndex;
		this.rollingData = new HashMap<Integer, InstantaneousEnemyData>();
	}

	public ContinuousEnemyData(int robotIndex, String robotName)
	{
		this(robotIndex);
		this.robotName = robotName;
	}
	
	public ContinuousEnemyData(String robotName)
	{
		this(-1, robotName);
	}
	
	public boolean recentlyUpdated(long time)
	{
		if (this.rollingData.size() == 0) return false;
		if (rollingData.get(new Integer(rollingData.size() - 1)).recentlyUpdated(time)) return true;
		return false;
	}

	public void addData(ScannedRobotEvent e)
	{
		this.rollingData.put(new Integer(this.dataIndex),
				new InstantaneousEnemyData(this.robotIndex, e));
		this.dataIndex++;
		if (this.rollingData.size() > 1)
			this.lastEnergyDelta = (this.rollingData.get(this.rollingData.size() - 1).getEnemyEnergy() - this.rollingData.get(this.rollingData.size() - 2).getEnemyEnergy())/2;
	}
	public InstantaneousEnemyData getAverageData(int numPastIterations)
	{
		ArrayList<InstantaneousEnemyData> data = this.getLastData(numPastIterations);
		if (data == null) return null;
		
		double avrHeading = 0.;
		double avrBearing = 0.;
		double avrDistance = 0.;
		double avrEnergy = 0.;
		double avrVelocity = 0.;
		int counter = 0;
		long scanTime = data.get(data.size() - 1).getEnemyScanTime();
		
		for (InstantaneousEnemyData ed: data)
		{
			avrHeading += ed.getEnemyAbsHeading();
			avrBearing += ed.getEnemyBearing();
			avrDistance += ed.getEnemyDistance();
			avrEnergy += ed.getEnemyEnergy();
			avrVelocity += ed.getEnemyVelocity();
			counter++;
		}
		return new InstantaneousEnemyData(robotIndex, avrHeading/counter, avrBearing/counter, avrDistance/counter, avrEnergy/counter, robotName, avrVelocity/counter, scanTime);
	}

	public int getDataIndex()
	{
		return this.dataIndex;
	}

	public ArrayList<InstantaneousEnemyData> getLastData(int numOfLatestDataSets)
	{
		if (numOfLatestDataSets > this.rollingData.size()) 
			return null;
		ArrayList<InstantaneousEnemyData> temp = new ArrayList<InstantaneousEnemyData>();
		for (int i = this.rollingData.size() - 1; i > (this.rollingData.size() - numOfLatestDataSets - 1); i--)
			temp.add(this.rollingData.get(new Integer(i)));
		
		return temp;
	}

	public int getRobotIndex()
	{
		return this.robotIndex;
	}

	public String getRobotName()
	{
		return this.robotName;
	}

	public void setRobotIndex(int robotIndex)
	{
		this.robotIndex = robotIndex;
	}

	public void setRobotName(String robotName)
	{
		this.robotName = robotName;
	}

	public double getLastEnergyDelta()
	{
		return lastEnergyDelta;
	}
}
