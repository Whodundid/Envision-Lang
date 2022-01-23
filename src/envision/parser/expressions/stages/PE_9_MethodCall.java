package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.GetExpression;
import envision.parser.expressions.types.ListIndexExpression;
import envision.parser.expressions.types.MethodCallExpression;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PE_9_MethodCall extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_A_Primary.primary();
		
		while (true) {
			if (check(EXPR_LEFT)) {
				e = finishCall(e);
			}
			else if (match(BRACKET_LEFT)) {
				Expression index = expression();
				consume(BRACKET_RIGHT, "Expected ']' after list index!");
				e = new ListIndexExpression(e, index);
			}
			else if (match(PERIOD)) {
				Token name = consume("Expected property name after '.'!", IDENTIFIER/*, INIT*/);
				
				//check if method call
				if (match(EXPR_LEFT)) {
					//MethodCallExpression next = null;
					EArrayList<Expression> args = new EArrayList();
					
					if (!check(EXPR_RIGHT)) {
						do { args.add(expression()); }
						while (match(COMMA));
					}
					
					consume(EXPR_RIGHT, "Expected a ')' to end method arguments!");
					
					MethodCallExpression mce = new MethodCallExpression(e, name, args);
					
					while (match(PERIOD)) {
						Token nextName = consume("Expected property name after '.'!", IDENTIFIER/*, INIT*/);
						//System.out.println("next name: " + nextName);
						EArrayList<Expression> nextArgs = new EArrayList();
						
						if (match(EXPR_LEFT)) {
							if (!check(EXPR_RIGHT)) {
								do {
									nextArgs.add(expression());
								}
								while (match(COMMA));
							}
							consume(EXPR_RIGHT, "Expected a ')' to end method arguments!");
						}
						
						mce.addNext(new MethodCallExpression(null, nextName, nextArgs));
					}
					
					e = mce;
				}
				else { e = new GetExpression(e, name); }
			}
			else break;
		}
		
		return e;
	}
	
	public static Expression finishCall(Expression callee) {
		EArrayList<Expression> args = new EArrayList();
		
		//arguments
		consume(EXPR_LEFT, "Expected '(' to begin arguments!");
		if (!check(EXPR_RIGHT)) {
			do {
				if (args.size() >= 255) {
					error("Can't have more than 255 args!");
				}
				Expression exp = expression();
				args.add(exp);
			}
			while (match(COMMA));
		}
		//conclude args/params
		consume(EXPR_RIGHT, "Expected ')' after arguments!");
		
		MethodCallExpression e = new MethodCallExpression(callee, args);
		return e;
	}
	
}

/**
 * From finishCall
 * 
EArrayList<Expression> args = new EArrayList();
		EArrayList<Token> generics = null;
		
		//generics
		if (match(LESS_THAN)) {
			generics = new EArrayList();
			do {
				if (check(IDENTIFIER)) generics.add(consume(IDENTIFIER, "Expected a valid identifier for a generic!"));
				else if (checkType(DATATYPE)) generics.add(consumeType(DATATYPE, "Expected a valid datatype for a generic!"));
			}
			while (match(COMMA));
			consume(GREATER_THAN, "Expected '>' after generics!");
		}
		
		//true if this statement is actually a typeless method declaration
		@Experimental_Envision
		boolean isDeclaration = false;
		
		//EXPERIMENTAL -- typeless method declaration
		
		//check for declaration
		if (checkAll(EXPR_LEFT, EXPR_RIGHT, LAMBDA)) isDeclaration = true;
		if (!isDeclaration) {
			int start = getCurrentNum();
			while (!check(EXPR_RIGHT, EOF)) { advance(); }
			if (checkNext(SCOPE_LEFT, LAMBDA)) isDeclaration = true;
			setCurrentNum(start);
		}
		
		//handle as if this were a method declaration instead
		if (isDeclaration) {
			EArrayList<StatementParameter> params = PS_Method.getMethodParameters();
			EArrayList<Statement> body = PS_Method.getMethodBody();
			Token name = ((VarExpression) callee).name;
			
			return new MethodDeclarationExpression(name, params, body);
		}
		else {
			//arguments
			consume(EXPR_LEFT, "Expected '(' to begin arguments!");
			if (!check(EXPR_RIGHT)) {
				do {
					if (args.size() >= 255) {
						error("Can't have more than 255 args!");
					}
					Expression exp = expression();
					args.add(exp);
				}
				while (match(COMMA));
			}
			//conclude args/params
			consume(EXPR_RIGHT, "Expected ')' after arguments!");
		}
		
		MethodCallExpression e = new MethodCallExpression(callee, args);
		e.generics = generics;
		return e;
*/