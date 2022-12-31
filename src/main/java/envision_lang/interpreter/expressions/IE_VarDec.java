package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.util.EnvisionVis;
import envision_lang.parser.expressions.expression_types.Expr_VarDef;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Token;

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
		IKeyword k = typeToken.getKeyword();
		
		if (k.isDataType()) {
			var type = NativeTypeManager.datatypeOf(typeToken.getPrimitiveDataType());
			var obj = ObjectCreator.createDefault(type, false);
			if (obj != null) obj.setVisibility(EnvisionVis.PUBLIC);
			return obj;
		}
		
		return EnvisionNull.NULL;
	}
	
}
