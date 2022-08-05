package envision_lang.exceptions.errors.file;

import java.io.File;

import envision_lang.exceptions.EnvisionLangError;

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
