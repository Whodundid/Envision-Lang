package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.ArithmeticError;
import envision_lang.exceptions.errors.FinalVarReassignmentError;
import envision_lang.exceptions.errors.InvalidTargetError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.CastingUtil;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.packages.EnvisionPackage;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class IE_Assign extends ExpressionExecutor<Expr_Assign> {

	private String name;
	private Integer dist;
	private Operator op;
	private EnvisionObject obj;
	private EnvisionObject value;
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	protected IE_Assign(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Assign e) {
		return new IE_Assign(in).run(e);
	}

	public static EnvisionObject handleAssign(EnvisionInterpreter in, Expr_Binary e, Operator opIn) {
		if (e.left instanceof Expr_Var v) {
			IE_Assign inst = new IE_Assign(in);
			
			String name = v.getName();
			EnvisionObject value = in.evaluate(e.right);
			Operator op = opIn;
			
			return inst.execute(name, value, op);
		}
		
		throw new InvalidTargetError("Expected a valid BinaryExpression with a left-handed var assignment model! Got: '" + e + "' instead!");
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	@Override
	public EnvisionObject run(Expr_Assign expression) {
		Token name_token = expression.name;
		String name = (name_token != null) ? name_token.lexeme : null;
		//Expr_Assign leftAssign = expression.leftAssign;
		EnvisionObject value = evaluate(expression.value);
		Operator op = expression.operator;
		
		//handle left-hand assignment expressions
		//if (leftAssign != null) {
			//EnvisionObject left_result = run(leftAssign);
			
			//Set the target name from the evaluated assignment expression.
			//The assignment expression result SHOULD only return an EnvisionObject
			//or a String for the identifier name. If it is neither, throw error
			//if (left_result instanceof EnvisionString)
			//if (left_result instanceof EnvisionObject env_obj) name = leftAssign.getName();
			//else if (left_result instanceof String str) name = str;
			//else throw new InvalidTargetError("The object '" + left_result + "' is an invalid assignment target!");
		//}
		
		return execute(name, value, op);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	private EnvisionObject execute(String nameIn, EnvisionObject valueIn, Operator opIn) {
		name = nameIn;
		value = valueIn;
		op = opIn;
		obj = scope().get(name);
		
		//if the given object is a class instance and supports
		//the given operator, run the operator overload
		if (obj instanceof ClassInstance env_class) {
			//System.out.println(env_class + " : " + env_class.getClass() + " : " + op);
			if (env_class.supportsOperator(op)) {
				return OperatorOverloadHandler.handleOverload(interpreter, name, op, env_class, value);
			}
		}
		
		//otherwise, handle default assignment
		if (op == Operator.ASSIGN) return assign();
		/*
		return switch (op) {
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
		*/
		//error if this point is reached
		throw new EnvisionLangError("Invalid assignment operator! " + op);
	}

	private EnvisionObject assign() {
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
	public static EnvisionObject assign(EnvisionInterpreter interpreter,
										String name,
										EnvisionObject obj,
										EnvisionObject value) {
		IScope s = interpreter.scope();
		//System.out.println(name + " -- " + obj + " -- " + value);
		
		//---------------------------------------------------------
		
		//the name of the variable
		String var_name = name;
		//the new value to be assigned the the variable
		EnvisionObject assignment_value = value;
		//the datatype of the new assignment value
		IDatatype var_datatype = null;
		//the existing variable -- if present
		EnvisionObject var_obj = obj;
		
		//---------------------------------------------------------
		
		//handle specific assignment_value types
		if (assignment_value instanceof EnvisionVariable env_var) assignment_value = env_var.get();
		else assignment_value = value;
		
		//System.out.println(var_name + " : " + assignment_value);
		
		//grab datatype
		var_datatype = assignment_value.getDatatype();
		//if (assignment_value instanceof ClassInstance inst) var_datatype = inst.getDatatype();
		//dynamically determine assignment value type
		//else var_datatype = EnvisionDatatype.dynamicallyDetermineType(assignment_value);
		
		//in the event that the incomming value is a string, format the input
		if (var_datatype.isString()) {
			String prt = EnvisionStringFormatter.formatPrint(interpreter, assignment_value);
			assignment_value = EnvisionStringClass.newString(prt);
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
			s.define(var_name, StaticTypes.VAR_TYPE, var_obj);
		}
		//if the object does exist, attempt to assign the new value to it
		else {
			//don't allow final values to be reassigned
			if (var_obj.isFinal()) throw new FinalVarReassignmentError(var_obj, assignment_value);
			
			//only allow the same type assignment for strong variables
			if (var_obj.isStrong()) {
				CastingUtil.assert_expected_datatype(var_obj.getDatatype(), var_datatype);
			}
			
			EnvisionObject theObject = ObjectCreator.wrap(assignment_value);
			if (theObject.isPrimitive()) theObject = theObject.copy();
			
			//assign new value to existing variable
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
		if (in instanceof EnvisionPackage env_pkg) return env_pkg;
		return null;
	}
	
	/**
	 * This method specifically relates to the '+=' operation and
	 * can only be applied to EnvisionVariables and EnvisionLists.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject add() {
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
			return new_obj;
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '-=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject sub() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
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
	/*
	private EnvisionObject mul() {
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
			return new_obj;
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '/=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject div() {
		//don't allow null vars
		if (obj == null) throw new NullVariableError(name);
		//don't allow final reassignment
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
		//don't allow non-numbers to advance
		if (!(obj instanceof EnvisionNumber)) throw new NotANumberError(obj);
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		EnvisionDatatype var_type = var.getDatatype();
		Primitives var_ptype = var_type.getPrimitiveType();
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '%=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject mod() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '&=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject and() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '|=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject or() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '^=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject xor() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '<<=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject shiftL() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '>>=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject shiftR() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	/**
	 * This method specifically relates to the '>>>=' operation and
	 * can only be applied to EnvisionInt and EnvisionDouble.
	 * 
	 * @return the new value
	 */
	/*
	private EnvisionObject arithShiftR() {
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
		Object var_val = var.get_i();
		
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
		
		return var;
	}
	*/
	
	//---------------------------------------------------------------------------
	
	private void assert_number(IDatatype type) {
		if (!type.isNumber()) {
			throw new ArithmeticError("Invalid operation: '" + type + "'! Can only operate on numbers!'");
		}
	}
	
}	
