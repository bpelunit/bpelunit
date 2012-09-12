package net.bpelunit.utils.bptstool.ui.command;

public class Main {
	
	public static void main(String[] args) {
		new Main().run(args);
	}

	private void run(String[] args) {
		if(args.length == 0) {
			helpAndExit();
		}
		
		String toolName = args[0];
		
	}

	private void helpAndExit() {
		// TODO Auto-generated method stub
		
	}
	
}
