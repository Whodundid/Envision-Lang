package envision.exceptions.errors.workingDirectory;

import envision.exceptions.EnvisionError;

public class NullDirError extends EnvisionError {
	
	public NullDirError() {
		super ("The launch working directory is null!");
	}
	
}
