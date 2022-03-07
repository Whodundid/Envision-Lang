package envision.tokenizer;

/**
 * Envision String escape codes.
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
		switch (c) {
		case ('n'): return NEW_LINE;
		case ('t'): return TAB;
		case ('\''): return SINGLE_QUOTE;
		case ('"'): return DOUBLE_QUOTE;
		case ('\\'): return BACKSLASH;
		case ('{'): return SCOPE_LEFT;
		case ('}'): return SCOPE_RIGHT;
		}
		return null;
	}
	
	/**
	 * Converts the escape code to its string equivalent operation.
	 *  
	 * @param code
	 * @return String the code in string form
	 */
	public static String convertCode(EscapeCode code) {
		switch (code) {
		case BACKSLASH: return "\\";
		case DOUBLE_QUOTE: return "\"";
		case NEW_LINE: return "\n";
		case SINGLE_QUOTE: return "'";
		case SCOPE_LEFT: return "{";
		case SCOPE_RIGHT: return "}";
		case TAB: return "\t";
		}
		return "";
	}
	
}
