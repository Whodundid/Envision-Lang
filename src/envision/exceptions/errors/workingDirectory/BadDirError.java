package envision.exceptions.errors.workingDirectory;

import envision.WorkingDirectory;
import envision.exceptions.EnvisionError;

public class BadDirError extends EnvisionError {
	
	public BadDirError(WorkingDirectory dir) {
		super ("The working directory: '" + dir.getDirFile() + "' is not a valid directory!");
	}
	
}
