package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.tokenizer.IKeyword;

public class UnsupportedOverloadError extends EnvisionError {
	
	public UnsupportedOverloadError(EnvisionObject in, IKeyword operator) {
		super("Unsupported Operator: [" + in + "': '" + operator.typeString() + "']!");
	}
	
	public UnsupportedOverloadError(EnvisionObject in, IKeyword operator, String other) {
		super("Unsupported Operator! [" + in + ": '" + operator.typeString() + "' and '" + other
				+ "']!");
	}
	
	public UnsupportedOverloadError(EnvisionObject in, IKeyword operator, EnvisionObject other) {
		super("Unsupported Operator! [" + in + ": '" + operator.typeString() + "' and '" + "["
				+ other.getDatatype() + ":" + other + "]]!");
	}
	
}
