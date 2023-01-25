package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_While;
import envision_lang.tokenizer.Token;

public class PS_While extends ParserHead {
	
	public static ParsedStatement whileStatement() {
		ignoreNL();
		if (match(DO)) {
			Token<?> start = previous();
			ParsedStatement body = declaration();
			ignoreNL();
			consume(WHILE, "Expected continuing 'while' statement after preceding 'do'!");
			ignoreNL();
			consume(PAREN_L, "Expected '(' after while declaration!");
			ignoreNL();
			ParsedExpression condition = ExpressionParser.parseExpression();
			ignoreNL();
			consume(PAREN_R, "Expected ')' after while condition!");
			return new Stmt_While(start, true, condition, body);
		}
		else if (match(WHILE)) {
			Token<?> start = previous();
			ignoreNL();
			consume(PAREN_L, "Expected '(' after while declaration!");
			ParsedExpression condition = ExpressionParser.parseExpression();
			ignoreNL();
			consume(PAREN_R, "Expected ')' after while condition!");
			ParsedStatement body = declaration();
			return new Stmt_While(start, false, condition, body);
		}
		error("Expected either a 'do' or a 'while' at this point!");
		return null;
	}
	
}
