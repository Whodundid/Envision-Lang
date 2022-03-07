package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.RangeExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class RangeForStatement implements Statement {
	
	public final Statement init;
	public final EArrayList<RangeExpression> ranges = new EArrayList();
	public final Statement body;
	
	public RangeForStatement(Statement initIn, Statement bodyIn) {
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
	
	public RangeForStatement addRange(RangeExpression rangeIn) {
		ranges.addIfNotNull(rangeIn);
		return this;
	}
	
	public RangeForStatement addAll(EArrayList<RangeExpression> rangesIn) {
		if (rangesIn != null) { ranges.addAll(rangesIn); }
		return this;
	}
	
}
