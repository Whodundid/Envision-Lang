package envision_lang.parser.expressions.expression_types.unused;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Expr_Enum extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> name;
	public final EList<ParsedExpression> args;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Enum(Token<?> nameIn, EList<ParsedExpression> argsIn) {
		super(nameIn);
		name = nameIn;
		args = argsIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String a = (args != null && args.isNotEmpty()) ? "(" + EStringUtil.toString(args, ", ") + ")" : "";
		return name.getLexeme() + a;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return null;
//		return handler.handleEnum_E(this);
	}
	
}
