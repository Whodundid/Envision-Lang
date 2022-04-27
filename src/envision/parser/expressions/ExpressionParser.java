package envision.parser.expressions;

import static envision.tokenizer.KeywordType.ASSIGNMENT;
import static envision.tokenizer.KeywordType.DATATYPE;
import static envision.tokenizer.KeywordType.OPERATOR;
import static envision.tokenizer.Operator.ADD;
import static envision.tokenizer.Operator.AND;
import static envision.tokenizer.Operator.BRACKET_L;
import static envision.tokenizer.Operator.BRACKET_R;
import static envision.tokenizer.Operator.COLON;
import static envision.tokenizer.Operator.COMMA;
import static envision.tokenizer.Operator.COMPARE;
import static envision.tokenizer.Operator.DEC;
import static envision.tokenizer.Operator.DIV;
import static envision.tokenizer.Operator.GT;
import static envision.tokenizer.Operator.GTE;
import static envision.tokenizer.Operator.INC;
import static envision.tokenizer.Operator.LAMBDA;
import static envision.tokenizer.Operator.LT;
import static envision.tokenizer.Operator.LTE;
import static envision.tokenizer.Operator.MOD;
import static envision.tokenizer.Operator.MUL;
import static envision.tokenizer.Operator.NEGATE;
import static envision.tokenizer.Operator.NOT_EQUALS;
import static envision.tokenizer.Operator.OR;
import static envision.tokenizer.Operator.PAREN_L;
import static envision.tokenizer.Operator.PAREN_R;
import static envision.tokenizer.Operator.PERIOD;
import static envision.tokenizer.Operator.SUB;
import static envision.tokenizer.Operator.TERNARY;
import static envision.tokenizer.ReservedWord.BY;
import static envision.tokenizer.ReservedWord.CHAR_LITERAL;
import static envision.tokenizer.ReservedWord.FALSE;
import static envision.tokenizer.ReservedWord.IDENTIFIER;
import static envision.tokenizer.ReservedWord.INIT;
import static envision.tokenizer.ReservedWord.IS;
import static envision.tokenizer.ReservedWord.NEWLINE;
import static envision.tokenizer.ReservedWord.NULL;
import static envision.tokenizer.ReservedWord.NUMBER_LITERAL;
import static envision.tokenizer.ReservedWord.STRING_LITERAL;
import static envision.tokenizer.ReservedWord.SUPER;
import static envision.tokenizer.ReservedWord.THIS;
import static envision.tokenizer.ReservedWord.TO;
import static envision.tokenizer.ReservedWord.TRUE;

import envision.parser.GenericParser;
import envision.parser.expressions.expression_types.Expr_Assign;
import envision.parser.expressions.expression_types.Expr_Binary;
import envision.parser.expressions.expression_types.Expr_Cast;
import envision.parser.expressions.expression_types.Expr_Compound;
import envision.parser.expressions.expression_types.Expr_FunctionCall;
import envision.parser.expressions.expression_types.Expr_Get;
import envision.parser.expressions.expression_types.Expr_Lambda;
import envision.parser.expressions.expression_types.Expr_ListIndex;
import envision.parser.expressions.expression_types.Expr_ListInitializer;
import envision.parser.expressions.expression_types.Expr_Literal;
import envision.parser.expressions.expression_types.Expr_Logic;
import envision.parser.expressions.expression_types.Expr_Range;
import envision.parser.expressions.expression_types.Expr_Set;
import envision.parser.expressions.expression_types.Expr_SetListIndex;
import envision.parser.expressions.expression_types.Expr_Super;
import envision.parser.expressions.expression_types.Expr_Ternary;
import envision.parser.expressions.expression_types.Expr_This;
import envision.parser.expressions.expression_types.Expr_TypeOf;
import envision.parser.expressions.expression_types.Expr_Unary;
import envision.parser.expressions.expression_types.Expr_Var;
import envision.parser.expressions.expression_types.Expr_VarDef;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class ExpressionParser extends GenericParser {
	
	//-----------------------------------------------------------------------------------------------------
	
	private ExpressionParser() {}
	
	/**
	 * Descends through the parse tree to determine if there
	 * are any valid expressions.
	 * 
	 * @return A built expression
	 */
	public static Expression parseExpression() {
		return assignment();
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression assignment() {
		Expression e = or();
		
		if (matchType(ASSIGNMENT)) {
			Operator operator = previous().asOperator();
			Expression value = assignment();
			
			if (e instanceof Expr_Var v) return new Expr_Assign(v.name, operator, value);
			//else if (e instanceof ModularExpression m) return new AssignExpression(null, operator, value, m.modulars);
			else if (e instanceof Expr_Get g) return new Expr_Set(g.object, g.name, value);
			else if (e instanceof Expr_ListIndex lie) return new Expr_SetListIndex(lie, value);
			else if (e instanceof Expr_Assign ae) return new Expr_Assign(ae, operator, value);
			
			setPrevious();
			error("'" + e + "' Invalid assignment target.");
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression or() {
		Expression e = And();
		
		while (match(OR)) {
			Operator operator = previous().asOperator();
			Expression right = And();
			e = new Expr_Logic(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression And() {
		Expression e = equality();
		
		while (match(AND)) {
			Operator operator = previous().asOperator();
			Expression right = equality();
			e = new Expr_Logic(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression equality() {
		Expression e = lambda();
		
		if (match(NOT_EQUALS, COMPARE, GT, GTE, LT, LTE)) {
			Operator operator = previous().asOperator();
			Expression right = lambda();
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
		if (match(IS/*, ISNOT*/)) {
			boolean is = previous().keyword == IS;
			Expression right = lambda();
			e = new Expr_TypeOf(e, is, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression lambda() {
		Expression e = arithmetic();
		
		while (match(LAMBDA)) {
			/*
			if (c instanceof CompoundExpression) {
				compound = (CompoundExpression) c;
			}
			else if (c instanceof GroupingExpression) {
				GroupingExpression g = (GroupingExpression) c;
				
				if (g.expression instanceof CompoundExpression) { compound = (CompoundExpression) g.expression; }
				else { compound.add(g.expression); }
			}
			else compound.add(c);
			*/
			e = new Expr_Lambda(e, parseExpression());
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression arithmetic() {
		Expression e = factor();
		
		while (match(ADD, SUB)) {
			Operator operator = previous().asOperator();
			Expression right = factor();
			e = new Expr_Binary(e, operator, right);
		}
		
		return e;
	}
	
	public static Expression factor() {
		Expression e = unary();
		
		while (match(MUL, DIV, MOD)) {
			Operator operator = previous().asOperator();
			Expression right = unary();
			e = new Expr_Binary(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression unary() {
		if (check(NEGATE, SUB, DEC, INC) && checkNext(IDENTIFIER, IS, FALSE, TRUE)) {
			match(NEGATE, SUB, DEC, INC);
			Operator operator = previous().asOperator();
			Expression right = unary();
			Expression e = new Expr_Unary(operator, right, null);
			return e;
		}
		
		Expression e = range();
		
		if (match(DEC, INC)) {
			Operator o = previous().asOperator();
			e = new Expr_Unary(o, null, e);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression range() {
		Expression e = functionCall();
		
		if (!(e instanceof Expr_Range) && match(TO)) {
			Expression right = range();
			Expression by = null;
			if (match(BY)) {
				by = range();
			}
			e = new Expr_Range(e, right, by);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression functionCall() {
		Expression e = primary();
		
		while (true) {
			//check if standard function call
			if (check(PAREN_L)) {
				e = new Expr_FunctionCall(e, collectFuncArgs());
			}
			//check if accessing array element
			else if (match(BRACKET_L)) {
				Expression index = parseExpression();
				consume(BRACKET_R, "Expected ']' after list index!");
				e = new Expr_ListIndex(e, index);
			}
			//check if accessing member object
			else if (match(PERIOD)) {
				//grab the name of the member being accessed
				Token name = consume("Expected property name after '.'!", IDENTIFIER);
				
				//check if member function call
				if (check(PAREN_L)) e = new Expr_FunctionCall(e, name, collectFuncArgs());
				//otherwise, create a member 'get' call
				else e = new Expr_Get(e, name);
			}
			else break;
		}
		
		return e;
	}
	
	public static EArrayList<Expression> collectFuncArgs() {
		EArrayList<Expression> args = new EArrayList();
		
		//arguments
		consume(PAREN_L, "Expected '(' to begin arguments!");
		if (!check(PAREN_R)) {
			do {
				if (args.size() >= 255) error("Can't have more than 255 args!");
				Expression exp = parseExpression();
				args.add(exp);
			}
			while (match(COMMA));
		}
		//conclude args/params
		consume(PAREN_R, "Expected ')' after arguments!");
		
		return args;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression primary() {
		Expression e = null;
		
		//if (match(Keyword.MODULAR_VALUE)) return e = new ModularExpression(ParserStage.modularValues);
		if (match(INIT)) return new Expr_Var(previous());
		if (matchType(OPERATOR)) return new Expr_Literal(previous().keyword);
		
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
	
	private static Expression checkLiteral() {
		if (match(FALSE)) return new Expr_Literal(false);
		if (match(TRUE)) return new Expr_Literal(true);
		if (match(NULL)) return new Expr_Literal(null);
		if (match(STRING_LITERAL, CHAR_LITERAL, NUMBER_LITERAL)) return new Expr_Literal(previous().literal);
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkLists() {
		if (match(TO)) {
			Expression right = range();
			Expression by = null;
			if (match(BY)) {
				by = range();
			}
			return new Expr_Range(new Expr_Literal(0), right, by);
		}
		
		if (match(BRACKET_L)) {
			Expr_ListInitializer e = new Expr_ListInitializer();
			
			while (match(NEWLINE));
			if (!check(BRACKET_R)) {
				do {
					while (match(NEWLINE));
					e.addValue(assignment());
				}
				while (match(COMMA));
			}
			while (match(NEWLINE));
			consume(BRACKET_R, "Expected ']' after list initializer!");
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkLambda() {
		if (match(PAREN_L)) {
			Expression e = null;
			
			if (!check(PAREN_R)) {
				e = parseExpression();
				
				if (match(COMMA)) {
					EArrayList<Expression> expressions = new EArrayList();
					expressions.add(e);
					do {
						expressions.add(parseExpression());
					}
					while (match(COMMA));
					e = new Expr_Compound(expressions);
				}
			}
			consume(PAREN_R, "Expected ')' after expression!");
			
			//check for cast expressions
			if (e instanceof Expr_VarDef var_def) {
				Token type = var_def.type;
				//can only be a cast expression if either a datatype or a object type
				if (type.isDatatype() || type.isReference()) {
					Expression target = parseExpression();
					e = new Expr_Cast(type, target);
					return e;
				}
			}
			
			/*if (e == null && !check(LAMBDA)) {
				error("An empty expression can only be followed with a lambda expression!");
			}
			else*/
			if (match(TERNARY)) {
				Expression t = parseExpression();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				Expression f = parseExpression();
				return new Expr_Ternary(e, t, f);
			}
			else {
				//parser.pd("CUR PRI: " + current());
				
				// This is duct tape at best
				if (e instanceof Expr_Compound) return e;
				
				//if there was no lambda production, return empty
				if (e == null) return new Expr_Compound();
				//else return new CompoundExpression(e);
				
				return e;
			}
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkObject() {
		if (match(THIS)) {
			if (match(PAREN_L)) {
				EArrayList<Expression> args = new EArrayList();
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
				
				//return new ThisConExpression(args);
			}
			if (match(PERIOD)) { return new Expr_This(consume(IDENTIFIER, "Expected a valid identifier!")); }
			return new Expr_This();
		}
		
		if (match(SUPER)) {
			consume(PERIOD, "Expected '.' after super call!");
			Token m = consume(IDENTIFIER, "Expected superclass method name!");
			
			if (match(PAREN_L)) {
				EArrayList<Expression> args = new EArrayList();
				if (!check(PAREN_R)) {
					do {
						args.add(parseExpression());
					}
					while (match(COMMA));
				}
				consume(PAREN_R, "Expected a ')' to close method arguments!");
				
				return new Expr_Super(m, args);
			}
			
			return new Expr_Super(m);
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkTernary() {
		if (match(IDENTIFIER, THIS)) {
			Expr_Var e = new Expr_Var(previous());
			
			if (match(TERNARY)) {
				Expression t = parseExpression();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				Expression f = parseExpression();
				return new Expr_Ternary(e, t, f);
			}
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkVariable() {
		if (match(IDENTIFIER) || matchType(DATATYPE)) {
			Token type = previous();
			EArrayList<Token> params = null;
			
			//check for parameters
			if (match(LT)) {
				params = new EArrayList();
				while (!atEnd() && !match(GT)) {
					//check if valid parameter
					if (check(IDENTIFIER, TERNARY) || checkType(DATATYPE)) {
						params.add(getAdvance());
					}
					else error("Invalid parameter type for variable declaration expression! '" + current().lexeme + "'");
				}
			}
			
			return new Expr_VarDef(type, params);
		}
		
		return null;
	}
}
