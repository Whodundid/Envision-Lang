package envision_lang.exceptions.errors.classErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.parser.statements.ParsedStatement;

public class InvalidClassStatement extends EnvisionLangError {
	
	public InvalidClassStatement(ParsedStatement s) {
		super("The statement type of: " + s.getClass() + " is invalid inside of classes!");
	}

}