package envision_lang.lang.language_errors.error_types.workingDirectory;

import envision_lang.lang.language_errors.EnvisionLangError;

public class NullDirError extends EnvisionLangError {
	
	public NullDirError() {
		super ("The launch working directory is null!");
	}
	
}
