/** A representation of a binary tree with the current node as the root.
  *  Invariant: There are no duplicate values in the tree. */
public class TreeNode {
  private int value;
  private TreeNode left; //null if no left child.
  private TreeNode right;//null if no right child.
  
  /** Constructor: A tree with root value v, left subtree left, and right subtree right. */
  public TreeNode(TreeNode left, int v, TreeNode right) {
    this.value= v;
    this.left= left;
    this.right= right;
  }
  
  /** Return true iff this tree is a Binary Search Tree (BST).
    *  A BST has these following properties:
    *  1. The values in the left subtree of every TreeNode in 
    *     this tree are less than the TreeNode's value.
    *  2. The values in the right subtree of every TreeNode in
    *     this tree are greater than the TreeNode's value.
    *  3. The left and right subtree each must also be a binary search tree.
    * 
    *  Precondition: This tree has no duplicate values.
    */
  public boolean isBST() {
    //TODO: implement the function isBST
    return false;
  }
}
