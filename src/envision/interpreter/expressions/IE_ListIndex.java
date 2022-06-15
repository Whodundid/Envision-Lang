package envision.interpreter.expressions;

import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionString;
import envision.lang.util.StaticTypes;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_ListIndex;
import envision.tokenizer.Operator;

public class IE_ListIndex extends ExpressionExecutor<Expr_ListIndex> {

	public IE_ListIndex(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_ListIndex e) {
		return new IE_ListIndex(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_ListIndex expression) {
		Expression list = expression.list;
		Expression index = expression.index;
		
		EnvisionObject theList = evaluate(list);
		EnvisionObject theIndex = evaluate(index);
		
		//System.out.println(theIndex.getDatatype() + " : " + EnvisionDatatype.INT_TYPE);
		//only allow integers to be used for the array index
		if (!StaticTypes.INT_TYPE.compare(theIndex.getDatatype()))
			throw new InvalidDatatypeError(StaticTypes.INT_TYPE, theIndex.getDatatype());
		
		EnvisionInt int_index = (EnvisionInt) theIndex;
		
		//check for class instance and attempt to handle operators
		if (theList instanceof ClassInstance inst && inst.supportsOperator(Operator.ARRAY_OP)) {
			return OperatorOverloadHandler.handleOverload(interpreter, null, Operator.ARRAY_OP, inst, theIndex);
		}
		
		//THESE SHOULD BE MOVED TO THEIR RESPECTIVE CLASSES!
		
		//otherwise check for other valid array types
		if (theList instanceof EnvisionList env_list) return env_list.get(int_index);
		if (theList instanceof EnvisionString env_str) return env_str.charAt(int_index);
		
		/*
		if (theList instanceof EnvisionEnum) {
			EnvisionEnum en = (EnvisionEnum) theList;
			return en.getValues().get((int) (long) theIndex);
		}
		*/
		
		throw new InvalidTargetError(theList + " is not a valid target for '[]'!");
	}
	
}
