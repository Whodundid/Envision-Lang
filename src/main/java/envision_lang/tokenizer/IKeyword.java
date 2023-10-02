package envision_lang.tokenizer;

import envision_lang.lang.natives.Primitives;

public interface IKeyword {
	
	default Operator asOperator() { return (Operator) this; }
	default ReservedWord asReservedWord() { return (ReservedWord) this; }
	
	boolean isOperator();
	boolean isReservedWord();
	
	default String typeString() {
		if (isOperator()) return asOperator().operatorString;
		else return asReservedWord().typeString;
	}
	
	/**
	 * Returns true if this IKeyword is of the specified KeywordType.
	 * 
	 * @param typeIn
	 * @return true if this is the given type
	 */
	boolean hasType(KeywordType typeIn);
	
	/**
	 * Returns true if this IKeyword value is a literal value.
	 */
	default boolean isLiteral() {
		return hasType(KeywordType.LITERAL);
	}
	
	/**
	 * Returns true if this keyword is a datatype.
	 */
	default boolean isDataType() {
		return hasType(KeywordType.DATATYPE);
	}
	
	/**
	 * Returns true if this keyword is a data modifier.
	 */
	default boolean isDataModifier() {
		return hasType(KeywordType.DATA_MODIFIER);
	}
	
	/**
	 * Returns the associated Primitive type of this keyword.
	 * If this keyword is not a primitive type, Java::null is
	 * returned instead.
	 */
	default Primitives getPrimitiveType() {
		return Primitives.getPrimitiveType(this);
	}
	
	/**
	 * Returns true if this keyword is a statement terminator.
	 */
	default boolean isTerminator() {
		return hasType(KeywordType.TERMINATOR);
	}
	
}
