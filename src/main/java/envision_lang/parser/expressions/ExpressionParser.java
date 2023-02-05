package envision_lang.parser.expressions;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
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
		return assignment();
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression assignment() {
		ParsedExpression e = or();
		
		if (matchType(ASSIGNMENT)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression value = assignment();
			ParsedExpression check = null;
			
			if (e instanceof Expr_Var v) check = new Expr_Assign(v.name, operator, value);
			if (e instanceof Expr_Get g) check = new Expr_Set(g.object, g.name, value);
			if (e instanceof Expr_ListIndex lie) check = new Expr_SetListIndex(lie, operator, value);
			if (e instanceof Expr_Assign ae) check = new Expr_Assign(ae, operator, value);
			
			if (check != null) {
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
		
		if (match(NOT_EQUALS, EQUALS, GT, GTE, LT, LTE)) {
			var prev = previousNonTerminator();
			Operator operator = prev.asOperator();
			ParsedExpression right = lambda();
			e = new Expr_Binary(e, operator, right);
		}
		
		//typeof expressions
		if (check(NEGATE) && checkNext(TYPEOF)) {
			consume(NEGATE, "Expected a '!' here!"); //consume the '!'
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
		
		while (match(LAMBDA)) {
			e = new Expr_Lambda(previousNonTerminator(), e, parseExpression());
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression arithmetic() {
		ParsedExpression e = factor();
		
		while (match(ADD, SUB)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression right = factor();
			e = new Expr_Binary(e, operator, right);
		}
		
		return e;
	}
	
	public static ParsedExpression factor() {
		ParsedExpression e = unary();
		
		while (match(MUL, DIV, MOD)) {
			Operator operator = previousNonTerminator().asOperator();
			ParsedExpression right = unary();
			e = new Expr_Binary(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedExpression unary() {
		if (check(NEGATE, SUB, DEC, INC) && checkNextNonTerminator(IDENTIFIER, TYPEOF, TRUE, FALSE)) {
			Operator operator = current().asOperator();
			match(NEGATE, SUB, DEC, INC);
			
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
		
		if (!(e instanceof Expr_Range) && match(TO)) {
			ParsedExpression right = range();
			ParsedExpression by = null;
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
			//check if standard function call
			if (check(PAREN_L)) {
				e = new Expr_FunctionCall(e, collectFuncArgs());
			}
			//check if accessing array element
			else if (match(BRACKET_L)) {
				ParsedExpression index = parseExpression();
				consume(BRACKET_R, "Expected ']' after list index!");
				e = new Expr_ListIndex(e, index);
			}
			//check if accessing member object
			else if (match(PERIOD)) {
				//grab the name of the member being accessed
				Token<?> name = consume("Expected property name after '.'!", IDENTIFIER);
				
				//check if member function call
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
		consume(PAREN_L, "Expected '(' to begin arguments!");
		if (!check(PAREN_R)) {
			do {
				if (args.size() >= 255) error("Can't have more than 255 args!");
				ParsedExpression exp = parseExpression();
				args.add(exp);
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
			
			if (match(BY)) {
				by = range();
			}
			
			return new Expr_Range(new Expr_Literal(start, 0), right, by);
		}
		
		if (match(BRACKET_L)) {
			Expr_ListInitializer e = new Expr_ListInitializer(previousNonTerminator());
			
			if (!check(BRACKET_R)) {
				do {
					e.addValue(assignment());
				}
				while (match(COMMA));
			}
			
			consume(BRACKET_R, "Expected ']' after list initializer!");
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkLambda() {
		if (!match(PAREN_L)) return null;
		
		ParsedExpression e = null;
		Token<?> start = previousNonTerminator();
		EList<ParsedExpression> expressions = null;
		
		if (!check(PAREN_R)) {
			e = parseExpression();
			
			if (match(COMMA)) {
				expressions = EList.newList();
				expressions.add(e);
				
				do {
					expressions.add(parseExpression());
				}
				while (match(COMMA));
				
				e = new Expr_Compound(start, expressions);
			}
		}
		
		consume(PAREN_R, "Expected ')' after expression!");
		
		//check for cast expressions
//		if (e instanceof Expr_VarDef var_def) {
//			Token<?> type = var_def.type;
//			//can only be a cast ParsedExpression if either a datatype or a object type
//			if (type.isDatatype() || type.isReference()) {
//				ParsedExpression target = parseExpression();
//				e = new Expr_Cast(type, target);
//				return e;
//			}
//		}
		
		if (match(TERNARY)) {
			ParsedExpression t = parseExpression();
			consume(COLON, "Expected a ':' in between ternary expressions!");
			ParsedExpression f = parseExpression();
			return new Expr_Ternary(e, t, f);
		}
		else if (e == null && !check(LAMBDA) && expressions != null && expressions.isEmpty()) {
			error("An empty ParsedExpression can only be followed with a lambda expression!");
		}
		else {
			// This is duct tape at best
			if (e instanceof Expr_Compound) return e;
			
			//if there was no lambda production, return empty
			if (e == null) return new Expr_Compound(current());
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkObject() {
		if (match(THIS)) {
			Token<?> start = previous();
			if (match(PAREN_L)) {
				EList<ParsedExpression> args = EList.newList();
				if (!check(PAREN_R)) {
					do {
						if (args.size() >= 255) {
							error("Can't have more than 255 args!");
						}
						args.add(parseExpression());
					}
					while (match(COMMA));
				}
				consume(PAREN_R, "Expected a ')' after arguments!");
			}
			
			if (match(PERIOD)) {
				return new Expr_This(consume(IDENTIFIER, "Expected a valid identifier!"));
			}
			
			return new Expr_This(start);
		}
		
//		if (match(SUPER)) {
//			Token<?> start = previous();
//			consume(PERIOD, "Expected '.' after super call!");
//			Token<?> m = consume(IDENTIFIER, "Expected superclass method name!");
//			
//			if (match(PAREN_L)) {
//				EList<ParsedExpression> args = EList.newList();
//				if (!check(PAREN_R)) {
//					do {
//						args.add(parseExpression());
//					}
//					while (match(COMMA));
//				}
//				
//				consume(PAREN_R, "Expected a ')' to close method arguments!");
//				
//				return new Expr_Super(start, m, args);
//			}
//			
//			return new Expr_Super(start, m);
//		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static ParsedExpression checkTernary() {
		if (match(IDENTIFIER, THIS)) {
			Expr_Var e = new Expr_Var(previousNonTerminator());
			
			if (match(TERNARY)) {
				ParsedExpression t = parseExpression();
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
		if (match(IDENTIFIER) || matchType(DATATYPE)) {
			Token<?> type = previousNonTerminator();
			EList<Token<?>> params = null;
			
			//check if primitive type
			if (type.isDatatype()) return new Expr_Primitive(type);
			
			//check for parameters
			if (match(LT)) {
				params = new EArrayList<>();
				while (!atEnd() && !match(GT)) {
					//check if valid parameter
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
