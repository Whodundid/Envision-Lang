package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Range;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class Stmt_RangeFor implements Statement {
	
	public final Statement init;
	public final EArrayList<Expr_Range> ranges = new EArrayList();
	public final Statement body;
	
	public Stmt_RangeFor(Statement initIn, Statement bodyIn) {
		init = initIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String i = (init != null) ? init + "; " : "";
		String range = "";
		for (Expression r : ranges) { range += r + ", "; }
		range = (ranges.isNotEmpty()) ? range.substring(0, range.length() - 2) : range;
		String b = (body != null) ? " " + body + " " : "";
		return "for (" + i + range + ") {" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleRangeForStatement(this);
	}
	
	public Stmt_RangeFor addRange(Expr_Range rangeIn) {
		ranges.addIfNotNull(rangeIn);
		return this;
	}
	
	public Stmt_RangeFor addAll(EArrayList<Expr_Range> rangesIn) {
		if (rangesIn != null) { ranges.addAll(rangesIn); }
		return this;
	}
	
}
