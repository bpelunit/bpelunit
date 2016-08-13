/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

/**
 * Used to store values by pairs
 * @author Antonio García Domínguez
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {
	private T1 a;
	private T2 b;

        /**
         * Constructor
         * @param a     The value of the first element
         * @param b     The value of the second element
         */
	public Pair(T1 a, T2 b) {
		this.setLeft(a);
		this.setRight(b);
	}

        /**
         * Sets the value of the first element
         * @param a     The new value
         */
	public void setLeft(T1 a) {
		this.a = a;
	}

        /**
         * Returns the value of the first element
         * @return  The current value of the fist element
         */
	public T1 getLeft() {
		return a;
	}

        /**
         * Sets the value of the second element
         * @param b The new value
         */
	public void setRight(T2 b) {
		this.b = b;
	}

        /**
         * Returns the value of the second element
         * @return  The current value of the second element
         */
	public T2 getRight() {
		return b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pair [a=" + a + ", b=" + b + "]";
	}

}
