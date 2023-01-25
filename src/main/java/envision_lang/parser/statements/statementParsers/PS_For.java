package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_For;
import envision_lang.parser.statements.statement_types.Stmt_LambdaFor;
import envision_lang.parser.statements.statement_types.Stmt_RangeFor;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class PS_For extends ParserHead {
	
	public static ParsedStatement forStatement() {
		Token<?> forToken = consume(FOR, "Expected 'for' here!");
		var prev = previous();
		var cur = current();
		var next = next();
		ignoreNL();
		consume(PAREN_L, "Expected '(' after for statement!");
		
		//0 = normal, 1 = range (to), 2 = lambda
		int type = 0;
		
		ParsedStatement initializer = null;
		ParsedExpression middle = null;
		EList<ParsedExpression> post = EList.newList();
		ParsedStatement body = null;
		
		Stmt_VarDef vars = null;
		EList<Expr_Range> ranges = null;
		
		//Token valueType = null, var = null;
		//Expression list = null;
		
		//determine the number of semicolons -- parts
		int depth = 1, numSemi = 0;
		EList<Token<?>> loopTokens = EList.newList();
		int pos = getCurrentParsingIndex();
		while (depth != 0 && !atEnd()) {
			Token<?> t = getAdvance();
			IKeyword k = t.getKeyword();
			
			if (k == PAREN_L) depth++;
			else if (k == PAREN_R) depth--;
			else if (k == SEMICOLON) numSemi++;
			
			loopTokens.add(t);
		}
		setCurrentParsingIndex(pos);
		
		//error on invalid block size
		errorIf(numSemi > 2, "A for loop can at most have 3 statement declaration blocks!");
		
		//determine type
		EList<IKeyword> keys = loopTokens.map(t -> t.getKeyword());
		
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
			ignoreNL();
			if (!check(SEMICOLON)) {
				if (type != 2) {
					initializer = PS_VarDef.variableDeclaration(true);
					match(SEMICOLON);
				}
				else {
					vars = new Stmt_VarDef(current(), new ParserDeclaration());
					
					do {
						Token<?> name = consume(IDENTIFIER, "Expected a lambda loop index variable name!");
						ParsedExpression value = null;
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
		ignoreNL();
		if (!check(SEMICOLON)) {
			//handle range
			if (type == 1) {
				middle = ExpressionParser.parseExpression();
				
				errorIf(!(middle instanceof Expr_Range), "Range (to) for loops can only accept range expressions as arguments!");
				ranges = EList.of((Expr_Range) middle);
				
				ignoreNL();
				while (match(COMMA)) {
					ParsedExpression e = ExpressionParser.parseExpression();
					errorIf(!(e instanceof Expr_Range), "Range (to) for loops can only accept range expressions as arguments!");
					Expr_Range range = (Expr_Range) e;
					ranges.add(range);
					ignoreNL();
				}
			}
			//normal and lambda
			else {
				middle = ExpressionParser.parseExpression();
			}
		}
		
		prev = previous();
		cur = current();
		next = next();
		
		//check for post actions
		ignoreNL();
		if (!check(PAREN_R)) {
			if (match(SEMICOLON)) {
				ignoreNL();
				if (check(PAREN_R)) {}
				else {
					//gather all expressions separated by commas
					do {
						post.add(ExpressionParser.parseExpression());
						ignoreNL();
					}
					while (match(COMMA));
				}
			}
		}
		
		ignoreNL();
		consume(PAREN_R, "Expected ')' to close for loop declaration!");
		
		body = declaration();
		
		ParsedStatement forStatement = null;
		switch (type) {
		case 0: forStatement = new Stmt_For(forToken, initializer, middle, post, body); break;
		case 1: forStatement = new Stmt_RangeFor(forToken, initializer, body, ranges); break;
		case 2: forStatement = new Stmt_LambdaFor(forToken, vars, (Expr_Lambda) middle, post, body); break;
		default: error("INVALID FOR LOOP TYPE! (" + type + ") -- THIS SHOULDN'T BE POSSIBLE!!");
		}
		
		return forStatement;
	}
	
}
