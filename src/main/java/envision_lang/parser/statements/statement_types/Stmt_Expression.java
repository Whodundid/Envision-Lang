package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class Stmt_Expression extends BasicStatement {
	
	public final Expression expression;
	
	public Stmt_Expression(Expression expressionIn) {
		super(expressionIn.definingToken());
		expression = expressionIn;
	}
	
	@Override
	public String toString() {
		return expression + "";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleExpressionStatement(this);
	}
	
	@Override
	public Stmt_Expression copy() {
		return new Stmt_Expression(expression.copy());
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
