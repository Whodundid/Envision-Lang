package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.natives.EnvisionVisibilityModifier;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.parser.expressions.expression_types.Expr_VarDef;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Token;

public class IE_VarDec extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_VarDef e) {
		Token<?> typeToken = e.type;
		IKeyword k = typeToken.getKeyword();
		
		if (k.isDataType()) {
			var type = NativeTypeManager.datatypeOf(typeToken.getPrimitiveDataType());
			var obj = ObjectCreator.createDefault(type, false);
			if (obj != null) obj.setVisibility(EnvisionVisibilityModifier.PUBLIC);
			return obj;
		}
		
		return EnvisionNull.NULL;
	}
	
}
