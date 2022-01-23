package envision;

import envision.exceptions.EnvisionError;

public abstract class EnvisionErrorCallback {
	
	public abstract void handleError(EnvisionError e);
	public abstract void handleException(Exception e);
	
}
