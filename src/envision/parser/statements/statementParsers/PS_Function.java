package envision.parser.statements.statementParsers;

import static envision.tokenizer.KeywordType.ASSIGNMENT;
import static envision.tokenizer.KeywordType.DATATYPE;
import static envision.tokenizer.KeywordType.OPERATOR;
import static envision.tokenizer.Operator.ARRAY_OP;
import static envision.tokenizer.Operator.ASSIGN;
import static envision.tokenizer.Operator.BRACKET_L;
import static envision.tokenizer.Operator.BRACKET_R;
import static envision.tokenizer.Operator.COMMA;
import static envision.tokenizer.Operator.CURLY_L;
import static envision.tokenizer.Operator.LAMBDA;
import static envision.tokenizer.Operator.PAREN_L;
import static envision.tokenizer.Operator.PAREN_R;
import static envision.tokenizer.Operator.SEMICOLON;
import static envision.tokenizer.Operator.VARARGS;
import static envision.tokenizer.ReservedWord.IDENTIFIER;
import static envision.tokenizer.ReservedWord.INIT;
import static envision.tokenizer.ReservedWord.NEWLINE;
import static envision.tokenizer.ReservedWord.NUMBER;
import static envision.tokenizer.ReservedWord.OPERATOR_;

import envision.lang.util.DataModifier;
import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.expressions.expression_types.Expr_Assign;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_FuncDef;
import envision.parser.util.ParserDeclaration;
import envision.parser.util.StatementParameter;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

/**
 * Attempts to parse a method declaration statement from tokens.
 * 
 * @author Hunter Bragg
 */
public class PS_Function extends GenericParser {
	
	//-------------------------------------------------------------------------------------
	// Function Declaration Parsing
	//-------------------------------------------------------------------------------------
	// These parsing stages attempt to parse a function declaration statement from tokens.
	//-------------------------------------------------------------------------------------
	
	//--------------------------------------------------
	// NOTE:
	//
	// An operator overload function will always only
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
	
	public static Statement functionDeclaration(boolean init, boolean operator, ParserDeclaration declaration) {
		if (declaration == null) declaration = new ParserDeclaration();
		checkDeclaration(declaration);
		
		//variables used to build the function statement
		Token name = null, op = null;
		Token returnType = null;
		boolean constructor = init;
		
		//first check if this function should be handled as an operator overload function
		if (operator) op = getOperator();
		//if constructor, must declare 'init'
		else if (constructor) consume(INIT, "Expected 'init' here!");
		else if (checkType(DATATYPE)) {
			returnType = consumeType(DATATYPE, "Expected a valid function return type!");
			name = consume(IDENTIFIER, "Expected a valid name!");
			declaration.applyReturnType(returnType);
		}
		else {
			name = consume(IDENTIFIER, "Expected a valid name!");
			if (check(IDENTIFIER)) {
				returnType = name;
				consume(IDENTIFIER, "Expected a valid function return name!");
			}
			declaration.applyReturnType(returnType);
		}
		
		//check if local function variable declaration.
		// EX: func f = object.toString
		//if (match )
		
		//internal value used for error outputs
		String funcType = (constructor) ? "initializer" : "function";
		//start parsing for function parameters
		EArrayList<StatementParameter> parameters = getFunctionParameters(operator, funcType);
		//attempt to parse function body
		EArrayList<Statement> body = getFunctionBody(constructor);
		
		return new Stmt_FuncDef(name, op, parameters, body, declaration, constructor, operator);
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
	 * Gathers up all of the declared function parameters including parameter types (if they exist).
	 * 
	 * @param operator : used to determine if this should parse for an operator overload parameter
	 * @param methodType : passed for potential error outputs
	 * @return A list of all parsed method parameters
	 */
	public static EArrayList<StatementParameter> getFunctionParameters() { return getFunctionParameters(false, "method"); }
	public static EArrayList<StatementParameter> getFunctionParameters(boolean operator, String funcType) {
		EArrayList<StatementParameter> parameters = new EArrayList();
		boolean varargs = false;
		
		//consume the '(' token for parameter start
		consume(PAREN_L, "Expected '(' after function name!");
		
		//if the next token is a ')', then there are no parameters
		if (!check(PAREN_R)) {
			Token lastType = null;
			
			//If this is an operator function, only read in one parameter
			if (operator) {
				//read in a parameter type
				if ((checkType(DATATYPE) || check(IDENTIFIER)) && (checkNext(VARARGS) || !checkNext(COMMA, PAREN_R, ASSIGN))) {
					lastType = getAdvance();
				}
				
				//ensure that parameters are valid for an operator overload function
				errorIf(lastType == null, "An operator function must specify a parameter type!");
				errorIf(match(VARARGS), "An operator function cannot take '...' varaiable arguments!");
				
				//get the parameter's name (always required)
				Token paramName = consume(IDENTIFIER, "Expected parameter name!");
				
				//used for direct value assignment if passed value is null
				//ex: var thing(int x = 5) ..
				Expr_Assign assign = null;
				if (matchType(ASSIGNMENT)) {
					assign = new Expr_Assign(paramName, previous().asOperator(), ExpressionParser.parseExpression());
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
			error("Variable arguments '...' must be the last argument in a " + funcType + "!");
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
	public static EArrayList<Statement> getFunctionBody() { return getFunctionBody(false); }
	public static EArrayList<Statement> getFunctionBody(boolean constructor) {
		EArrayList<Statement> body = null;
		
		//consume newlines
		//while (match(NEWLINE));
		
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


