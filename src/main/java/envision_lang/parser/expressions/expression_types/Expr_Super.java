package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Expr_Super extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> target;
	public final EList<ParsedExpression> args;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Super(Token start, Token<?> methodIn) { this(start, methodIn, null); }
	public Expr_Super(Token start, Token<?> methodIn, EList<ParsedExpression> argsIn) {
		super(start);
		target = methodIn;
		args = argsIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String a = (args != null) ? "(" + ((args.isNotEmpty()) ? EStringUtil.toString(args, ", ") : "") + ")" : "";
		return "super." + target.getLexeme() + a;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleSuper_E(this);
	}
	
}
