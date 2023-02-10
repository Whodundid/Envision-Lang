package envision_lang.lang.natives;

import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;
import eutil.reflection.ObjectVisibility;

public enum EnvisionVisibilityModifier {
	
	PUBLIC("+"),
	PROTECTED("_"),
	PRIVATE("-"),
	/** Assigned by not specifying explicit visibility. */
	SCOPE(""),
	/** Strictly internal. Cannot be explicitly assigned within Envision. */
	RESTRICTED("###_");
	
	public final String lexeme;
	
	private EnvisionVisibilityModifier(String lexemeIn) {
		lexeme = lexemeIn;
	}
	
	public static EnvisionVisibilityModifier parse(Token t) {
		return parse(t.getKeyword());
	}
	
	public static EnvisionVisibilityModifier parse(IKeyword k) {
		if (k.isOperator()) return parse(k.asOperator());
		return null;
	}
	
	public static EnvisionVisibilityModifier parse(Operator k) {
		switch (k) {
		case ADD: return PUBLIC;
		case PROTECTED: return PROTECTED;
		case SUB: return PRIVATE;
		default: return null;
		}
	}
	
	public static EnvisionVisibilityModifier parse(String s) {
		switch (s.toLowerCase()) {
		case "+": return PUBLIC;
		case "_": return PROTECTED;
		case "-": return PRIVATE;
		default: return null;
		}
	}
	
	public static EnvisionVisibilityModifier of(ObjectVisibility v) {
		return switch (v) {
		case PUBLIC -> PUBLIC;
		case PROTECTED -> PROTECTED;
		case PRIVATE -> PRIVATE;
		default -> SCOPE;
		};
	}
	
	@Override
	public String toString() {
		return lexeme;
	}
	
}
