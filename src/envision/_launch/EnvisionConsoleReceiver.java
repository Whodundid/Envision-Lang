package envision._launch;

public interface EnvisionConsoleReceiver {

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
