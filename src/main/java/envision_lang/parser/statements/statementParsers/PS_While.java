package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_While;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class PS_While extends ParserHead {
	
	public static ParsedStatement whileStatement(ParserDeclaration dec) {
		if (match(DO)) {
			Token<?> start = previous();
			ParsedStatement body = declaration();
			consume(WHILE, "Expected continuing 'while' statement after preceding 'do'!");
			consume(PAREN_L, "Expected '(' after while declaration!");
			ParsedExpression condition = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected ')' after while condition!");
			var stmt = new Stmt_While(start, true, condition, body);
			if (dec != null) stmt.setBlockStatement(dec.isBlockingStatement());
			return stmt;
		}
		else if (match(WHILE)) {
			Token<?> start = previous();
			consume(PAREN_L, "Expected '(' after while declaration!");
			ParsedExpression condition = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected ')' after while condition!");
			ParsedStatement body = declaration();
			var stmt = new Stmt_While(start, false, condition, body);
			if (dec != null) stmt.setBlockStatement(dec.isBlockingStatement());
			return stmt;
		}
		
		error("Expected either a 'do' or a 'while' at this point!");
		return null;
	}
	
}
