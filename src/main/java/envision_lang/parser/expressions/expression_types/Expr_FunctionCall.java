package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Expr_FunctionCall extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public Expr_FunctionCall next;
	public ParsedExpression callee;
	public Token<?> name;
	public EList<Token<?>> generics;
	public final EList<ParsedExpression> args;
	
	//==============
	// Constructors
	//==============
	
	public Expr_FunctionCall(ParsedExpression calleeIn, EList<ParsedExpression> argsIn) {
		this(null, calleeIn, null, argsIn);
	}
	
	public Expr_FunctionCall(Token nameIn, EList<ParsedExpression> argsIn) {
		this(null, null, nameIn, argsIn);
	}
	
	public Expr_FunctionCall(ParsedExpression calleeIn, Token<?> nameIn, EList<ParsedExpression> argsIn) {
		this(null, calleeIn, nameIn, argsIn);
	}
	
	public Expr_FunctionCall(Expr_FunctionCall nextIn,
							 ParsedExpression calleeIn,
							 Token<?> nameIn,
							 EList<ParsedExpression> argsIn)
	{
		super(calleeIn);
		next = nextIn;
		callee = calleeIn;
		name = nameIn;
		args = argsIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String nex = (next != null) ? "" + next : "";
		String c = (callee != null) ? callee + "" : "";
		String n = (name != null) ? "." + name.getLexeme() : "";
		String g = (generics != null) ? "<" + EStringUtil.toString(generics, gn -> gn.getLexeme()) + ">" : "";
		String a = (args != null && args.isNotEmpty()) ? EStringUtil.toString(args, ", ") : "";
		return c + n + g + "(" + a + ")" + nex;
	}
	
	@Override
	public Expr_FunctionCall copy() {
		return new Expr_FunctionCall((Expr_FunctionCall) next.copy(), callee.copy(), name.copy(), args);
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleMethodCall_E(this);
	}
	
	//=========
	// Methods
	//=========
	
	public Expr_FunctionCall addNext(Expr_FunctionCall nextIn) {
		next = nextIn;
		return this;
	}
	
}
