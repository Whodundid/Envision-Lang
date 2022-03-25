package envision.interpreter.expressions;

import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArithmeticError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.NotANumberError;
import envision.exceptions.errors.NotAVariableError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.StrongVarReassignmentError;
import envision.exceptions.errors.listErrors.SelfAdditionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.CastingUtil;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.packages.EnvisionPackage;
import envision.parser.expressions.expression_types.Expr_Assign;
import envision.parser.expressions.expression_types.Expr_Binary;
import envision.parser.expressions.expression_types.Expr_Var;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;
import eutil.strings.StringUtil;

public class IE_Assign extends ExpressionExecutor<Expr_Assign> {

	private String name;
	private Object value;
	private Integer dist;
	private Operator op;
	private EnvisionObject obj;
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	protected IE_Assign(EnvisionInterpreter in) {
		super(in);
	}

	public static Object handleAssign(EnvisionInterpreter in, Expr_Binary e, Operator opIn) {
		if (e.left instanceof Expr_Var v) {
			IE_Assign inst = new IE_Assign(in);
			
			String name = v.getName();
			Object value = in.evaluate(e.right);
			Operator op = opIn;
			
			return inst.execute(name, value, op);
		}
		
		throw new InvalidTargetError("Expected a valid BinaryExpression with a left-handed var assignment model! Got: '" + e + "' instead!");
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Assign e) {
		return new IE_Assign(in).run(e);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	@Override
	public Object run(Expr_Assign expression) {
		Token name_token = expression.name;
		String name = (name_token != null) ? name_token.lexeme : null;
		Expr_Assign leftAssign = expression.leftAssign;
		Object value = evaluate(expression.value);
		Operator op = expression.operator;
		
		//handle left-hand assignment expressions
		if (leftAssign != null) {
			Object left_result = run(leftAssign);
			
			//Set the target name from the evaluated assignment expression.
			//The assignment expression result SHOULD only return an EnvisionObject
			//or a String for the identifier name. If it is neither, throw error
			if (left_result instanceof EnvisionObject env_obj) name = leftAssign.getName();
			else if (left_result instanceof String str) name = str;
			else throw new InvalidTargetError("The object '" + left_result + "' is an invalid assignment target!");
		}
		
		return execute(name, value, op);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	private Object execute(String nameIn, Object valueIn, Operator opIn) {
		name = nameIn;
		value = EnvisionObject.convert(valueIn);
		op = opIn;
		obj = scope().get(name);
		
		if (obj instanceof ClassInstance env_class) {
			return OperatorOverloadHandler.handleOverload(interpreter, op, env_class, value);
		}
		else return switch (op) {
		case ASSIGN -> assign();
		case ADD_ASSIGN -> add();
		case SUB_ASSIGN -> sub();
		case MUL_ASSIGN -> mul();
		case DIV_ASSIGN -> div();
		case MOD_ASSIGN -> mod();
		case BW_AND_ASSIGN -> and();
		case BW_OR_ASSIGN -> or();
		case BW_XOR_ASSIGN -> xor();
		case SHL_ASSIGN -> shiftL();
		case SHR_ASSIGN -> shiftR();
		case SHR_AR_ASSIGN -> arithShiftR();
		default -> throw new EnvisionError("Invalid assignment operator! " + op);
		};
	}

	private Object assign() {
		return assign(interpreter, name, obj, value);
	}
	
	/**
	 * Direct variable value assignment.
	 * <p>
	 * This method will attempt assign the given 'value' to the given 'obj'.
	 * If the given 'obj' does not exist, then a new variable under the given
	 * 'name' will be created instead.
	 * 
	 * @param interpreter
	 * @param name the name of the variable
	 * @param obj the object being assigned (if present)
	 * @param value the new value to be assigned
	 * @return the new value
	 */
	public static Object assign(EnvisionInterpreter interpreter, String name, EnvisionObject obj, Object value) {
		Scope s = interpreter.scope();
		//System.out.println(name + " -- " + obj + " -- " + value);
		
		//---------------------------------------------------------
		
		//the name of the variable
		String var_name = name;
		//the new value to be assigned the the variable
		Object assignment_value = value;
		//the datatype of the new assignment value
		EnvisionDatatype var_datatype = null;
		//the existing variable -- if present
		EnvisionObject var_obj = obj;
		
		//---------------------------------------------------------
		
		//handle specific assignment_value types
		if (assignment_value instanceof EnvisionVariable env_var) assignment_value = env_var.get();
		else if (assignment_value instanceof EnvisionObject env_obj) assignment_value = env_obj;
		
		//dynamically determine assignment value type
		var_datatype = EnvisionDatatype.dynamicallyDetermineType(assignment_value);
		
		//in the event that the incomming value is a string, format the input
		if (var_datatype.isString()) {
			assignment_value = EnvisionStringFormatter.formatPrint(interpreter, assignment_value);
		}
		
		//---------------------------------------------------------
		
		//if obj is null, attempt to define variable on the spot
		if (var_obj == null) {
			//if the assignment_value is a reference object (function, class, list, etc.)
			//create a reference to the same object instead of creating a new object instance
			var_obj = checkObjectConversion(assignment_value);
			
			//if the assignment_value was not an object conversion - create new object
			if (var_obj == null) {
				var_obj = ObjectCreator.createObject(var_datatype, assignment_value, false);
			}
			
			//define as 'var' type variable
			s.define(var_name, EnvisionDatatype.NULL_TYPE, var_obj);
		}
		//if the object does exist, attemt to assign the new value to it
		else {
			//don't allow final values to be reassigned
			if (var_obj.isFinal()) throw new FinalVarReassignmentError(var_obj, assignment_value);
			
			//only allow the same type assignment for strong variables
			if (var_obj.isStrong()) {
				CastingUtil.assert_expected_datatype(var_obj.getDatatype(), var_datatype);
			}
			
			//assign new value to existing variable
			s.set(var_name, ObjectCreator.wrap(assignment_value));
		}
		
		return assignment_value;
	}
	
	private static EnvisionObject checkObjectConversion(Object in) {
		if (in instanceof EnvisionFunction env_func) return env_func;
		if (in instanceof EnvisionList env_list) return env_list;
		if (in instanceof EnvisionClass env_class) return env_class;
		if (in instanceof ClassInstance env_inst) return env_inst;
		//if (in instanceof EnvisionEnum env_enum) return env_enum;
		//if (in instanceof EnumValue env_enum_value) return env_enum_value;
		if (in instanceof EnvisionCodeFile env_code) return env_code;
		if (in instanceof EnvisionPackage env_pkg) return env_pkg;
		return null;
	}
	
	/**
	 * This method specifically relates to the '+=' operation and
	 * can only be applied to EnvisionVariables and EnvisionLists.
	 * 
	 * @return the new value
	 */
	private Object add() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		
		//check for list additions
		if (obj instanceof EnvisionList env_list) {
			if (value == env_list) throw new SelfAdditionError(env_list);
			if (value instanceof EnvisionObject env_obj) env_list.add(env_obj);
			else {
				EnvisionDatatype type = EnvisionDatatype.dynamicallyDetermineType(value);
				EnvisionObject obj = ObjectCreator.createObject(type, value, false, false);
				env_list.add(obj);
			}
			return env_list;
		}
		
		//don't allow forward if not a variable
		if (!(obj instanceof EnvisionVariable)) throw new NotAVariableError(obj);
		
		//---------------------------------------------------------
	
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		
		//switch on 'Left' object's datatype
		switch (var_ptype) {
		case CHAR:
		{
			//due to the fact that char additions require the datatype being
			//upgraded to a string, check for strong char and error if true
			if (var.isStrong()) throw new StrongVarReassignmentError(var, "");
			
			//char additions require the char to be upgraded to a string
			var new_val = new StringBuilder(String.valueOf((char) var_val));
			new_val.append(value);
			
			//to upgrade the datatype from char -> string requires creating new string object
			EnvisionString new_obj = EnvisionStringClass.newString(new_val.toString());
			
			//assign new value to vars and immediately return to stop double assignment
			scope().set(name, new_obj.getDatatype(), new_obj);
			return new_val;
		}
		case DOUBLE:
		{
			assert_number(value_type);
			var_val = ((Double) var_val) + ((Number) value).doubleValue();
			var.set_i(var_val);
			break;
		}
		case INT:
		{
			assert_number(value_type);
			var_val = ((Long) var_val) + ((Number) value).longValue();
			var.set_i(var_val);
			break;
		}
		case STRING:
		{
			var_val = ((String) var_val) + value;
			var_val = EnvisionStringFormatter.formatPrint(interpreter, var_val);
			break;
		}
		default:
			throw new ArithmeticError("The operation: '+=' is invalid for the given object: '" +
									  obj + "' (" + var_ptype + ")!");
		};
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '-=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object sub() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//dont allow non-numbers
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get incomming value type
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE: var_val = ((Double) var_val) - ((Number) value).doubleValue(); break;
		case INT: var_val = ((Long) var_val) - ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '-=' is invalid for the given object: '" +
									  obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '*=' operation and
	 * can only be applied to the following types:
	 * 		EnvisionChar,
	 * 		EnvisionInt,
	 * 		EnvisionDouble,
	 * 		EnvisionString.
	 * 
	 * @return the new value
	 */
	private Object mul() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-variables to advance
		if (!(obj instanceof EnvisionVariable)) throw new NotAVariableError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case CHAR:
		{
			//due to the fact that char mul_additions require the datatype being
			//upgraded to a string, check for strong char and error if true
			if (var.isStrong()) throw new StrongVarReassignmentError(var, "");
			
			//char mul_additions require the char to be upgraded to a string
			var old_val = (char) var_val;
			var new_val = new StringBuilder(String.valueOf(old_val));
			for (int i = 0; i < ((Number) value).longValue(); i++) {
				new_val.append(old_val);
			}
			
			//to upgrade the datatype from char -> string requires creating new string object
			EnvisionString new_obj = EnvisionStringClass.newString(new_val.toString());
			
			//assign new value to vars and immediately return to stop double assignment
			scope().set(name, new_obj.getDatatype(), new_obj);
			return new_val;
		}
		case DOUBLE:
		{
			var_val = ((Double) var_val) * ((Number) value).doubleValue();
			break;
		}
		case INT:
		{
			var_val = ((Long) var_val) * ((Number) value).longValue();
			break;
		}
		case STRING:
		{
			var_val = StringUtil.repeatString((String) var_val, ((Number) value).intValue());
			break;
		}
		default:
			throw new ArithmeticError("The operation: '*=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '/=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object div() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE: var_val = ((Double) var_val) / ((Number) value).doubleValue(); break;
		case INT: var_val = ((Long) var_val) / ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '/=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '%=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object mod() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE: var_val = ((Double) var_val) % ((Number) value).doubleValue(); break;
		case INT: var_val = ((Long) var_val) % ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '%=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '&=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object and() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE:
		case INT: var_val = ((Number) var_val).longValue() & ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '&=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '|=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object or() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE:
		case INT: var_val = ((Number) var_val).longValue() | ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '|=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '^=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object xor() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE:
		case INT: var_val = ((Number) var_val).longValue() ^ ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '^=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '<<=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object shiftL() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE:
		case INT: var_val = ((Number) var_val).longValue() << ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '<<=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '>>=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object shiftR() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE:
		case INT: var_val = ((Number) var_val).longValue() >> ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '>>=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	/**
	 * This method specifically relates to the '>>>=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	private Object arithShiftR() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		var var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get();
		
		//get datatype of incoming value
		EnvisionDatatype value_type = EnvisionDatatype.dynamicallyDetermineType(value);
		assert_number(value_type);
		
		//determine variable type
		switch (var_ptype) {
		case DOUBLE:
		case INT: var_val = ((Number) var_val).longValue() >>> ((Number) value).longValue(); break;
		default:
			throw new ArithmeticError("The operation: '>>>=' is invalid for the given object: '"
									  + obj + "' (" + var_type + ")!");
		}
		
		//assign the new value
		var.set_i(var_val);
		
		return var_val;
	}
	
	//---------------------------------------------------------------------------
	
	private void assert_number(EnvisionDatatype type) {
		assert_number(type.getPrimitiveType());
	}
	
	private void assert_number(Primitives type) {
		if (!type.isNumber()) {
			throw new ArithmeticError("Invalid operation: '" + type + "'! Can only operate on numbers!'");
		}
	}
	
}	
