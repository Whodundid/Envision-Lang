package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_If;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class PS_If extends ParserHead {
	
	/**
	 * Attempts to parse an if statement from tokens.
	 * @return Statement
	 */
	public static ParsedStatement ifStatement(ParserDeclaration dec) {
		Token<?> ifToken = consume(IF, "Expected 'if' here!");
		consume(PAREN_L, "Expected an expression start '(' after an if statement!");
		ParsedExpression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected a ')' to end expression!");
		
		ParsedStatement thenBranch = parseStatement();
		ignoreNL();
		ParsedStatement elseBranch = (match(ELSE)) ? parseStatement() : null;
		
		return new Stmt_If(ifToken, condition, thenBranch, elseBranch);
	}
	
}
