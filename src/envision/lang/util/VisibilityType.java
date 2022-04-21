package envision.lang.util;

import envision.tokenizer.IKeyword;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;

public enum VisibilityType {
	
	PUBLIC,
	PROTECTED,
	PRIVATE,
	/** Assigned by not specifying explicit visibility. */
	SCOPE,
	/** Strictly internal. Cannot be explicitly assigned within Envision. */
	RESTRICTED;
	
	public static VisibilityType parse(Token t) {
		return parse(t.keyword);
	}
	
	public static VisibilityType parse(IKeyword k) {
		if (k.isOperator()) return parse(k.asOperator());
		return null;
	}
	
	public static VisibilityType parse(Operator k) {
		switch (k) {
		case ADD: return PUBLIC;
		case PROTECTED: return PROTECTED;
		case SUB: return PRIVATE;
		default: return null;
		}
	}
	
	public static VisibilityType parse(String s) {
		switch (s.toLowerCase()) {
		case "+": return PUBLIC;
		case "_": return PROTECTED;
		case "-": return PRIVATE;
		default: return null;
		}
	}
	
	@Override
	public String toString() {
		return switch (this) {
		case PUBLIC -> "+";
		case PROTECTED -> "_";
		case PRIVATE -> "-";
		case SCOPE -> "";
		default -> "";
		};
	}
	
}
