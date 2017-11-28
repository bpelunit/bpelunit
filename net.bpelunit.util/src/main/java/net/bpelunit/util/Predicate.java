/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

/**
 * Interface for classes that model a boolean predicate taking a single argument of a certain type.
 *
 * @author Antonio García-Domínguez
 */
public interface Predicate<T> {
	boolean evaluate(T x);
}
