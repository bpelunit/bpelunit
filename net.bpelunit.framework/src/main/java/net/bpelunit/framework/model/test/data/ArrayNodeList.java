/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Simple NodeList implementation, backed by an ArrayList.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class ArrayNodeList implements NodeList, Iterable<Node> {

    private List<Node> store = new ArrayList<Node>();

    public int getLength() {
        return store.size();
    }

    public Node item(int index) {
        return index < store.size() ? store.get(index) : null;
    }

    /**
     * Adds a Node at the end of the list.
     * 
     * @param n
     *            Node to be added.
     */
    public void add(Node n) {
        store.add(n);
    }

    public Iterator<Node> iterator() {
        return store.iterator();
    }
}
