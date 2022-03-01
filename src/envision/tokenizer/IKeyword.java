package envision.tokenizer;

import envision.lang.util.Primitives;

public interface IKeyword {
	
	default Operator asOperator() { return Operator.class.cast(this); }
	default ReservedWord asReservedWord() { return ReservedWord.class.cast(this); }
	
	default boolean isOperator() { return Operator.class.isInstance(this); }
	default boolean isReservedWord() { return ReservedWord.class.isInstance(this); }
	
	default String chars() {
		if (isOperator()) return asOperator().chars;
		else return asReservedWord().chars;
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
	 * Returns the associated EDataType type of this keyword.
	 * If this keyword is not a datatype, EDataType.NULL is
	 * returned instead.
	 */
	default Primitives getDataType() {
		return Primitives.getDataType(this);
	}
	
}
