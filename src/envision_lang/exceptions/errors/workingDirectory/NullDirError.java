package envision_lang.exceptions.errors.workingDirectory;

import envision_lang.exceptions.EnvisionLangError;

public class NullDirError extends EnvisionLangError {
	
	public NullDirError() {
		super ("The launch working directory is null!");
	}
	
}
