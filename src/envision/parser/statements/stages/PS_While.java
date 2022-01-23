package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.WhileStatement;

public class PS_While extends ParserStage {
	
	public static Statement whileStatement() {
		if (match(DO)) {
			Statement body = declaration();
			while (match(NEWLINE));
			consume(WHILE, "Expected continuing 'while' statement after preceding 'do'!");
			while (match(NEWLINE));
			consume(EXPR_LEFT, "Expected '(' after while declaration!");
			while (match(NEWLINE));
			Expression condition = expression();
			while (match(NEWLINE));
			consume(EXPR_RIGHT, "Expected ')' after while condition!");
			while (match(NEWLINE));
			return new WhileStatement(true, condition, body);
		}
		else if (match(WHILE)) {
			consume(EXPR_LEFT, "Expected '(' after while declaration!");
			while (match(NEWLINE));
			Expression condition = expression();
			consume(EXPR_RIGHT, "Expected ')' after while condition!");
			while (match(NEWLINE));
			Statement body = declaration();
			return new WhileStatement(false, condition, body);
		}
		error("Expected either a 'do' or a 'while' at this point!");
		return null;
	}
	
}
