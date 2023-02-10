package envision_lang.lang.language_errors.error_types.classErrors;

import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.parser.statements.ParsedStatement;

public class InvalidClassStatement extends EnvisionLangError {
	
	public InvalidClassStatement(ParsedStatement s) {
		super("The statement type of: " + s.getClass() + " is invalid inside of classes!");
	}

}