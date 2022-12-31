package envision_lang._launch;

import envision_lang.EnvisionLang;

public class EnvisionLangConsoleOutputHandler {

	private EnvisionLang inst;
	
	/** Hide Constructor. */
	public EnvisionLangConsoleOutputHandler(EnvisionLang instance) {
		inst = instance;
	}
	
	public void print(Object line) {
		String lineString = String.valueOf(line);
		var cr = inst.getConsoleReceiver();
		if (cr != null) cr.onEnvisionPrint(lineString);
		else System.out.print(lineString);
	}
	
	public void println(Object line) {
		String lineString = String.valueOf(line);
		var cr = inst.getConsoleReceiver();
		if (cr != null) cr.onEnvisionPrintln(lineString);
		else System.out.println(lineString);
	}
	
}
