package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_While;

public class PS_While extends GenericParser {
	
	public static Statement whileStatement() {
		if (match(DO)) {
			Statement body = declaration();
			while (match(NEWLINE));
			consume(WHILE, "Expected continuing 'while' statement after preceding 'do'!");
			while (match(NEWLINE));
			consume(PAREN_L, "Expected '(' after while declaration!");
			while (match(NEWLINE));
			Expression condition = ExpressionParser.parseExpression();
			while (match(NEWLINE));
			consume(PAREN_R, "Expected ')' after while condition!");
			while (match(NEWLINE));
			return new Stmt_While(true, condition, body);
		}
		else if (match(WHILE)) {
			consume(PAREN_L, "Expected '(' after while declaration!");
			while (match(NEWLINE));
			Expression condition = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected ')' after while condition!");
			while (match(NEWLINE));
			Statement body = declaration();
			return new Stmt_While(false, condition, body);
		}
		error("Expected either a 'do' or a 'while' at this point!");
		return null;
	}
	
}