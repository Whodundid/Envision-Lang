package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.LambdaExpression;
import envision.parser.expressions.types.NullExpression;
import envision.parser.expressions.types.RangeExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.ForStatement;
import envision.parser.statements.types.LambdaForStatement;
import envision.parser.statements.types.RangeForStatement;
import envision.parser.statements.types.VariableStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_For extends ParserStage {
	
	public static Statement forStatement() {
		consume(EXPR_LEFT, "Expected '(' after for statement!");
		
		//0 = normal, 1 = range (to), 2 = lambda
		int type = 0;
		
		Statement initializer = null;
		Expression middle = null;
		EArrayList<Expression> post = new EArrayList();
		Statement body = null;
		
		VariableStatement vars = null;
		EArrayList<RangeExpression> ranges = null;
		
		//Token valueType = null, var = null;
		//Expression list = null;
		
		//determine the number of semicolons -- parts
		int depth = 1, numSemi = 0;
		EArrayList<Token> loopTokens = new EArrayList();
		int pos = getCurrentNum();
		while (depth != 0 && !atEnd()) {
			Token t = advance();
			if (t.keyword == EXPR_LEFT) { depth++; }
			else if (t.keyword == EXPR_RIGHT) { depth--; }
			else if (t.keyword == SEMICOLON) { numSemi++; }
			
			loopTokens.add(t);
		}
		setCurrentNum(pos);
		
		//error on invalid block size
		errorIf(numSemi > 2, "A for loop can at most have 3 statement declaration blocks!");
		
		//determine type
		EArrayList<Keyword> keys = loopTokens.map(t -> t.keyword);
		
		//check if lambda
		if (keys.contains(Keyword.LAMBDA)) { type = 2; }
		//check if range
		else if (keys.contains(Keyword.TO)) { type = 1; }
		//otherwise is normal
		else { type = 0; }
		
		//don't care if there is only one statement block
		if (numSemi > 0) {
			//check for initializers
			if (!check(SEMICOLON)) {
				if (type != 2) {
					initializer = PS_Type.handleType();
					match(SEMICOLON);
				}
				else {
					vars = new VariableStatement(new ParserDeclaration());
					
					do {
						Token name = consume(IDENTIFIER, "Expected a lambda loop index variable name!");
						Expression value = null;
						if (match(ASSIGN)) {
							value = expression();
						}
						vars.addVar(name, value);
					}
					while (match(COMMA));
					
					consume(SEMICOLON, "Expected a ';' to conclude lambda loop initializer statement!");
				}
			}
			else {
				match(SEMICOLON);
			}
		}
		
		//check for middle condition/lambda
		if (!check(SEMICOLON)) {
			//handle range
			if (type == 1) {
				middle = expression();
				
				errorIf(!(middle instanceof RangeExpression), "Range (to) for loops can only accept range expressions as arguments!");
				ranges = new EArrayList((RangeExpression) middle);
				
				while (match(COMMA)) {
					Expression e = expression();
					errorIf(!(e instanceof RangeExpression), "Range (to) for loops can only accept range expressions as arguments!");
					RangeExpression range = (RangeExpression) e;
					ranges.add(range);
				}
			}
			//normal and lambda
			else {
				middle = expression();
			}
		}
		
		//check for post actions
		if (!check(EXPR_RIGHT)) {
			if (match(SEMICOLON)) {
				if (check(EXPR_RIGHT)) { post.add(new NullExpression()); }
				else {
					//gather all expressions separated by commas
					do {
						post.add(expression());
					}
					while (match(COMMA));
				}
			}
		}
		
		consume(EXPR_RIGHT, "Expected ')' to close for loop declaration!");
		
		//consume new lines
		while (match(NEWLINE));
		
		body = declaration();
		
		Statement forStatement = null;
		switch (type) {
		case 0: forStatement = new ForStatement(initializer, middle, post, body); break;
		case 1: forStatement = new RangeForStatement(initializer, body).addAll(ranges); break;
		case 2: forStatement = new LambdaForStatement(vars, (LambdaExpression) middle, post, body); break;
		default: error("INVALID FOR LOOP TYPE! (" + type + ") -- THIS SHOULDN'T BE POSSIBLE!!");
		}
		
		return forStatement;
	}
	
}
