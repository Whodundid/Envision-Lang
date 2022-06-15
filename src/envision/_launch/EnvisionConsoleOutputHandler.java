package envision._launch;

import envision.Envision;

public class EnvisionConsoleOutputHandler {

	private Envision inst;
	
	/** Hide Constructor. */
	public EnvisionConsoleOutputHandler(Envision instance) {
		inst = instance;
	}
	
	public void print(String line) {
		var cr = inst.getConsoleReceiver();
		if (cr != null) cr.onEnvisionPrint(line);
		else System.out.print(line);
	}
	
	public void println(String line) {
		var cr = inst.getConsoleReceiver();
		if (cr != null) cr.onEnvisionPrintln(line);
		else System.out.println(line);
	}
	
}
