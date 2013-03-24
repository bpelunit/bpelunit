package net.bpelunit.framework.control.datasource;

import java.util.HashSet;
import java.util.Set;

import org.apache.velocity.context.Context;

/**
 * Wrapper around a regular Velocity {@link Context} that allows for
 * defining certain variables as read-only, preventing testers from
 * overriding them.
 *
 * @author Antonio García-Domínguez
 */
public final class WrappedContext implements Context {
	private final Context ctx;
	private final Set<String> readOnly = new HashSet<String>();

	public WrappedContext(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * Sets the value of variable <code>key</code> to <code>value</code>
	 * 
	 * @throws IllegalArgumentException
	 *             The caller attempted to overwrite a read-only variable.
	 */
	@Override
	public Object put(String key, Object value) {
		if (readOnly.contains(key)) {
			throw new IllegalArgumentException(String.format(
				"Cannot overwrite %s: it is a read only variable", key));
		}
		return ctx.put(key, value);
	}

	@Override
	public Object get(String key) {
		return ctx.get(key);
	}

	@Override
	public boolean containsKey(Object key) {
		return ctx.containsKey(key);
	}

	@Override
	public Object[] getKeys() {
		return ctx.getKeys();
	}

	@Override
	public Object remove(Object key) {
		return ctx.remove(key);
	}

	/**
	 * Sets the value of variable <code>key</code> to <code>value</code> and
	 * marks it as read-only.
	 * 
	 * @throws IllegalArgumentException
	 *             The caller attempted to overwrite an existing variable.
	 */
	public void putReadOnly(String key, Object value) {
		if (containsKey(key)) {
			throw new IllegalArgumentException(String.format(
				"Cannot overwrite the existing read-write variable %s as a " +
				"read-only variable", key));
		}
		readOnly.add(key);
		ctx.put(key, value);
	}
}