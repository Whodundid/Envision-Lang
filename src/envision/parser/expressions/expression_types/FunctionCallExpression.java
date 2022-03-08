package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class FunctionCallExpression implements Expression {
	
	public FunctionCallExpression next;
	public Object callee;
	public Token name;
	public EArrayList<Token> generics;
	public final EArrayList<Expression> args;
	
	public FunctionCallExpression(Expression calleeIn, EArrayList<Expression> argsIn) { this(null, calleeIn, null, argsIn); }
	public FunctionCallExpression(Token nameIn, EArrayList<Expression> argsIn) { this(null, null, nameIn, argsIn); }
	public FunctionCallExpression(Expression calleeIn, Token nameIn, EArrayList<Expression> argsIn) { this(null, calleeIn, nameIn, argsIn); }
	public FunctionCallExpression(FunctionCallExpression nextIn, Expression calleeIn, Token nameIn, EArrayList<Expression> argsIn) {
		this(nextIn, (Object) calleeIn, nameIn, argsIn);
	}
	
	private FunctionCallExpression(FunctionCallExpression nextIn, Object calleeIn, Token nameIn, EArrayList<Expression> argsIn) {
		next = nextIn;
		callee = calleeIn;
		name = nameIn;
		args = argsIn;
	}
	
	public FunctionCallExpression copy(FunctionCallExpression in) {
		return new FunctionCallExpression(in.next, in.callee, in.name, in.args);
	}
	
	public FunctionCallExpression setCallee(Object in) {
		FunctionCallExpression c = copy(this);
		c.callee = in;
		return c;
	}
	
	public FunctionCallExpression addNext(FunctionCallExpression nextIn) {
		next = nextIn;
		return this;
	}
	
	public FunctionCallExpression applyNext(Object in) {
		FunctionCallExpression c = copy(this.next);
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
