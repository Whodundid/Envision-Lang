package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.tokenizer.IKeyword;

public class UnsupportedOverloadError extends EnvisionLangError {
	
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
