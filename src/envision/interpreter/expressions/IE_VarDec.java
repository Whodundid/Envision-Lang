package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionNull;
import envision.lang.natives.NativeTypeManager;
import envision.lang.util.VisibilityType;
import envision.parser.expressions.expression_types.Expr_VarDef;
import envision.tokenizer.IKeyword;
import envision.tokenizer.Token;

public class IE_VarDec extends ExpressionExecutor<Expr_VarDef> {

	public IE_VarDec(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_VarDef e) {
		return new IE_VarDec(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_VarDef e) {
		Token typeToken = e.type;
		IKeyword k = typeToken.keyword;
		
		if (k.isDataType()) {
			var type = NativeTypeManager.datatypeOf(typeToken.getPrimitiveDataType());
			var obj = ObjectCreator.createDefault(type, false);
			if (obj != null) obj.setVisibility(VisibilityType.PUBLIC);
			return obj;
		}
		
		return EnvisionNull.NULL;
	}
	
}
