package envision_lang.lang.util;

import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public enum VisibilityType {
	
	PUBLIC("+"),
	PROTECTED("_"),
	PRIVATE("-"),
	/** Assigned by not specifying explicit visibility. */
	SCOPE(""),
	/** Strictly internal. Cannot be explicitly assigned within Envision. */
	RESTRICTED("###_");
	
	public final String lexeme;
	
	private VisibilityType(String lexemeIn) {
		lexeme = lexemeIn;
	}
	
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
		return lexeme;
	}
	
}
