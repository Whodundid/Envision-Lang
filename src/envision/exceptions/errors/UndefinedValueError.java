package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.tokenizer.Token;

public class UndefinedValueError extends EnvisionError {
	
	public UndefinedValueError(Token value) {
		super("The value: " + value.lexeme + " is undefined within the current scope!");
	}
	
	public UndefinedValueError(String value) {
		super("The value: " + value + " is undefined within the current scope!");
	}
	
}
