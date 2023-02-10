package envision_lang.parser.util;

import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.KeywordType;
import envision_lang.tokenizer.Token;

/**
 * When parsing for a statement declaration, the first step is to
 * determine the type of declaration the statement will have. This
 * 'DeclarationType' will fast-track further specific statement
 * parsing.
 * 
 * @author Hunter Bragg
 */
public enum DeclarationType {
	CAST_DEF,
	VAR_DEF,
	FUNC_DEF,
	INIT_DEF,
	CLASS_DEF,
	INTERFACE_DEF,
	OPERATOR_DEF,
	ENUM_DEF,
	BLOCK_DEF,
	GETSET,
	EXPR,
	OTHER,
	;
	
	public static DeclarationType parseType(Token t) {
		//check for specific keywords
		IKeyword k = t.getKeyword();
		
		//check if expression
		if (k.hasType(KeywordType.ARITHMETIC) || k.hasType(KeywordType.ASSIGNMENT)) return EXPR;
		
		//check for variable definition
		if (k.isDataModifier() || k.isDataType() || t.isLiteral()) return VAR_DEF;
		
		//otherwise attempt to match exact types
		if (k.isReservedWord()) {
			return switch (k.asReservedWord()) {
			case FUNC -> FUNC_DEF;
			case INIT -> INIT_DEF;
			case CLASS -> CLASS_DEF;
//			case ENUM -> ENUM_DEF;
//			case INTERFACE -> INTERFACE_DEF;
			case OPERATOR_ -> OPERATOR_DEF;
//			case GET, SET -> GETSET;
			default -> OTHER;
			};
		}
		else return OTHER;
	}
}
