package envision_lang.parser;

import envision_lang.lang.language_errors.EnvisionLangError;

/**
 * Wrapper error to catch any error that is thrown during parsing.
 * 
 * @author Hunter Bragg
 */
public class ParsingError extends EnvisionLangError {
	
    public ParsingError(String reason) {
        super("EnvisionLang Parsing Error! " + reason);
    }
    
	public ParsingError(Exception thrownError) {
	    super("EnvisionLang Parsing Error!", thrownError);
	}
	
	public ParsingError(String reason, Exception thrownError) {
	    super("EnvisionLang Parsing Error! " + reason, thrownError);
	}
	
}
