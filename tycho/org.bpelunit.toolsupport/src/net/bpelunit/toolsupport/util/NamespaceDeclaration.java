package net.bpelunit.toolsupport.util;

public class NamespaceDeclaration {
	private String fPrefix;
	private String fUrl;

	public NamespaceDeclaration(String prefix, String url) {
		super();
		this.fPrefix = prefix;
		this.fUrl = url;
	}

	public String getPrefix() {
		return this.fPrefix;
	}

	public String getUrl() {
		return this.fUrl;
	}
}
