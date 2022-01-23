package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.CompoundExpression;
import envision.parser.expressions.types.ListInitializerExpression;
import envision.parser.expressions.types.LiteralExpression;
import envision.parser.expressions.types.RangeExpression;
import envision.parser.expressions.types.SuperExpression;
import envision.parser.expressions.types.TernaryExpression;
import envision.parser.expressions.types.ThisConExpression;
import envision.parser.expressions.types.ThisGetExpression;
import envision.parser.expressions.types.VarDecExpression;
import envision.parser.expressions.types.VarExpression;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PE_A_Primary extends ParserStage {
	
	public static Expression run() {
		Expression e = null;
		
		//if (match(Keyword.MODULAR_VALUE)) return e = new ModularExpression(ParserStage.modularValues);
		if (match(INIT)) return new LiteralExpression(previous().keyword);
		if (matchType(Keyword.KeywordType.OPERATOR)) return new LiteralExpression(previous().keyword);
		
		if ((e = checkLiteral()) != null) return e;
		if ((e = checkLists()) != null) return e;
		if ((e = checkLambda()) != null) return e;
		if ((e = checkObject()) != null) return e;
		if ((e = checkTernary()) != null) return e;
		if ((e = checkVariable()) != null) return e;
		
		error("Expected an expression!");
		return null;
	}
	
	//-------
	// Types
	//-------
	
	private static Expression checkLiteral() {
		if (match(FALSE)) { return new LiteralExpression(false); }
		if (match(TRUE)) { return new LiteralExpression(true); }
		if (match(NULL) ) { return new LiteralExpression(null); }
		if (match(STRING_LITERAL, NUMBER_LITERAL)) { return new LiteralExpression(previous().literal); }
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
		
		if (match(BRACKET_LEFT)) {
			ListInitializerExpression e = new ListInitializerExpression();
			
			while (match(NEWLINE));
			if (!check(BRACKET_RIGHT)) {
				do {
					while (match(NEWLINE));
					e.addValue(assignment());
				}
				while (match(COMMA));
			}
			while (match(NEWLINE));
			consume(BRACKET_RIGHT, "Expected ']' after list initializer!");
			
			return e;
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkLambda() {
		if (match(EXPR_LEFT)) {
			Expression e = null;
			
			if (!check(EXPR_RIGHT)) {
				e = expression();
				
				if (match(COMMA)) {
					EArrayList<Expression> expressions = new EArrayList();
					expressions.add(e);
					do {
						expressions.add(expression());
					}
					while (match(COMMA));
					e = new CompoundExpression(expressions);
				}
			}
			consume(EXPR_RIGHT, "Expected ')' after expression!");
			
			if (e == null && !check(LAMBDA)) {
				error("An empty expression can only be followed with a lambda expression!");
			}
			else if (match(TERNARY)) {
				Expression t = expression();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				Expression f = expression();
				return new TernaryExpression(e, t, f);
			}
			else {
				//parser.pd("CUR PRI: " + current());
				
				// This is duct tape at best
				if (e instanceof CompoundExpression) { return e; }
				
				//return new CompoundExpression(e);
				return e;
			}
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkObject() {
		if (match(THIS)) {
			if (match(EXPR_LEFT)) {
				EArrayList<Expression> args = new EArrayList();
				if (!check(EXPR_RIGHT)) {
					do {
						if (args.size() >= 255) {
							error("Can't have more than 255 args!");
						}
						args.add(expression());
					}
					while (match(COMMA));
				}
				consume(EXPR_RIGHT, "Expected a ')' after arguments!");
				
				return new ThisConExpression(args);
			}
			if (match(PERIOD)) { return new ThisGetExpression(consume(IDENTIFIER, "Expected a valid identifier!")); }
			return new ThisGetExpression();
		}
		
		if (match(SUPER)) {
			consume(PERIOD, "Expected '.' after super call!");
			Token m = consume(IDENTIFIER, "Expected superclass method name!");
			
			if (match(EXPR_LEFT)) {
				EArrayList<Expression> args = new EArrayList();
				if (!check(EXPR_RIGHT)) {
					do {
						args.add(expression());
					}
					while (match(COMMA));
				}
				consume(EXPR_RIGHT, "Expected a ')' to close method arguments!");
				
				return new SuperExpression(m, args);
			}
			
			return new SuperExpression(m);
		}
		
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	private static Expression checkTernary() {
		if (match(IDENTIFIER, THIS, /*INIT,*/ READ)) {
			VarExpression e = new VarExpression(previous());
			
			if (match(TERNARY)) {
				Expression t = expression();
				consume(COLON, "Expected a ':' in between ternary expressions!");
				Expression f = expression();
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
			if (match(LESS_THAN)) {
				params = new EArrayList();
				while (!atEnd() && !match(GREATER_THAN)) {
					//check if valid parameter
					if (check(IDENTIFIER, TERNARY) || checkType(DATATYPE)) {
						params.add(getAdvance());
					}
					else { error("Invalid parameter type for variable declaration expression! '" + current().lexeme + "'"); }
				}
			}
			
			return new VarDecExpression(type, params);
		}
		
		return null;
	}
	
}
