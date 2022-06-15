package envision.exceptions.errors.workingDirectory;

import envision._launch.WorkingDirectory;
import envision.exceptions.EnvisionError;

public class NoMainError extends EnvisionError {
	
	public NoMainError(WorkingDirectory dirIn) {
		super ("No 'main.nvis' file exists within working directory: '" + dirIn.getDirFile() + "'!");
	}
	
}
