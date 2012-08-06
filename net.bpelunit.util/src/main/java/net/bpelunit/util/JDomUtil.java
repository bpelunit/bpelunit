package net.bpelunit.util;

import java.util.Iterator;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.ElementFilter;

/**
 * Simple static wrapper methods around non-template returns from JDom.
 * 
 * Used for getting rid of some compile warnings
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 * 
 */
public final class JDomUtil {

	private JDomUtil() {
	}
	
	@SuppressWarnings("unchecked")
	public static Iterator<Element> getDescendants(Element parent, ElementFilter filter) {
		return parent.getDescendants(filter);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Element> getChildren(Element parent, String name, Namespace ns) {
		return parent.getChildren(name, ns);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Element> getChildren(Element parent, String name) {
		return parent.getChildren(name);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Element> getElementsInContent(Element element) {
		return element.getContent(new ContentFilter(
				ContentFilter.ELEMENT));
	}
	
	@SuppressWarnings("unchecked")
	public static List<Comment> getCommentsInContent(Element element) {
		return element.getContent(new ContentFilter(
				ContentFilter.COMMENT));
	}
}
