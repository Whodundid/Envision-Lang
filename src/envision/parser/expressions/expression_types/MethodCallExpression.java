package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class MethodCallExpression implements Expression {
	
	public MethodCallExpression next;
	public Object callee;
	public Token name;
	public EArrayList<Token> generics;
	public final EArrayList<Expression> args;
	
	public MethodCallExpression(Expression calleeIn, EArrayList<Expression> argsIn) { this(null, calleeIn, null, argsIn); }
	public MethodCallExpression(Token nameIn, EArrayList<Expression> argsIn) { this(null, null, nameIn, argsIn); }
	public MethodCallExpression(Expression calleeIn, Token nameIn, EArrayList<Expression> argsIn) { this(null, calleeIn, nameIn, argsIn); }
	public MethodCallExpression(MethodCallExpression nextIn, Expression calleeIn, Token nameIn, EArrayList<Expression> argsIn) {
		this(nextIn, (Object) calleeIn, nameIn, argsIn);
	}
	
	private MethodCallExpression(MethodCallExpression nextIn, Object calleeIn, Token nameIn, EArrayList<Expression> argsIn) {
		next = nextIn;
		callee = calleeIn;
		name = nameIn;
		args = argsIn;
	}
	
	public MethodCallExpression copy(MethodCallExpression in) {
		return new MethodCallExpression(in.next, in.callee, in.name, in.args);
	}
	
	public MethodCallExpression setCallee(Object in) {
		MethodCallExpression c = copy(this);
		c.callee = in;
		return c;
	}
	
	public MethodCallExpression addNext(MethodCallExpression nextIn) {
		next = nextIn;
		return this;
	}
	
	public MethodCallExpression applyNext(Object in) {
		MethodCallExpression c = copy(this.next);
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
