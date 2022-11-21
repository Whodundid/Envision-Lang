package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_LambdaFor extends BasicStatement {
	
	public final Statement init;
	public final Expr_Lambda lambda;
	public final EArrayList<Expression> post;
	public final Statement body;
	
	public Stmt_LambdaFor(Token start, Statement initIn, Expr_Lambda lambdaIn, Expression postIn, Statement bodyIn) {
		this(start, initIn, lambdaIn, new EArrayList<>(postIn), bodyIn);
	}
	
	public Stmt_LambdaFor(Token start, Statement initIn, Expr_Lambda lambdaIn, EArrayList<Expression> postIn, Statement bodyIn) {
		super(start);
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
