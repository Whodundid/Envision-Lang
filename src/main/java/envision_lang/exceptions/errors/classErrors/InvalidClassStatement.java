package envision_lang.exceptions.errors.classErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.parser.statements.Statement;

public class InvalidClassStatement extends EnvisionLangError {
	
	public InvalidClassStatement(Statement s) {
		super("The statement type of: " + s.getClass() + " is invalid inside of classes!");
	}

}