package envision_lang._launch;

public interface EnvisionLangConsoleReceiver {

	/**
	 * Called whenever executed Envision code is printing to the console.
	 * 
	 * @param line The output line
	 */
	public void onEnvisionPrint(String line);
	
	/**
	 * Called whenever executed Envision code is printline-ing to the console.
	 * 
	 * @param line The output line
	 */
	public void onEnvisionPrintln(String line);
	
}
