package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_RangeFor extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final ParsedStatement init;
	public final EList<Expr_Range> ranges = EList.newList();
	public final ParsedStatement body;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_RangeFor(Token<?> start, ParsedStatement initIn, ParsedStatement bodyIn) {
		super(start);
		init = initIn;
		body = bodyIn;
	}
	
	public Stmt_RangeFor(Token<?> start, ParsedStatement initIn, ParsedStatement bodyIn, EList<Expr_Range> rangesIn) {
		super(start);
		init = initIn;
		body = bodyIn;
		ranges.addAll(rangesIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String i = (init != null) ? init + "; " : "";
		String range = "";
		for (ParsedExpression r : ranges) { range += r + ", "; }
		range = (ranges.isNotEmpty()) ? range.substring(0, range.length() - 2) : range;
		String b = (body != null) ? " " + body + " " : "";
		return "for (" + i + range + ") {" + b + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleRangeForStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addRange(Expr_Range rangeIn) {
		ranges.addIfNotNull(rangeIn);
	}
	
	public void addAll(EList<Expr_Range> rangesIn) {
		if (rangesIn != null) ranges.addAll(rangesIn);
	}
	
}
