package envision_lang.interpreter.expressions;

import static envision_lang.lang.natives.EnvisionStaticTypes.*;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Literal;
import envision_lang.parser.expressions.expression_types.Expr_Primitive;
import envision_lang.parser.expressions.expression_types.Expr_TypeOf;

/**
 * Compares the underlying datatype of two given objects.
 * 
 * @author Hunter Bragg
 */
public class IE_TypeOf extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_TypeOf e) {
		ParsedExpression left = e.left;
		ParsedExpression right = e.right;
		boolean is = e.is;
		
		IDatatype typeA = null, typeB = null;
		
		// convert literals and primitives to their internal datatype
		if (left instanceof Expr_Literal literal) typeA = literal.literalToken.getPrimitiveDataType();
		if (left instanceof Expr_Primitive primitive) typeA = primitive.primitiveType.getPrimitiveType();
		
		if (right instanceof Expr_Literal literal) typeB = literal.literalToken.getPrimitiveDataType();
		if (right instanceof Expr_Primitive primitive) typeB = primitive.primitiveType.getPrimitiveType();
		
		// if typeA is still null, attempt to evaluate its expression and grab its datatype
		if (typeA == null) {
			var lobj = interpreter.evaluate(left);
			typeA = lobj.getDatatype();
		}
		
		// if typeB is still null, attempt to evaluate its expression and grab its datatype
		if (typeB == null) {
			var robj = interpreter.evaluate(right);
			typeB = robj.getDatatype();
		}
		
		boolean same = compare_datatypes(typeA, typeB);
		boolean val = (is) ? same : !same;
		
		return EnvisionBooleanClass.valueOf(val);
	}
	
	public static boolean compare_datatypes(IDatatype toCheck, IDatatype expected) {
		if (expected == null || toCheck == null) {
			throw new EnvisionLangError("Error! Comparing Java::NULL types!");
		}
		
		// always accept expected vars except if they are null or void
		if (VAR_TYPE.compare(expected)) return !NULL_TYPE.compare(toCheck) &&
			                                   !VOID_TYPE.compare(toCheck);
		
		// check if an int or double is going into number
		if (NUMBER_TYPE.compare(expected)) return NUMBER_TYPE.compare(toCheck) ||
			                                      INT_TYPE.compare(toCheck) ||
			                                      DOUBLE_TYPE.compare(toCheck);
		
		// if none of the above, check for exact datatype match
		return expected.compare(toCheck);
	}
	
}
