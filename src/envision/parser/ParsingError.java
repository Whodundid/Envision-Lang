package envision.parser;

import envision.exceptions.EnvisionError;

/**
 * Wrapper error to catch any error that is thrown during parsing.
 * 
 * @author Hunter Bragg
 */
public class ParsingError extends EnvisionError {
	
	public ParsingError(Exception thrownError) {
		super("Parsing Error: " + thrownError.toString());
	}
	
}
