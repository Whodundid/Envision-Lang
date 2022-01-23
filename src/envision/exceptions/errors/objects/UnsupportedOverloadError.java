package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.tokenizer.Keyword;

public class UnsupportedOverloadError extends EnvisionError {
	
	public UnsupportedOverloadError(EnvisionObject in, Keyword operator) {
		super("Unsupported Operator: [" + in + "': '" + operator.chars + "']!");
	}

	public UnsupportedOverloadError(EnvisionObject in, Keyword operator, Object other) {
		super("Unsupported Operator! [" + in + ": '" + operator.chars + "' and '" + other + "']!");
	}
	
}
