package envision_lang._launch;

import envision_lang.EnvisionLang;

public class EnvisionLangConsoleOutputHandler {

	private EnvisionLang inst;
	
	/** Hide Constructor. */
	public EnvisionLangConsoleOutputHandler(EnvisionLang instance) {
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
