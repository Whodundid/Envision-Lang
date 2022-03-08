package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.expressions.expression_types.LambdaExpression;
import envision.parser.expressions.expression_types.EmptyExpression;
import envision.parser.expressions.expression_types.RangeExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.ForStatement;
import envision.parser.statements.statement_types.LambdaForStatement;
import envision.parser.statements.statement_types.RangeForStatement;
import envision.parser.statements.statement_types.VariableStatement;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.IKeyword;
import envision.tokenizer.Operator;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_For extends GenericParser {
	
	public static Statement forStatement() {
		consume(PAREN_L, "Expected '(' after for statement!");
		
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
			Token t = getAdvance();
			IKeyword k = t.keyword;
			
			if (k == PAREN_L) depth++;
			else if (k == PAREN_R) depth--;
			else if (k == SEMICOLON) numSemi++;
			
			loopTokens.add(t);
		}
		setCurrentNum(pos);
		
		//error on invalid block size
		errorIf(numSemi > 2, "A for loop can at most have 3 statement declaration blocks!");
		
		//determine type
		EArrayList<IKeyword> keys = loopTokens.map(t -> t.keyword);
		
		//check if lambda
		if (keys.contains(Operator.LAMBDA)) type = 2;
		//check if range
		else if (keys.contains(ReservedWord.TO)) type = 1;
		//otherwise is normal
		else type = 0;
		
		switch (type) {
		//normal
		case 0:
		//range
		case 1:
		//lambda
		case 2:
		}
		
		//don't care if there is only one statement block
		if (numSemi > 0) {
			//check for initializers
			if (!check(SEMICOLON)) {
				if (type != 2) {
					initializer = PS_VarDec.variableDeclaration();
					match(SEMICOLON);
				}
				else {
					vars = new VariableStatement(new ParserDeclaration());
					
					do {
						Token name = consume(IDENTIFIER, "Expected a lambda loop index variable name!");
						Expression value = null;
						if (match(ASSIGN)) {
							value = ExpressionParser.parseExpression();
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
				middle = ExpressionParser.parseExpression();
				
				errorIf(!(middle instanceof RangeExpression), "Range (to) for loops can only accept range expressions as arguments!");
				ranges = new EArrayList((RangeExpression) middle);
				
				while (match(COMMA)) {
					Expression e = ExpressionParser.parseExpression();
					errorIf(!(e instanceof RangeExpression), "Range (to) for loops can only accept range expressions as arguments!");
					RangeExpression range = (RangeExpression) e;
					ranges.add(range);
				}
			}
			//normal and lambda
			else {
				middle = ExpressionParser.parseExpression();
			}
		}
		
		//check for post actions
		if (!check(PAREN_R)) {
			if (match(SEMICOLON)) {
				if (check(PAREN_R)) post.add(new EmptyExpression());
				else {
					//gather all expressions separated by commas
					do {
						post.add(ExpressionParser.parseExpression());
					}
					while (match(COMMA));
				}
			}
		}
		
		consume(PAREN_R, "Expected ')' to close for loop declaration!");
		
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
