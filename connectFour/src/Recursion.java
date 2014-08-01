/** A class to hold static methods about recursion. */
public class Recursion {
  
  /** Example recursion problem:
    * 
    *  factorial(int n) returns n! = n*n-1*...*3*2*1
    *  Precondition: 0 <= n
    *  
    *  NOTE: This method is here to demonstrate recursion.
    *  You do not need to edit this function. It will not be tested.
    */ 
  public static int factorial(int n) {
    /* Every recursion problem needs a base case in order
     * for the recursion to stop. In this case, we know
     * that 0! and 1! both equal 1. */
    if (n <= 1) {
      return 1;
    }
    
    /*Every recursion problem also needs a recursive case. 
     * In this case, the definition is: n! = n*(n-1)!. It is
     * important that it is  n-1  because every recursive
     * case must be working on a smaller subset, working
     * toward termination.
     */
    return n * factorial(n-1);
  }
  
  /* You have to implement two recursion problems.
   * 1. matchTarget in Recursion.java
   * 2. isBST in TreeNode.java
   */
  
  /** Return true iff is it possible to choose a group of ints in nums
    * that sums to t. You may create a helper method if you see fit.
    * For example:
    *     matchTarget({2, 4, 8}, 10) -> true  because 2+8 == 10
    *     matchTarget({2, 4, 8}, 14) -> true  because 2+8+4 == 14
    *     matchTarget({2, 4, 8},  9) -> false because no grouping of these numbers can sum to 9
    * 
    * NOTE: There may be duplicate numbers in nums.
    *       You may only use each number in nums at most once.
    *       (i.e. matchTarget({2},6) -> false despite 2+2+2 == 6
    *         but matchTarget({2, 2, 2}, 6} -> true)
    */
  public static boolean matchTarget(int[] nums, int t) {
    //TODO: Fill in the method matchTarget
    return false;
  }
  
  /** Main method for testing recursion functions.You may write any code here.
    * Or you may use a JUnit test class to test your work. We do not have to
    * see the JUnit class.*/
  public static void main(String[] args) {
    
  }
}




