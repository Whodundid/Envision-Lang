package envision.exceptions.errors.classErrors;

import envision.exceptions.EnvisionError;
import envision.parser.statements.Statement;

public class InvalidClassStatement extends EnvisionError {
	
	public InvalidClassStatement(Statement s) {
		super("The statement type of: " + s.getClass() + " is invalid inside of classes!");
	}

}