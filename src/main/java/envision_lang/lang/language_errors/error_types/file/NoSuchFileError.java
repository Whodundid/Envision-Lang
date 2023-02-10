package envision_lang.lang.language_errors.error_types.file;

import java.io.File;

import envision_lang.lang.language_errors.EnvisionLangError;

public class NoSuchFileError extends EnvisionLangError {

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
