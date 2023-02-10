package envision_lang._launch;

import envision_lang.EnvisionLang;

public class EnvisionConsoleHandler {
	
	/** Hide Constructor. */
	private EnvisionConsoleHandler() {}
	
	public static void print(Object line) {
		String lineString = String.valueOf(line);
		var cr = EnvisionLang.getConsoleReceiver();
		if (cr != null) cr.onEnvisionPrint(lineString);
		else System.out.print(lineString);
	}
	
	public static void println(Object line) {
		String lineString = String.valueOf(line);
		var cr = EnvisionLang.getConsoleReceiver();
		if (cr != null) cr.onEnvisionPrintln(lineString);
		else System.out.println(lineString);
	}
	
}
