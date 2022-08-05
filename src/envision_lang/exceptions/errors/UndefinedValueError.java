package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.tokenizer.Token;

public class UndefinedValueError extends EnvisionLangError {
	
	public UndefinedValueError(Token value) {
		super("The value: " + value.lexeme + " is undefined within the current scope!");
	}
	
	public UndefinedValueError(String value) {
		super("The value: " + value + " is undefined within the current scope!");
	}
	
}
