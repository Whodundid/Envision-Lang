package envision_lang.lang.exceptions.errors.workingDirectory;

import envision_lang._launch.WorkingDirectory;
import envision_lang.lang.exceptions.EnvisionLangError;

public class NoMainError extends EnvisionLangError {
	
	public NoMainError(WorkingDirectory dirIn) {
		super ("No 'main.nvis' file exists within working directory: '" + dirIn.getDirFile() + "'!");
	}
	
}
