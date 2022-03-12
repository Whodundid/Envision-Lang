package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_If;

public class PS_If extends GenericParser {
	
	/**
	 * Attempts to parse an if statement from tokens.
	 * @return Statement
	 */
	public static Statement ifStatement() {
		consume(PAREN_L, "Expected an expression start '(' after an if statement!");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected a ')' to end expression!");
		
		match(NEWLINE);
		Statement thenBranch = declaration();
		match(NEWLINE);
		Statement elseBranch = (match(ELSE)) ? declaration() : null;
		
		return new Stmt_If(condition, thenBranch, elseBranch);
	}
	
}
