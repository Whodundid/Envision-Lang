package envision.exceptions.errors.workingDirectory;

import envision.WorkingDirectory;
import envision.exceptions.EnvisionError;

public class MultipleMainsError extends EnvisionError {
	
	public MultipleMainsError(WorkingDirectory dirIn) {
		super ("Multiple 'main.nvis' files exist within working directory: '" + dirIn.getDirFile() + "'!");
	}
	
}
