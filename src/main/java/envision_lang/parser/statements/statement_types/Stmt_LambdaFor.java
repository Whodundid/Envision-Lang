package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_LambdaFor extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final ParsedStatement init;
	public final Expr_Lambda lambda;
	public final EList<ParsedExpression> post;
	public final ParsedStatement body;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_LambdaFor(Token<?> start,
						  ParsedStatement initIn,
						  Expr_Lambda lambdaIn,
						  ParsedExpression postIn,
						  ParsedStatement bodyIn)
	{
		this(start, initIn, lambdaIn, EList.of(postIn), bodyIn);
	}
	
	public Stmt_LambdaFor(Token<?> start,
						  ParsedStatement initIn,
						  Expr_Lambda lambdaIn,
						  EList<ParsedExpression> postIn,
						  ParsedStatement bodyIn)
	{
		super(start);
		init = initIn;
		lambda = lambdaIn;
		post = postIn;
		body = bodyIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String i = (init != null) ? init + "; " : "";
		String m = (lambda != null) ? lambda + ((post != null) ? "; " : "") : ""; 
		String p = (post != null) ? post + "" : "";
		return "For (" + i + m + p + ") { " + body + " }";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleLambdaForStatement(this);
	}
	
}
