import java.awt.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


public class Recursion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int sum = 0 ;
		//mnemonicPlease("4376275");
		
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++)
		{
			Integer temp = new Integer(rand.nextInt());
			numbers.add(temp);
			sum += temp;
		}
		System.out.println(canSumTo(numbers, sum));
	}
	
	public static boolean canSumTo(ArrayList<Integer> nums, int target)
	{
		if (Sum(nums) == target)
			return true;
		return false;
	}
	
	public static int Sum(ArrayList<Integer> nums)
    {
		if (nums.size() == 1)
			return nums.get(0);
		else
		{
			int temp = nums.get(0);
			nums.remove(0);
			return temp + Sum(nums);
		}
    }
	
	public static void mnemonicPlease(String str)
	{		
		try {
			recursive("", str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String recursive(String builtUpText, String phoneLeft) throws Exception
	{
		if (phoneLeft.length() == 0)
		{
			System.out.println(builtUpText);
			return null;
		}
		int numAtOne = Integer.parseInt("" + phoneLeft.charAt(0));
		
		switch(numAtOne)
		{
			case 0:
				throw new Exception("INVALID NUMBER");
			case 1:
				throw new Exception("INVALID NUMBER");
			case 2:
				recursive(builtUpText + "A", phoneLeft.substring(1));
				recursive(builtUpText + "B", phoneLeft.substring(1));
				recursive(builtUpText + "C", phoneLeft.substring(1));
				break;
			case 3:
				recursive(builtUpText + "D", phoneLeft.substring(1));
				recursive(builtUpText + "E", phoneLeft.substring(1));
				recursive(builtUpText + "F", phoneLeft.substring(1));
				break;
			case 4:
				recursive(builtUpText + "G", phoneLeft.substring(1));
				recursive(builtUpText + "H", phoneLeft.substring(1));
				recursive(builtUpText + "I", phoneLeft.substring(1));
				break;
			case 5:
				recursive(builtUpText + "J", phoneLeft.substring(1));
				recursive(builtUpText + "K", phoneLeft.substring(1));
				recursive(builtUpText + "L", phoneLeft.substring(1));
				break;
			case 6:
				recursive(builtUpText + "M", phoneLeft.substring(1));
				recursive(builtUpText + "N", phoneLeft.substring(1));
				recursive(builtUpText + "O", phoneLeft.substring(1));
				break;
			case 7:
				recursive(builtUpText + "P", phoneLeft.substring(1));
				recursive(builtUpText + "Q", phoneLeft.substring(1));
				recursive(builtUpText + "R", phoneLeft.substring(1));
				recursive(builtUpText + "S", phoneLeft.substring(1));
				break;
			case 8:
				recursive(builtUpText + "T", phoneLeft.substring(1));
				recursive(builtUpText + "U", phoneLeft.substring(1));
				recursive(builtUpText + "V", phoneLeft.substring(1));
				break;
			case 9: 
				recursive(builtUpText + "W", phoneLeft.substring(1));
				recursive(builtUpText + "X", phoneLeft.substring(1));
				recursive(builtUpText + "Y", phoneLeft.substring(1));
				recursive(builtUpText + "Z", phoneLeft.substring(1));
				break;
		}
		return null;
	}

}
