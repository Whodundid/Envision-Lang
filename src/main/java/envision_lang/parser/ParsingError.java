package envision_lang.parser;

import envision_lang.exceptions.EnvisionLangError;

/**
 * Wrapper error to catch any error that is thrown during parsing.
 * 
 * @author Hunter Bragg
 */
public class ParsingError extends EnvisionLangError {
	
	public ParsingError(Exception thrownError) {
		super("Parsing Error: " + thrownError.toString());
	}
	
}