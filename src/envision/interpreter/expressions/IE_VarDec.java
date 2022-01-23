package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.types.VarDecExpression;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;

public class IE_VarDec extends ExpressionExecutor<VarDecExpression> {

	public IE_VarDec(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(VarDecExpression e) {
		Token typeToken = e.type;
		Keyword k = typeToken.keyword;
		
		if (k.isDataType()) { return ObjectCreator.createObject(typeToken.getDataType()); }
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, VarDecExpression e) {
		return new IE_VarDec(in).run(e);
	}
	
}
