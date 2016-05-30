package net.bpelunit.utils.bptstool.ui.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bpelunit.utils.bptstool.functions.IFunction;
import net.bpelunit.utils.bptstool.functions.create.CreateFunction;
import net.bpelunit.utils.bptstool.functions.largetestsuite.LargeTestSuiteFunction;

public class Main {
	
	private static final IFunction[] FUNCTIONS = new IFunction[] {
		new CreateFunction(),
		new LargeTestSuiteFunction()
	};
	
	public static void main(String[] args) {
		new Main().run(args);
	}

	private void run(String[] args) {
		if(args.length == 0) {
			helpAndExit();
		}
		
		String toolName = args[0];
		IFunction activeFunction = null;
		for(IFunction f : FUNCTIONS) {
			if(toolName.equals(f.getName())) {
				activeFunction = f;
				break;
			}
		}
		
		List<String> paramsList = new ArrayList<String>(Arrays.asList(args));
		paramsList.remove(0);
		
		activeFunction.execute(paramsList.toArray(new String[paramsList.size()]));
	}

	private void helpAndExit() {
		// TODO Auto-generated method stub
		
	}
	
}
