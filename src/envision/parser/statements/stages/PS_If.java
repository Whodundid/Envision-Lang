package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.IfStatement;

public class PS_If extends ParserStage {
	
	/**
	 * Attempts to parse an if statement from tokens.
	 * @return Statement
	 */
	public static Statement ifStatement() {
		consume(EXPR_LEFT, "Expected an expression start '(' after an if statement!");
		Expression condition = expression();
		consume(EXPR_RIGHT, "Expected a ')' to end expression!");
		
		match(NEWLINE);
		Statement thenBranch = declaration();
		match(NEWLINE);
		Statement elseBranch = (match(ELSE)) ? declaration() : null;
		
		return new IfStatement(condition, thenBranch, elseBranch);
	}
	
}
