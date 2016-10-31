package net.bpelunit.utils.bpelstats.languagestats;

import java.io.File;

public class Import {
	File location;
	String importType;
	
	@Override
	public String toString() {
		return importType + "@" + location.getAbsolutePath();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(obj == this) {
			return true;
		}
		
		if(obj.getClass() == this.getClass()) {
			Import i = (Import)obj;
			return location.getAbsoluteFile().equals(i.location.getAbsoluteFile());
		} else {
			return false;
		}
	}
}