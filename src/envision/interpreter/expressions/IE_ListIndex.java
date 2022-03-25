package envision.interpreter.expressions;

import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.UnexpectedTypeError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.classes.ClassInstance;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.Primitives;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_ListIndex;
import envision.tokenizer.Operator;
import eutil.math.NumberUtil;

public class IE_ListIndex extends ExpressionExecutor<Expr_ListIndex> {

	public IE_ListIndex(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_ListIndex expression) {
		Expression list = expression.list;
		Expression index = expression.index;
		
		Object theList = evaluate(list);
		Object theIndex = EnvisionVariable.convert(evaluate(index));
		
		if (!NumberUtil.isInteger(theIndex)) { throw new UnexpectedTypeError(Primitives.getDataType(theIndex), Primitives.INT); }
		
		//Object r = null;
		
		if (theList instanceof EnvisionList env_list) return env_list.get((long) theIndex);
		if (theList instanceof EnvisionString env_str) return env_str.charAt((int) (long) theIndex);
		if (theList instanceof String str) return str.charAt((int) (long) theIndex);
		
		if (theList instanceof ClassInstance) {
			ClassInstance ci = (ClassInstance) theList;
			return OperatorOverloadHandler.handleOverload(interpreter, Operator.ARRAY_OP, ci, theIndex);
		}
		
		/*
		if (theList instanceof EnvisionEnum) {
			EnvisionEnum en = (EnvisionEnum) theList;
			return en.getValues().get((int) (long) theIndex);
		}
		*/
		
		throw new InvalidTargetError(theList + " is not a valid target for '[]'!");
	}
	
	public static Object run(EnvisionInterpreter in, Expr_ListIndex e) {
		return new IE_ListIndex(in).run(e);
	}
	
}
