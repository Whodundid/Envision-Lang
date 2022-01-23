package envision.lang.util;

import envision.tokenizer.Keyword;

public enum VisibilityType {
	
	PUBLIC,
	PROTECTED,
	PRIVATE,
	SCOPE,
	/** Permitted to for special cases. IE: a field wrapping a Java object. */
	RESTRICTED;
	
	public static VisibilityType parse(Keyword k) {
		switch (k) {
		case ADD: return PUBLIC;
		case PROTECTED: return PROTECTED;
		case SUBTRACT: return PRIVATE;
		default: return null;
		}
	}
	
	public static VisibilityType parse(String s) {
		switch (s.toLowerCase()) {
		case "public": return PUBLIC;
		case "protected": return PROTECTED;
		case "private": return PRIVATE;
		default: return null;
		}
	}
	
}
