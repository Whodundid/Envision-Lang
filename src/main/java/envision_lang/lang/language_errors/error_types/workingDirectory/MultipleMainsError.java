package envision_lang.lang.language_errors.error_types.workingDirectory;

import envision_lang._launch.WorkingDirectory;
import envision_lang.lang.language_errors.EnvisionLangError;

public class MultipleMainsError extends EnvisionLangError {
	
	public MultipleMainsError(WorkingDirectory dirIn) {
		super ("Multiple 'main.nvis' files exist within working directory: '" + dirIn.getDirFile() + "'!");
	}
	
}
