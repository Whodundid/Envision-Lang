package envision_lang.exceptions.errors.workingDirectory;

import envision_lang._launch.WorkingDirectory;
import envision_lang.exceptions.EnvisionLangError;

public class MultipleMainsError extends EnvisionLangError {
	
	public MultipleMainsError(WorkingDirectory dirIn) {
		super ("Multiple 'main.nvis' files exist within working directory: '" + dirIn.getDirFile() + "'!");
	}
	
}
