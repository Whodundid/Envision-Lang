package envision.parser.statements.statements;

import envision.parser.expressions.Expression;
import envision.parser.expressions.expressions.LambdaExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class LambdaForStatement implements Statement {
	
	public final Statement init;
	public final LambdaExpression lambda;
	public final EArrayList<Expression> post;
	public final Statement body;
	
	public LambdaForStatement(Statement initIn, LambdaExpression lambdaIn, Expression postIn, Statement bodyIn) {
		this(initIn, lambdaIn, new EArrayList<Expression>(postIn), bodyIn);
	}
	
	public LambdaForStatement(Statement initIn, LambdaExpression lambdaIn, EArrayList<Expression> postIn, Statement bodyIn) {
		init = initIn;
		lambda = lambdaIn;
		post = postIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String i = (init != null) ? init + "; " : "";
		String m = (lambda != null) ? lambda + ((post != null) ? "; " : "") : ""; 
		String p = (post != null) ? post + "" : "";
		return "For (" + i + m + p + ") { " + body + " }";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleLambdaForStatement(this);
	}
	
}
