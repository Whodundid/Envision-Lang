package envision.parser.expressions;

import static envision.tokenizer.KeywordType.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.expression_types.AssignExpression;
import envision.parser.expressions.expression_types.BinaryExpression;
import envision.parser.expressions.expression_types.CompoundExpression;
import envision.parser.expressions.expression_types.GetExpression;
import envision.parser.expressions.expression_types.LambdaExpression;
import envision.parser.expressions.expression_types.ListIndexExpression;
import envision.parser.expressions.expression_types.ListIndexSetExpression;
import envision.parser.expressions.expression_types.ListInitializerExpression;
import envision.parser.expressions.expression_types.LiteralExpression;
import envision.parser.expressions.expression_types.LogicalExpression;
import envision.parser.expressions.expression_types.FunctionCallExpression;
import envision.parser.expressions.expression_types.RangeExpression;
import envision.parser.expressions.expression_types.SetExpression;
import envision.parser.expressions.expression_types.SuperExpression;
import envision.parser.expressions.expression_types.TernaryExpression;
import envision.parser.expressions.expression_types.ThisConExpression;
import envision.parser.expressions.expression_types.ThisGetExpression;
import envision.parser.expressions.expression_types.TypeOfExpression;
import envision.parser.expressions.expression_types.UnaryExpression;
import envision.parser.expressions.expression_types.VarDecExpression;
import envision.parser.expressions.expression_types.VarExpression;
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
			
			if (e instanceof VarExpression v) return new AssignExpression(v.name, operator, value);
			//else if (e instanceof ModularExpression m) return new AssignExpression(null, operator, value, m.modulars);
			else if (e instanceof GetExpression g) return new SetExpression(g.object, g.name, value);
			else if (e instanceof ListIndexExpression lie) return new ListIndexSetExpression(lie, value);
			else if (e instanceof AssignExpression ae) return new AssignExpression(ae, operator, value);
			
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
			e = new LogicalExpression(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression And() {
		Expression e = equality();
		
		while (match(AND)) {
			Operator operator = previous().asOperator();
			Expression right = equality();
			e = new LogicalExpression(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression equality() {
		Expression e = lambda();
		
		if (match(NOT_EQUALS, COMPARE, GT, GTE, LT, LTE)) {
			Operator operator = previous().asOperator();
			Expression right = lambda();
			e = new BinaryExpression(e, operator, right);
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
			e = new TypeOfExpression(e, is, right);
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
			e = new LambdaExpression(e, parseExpression());
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression arithmetic() {
		Expression e = factor();
		
		while (match(ADD, SUB)) {
			Operator operator = previous().asOperator();
			Expression right = factor();
			e = new BinaryExpression(e, operator, right);
		}
		
		return e;
	}
	
	public static Expression factor() {
		Expression e = unary();
		
		while (match(MUL, DIV, MOD)) {
			Operator operator = previous().asOperator();
			Expression right = unary();
			e = new BinaryExpression(e, operator, right);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression unary() {
		if (check(NEGATE, SUB, DEC, INC) && checkNext(IDENTIFIER, IS, FALSE, TRUE)) {
			match(NEGATE, SUB, DEC, INC);
			Operator operator = previous().asOperator();
			Expression right = unary();
			Expression e = new UnaryExpression(operator, right, null);
			return e;
		}
		
		Expression e = range();
		
		if (match(DEC, INC)) {
			Operator o = previous().asOperator();
			e = new UnaryExpression(o, null, e);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression range() {
		Expression e = functionCall();
		
		if (!(e instanceof RangeExpression) && match(TO)) {
			Expression right = range();
			Expression by = null;
			if (match(BY)) {
				by = range();
			}
			e = new RangeExpression(e, right, by);
		}
		
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression functionCall() {
		Expression e = primary();
		
		while (true) {
			if (check(PAREN_L)) {
				e = finishCall(e);
			}
			else if (match(BRACKET_L)) {
				Expression index = parseExpression();
				consume(BRACKET_R, "Expected ']' after list index!");
				e = new ListIndexExpression(e, index);
			}
			else if (match(PERIOD)) {
				Token name = consume("Expected property name after '.'!", IDENTIFIER);
				
				//check if function call
				if (match(PAREN_L)) {
					//MethodCallExpression next = null;
					EArrayList<Expression> args = new EArrayList();
					
					if (!check(PAREN_R)) {
						do args.add(parseExpression());
						while (match(COMMA));
					}
					
					consume(PAREN_R, "Expected a ')' to end function arguments!");
					
					FunctionCallExpression mce = new FunctionCallExpression(e, name, args);
					
					while (match(PERIOD)) {
						Token nextName = consume("Expected property name after '.'!", IDENTIFIER/*, INIT*/);
						//System.out.println("next name: " + nextName);
						EArrayList<Expression> nextArgs = new EArrayList();
						
						if (match(PAREN_L)) {
							if (!check(PAREN_R)) {
								do {
									nextArgs.add(parseExpression());
								}
								while (match(COMMA));
							}
							consume(PAREN_R, "Expected a ')' to end function arguments!");
						}
						
						mce.addNext(new FunctionCallExpression(null, nextName, nextArgs));
					}
					
					e = mce;
				}
				else e = new GetExpression(e, name);
			}
			else break;
		}
		
		return e;
	}
	
	public static Expression finishCall(Expression callee) {
		EArrayList<Expression> args = new EArrayList();
		
		//arguments
		consume(PAREN_L, "Expected '(' to begin arguments!");
		if (!check(PAREN_R)) {
			do {
				if (args.size() >= 255) {
					error("Can't have more than 255 args!");
				}
				Expression exp = parseExpression();
				args.add(exp);
			}
			while (match(COMMA));
		}
		//conclude args/params
		consume(PAREN_R, "Expected ')' after arguments!");
		
		FunctionCallExpression e = new FunctionCallExpression(callee, args);
		return e;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public static Expression primary() {
		Expression e = null;
		
		//if (match(Keyword.MODULAR_VALUE)) return e = new ModularExpression(ParserStage.modularValues);
		if (match(INIT)) return new VarExpression(previous());
		if (matchType(OPERATOR)) return new LiteralExpression(previous().keyword);
		
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
		if (match(FALSE)) return new LiteralExpression(false);
		if (match(TRUE)) return new LiteralExpression(true);
		if (match(NULL)) return new LiteralExpression(null);
		if (match(STRING_LITERAL, CHAR_LITERAL, NUMBER_LITERAL)) return new LiteralExpression(previous().literal);
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
			return new RangeExpression(new LiteralExpression(0), right, by);
		}
		
		if (match(BRACKET_L)) {
			ListInitializerExpression e = new ListInitializerExpression();
			
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
					e = new CompoundExpression(expressions);
				}
			}
			consume(PAREN_R, "Expected ')' after expression!");
			
			/*if (e == null && !check(LAMBDA)) {
				error("An empty expression can only be followed with a lambda expression!");
			}
			else*/
			if (match(TERNARY)) {
				Expression t = parseExpression();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				Expression f = parseExpression();
				return new TernaryExpression(e, t, f);
			}
			else {
				//parser.pd("CUR PRI: " + current());
				
				// This is duct tape at best
				if (e instanceof CompoundExpression) return e;
				
				//if there was no lambda production, return empty
				if (e == null) return new CompoundExpression();
				else return new CompoundExpression(e);
				
				//return e;
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
				
				return new ThisConExpression(args);
			}
			if (match(PERIOD)) { return new ThisGetExpression(consume(IDENTIFIER, "Expected a valid identifier!")); }
			return new ThisGetExpression();
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
				
				return new SuperExpression(m, args);
			}
			
			return new SuperExpression(m);
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkTernary() {
		if (match(IDENTIFIER, THIS)) {
			VarExpression e = new VarExpression(previous());
			
			if (match(TERNARY)) {
				Expression t = parseExpression();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				Expression f = parseExpression();
				return new TernaryExpression(e, t, f);
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
			
			return new VarDecExpression(type, params);
		}
		
		return null;
	}
}
