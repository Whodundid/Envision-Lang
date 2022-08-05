package envision_lang.tokenizer;

import envision_lang.lang.natives.Primitives;

public interface IKeyword {
	
	default Operator asOperator() { return Operator.class.cast(this); }
	default ReservedWord asReservedWord() { return ReservedWord.class.cast(this); }
	
	default boolean isOperator() { return Operator.class.isInstance(this); }
	default boolean isReservedWord() { return ReservedWord.class.isInstance(this); }
	
	default String typeString() {
		if (isOperator()) return asOperator().typeString;
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
	 * Returns the associated EDataType type of this keyword.
	 * If this keyword is not a datatype, EDataType.NULL is
	 * returned instead.
	 */
	default Primitives getDataType() {
		return Primitives.getDataType(this);
	}
	
}
