package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.util.DataModifier;
import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

/**
 * Attempts to parse a method declaration statement from tokens.
 * 
 * @author Hunter Bragg
 */
public class PS_Function extends ParserHead {
	
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
	// This example will automatically initialize the
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
	
	public static ParsedStatement functionDeclaration(boolean init, boolean operator, ParserDeclaration declaration) {
		if (declaration == null) declaration = new ParserDeclaration();
		checkDeclaration(declaration);
		
		//variables used to build the function statement
		Token<?> name = null, op = null;
		Token<?> returnType = null;
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
		EList<StatementParameter> parameters = getFunctionParameters(operator, funcType);
		//attempt to parse function body
		EList<ParsedStatement> body = getFunctionBody(constructor);
		
		return new Stmt_FuncDef(declaration.getStartToken(), name, op, parameters, body, declaration, constructor, operator);
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
	private static Token<?> getOperator() {
		if (match(BRACKET_L)) {
			errorIf(!match(BRACKET_R), "Expected an operator!");
			return Token.create(ARRAY_OP, "[]", current().getLineNum());
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
	public static EList<StatementParameter> getFunctionParameters() { return getFunctionParameters(false, "method"); }
	public static EList<StatementParameter> getFunctionParameters(boolean operator, String funcType) {
		EList<StatementParameter> parameters = EList.newList();
		boolean varargs = false;
		
		//consume the '(' token for parameter start
		consume(PAREN_L, "Expected '(' after function name!");
		
		//if the next token is a ')', then there are no parameters
		if (!check(PAREN_R)) {
 			Token<?> lastType = null;
			
			//If this is an operator function, only read in one parameter
			if (operator) {
				//read in a parameter type
				if ((checkType(DATATYPE) || check(IDENTIFIER)) && (checkNextNL(VARARGS) || !checkNextNL(COMMA, PAREN_R, ASSIGN))) {
					lastType = getAdvance();
				}
				
				//ensure that parameters are valid for an operator overload function
				errorIf(lastType == null, "An operator function must specify a parameter type!");
				errorIf(match(VARARGS), "An operator function cannot take '...' varaiable arguments!");
				
				//get the parameter's name (always required)
				Token<?> paramName = consume(IDENTIFIER, "Expected parameter name!");
				
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
						lastType = getAdvance();
					}
					
					if (match(VARARGS)) varargs = true;
					Token<?> paramName = consume(IDENTIFIER, "Expected parameter name!");
					
					ParsedExpression assign = null;
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
	 * <p>
	 * constructors do not necessarily need to specify a body EX: 'init(x, y)'
	 * 
	 * @param constructor : don't necessarily have a body
	 * @return EArrayList<Statement> : list of all parsed method body statements
	 */
	public static EList<ParsedStatement> getFunctionBody() { return getFunctionBody(false); }
	public static EList<ParsedStatement> getFunctionBody(boolean constructor) {
		EList<ParsedStatement> body = null;
		
		//if this is a normal function definition and not a constructor, require a body
		if (!constructor) {
			if (check(LAMBDA)) {
				body = new EArrayList<>();
				body.add(PS_Return.returnStatement());
			}
			else if (match(CURLY_L)) {
				body = getBlock(true);
			}
			else {
				(body = new EArrayList<>()).addIfNotNull(declaration());
			}
		}
		// EX: 'init() -> 5'
		else if (match(LAMBDA)) {
			body = new EArrayList<>();
			body.add(PS_Return.returnStatement());
		}
		// EX: 'func test() { return 5 }'
		else if (match(CURLY_L)) {
			body = getBlock(true);
		}
		
		return body;
	}
	
}
