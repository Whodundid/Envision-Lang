package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.WhileStatement;

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
			return new WhileStatement(true, condition, body);
		}
		else if (match(WHILE)) {
			consume(PAREN_L, "Expected '(' after while declaration!");
			while (match(NEWLINE));
			Expression condition = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected ')' after while condition!");
			while (match(NEWLINE));
			Statement body = declaration();
			return new WhileStatement(false, condition, body);
		}
		error("Expected either a 'do' or a 'while' at this point!");
		return null;
	}
	
}
