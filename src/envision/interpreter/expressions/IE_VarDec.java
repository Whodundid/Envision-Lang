package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.parser.expressions.expression_types.Expr_VarDef;
import envision.tokenizer.IKeyword;
import envision.tokenizer.Token;

public class IE_VarDec extends ExpressionExecutor<Expr_VarDef> {

	public IE_VarDec(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_VarDef e) {
		Token typeToken = e.type;
		IKeyword k = typeToken.keyword;
		
		if (k.isDataType()) {
			var type = new EnvisionDatatype(typeToken.getPrimitiveDataType());
			return ObjectCreator.createDefault(EnvisionObject.DEFAULT_NAME, type, false);
		}
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_VarDef e) {
		return new IE_VarDec(in).run(e);
	}
	
}
