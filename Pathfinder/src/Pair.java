/**
 * A simple data structure to store pairs of variables of
 * the same type.
 * 
 * @author Lekan Wang (lekan@lekanwang.com)
 *
 * @param <T>
 */
public class Pair<T> {
	/**
	 * The left and right elements. Note these are public,
	 * so the fields are directly accessible.
	 */
	public T left, right;

	/**
	 * Creates a pair with the parameter elements
	 * @param left
	 * @param right
	 */
	public Pair(T left, T right) {
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Gets the left element.
	 * @return
	 */
	public T getLeft() {
		return this.left;
	}

	/**
	 * Gets the right element.
	 * @return
	 */
	public T getRight() {
		return this.right;
	}

	/**
	 * Sets the left element.
	 * @param t
	 */
	public void setLeft(T t) {
		this.left = t;
	}

	/**
	 * Sets the right element.
	 * @param t
	 */
	public void setRight(T t) {
		this.right = t;
	}
}