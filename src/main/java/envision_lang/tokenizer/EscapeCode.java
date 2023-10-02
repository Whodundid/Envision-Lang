package envision_lang.tokenizer;

/**
 * Envision String escape codes.
 * 
 * @author Hunter Bragg
 */
public enum EscapeCode {
	
	NEW_LINE,		// \n
	TAB,			// \t
	SINGLE_QUOTE,	// \'
	DOUBLE_QUOTE,	// \"
	BACKSLASH,		// \\
	SCOPE_LEFT,		// \{
	SCOPE_RIGHT,	// \}
	;
	
	//--------------
	// Constructors
	//--------------
	
	private EscapeCode() {}
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * Returns an EscapeCode associated with the given char,
	 * or null if no code matches.
	 * 
	 * @param c The char to check against
	 * @return An EscapeCode associated with the given char
	 */
	public static EscapeCode getCode(char c) {
		return switch (c) {
		case 'n' -> NEW_LINE;
		case 't' -> TAB;
		case '\'' -> SINGLE_QUOTE;
		case '"' -> DOUBLE_QUOTE;
		case '\\' -> BACKSLASH;
		case '{' -> SCOPE_LEFT;
		case '}' -> SCOPE_RIGHT;
		default -> null;
		};
	}
	
	/**
	 * Converts the escape code to its string equivalent operation.
	 *  
	 * @param code
	 * @return String the code in string form
	 */
	public static String convertCode(EscapeCode code) {
		return switch (code) {
		case BACKSLASH -> "\\";
		case DOUBLE_QUOTE -> "\"";
		case NEW_LINE -> "\n";
		case SINGLE_QUOTE -> "'";
		case SCOPE_LEFT -> "{";
		case SCOPE_RIGHT -> "}";
		case TAB -> "\t";
		default -> "";
		};
	}
	
}
