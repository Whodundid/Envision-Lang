package envision_lang.parser.expressions;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.parser.expressions.expression_types.Expr_Cast;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_FunctionCall;
import envision_lang.parser.expressions.expression_types.Expr_Get;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import envision_lang.parser.expressions.expression_types.Expr_ListIndex;
import envision_lang.parser.expressions.expression_types.Expr_ListInitializer;
import envision_lang.parser.expressions.expression_types.Expr_Literal;
import envision_lang.parser.expressions.expression_types.Expr_Logic;
import envision_lang.parser.expressions.expression_types.Expr_Primitive;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.expressions.expression_types.Expr_Set;
import envision_lang.parser.expressions.expression_types.Expr_SetListIndex;
import envision_lang.parser.expressions.expression_types.Expr_Super;
import envision_lang.parser.expressions.expression_types.Expr_Ternary;
import envision_lang.parser.expressions.expression_types.Expr_This;
import envision_lang.parser.expressions.expression_types.Expr_TypeOf;
import envision_lang.parser.expressions.expression_types.Expr_Unary;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.expressions.expression_types.Expr_VarDef;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class ExpressionParser extends ParserHead {
	
	//-----------------------------------------------------------------------------------------------------
	
	private ExpressionParser() {}
	
	/**
	 * Descends through the parse tree to determine if there
	 * are any valid expressions.
	 * 
	 * @return A built expression
	 */
	public static ParsedExpression parseExpression() {
		//ignoreNL();
		return assignment();
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression assignment() {
		ParsedExpression e = or();
		
		//ignoreNL();
		if (matchType(ASSIGNMENT)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression value = assignment();
			ParsedExpression check = null;
			
			if (e instanceof Expr_Var v) check = new Expr_Assign(v.name, operator, value);
			//if (e instanceof ModularExpression m) return new AssignExpression(null, operator, value, m.modulars);
			if (e instanceof Expr_Get g) check = new Expr_Set(g.object, g.name, value);
			if (e instanceof Expr_ListIndex lie) check = new Expr_SetListIndex(lie, operator, value);
			if (e instanceof Expr_Assign ae) check = new Expr_Assign(ae, operator, value);
			
			if (check != null) {
				//requireTerminator(); //THIS NEEDS TO KNOW WHETHER OR NOT IT IS IN A FUNCTION CALL AS AN ARGUMENT!
				return check;
			}
			
			decrementParsingIndex();
			error("'" + e + "' Invalid assignment target.");
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression or() {
		ParsedExpression e = And();
		
		//ignoreNL();
		while (match(OR)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression right = And();
			e = new Expr_Logic(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression And() {
		ParsedExpression e = equality();
		
		//ignoreNL();
		while (match(AND)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression right = equality();
			e = new Expr_Logic(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression equality() {
		ParsedExpression e = lambda();
		
		//ignoreNL();
		if (match(NOT_EQUALS, EQUALS, GT, GTE, LT, LTE)) {
			var prev = previousNonTerminator();
			Operator operator = prev.asOperator();
			ParsedExpression right = lambda();
			e = new Expr_Binary(e, operator, right);
		}
		
		//operator expression
		/*
		if (match(OPERATOR_EXPR)) {
			Token operator = null;
			if (check(IDENTIFIER)) operator = consume(IDENTIFIER, "Expected a valid name for the given operator!");
			if (ParserStage.modularValues != null && check(MODULAR_VALUE)) operator = consume(MODULAR_VALUE, "Expected a modular '@' reference for the given operator!");
			Expression right = PE_4_Lambda.run();
			e = new BinaryExpression(e, operator, right, true);
		}
		*/
		
		//typeof expressions
		//ignoreNL();
		if (check(NEGATE) && checkNext(TYPEOF)) {
			consume(NEGATE, "Expected a '!' here!"); //consume the '!'
			//ignoreNL();
			consume(TYPEOF, "Expected a 'typeof' here!"); //consume the 'typeof'
			ParsedExpression right = lambda();
			e = new Expr_TypeOf(e, false, right);
		}
		else if (match(TYPEOF)) {
			ParsedExpression right = lambda();
			e = new Expr_TypeOf(e, true, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression lambda() {
		ParsedExpression e = arithmetic();
		
		//ignoreNL();
		while (match(LAMBDA)) {
			e = new Expr_Lambda(previousNonTerminator(), e, parseExpression());
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression arithmetic() {
		ParsedExpression e = factor();
		
		//ignoreNL();
		while (match(ADD, SUB)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression right = factor();
			e = new Expr_Binary(e, operator, right);
		}
		
		return e;
	}
	
	public static ParsedExpression factor() {
		ParsedExpression e = unary();
		
		//ignoreNL();
		while (match(MUL, DIV, MOD)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression right = unary();
			e = new Expr_Binary(e, operator, right);
			//ignoreNL();
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression unary() {
		//ignoreNL();
		if (check(NEGATE, SUB, DEC, INC) && checkNextNonTerminator(IDENTIFIER, TYPEOF, TRUE, FALSE)) {
			Operator operator = current().asOperator();
			match(NEGATE, SUB, DEC, INC);
			
			//ignoreNL();
			if (operator == SUB || operator == DEC || operator == INC) {
				String opName;
				if (operator == SUB) opName = "negation";
				else if (operator == DEC) opName = "decrement";
				else opName = "increment";
				
				errorPreviousIf(!check(IDENTIFIER), "Cannot perform " + opName + " on anything other than a variable!");
			}
			
			ParsedExpression right = unary();
			ParsedExpression e = new Expr_Unary(previousNonTerminator(), operator, right, null);
			return e;
		}
		
		ParsedExpression e = range();
		
		//ignoreNL();
		if (match(DEC, INC)) {
			errorIf(!(e instanceof Expr_Var), "Cannot perform a unary operation on anything other than a variable!");
			
			Operator operator = previousNonTerminator().asOperator();
			e = new Expr_Unary(previousNonTerminator(), operator, null, e);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression range() {
		ParsedExpression e = functionCall();
		
		//ignoreNL();
		if (!(e instanceof Expr_Range) && match(TO)) {
			ParsedExpression right = range();
			ParsedExpression by = null;
			//ignoreNL();
			if (match(BY)) {
				by = range();
			}
			e = new Expr_Range(e, right, by);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression functionCall() {
		ParsedExpression e = primary();
		
		while (true) {
			//ignoreNL();
			//check if standard function call
			if (check(PAREN_L)) {
				e = new Expr_FunctionCall(e, collectFuncArgs());
				//requireTerminator();
			}
			//check if accessing array element
			else if (match(BRACKET_L)) {
				ParsedExpression index = parseExpression();
				//ignoreNL();
				consume(BRACKET_R, "Expected ']' after list index!");
				e = new Expr_ListIndex(e, index);
				//requireTerminator();
			}
			//check if accessing member object
			else if (match(PERIOD)) {
				//grab the name of the member being accessed
				Token<?> name = consume("Expected property name after '.'!", IDENTIFIER);
				
				//check if member function call
				//ignoreNL();
				if (check(PAREN_L)) e = new Expr_FunctionCall(e, name, collectFuncArgs());
				//otherwise, create a member 'get' call
				else e = new Expr_Get(e, name);
			}
			else break;
		}
		
		return e;
	}
	
	public static EList<ParsedExpression> collectFuncArgs() {
		EList<ParsedExpression> args = new EArrayList<>();
		
		//arguments
		//ignoreNL();
		consume(PAREN_L, "Expected '(' to begin arguments!");
		//ignoreNL();
		if (!check(PAREN_R)) {
			do {
				//ignoreNL();
				if (args.size() >= 255) error("Can't have more than 255 args!");
				ParsedExpression exp = parseExpression();
				args.add(exp);
				//ignoreNL();
			}
			while (match(COMMA));
		}
		//conclude args/params
		consume(PAREN_R, "Expected ')' after arguments!");
		
		return args;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression primary() {
		ParsedExpression e = null;
		
		//ignoreNL();
		if (match(INIT)) return new Expr_Var(previousNonTerminator());
		if (matchType(OPERATOR)) return new Expr_Literal(previousNonTerminator(), previousNonTerminator().getKeyword());
		
		if ((e = checkLiteral()) != null) return e;
		if ((e = checkLists()) != null) return e;
		if ((e = checkLambda()) != null) return e;
		if ((e = checkObject()) != null) return e;
		if ((e = checkTernary()) != null) return e;
		if ((e = checkVariable()) != null) return e;
		
		error("Expected an expression!");
		return null;
	}
	
	//---------------
	// Primary Types
	//---------------
	
	private static ParsedExpression checkLiteral() {
		if (match(FALSE)) return new Expr_Literal(previousNonTerminator(), false);
		if (match(TRUE)) return new Expr_Literal(previousNonTerminator(), true);
		if (match(NULL)) return new Expr_Literal(previousNonTerminator(), null);
		if (match(STRING_LITERAL, CHAR_LITERAL, NUMBER_LITERAL)) return new Expr_Literal(previousNonTerminator(), previousNonTerminator().getLiteral());
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkLists() {
		if (match(TO)) {
			Token<?> start = previousNonTerminator();
			ParsedExpression right = range();
			ParsedExpression by = null;
			
			//ignoreNL();
			if (match(BY)) {
				by = range();
			}
			
			return new Expr_Range(new Expr_Literal(start, 0), right, by);
		}
		
		//ignoreNL();
		if (match(BRACKET_L)) {
			Expr_ListInitializer e = new Expr_ListInitializer(previousNonTerminator());
			
			//ignoreNL();
			if (!check(BRACKET_R)) {
				do {
					e.addValue(assignment());
					//ignoreNL();
				}
				while (match(COMMA));
			}
			
			//ignoreNL();
			consume(BRACKET_R, "Expected ']' after list initializer!");
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkLambda() {
		//ignoreNL();
		if (!match(PAREN_L)) return null;
		
		ParsedExpression e = null;
		Token<?> start = previousNonTerminator();
		EList<ParsedExpression> expressions = null;
		
		//ignoreNL();
		if (!check(PAREN_R)) {
			e = parseExpression();
			
			//ignoreNL();
			if (match(COMMA)) {
				expressions = EList.newList();
				expressions.add(e);
				
				//ignoreNL();
				do {
					expressions.add(parseExpression());
					//ignoreNL();
				}
				while (match(COMMA));
				
				e = new Expr_Compound(start, expressions);
			}
		}
		
		//ignoreNL();
		consume(PAREN_R, "Expected ')' after expression!");
		
		//check for cast expressions
		if (e instanceof Expr_VarDef var_def) {
			Token<?> type = var_def.type;
			//can only be a cast ParsedExpression if either a datatype or a object type
			if (type.isDatatype() || type.isReference()) {
				ParsedExpression target = parseExpression();
				e = new Expr_Cast(type, target);
				return e;
			}
		}
		
		//ignoreNL();
		if (match(TERNARY)) {
			//ignoreNL();
			ParsedExpression t = parseExpression();
			//ignoreNL();
			consume(COLON, "Expected a ':' in between ternary expressions!");
			ParsedExpression f = parseExpression();
			return new Expr_Ternary(e, t, f);
		}
		else if (e == null && !check(LAMBDA) && expressions != null && expressions.isEmpty()) {
			error("An empty ParsedExpression can only be followed with a lambda expression!");
		}
		else {
			//parser.pd("CUR PRI: " + current());
			
			// This is duct tape at best
			if (e instanceof Expr_Compound) return e;
			
			//if there was no lambda production, return empty
			if (e == null) return new Expr_Compound(current());
			//else return new CompoundExpression(e);
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkObject() {
		//ignoreNL();
		if (match(THIS)) {
			Token<?> start = previous();
			//ignoreNL();
			if (match(PAREN_L)) {
				EList<ParsedExpression> args = EList.newList();
				//ignoreNL();
				if (!check(PAREN_R)) {
					do {
						if (args.size() >= 255) {
							error("Can't have more than 255 args!");
						}
						args.add(parseExpression());
						//ignoreNL();
					}
					while (match(COMMA));
				}
				//ignoreNL();
				consume(PAREN_R, "Expected a ')' after arguments!");
				
				//return new ThisConExpression(args);
			}
			
			//ignoreNL();
			if (match(PERIOD)) {
				//ignoreNL();
				return new Expr_This(consume(IDENTIFIER, "Expected a valid identifier!"));
			}
			
			return new Expr_This(start);
		}
		
		//ignoreNL();
		if (match(SUPER)) {
			Token<?> start = previous();
			//ignoreNL();
			consume(PERIOD, "Expected '.' after super call!");
			//ignoreNL();
			Token<?> m = consume(IDENTIFIER, "Expected superclass method name!");
			
			//ignoreNL();
			if (match(PAREN_L)) {
				EList<ParsedExpression> args = EList.newList();
				if (!check(PAREN_R)) {
					do {
						args.add(parseExpression());
						//ignoreNL();
					}
					while (match(COMMA));
				}
				
				//ignoreNL();
				consume(PAREN_R, "Expected a ')' to close method arguments!");
				
				return new Expr_Super(start, m, args);
			}
			
			return new Expr_Super(start, m);
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkTernary() {
		//ignoreNL();
		if (match(IDENTIFIER, THIS)) {
			Expr_Var e = new Expr_Var(previousNonTerminator());
			
			//ignoreNL();
			if (match(TERNARY)) {
				ParsedExpression t = parseExpression();
				//ignoreNL();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				ParsedExpression f = parseExpression();
				return new Expr_Ternary(e, t, f);
			}
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkVariable() {
		//ignoreNL();
		if (match(IDENTIFIER) || matchType(DATATYPE)) {
			Token<?> type = previousNonTerminator();
			EList<Token<?>> params = null;
			
			//check if primitive type
			if (type.isDatatype()) return new Expr_Primitive(type);
			
			//check for parameters
			//ignoreNL();
			if (match(LT)) {
				params = new EArrayList<>();
				//ignoreNL();
				while (!atEnd() && !match(GT)) {
					//check if valid parameter
					//ignoreNL();
					if (check(IDENTIFIER, TERNARY) || checkType(DATATYPE)) {
						params.add(getAdvance());
					}
					else error("Invalid parameter type for variable declaration expression! '" + current().getLexeme() + "'");
				}
			}
			
			return new Expr_VarDef(type, params);
		}
		
		return null;
	}
}
