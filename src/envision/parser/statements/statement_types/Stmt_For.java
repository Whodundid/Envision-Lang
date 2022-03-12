package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

/** Standard for loop. */
public class Stmt_For implements Statement {
	
	public final Statement init;
	public final Expression cond;
	public final EArrayList<Expression> post;
	public final Statement body;
	
	public Stmt_For(Statement initIn, Expression condIn, Expression postIn, Statement bodyIn) {
		this(initIn, condIn, new EArrayList<Expression>(postIn), bodyIn);
	}
	
	public Stmt_For(Statement initIn, Expression condIn, EArrayList<Expression> postIn, Statement bodyIn) {
		init = initIn;
		cond = condIn;
		post = postIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String i = (init != null) ? init.toString() : "";
		String c = (cond != null) ? " " + cond.toString() : "";
		String p = (post != null) ? " " + post.toString() : "";
		String b = (body != null) ? " " + body + " " : "";
		return "for (" + i + ";" + c + ";" + p + ") {" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleForStatement(this);
	}
	
}
