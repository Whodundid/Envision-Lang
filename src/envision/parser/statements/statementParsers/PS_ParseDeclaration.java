package envision.parser.statements.statementParsers;

import static envision.parser.util.DeclarationType.*;
import static envision.tokenizer.KeywordType.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.exceptions.EnvisionError;
import envision.lang.util.DataModifier;
import envision.lang.util.VisibilityType;
import envision.parser.GenericParser;
import envision.parser.expressions.expression_types.Expr_Generic;
import envision.parser.util.DeclarationType;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_ParseDeclaration extends GenericParser {
	
	/**
	 * Attempts to parse a complete statement declaration from current
	 * tokens. This function is to be used outside of method scopes.
	 * 
	 * <p>
	 * If attempting to parse a scope level variable declaration, use
	 * parseScopeVar instead.
	 * 
	 * @return A valid declaration for a following statement
	 */
	public static ParserDeclaration parseDeclaration() {
		ParserDeclaration dec = new ParserDeclaration();
		
		//collect each piece of the declaration
		parseVisibility(dec);
		
		//handle data modifiers
		parseDataModifiers(dec);
		
		if (check(GET, SET)) 	return dec.setDeclarationType(GETSET);
		if (match(ENUM)) 		return dec.setDeclarationType(ENUM_DEF);
		if (check(CURLY_L)) 	return dec.setDeclarationType(BLOCK_DEF);
		
		//parse generics
		parseGenerics(dec);
		
		//check for appropriate continuing statement
		if (check(INIT)) 				return dec.setDeclarationType(INIT_DEF);
		if (match(FUNC)) 				return dec.setDeclarationType(FUNC_DEF);
		if (match(OPERATOR_))			return dec.setDeclarationType(OPERATOR_DEF);
		if (match(CLASS)) 				return dec.setDeclarationType(CLASS_DEF);
		
		//parse datatype
		parseDataType(dec);
		
		return dec;
	}
	
	/**
	 * Will attempt to only parse meaningful tokens which are directly
	 * related to scope level variable declarations. IE. for loop vars.
	 * 
	 * @return A valid scope variable declaration
	 */
	public static ParserDeclaration parseScopeVar() {
		ParserDeclaration declaration = new ParserDeclaration();
		
		//get declaration type
		parseDataType(declaration);
		
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
		
		Token vis_token = consumeType(VISIBILITY_MODIFIER, "Expected a visibility modifier!");
		VisibilityType visibility = VisibilityType.parse(vis_token);
		
		errorIf(checkType(VISIBILITY_MODIFIER), "Can only have one visibility modifier!");
		
		dec.applyVisibility(visibility);
	}
	
	/**
	 * Attempts to determine what type of statement is about to be parsed.
	 * 
	 * @see DeclarationType
	 */
	public static void parseDataType(ParserDeclaration dec) {
		Token t = current();
		DeclarationType type = DeclarationType.parseType(t);
		
		if (type == VAR_DEF) {
			//check for method calls or class member references
			if (checkNext(PAREN_L, PERIOD)) type = OTHER;
			//check for typeless var assignment
			else if (dec.hasDataMods()) type = VAR_DEF;
			//check for expression calls
			else if (checkNextType(ARITHMETIC)) type = EXPR;
		}
		
		dec.setDeclarationType(type);
		
		//if the keyword is a datatype, immediately set return type
		if (t.keyword.isDataType()) dec.applyReturnType(t);
	}
	
	/**
	 * Parses data modifiers from tokens.
	 * 
	 * <pre>
	 * The recognized list of data modifiers are:
	 * 	static
	 * 	final
	 * 	strong
	 * 	abstract
	 * 	override
	 * </pre>
	 */
	public static void parseDataModifiers(ParserDeclaration dec) {
		//collect modifiers
		EArrayList<DataModifier> modifiers = new EArrayList();
		
		while (checkType(DATA_MODIFIER)) {
			Token mod_token = consumeType(DATA_MODIFIER, "Expected a data modifier!");
			DataModifier m = DataModifier.of(mod_token.keyword);
			modifiers.addIf(m != null, m);
		}
		
		//set modifiers
		dec.applyDataMods(modifiers);
	}
	
	/**
	 * Parses data generics from tokens.
	 */
	public static void parseGenerics(ParserDeclaration dec) {
		EArrayList<Expr_Generic> generics = new EArrayList();
		
		if (check(LT)) {
			consume(LT, "Expceted '<' for generic declaration start!");
			if (!check(GT)) {
				do {
					Token generic = consume(IDENTIFIER, "Expected generic type!");
					Token extension = null;
					if (match(COLON)) {
						extension = getAdvance();
					}
					generics.add(new Expr_Generic(generic, extension));
				}
				while (match(COMMA));
			}
		}
		
		dec.applyGenerics(generics);
	}
	
	public static void parseReturnType(ParserDeclaration dec) {
		Token t = consume(IDENTIFIER, "Expected a name identifier!");
		dec.applyReturnType(t);
	}
	
}
