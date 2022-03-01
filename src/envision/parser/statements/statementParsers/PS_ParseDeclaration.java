package envision.parser.statements.statementParsers;

import static envision.tokenizer.KeywordType.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.exceptions.EnvisionError;
import envision.lang.util.VisibilityType;
import envision.lang.util.data.DataModifier;
import envision.parser.GenericParser;
import envision.parser.expressions.expressions.GenericExpression;
import envision.parser.statements.statementUtil.DeclarationType;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_ParseDeclaration extends GenericParser {
	
	/**
	 * Attempts to parse a complete statement declaration from current tokens.
	 * This function is to be used outside of method scopes.
	 * 
	 * <p>If attempting to parse a scope level variable declaration, use
	 * parseScopeVar instead.
	 * 
	 * @return A valid declaration for a following statement
	 */
	public static ParserDeclaration parseDeclaration() {
		ParserDeclaration declaration = new ParserDeclaration();
		
		//collect each piece of the declaration
		parseVisibility(declaration);
		parseDeclarationType(declaration);
		
		DeclarationType type = declaration.getDeclarationType();
		
		if (type != DeclarationType.OTHER && type != DeclarationType.EXPR) {
			advance();
			parseDataModifiers(declaration);
			parseGenerics(declaration);
		}
		//-- parameters
		
		return declaration;
	}
	
	/**
	 * Will attempt to only parse meaningful tokens which are directly
	 * related to scope level variable declarations.
	 * IE. for loop vars.
	 *  
	 * @return A valid scope variable declaration
	 */
	public static ParserDeclaration parseScopeVar() {
		ParserDeclaration declaration = new ParserDeclaration();
		
		//get declaration type
		parseDeclarationType(declaration);
		
		//ensure it's actually a var dec
		if (declaration.getDeclarationType() != DeclarationType.VAR_DEF)
			throw new EnvisionError("Invalid variable declaration!");
		
		//grab any additional variable values
		advance();
		parseGenerics(declaration);
		
		return declaration;
	}
	
	/**
	 * Attempts to parse a visibility modifer from tokens.
	 */
	public static void parseVisibility(ParserDeclaration dec) {
		if (!checkType(VISIBILITY_MODIFIER)) return;
		
		VisibilityType visibility = VisibilityType.parse(consumeType(VISIBILITY_MODIFIER, "Expected a visibility modifier!"));
		
		errorIf(checkType(VISIBILITY_MODIFIER), "Can only have one visibility modifier!");
		
		dec.applyVisibility(visibility);
	}
	
	/**
	 * Attempts to determine what type of statement is about to be parsed.
	 * @see DeclarationType
	 */
	public static void parseDeclarationType(ParserDeclaration dec) {
		Token t = current();
		DeclarationType type = DeclarationType.parseType(t);
		
		//check for method calls or class member references
		if (type == DeclarationType.VAR_DEF && checkNext(PAREN_L, PERIOD)) type = DeclarationType.OTHER;
		//check for expression calls
		if (type == DeclarationType.VAR_DEF && checkNextType(ARITHMETIC)) type = DeclarationType.EXPR;
		//if (type == DeclarationType.FUNC_DEF) advance();
		
		dec.applyDeclarationType(type);
		
		if (t.keyword.isDataType() || t.isLiteral()) dec.applyReturnType(t);
	}
	
	/**
	 * Parses data modifiers from tokens.
	 * <pre>
	 * The recognized list of data modifiers are:
	 * 	static
	 * 	final
	 * 	strong
	 * 	abstract
	 * 	override</pre>
	 */
	public static void parseDataModifiers(ParserDeclaration dec) {
		//collect modifiers
		EArrayList<DataModifier> modifiers = new EArrayList();
		while (checkType(DATA_MODIFIER)) {
			DataModifier m = DataModifier.of(consumeType(DATA_MODIFIER, "Expected a data modifier!").keyword);
			modifiers.addIf(m != null, m);
		}
		
		//set modifiers
		dec.applyDataMods(modifiers);
	}
	
	/**
	 * Parses data generics from tokens.
	 */
	public static void parseGenerics(ParserDeclaration dec) {
		EArrayList<GenericExpression> generics = new EArrayList();
		
		if (check(LT)) {
			consume(LT, "Expceted '<' for generic declaration start!");
			if (!check(GT)) {
				do {
					Token generic = consume(IDENTIFIER, "Expected generic type!");
					Token extension = null;
					if (match(COLON)) {
						extension = getAdvance();
					}
					generics.add(new GenericExpression(generic, extension));
				}
				while (match(COMMA));
			}
		}
		
		dec.applyGenerics(generics);
	}
	
}
