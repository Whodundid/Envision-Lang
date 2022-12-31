package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Expr_FunctionCall implements Expression {
	
	public Expr_FunctionCall next;
	public Expression callee;
	public Token<?> name;
	public EList<Token<?>> generics;
	public final EList<Expression> args;
	public final Token<?> definingToken;
	
	public Expr_FunctionCall(Expression calleeIn, EList<Expression> argsIn) { this(null, calleeIn, null, argsIn); }
	public Expr_FunctionCall(Token nameIn, EList<Expression> argsIn) { this(null, null, nameIn, argsIn); }
	public Expr_FunctionCall(Expression calleeIn, Token<?> nameIn, EList<Expression> argsIn) { this(null, calleeIn, nameIn, argsIn); }
	public Expr_FunctionCall(Expr_FunctionCall nextIn, Expression calleeIn, Token<?> nameIn, EList<Expression> argsIn) {
		next = nextIn;
		callee = calleeIn;
		name = nameIn;
		args = argsIn;
		definingToken = calleeIn.definingToken();
	}
	
	public Expr_FunctionCall copy(Expr_FunctionCall in) {
		return new Expr_FunctionCall((Expr_FunctionCall) in.next.copy(), in.callee.copy(), in.name.copy(), in.args);
	}
	
	public Expr_FunctionCall addNext(Expr_FunctionCall nextIn) {
		next = nextIn;
		return this;
	}
	
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
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleMethodCall_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
