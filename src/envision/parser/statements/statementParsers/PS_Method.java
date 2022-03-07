package envision.parser.statements.statementParsers;

import static envision.tokenizer.KeywordType.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.lang.util.data.DataModifier;
import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.expressions.expression_types.AssignExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.MethodDeclarationStatement;
import envision.parser.util.ParserDeclaration;
import envision.parser.util.StatementParameter;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import main.Experimental_Envision;

/**
 * Attempts to parse a method declaration statement from tokens.
 * 
 * @author Hunter Bragg
 */
public class PS_Method extends GenericParser {
	
	//-----------------------------------------------------------------------------------
	// Method Declaration Parsing
	//-----------------------------------------------------------------------------------
	// These parsing stages attempt to parse a method declaration statement from tokens.
	//-----------------------------------------------------------------------------------
	
	//--------------------------------------------------
	// NOTE:
	//
	// An operator overload method will always only
	// take in a single parameter for which to perform
	// it's operation against. Furthermore, an operator
	// method MUST specify a static parameter type. As
	// such, generic parameter types are not permitted.
	//
	//--------------------------------------------------
	// Envision Code Example:
	//
	// This code will demonstrate adding a '+' operator
	// overload to 'Thing' objects. This code results
	// in a new Thing object being created from adding
	// B's value and A's value together.
	//
	//--------------------------------------------------
	//
	// +class Thing {
	//     -int value
	// 	   +init(value)
	//
	// 	   +operator +(Thing other) -> Thing(other.value + value)
	// }
	//
	// A = Thing(10)
	// B = Thing(25)
	//
	// C = A + B
	//
	// println(C.value) -- 'prints 35'
	//
	//--------------------------------------------------
	
	//--------------------------------------------------
	// NOTE:
	//
	// A class constructor does not necessarily need to
	// specify a parameter type due to potential
	// immediate variable assignment.
	//
	//--------------------------------------------------
	// Envision Code Example:
	//
	// This example will automatically initalize the
	// values of a, b and c when they are passed to
	// Example's init.
	//
	//--------------------------------------------------
	//
	// +class Example {
	//      -int a, b, c
	//		+init(a, b, c)
	// }
	//
	//--------------------------------------------------
	
	public static Statement methodDeclaration(boolean init, boolean operator, ParserDeclaration declaration) {
		if (declaration == null) declaration = new ParserDeclaration();
		checkDeclaration(declaration);
		
		//variables used to build the method statement
		Token name = null, op = null;
		boolean constructor = false;
		
		//first check if this method should be handled as an operator overload method
		if (operator) op = getOperator();
		else if (init) constructor = true;
		else name = consume(IDENTIFIER, "Expected a valid name!");
		
		/*
		//if it's not an initializer and it's not an operator then check to see if it could be a variable instead
		if (!operator && !init) {
			if (check(LESS_THAN, COMMA, SEMICOLON, NEWLINE, EOF) || checkType(ASSIGNMENT) || checkType(OPERATOR)) {
				return variableDeclaration(declaration);
			}
		}
		*/
		
		//internal value used for error outputs
		String methodType = (constructor) ? "initializer" : "function";
		//start parsing for method parameters
		EArrayList<StatementParameter> parameters = getMethodParameters(operator, methodType);
		//attempt to parse method body
		EArrayList<Statement> body = getMethodBody(constructor);
		
		return new MethodDeclarationStatement(name, op, parameters, body, declaration, constructor, operator);
	}
	
	
	
	
	//-----------------------
	// Static Helper Methods
	//-----------------------
	
	
	
	
	/**
	 * Ensures that the given declaration does not contain invalid data modifiers.
	 * 
	 * @param declaration : the declaration being checked
	 */
	private static void checkDeclaration(ParserDeclaration declaration) {
		//check for invalid variable data modifiers
		if (!DataModifier.checkMethod(declaration.getMods())) {
			error("Invalid method data modifiers in '" + declaration.getMods() + "'!");
		}
	}
	
	/**
	 * Attempts to parse an operator token from the given tokens.
	 * 
	 * @return The operator Token
	 */
	private static Token getOperator() {
		if (match(BRACKET_L)) {
			errorIf(!match(BRACKET_R), "Expected an operator!");
			return Token.create(ARRAY_OP, "[]", current().line);
		}
		else if (match(NUMBER)) {
			return Token.create(ReservedWord.NUMBER, current());
		}
		else {
			return consumeType(OPERATOR, "Expected an operator!");
		}
	}
	
	/**
	 * Checks if the method name matches an existing class name. If so this is a constructor for the given class.
	 * This can only possibly return true if the parser is currently parsing a class declaration statement.
	 * 
	 * @param nameIn : the name token being comapred
	 * @param declaration : used to check for invalid modifiers
	 * @return True if this is a constructor
	 */
	private static boolean checkConstructor(Token nameIn, ParserDeclaration declaration) {
		/*
		if (ParserStage.curClassName != null) {
			// if the names match, then this is a constructor
			if (ParserStage.curClassName.lexeme.equals(nameIn.lexeme)) {
				//error if the constructor is static, final, or overrided -- 'logically doesn't make any sense'
				if (declaration.isStatic()) { setPrevious(); error("Constructors cannot be static!"); }
				if (declaration.isFinal()) { setPrevious(); error("'Constructors cannot be final!"); }
				if (declaration.isOverriding()) { setPrevious(); error("'Constructors cannot override other constructors!"); }
				
				// return true because this is a constructor
				return true;
			}
		}
		*/
		//System.out.println("con name: " + current());
		
		return false;
	}
	
	/**
	 * Checks if this method is a modular method declaration.
	 * If so, the modular association values are gathered and are stored within the parserStage staticly.
	 * 
	 * @return True if this is a modular method declaration
	 */
	@Experimental_Envision
	private static boolean checkModular() {
		/*
		if (match(BRACKET_LEFT)) {
			//ParserStage.modularValues = new BoxHolder();
			
			do {
				Token assName = consume(IDENTIFIER, "Expected a valid modular association name!");
				consume(COLON, "Expected a ':' to indicate an association!");
				Token assRef = null;
				if (check(IDENTIFIER)) assRef = consume(IDENTIFIER, "Expected a valid modular association reference!");
				if (checkType(OPERATOR)) assRef = consumeType(OPERATOR, "Expected a valid modular association reference!");
				if (assRef == null) error("Invalid modular association reference: '" + current() + "'! Expected either an identifier or an operator!");
				
				//ParserStage.modularValues.add(assName, assRef);
			}
			while (match(COMMA));
			
			consume(BRACKET_RIGHT, "Expected a ']' to conclude modular associations!");
			return true;
		}
		*/
		return false;
	}
	
	/**
	 * Gathers up all of the declared method parameters including parameter types (if they exist).
	 * 
	 * @param operator : used to determine if this should parse for an operator overload parameter
	 * @param methodType : passed for potential error outputs
	 * @return A list of all parsed method parameters
	 */
	public static EArrayList<StatementParameter> getMethodParameters() { return getMethodParameters(false, "method"); }
	public static EArrayList<StatementParameter> getMethodParameters(boolean operator, String methodType) {
		EArrayList<StatementParameter> parameters = new EArrayList();
		boolean varargs = false;
		
		//consume the '(' token for parameter start
		consume(PAREN_L, "Expected '(' after method name!");
		
		//if the next token is a ')', then there are no parameters
		if (!check(PAREN_R)) {
			Token lastType = null;
			
			//If this is an operator method, only read in one parameter
			if (operator) {
				//read in a parameter type
				if ((checkType(DATATYPE) || check(IDENTIFIER)) && (checkNext(VARARGS) || !checkNext(COMMA, PAREN_R, ASSIGN))) {
					lastType = getAdvance();
				}
				
				//ensure that parameters are valid for an operator overload method
				errorIf(lastType == null, "An operator method must specify a parameter type!");
				errorIf(match(VARARGS), "An operator method cannot take '...' varaiable arguments!");
				
				//get the parameter's name (always required)
				Token paramName = consume(IDENTIFIER, "Expected parameter name!");
				
				//used for direct value assignment if passed value is null
				//ex: var thing(int x = 5) ..
				AssignExpression assign = null;
				if (matchType(ASSIGNMENT)) {
					assign = new AssignExpression(paramName, previous().asOperator(), ExpressionParser.parseExpression());
				}
				
				//build and add the parameter
				parameters.add(new StatementParameter(lastType, paramName, assign, varargs));
			}
			else {
				//parse each parameter
				do {
					//restrict parameter length
					errorIf(parameters.size() >= 255, "Can't have more than 255 parameters!");
					
					//if there is no type associated with the current parameter, use the last one (if there is one)
					if ((checkType(DATATYPE) || check(IDENTIFIER, OPERATOR_)) && (checkNext(VARARGS) || !checkNext(COMMA, PAREN_R, ASSIGN))) {
						lastType = current();
						advance();
					}
					
					if (match(VARARGS)) varargs = true;
					Token paramName = consume(IDENTIFIER, "Expected parameter name!");
					
					Expression assign = null;
					if (matchType(ASSIGNMENT)) {
						assign = ExpressionParser.parseExpression();
					}
					
					StatementParameter param = new StatementParameter(lastType, paramName, assign, varargs);
					parameters.add(param);
					
					//break if varargs
					if (varargs) break;
				}
				while (match(COMMA));
			}
		}
		
		if (varargs && match(COMMA)) {
			error("Variable arguments '...' must be the last argument in a " + methodType + "!");
		}
		
		consume(PAREN_R, "Expected ')' after parameters!");
		return parameters;
	}
	
	/**
	 * Gathers all method body statements.
	 * 
	 * @param constructor : don't necessarily have a body
	 * @return EArrayList<Statement> : list of all parsed method body statements
	 */
	public static EArrayList<Statement> getMethodBody() { return getMethodBody(false); }
	public static EArrayList<Statement> getMethodBody(boolean constructor) {
		EArrayList<Statement> body = null;
		
		//constructors do not necessarily need to specify a body
		if (!constructor) {
			if (match(LAMBDA)) {
				body = new EArrayList<Statement>();
				body.add(PS_Return.returnStatement());
			}
			else if (match(CURLY_L)) {
				body = getBlock(true);
			}
			else {
				(body = new EArrayList<Statement>()).addIfNotNull(declaration());
			}
		}
		else {
			if (match(LAMBDA)) {
				body = new EArrayList<Statement>();
				body.add(PS_Return.returnStatement());
			}
			else if (match(CURLY_L)) body = getBlock(true);
			else errorIf(!match(SEMICOLON, NEWLINE), "Constructor declaration must be concluded with either a ';' or a new line!");
		}
		
		return body;
	}
	
}





/*
 * From 'public static Statement methodDeclaration(boolean operator, ParserDeclaration declaration) {'
 * 
System.out.println(declaration);

//variables used to build the method statement
Token name = null, op = null;
boolean constructor = false;

//first check if this method should be handled as an operator overload method
if (operator) {
	op = getOperator();
}
else {
	if (check(MODULAR_VALUE)) name = consume(MODULAR_VALUE, "Expected a '@' to denote modular naming!");
	else name = consume(IDENTIFIER, "Expected a valid name!");
	//check if constructor
	constructor = checkConstructor(name, declaration);
}

//if it's not a constructor and it's not an operator then check to see if it could be a variable instead
if (!operator && !constructor) {
	if (check(LESS_THAN, COMMA, SEMICOLON, NEWLINE, EOF) || checkType(ASSIGNMENT) || checkType(OPERATOR)) {
		return varDeclaration(name, declaration);
	}
}

//internal value used for error outputs
String methodType = (constructor) ? "constructor" : "method";
//determine if this is a modular function declaration
@Experimental_Envision
boolean modular = checkModular();
//start parsing for method parameters
EArrayList<StatementParameter> parameters = getMethodParameters(operator, methodType);
//attempt to parse method body
EArrayList<Statement> body = getMethodBody(constructor);

//build the method statement
if (modular) {
	Statement r = new ModularMethodStatement(name, ParserStage.modularValues, parameters, body, declaration);
	ParserStage.modularValues = null; //clear the values from the parser
	return r;
}
return new MethodDeclarationStatement(name, op, parameters, body, declaration, constructor, operator);
*/


