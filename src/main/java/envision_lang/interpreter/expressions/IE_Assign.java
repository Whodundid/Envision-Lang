package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.CastingUtil;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.ArithmeticError;
import envision_lang.lang.exceptions.errors.FinalVarReassignmentError;
import envision_lang.lang.exceptions.errors.InvalidTargetError;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.lang.packages.EnvisionLangPackage;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class IE_Assign extends AbstractInterpreterExecutor {

	//private String name;
	//private Integer dist;
	//private Operator op;
	//private EnvisionObject obj;
	//private EnvisionObject value;
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------

	public static EnvisionObject handleAssign(EnvisionInterpreter interpreter, Expr_Binary e, Operator opIn) {
		if (e.left instanceof Expr_Var v) {
			String name = v.getName();
			EnvisionObject value = interpreter.evaluate(e.right);
			Operator op = opIn;
			
			return executeAssign(interpreter, name, value, op);
		}
		
		throw new InvalidTargetError("Expected a valid BinaryExpression with a left-handed var assignment model! Got: '" + e + "' instead!");
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Assign expression) {
		Token name_token = expression.name;
		String name = (name_token != null) ? name_token.getLexeme() : null;
		EnvisionObject value = interpreter.evaluate(expression.value);
		Operator op = expression.operator;
		
		return executeAssign(interpreter, name, value, op);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	private static EnvisionObject executeAssign(EnvisionInterpreter interpreter,
												String nameIn,
												EnvisionObject valueIn,
												Operator opIn)
	{
		String name = nameIn;
		EnvisionObject value = valueIn;
		Operator op = opIn;
		EnvisionObject obj = interpreter.scope().get(name);
		
		// if the given object is a class instance and supports
		// the given operator, run the operator overload
		if (obj instanceof ClassInstance env_class) {
			if (env_class.supportsOperator(op)) {
				return OperatorOverloadHandler.handleOverload(interpreter, name, op, env_class, value);
			}
		}
		
		// otherwise, handle default assignment
		if (op == Operator.ASSIGN) {
			return assign(interpreter, name, obj, value);
		}
		
		// error if this point is reached
		throw new EnvisionLangError("Invalid assignment operator! " + op);
	}
	
	/**
	 * Direct variable value assignment.
	 * <p>
	 * This method will attempt assign the given 'value' to the given 'obj'.
	 * If the given 'obj' does not exist, then a new variable under the given
	 * 'name' will be created instead.
	 * 
	 * @param executor
	 * @param name the name of the variable
	 * @param obj the object being assigned (if present)
	 * @param value the new value to be assigned
	 * @return the new value
	 */
	public static EnvisionObject assign(EnvisionInterpreter interpreter,
										String name,
										EnvisionObject obj,
										EnvisionObject value) {
		IScope s = interpreter.scope();
		
		//---------------------------------------------------------
		
		// the name of the variable
		String var_name = name;
		// the new value to be assigned the the variable
		EnvisionObject assignment_value = value;
		// the datatype of the new assignment value
		IDatatype var_datatype = null;
		// the existing variable -- if present
		EnvisionObject var_obj = obj;
		
		//---------------------------------------------------------
		
		// handle specific assignment_value types
		if (assignment_value instanceof EnvisionVariable env_var) assignment_value = env_var.get();
		
		// grab datatype
		var_datatype = assignment_value.getDatatype();
		
		// in the event that the incomming value is a string, format the input
		if (var_datatype.isString()) {
			String prt = EnvisionStringFormatter.formatPrint(interpreter, assignment_value);
			assignment_value = EnvisionStringClass.valueOf(prt);
		}
		
		//---------------------------------------------------------
		
		// if obj is null, attempt to define variable on the spot
		if (var_obj == null) {
			// if the assignment_value is a reference object (function, class, list, etc.)
			// create a reference to the same object instead of creating a new object instance
			var_obj = checkObjectConversion(assignment_value);
			
			// if the assignment_value was not an object conversion - create new object
			if (var_obj == null) {
				var_obj = ObjectCreator.createObject(var_datatype, assignment_value, false);
			}
			
			// define as 'var' type variable
			s.define(var_name, StaticTypes.VAR_TYPE, var_obj);
		}
		// if the object does exist, attempt to assign the new value to it
		else {
			// don't allow final values to be reassigned
			if (var_obj.isFinal()) throw new FinalVarReassignmentError(var_obj, assignment_value);
			
			// only allow the same type assignment for strong variables
			if (var_obj.isStrong()) {
				CastingUtil.assert_expected_datatype(var_obj.getDatatype(), var_datatype);
			}
			
			EnvisionObject theObject = ObjectCreator.wrap(assignment_value);
			if (theObject.isPassByValue()) theObject = theObject.copy();
			
			// assign new value to existing variable
			s.set(var_name, theObject);
		}
		
		return assignment_value;
	}
	
	private static EnvisionObject checkObjectConversion(EnvisionObject in) {
		if (in instanceof EnvisionFunction env_func) return env_func;
		if (in instanceof EnvisionList env_list) return env_list;
		if (in instanceof EnvisionClass env_class) return env_class;
		if (in instanceof ClassInstance env_inst) return env_inst;
		//if (in instanceof EnvisionEnum env_enum) return env_enum;
		//if (in instanceof EnumValue env_enum_value) return env_enum_value;
		if (in instanceof EnvisionCodeFile env_code) return env_code;
		if (in instanceof EnvisionLangPackage env_pkg) return env_pkg;
		return null;
	}
	
	private static void assert_number(IDatatype type) {
		if (!type.isNumber()) {
			throw new ArithmeticError("Invalid operation: '" + type + "'! Can only operate on numbers!'");
		}
	}
	
}	
