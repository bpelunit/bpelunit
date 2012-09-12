package net.bpelunit.framework.coverage.output;

import java.io.IOException;

import net.bpelunit.framework.coverage.result.ICoverageDocument;

public interface ICoverageOutputter {

	public @interface CoverageOutputOption {
		String description() default "";
	}
	
	void exportCoverageInformation(ICoverageDocument doc) throws IOException;
	
}
