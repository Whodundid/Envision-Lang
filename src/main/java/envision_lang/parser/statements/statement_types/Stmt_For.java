package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/** Standard for loop. */
public class Stmt_For extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final ParsedStatement init;
	public final ParsedExpression cond;
	public final EList<ParsedExpression> post;
	public final ParsedStatement body;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_For(Token<?> start,
					ParsedStatement initIn,
					ParsedExpression condIn,
					ParsedExpression postIn,
					ParsedStatement bodyIn)
	{
		this(start, initIn, condIn, EList.newList(postIn), bodyIn);
	}
	
	public Stmt_For(Token<?> start,
					ParsedStatement initIn,
					ParsedExpression condIn,
					EList<ParsedExpression> postIn,
					ParsedStatement bodyIn)
	{
		super(start);
		init = initIn;
		cond = condIn;
		post = postIn;
		body = bodyIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String i = (init != null) ? init.toString() : "";
		String c = (cond != null) ? " " + cond.toString() : "";
		String p = (post != null) ? " " + post.toString() : "";
		String b = (body != null) ? " " + body + " " : "";
		return "for (" + i + ";" + c + ";" + p + ") {" + b + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleForStatement(this);
	}
	
}
