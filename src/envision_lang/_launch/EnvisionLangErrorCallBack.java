package envision_lang._launch;

import envision_lang.exceptions.EnvisionLangError;

public interface EnvisionLangErrorCallBack {
	
	/**
	 * Called whenever an EnvisionError is thrown at any point during Envision's
	 * execution. An EnvisionError is indicative of some type of error that occurred
	 * within Envision's logic and not necessarily Java's. EnvisionErrors are
	 * usually thrown as a result of either improper code syntax, language logic,
	 * object creation, or object casting.
	 * 
	 * @param e The EnvisionError that was thrown
	 */
	public void handleError(EnvisionLangError e);

	/**
	 * Called whenever a Java Exception was thrown at any point during Envision's
	 * execution. A thrown Java Exception is indicative of a much more serious logic
	 * error that most likely occurred as an result of either unexpected or invalid
	 * runtime parameters.
	 * 
	 * @param e The Java Exception that was thrown
	 */
	public void handleException(Exception e);
	
}
