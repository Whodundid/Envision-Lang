package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.tokenizer.Token;

public class UndefinedValueError extends EnvisionLangError {
	
	public UndefinedValueError(Token value) {
		super("The value: '" + value.getLexeme() + "' is undefined within the current scope!");
	}
	
	public UndefinedValueError(String value) {
		super("The value: '" + value + "' is undefined within the current scope!");
	}
	
}
