package envision.interpreter.expressions;

import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.UnexpectedTypeError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.classes.ClassInstance;
import envision.lang.enums.EnvisionEnum;
import envision.lang.objects.EnvisionList;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionString;
import envision.lang.variables.EnvisionVariable;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.ListIndexExpression;
import envision.tokenizer.Keyword;
import eutil.math.NumberUtil;

public class IE_ListIndex extends ExpressionExecutor<ListIndexExpression> {

	public IE_ListIndex(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(ListIndexExpression expression) {
		Expression list = expression.list;
		Expression index = expression.index;
		
		Object theList = evaluate(list);
		Object theIndex = EnvisionVariable.convert(evaluate(index));
		
		if (!NumberUtil.isInteger(theIndex)) { throw new UnexpectedTypeError(EnvisionDataType.getDataType(theIndex), EnvisionDataType.INT); }
		
		Object r = null;
		
		if (theList instanceof EnvisionList) { return ((EnvisionList) theList).get((long) theIndex); }
		if (theList instanceof EnvisionString) { return ((EnvisionString) theList).charAt((int) (long) theIndex); }
		if (theList instanceof String) { return ((String) theList).charAt((int) (long) theIndex); }
		
		if (theList instanceof ClassInstance) {
			ClassInstance ci = (ClassInstance) theList;
			return OperatorOverloadHandler.handleOverload(interpreter, Keyword.ARRAY_OPERATOR, ci, theIndex);
		}
		
		if (theList instanceof EnvisionEnum) {
			EnvisionEnum en = (EnvisionEnum) theList;
			return en.getValues().get((int) (long) theIndex);
		}
		
		throw new InvalidTargetError(theList + " is not a valid target for '[]'!");
	}
	
	public static Object run(EnvisionInterpreter in, ListIndexExpression e) {
		return new IE_ListIndex(in).run(e);
	}
	
}
