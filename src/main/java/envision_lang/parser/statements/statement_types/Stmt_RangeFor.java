package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_RangeFor extends BasicStatement {
	
	public final Statement init;
	public final EArrayList<Expr_Range> ranges = new EArrayList<>();
	public final Statement body;
	
	public Stmt_RangeFor(Token start, Statement initIn, Statement bodyIn) {
		super(start);
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
