package envision.tokenizer;

/**
 *  A KeywordType refers to the 'category' or 'intended purpose' of
 *  a specific Keyword.
 * 
 *  <p>
 *  Keywords can be assigned multiple KeywordTypes to best express
 *  their intended application.
 * 
 *  <p>
 *  For instance:
 *  <pre>
 *  The Keyword '+' has each of the following types:
 *  
 *  	ARITHMETIC
 *  	OPERATOR
 *  	VISIBILITY_MODIFIER
 * 
 * 	This is because '+' could be used to create each of the given
 * 	statements depending on the statement it forms.
 *  </pre>
 * 
 * @author Hunter
 */
public enum KeywordType {
	LITERAL					(0b00000001),
	DATA_MODIFIER			(0b00000010),
	VISIBILITY_MODIFIER		(0b00000100),
	DATATYPE				(0b00001000),
	ARITHMETIC				(0b00010000),
	ASSIGNMENT				(0b00100000),
	OPERATOR				(0b01000000),
	SEPARATOR				(0b10000000);
	
	public final int byte_val;
	
	private KeywordType(int bIn) {
		byte_val = bIn;
	}
}
