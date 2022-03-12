package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class Expr_FunctionCall implements Expression {
	
	public Expr_FunctionCall next;
	public Object callee;
	public Token name;
	public EArrayList<Token> generics;
	public final EArrayList<Expression> args;
	
	public Expr_FunctionCall(Expression calleeIn, EArrayList<Expression> argsIn) { this(null, calleeIn, null, argsIn); }
	public Expr_FunctionCall(Token nameIn, EArrayList<Expression> argsIn) { this(null, null, nameIn, argsIn); }
	public Expr_FunctionCall(Expression calleeIn, Token nameIn, EArrayList<Expression> argsIn) { this(null, calleeIn, nameIn, argsIn); }
	public Expr_FunctionCall(Expr_FunctionCall nextIn, Expression calleeIn, Token nameIn, EArrayList<Expression> argsIn) {
		this(nextIn, (Object) calleeIn, nameIn, argsIn);
	}
	
	private Expr_FunctionCall(Expr_FunctionCall nextIn, Object calleeIn, Token nameIn, EArrayList<Expression> argsIn) {
		next = nextIn;
		callee = calleeIn;
		name = nameIn;
		args = argsIn;
	}
	
	public Expr_FunctionCall copy(Expr_FunctionCall in) {
		return new Expr_FunctionCall(in.next, in.callee, in.name, in.args);
	}
	
	public Expr_FunctionCall setCallee(Object in) {
		Expr_FunctionCall c = copy(this);
		c.callee = in;
		return c;
	}
	
	public Expr_FunctionCall addNext(Expr_FunctionCall nextIn) {
		next = nextIn;
		return this;
	}
	
	public Expr_FunctionCall applyNext(Object in) {
		Expr_FunctionCall c = copy(this.next);
		c.callee = in;
		return c;
	}
	
	@Override
	public String toString() {
		String nex = (next != null) ? "" + next : "";
		String c = (callee != null) ? callee + "" : "";
		String n = (name != null) ? "." + name.lexeme : "";
		String g = (generics != null) ? "<" + StringUtil.toString(generics, gn -> gn.lexeme) + ">" : "";
		String a = (args != null && args.isNotEmpty()) ? StringUtil.toString(args, ", ") : "";
		return c + n + g + "(" + a + ")" + nex;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleMethodCall_E(this);
	}
	
}
