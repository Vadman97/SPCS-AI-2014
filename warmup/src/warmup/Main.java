package warmup;

import java.util.Random;

public class Main {
	
	public static Random rand = new Random();
	
	public static long gambleOnZero(int maxNumber)
	{
		long counter = 0;
		
		while (true)
		{
			int random = rand.nextInt(maxNumber);
			System.out.println(random);
			if (random == 0)
				return counter;
			counter++;
		}
	}
	
	public static int getRandomBetween(int low, int high)
	{
		return (low + rand.nextInt((high - low)));
	}
	
	public static int triangleNumber(int n)
	{
		int sum = 0;
		for (int i = n; i > 0; i--)
		{
			sum += i;
		}
		return sum;
	}
	
	public static String textTwist(String str, char haltingChar)
	{
		char[] _str = str.toCharArray();
		char[] _str2 = new char[_str.length];
		
		
		for (int i = 0; i < _str.length - 1; i++)
		{
			int random = rand.nextInt(2);
			
			if (_str[i] == haltingChar)
			{
				for (int j = 0; j < i; j++)
				{
					_str2[j] = _str[j];
				}
				return new String(_str2);
			}
			
			if (random == 0)
			{
				char temp = _str[i];
				_str[i] = _str[i+1];
				_str[i+1] = temp;
			}
		}
		return new String(_str);
	}

	public static void main(String[] args) {
		
		System.out.println("Woo SPCS!");		
		System.out.println(getRandomBetween(-5, 5));
		System.out.println("Number of tries: " + gambleOnZero(1000));
		System.out.println("Triangles: " + triangleNumber(5));
		System.out.println("Twisted String Of This: " + textTwist("Twisted String Of This", 's'));
		
		int random = 51 + rand.nextInt(130);
		
		if (random < 90)
		{
			System.out.println("Low blood pressure!");
		}
		else if (random >= 90 && random <= 130)
		{
			System.out.println("All normal!");
		}
		else
		{
			System.out.println("High blood pressure!");
		}
		
		
	}

}
