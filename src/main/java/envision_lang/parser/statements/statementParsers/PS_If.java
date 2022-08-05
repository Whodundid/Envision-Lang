package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_If;

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
