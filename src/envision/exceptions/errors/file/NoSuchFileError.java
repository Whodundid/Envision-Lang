package envision.exceptions.errors.file;

import envision.exceptions.EnvisionError;
import java.io.File;

public class NoSuchFileError extends EnvisionError {

	public NoSuchFileError(File f) {
		super("The file: " + f + ": can not be found!");
	}
	
	public NoSuchFileError(String s) {
		super(s);
	}
	
	public NoSuchFileError(Exception e) {
		super(e);
	}
	
}
