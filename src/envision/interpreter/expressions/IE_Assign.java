package envision.interpreter.expressions;

import envision.exceptions.errors.ArithmeticError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.NotAVariableError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.listErrors.SelfAdditionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.interpreter.util.creationUtil.CastingUtil;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionList;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;
import envision.parser.expressions.types.AssignExpression;
import envision.parser.expressions.types.BinaryExpression;
import envision.parser.expressions.types.VarExpression;
import envision.tokenizer.Keyword;
import eutil.strings.StringUtil;

public class IE_Assign extends ExpressionExecutor<AssignExpression> {

	private String name;
	private Object value;
	private Integer dist;
	private Keyword op;
	private EnvisionObject obj;
	
	protected IE_Assign(EnvisionInterpreter in) {
		super(in);
	}

	public static Object handleAssign(EnvisionInterpreter in, BinaryExpression e, Keyword opIn) {
		if (e.left instanceof VarExpression) {
			IE_Assign inst = new IE_Assign(in);
			
			String name = ((VarExpression) e.left).getName();
			Object value = in.evaluate(e.right);
			Keyword op = opIn;
			
			return inst.execute(name, value, op);
		}
		
		throw new InvalidTargetError("Expected a valid BinaryExpression with a left-handed var assignment model! Got: '" + e + "' instead!");
	}
	
	@Override
	public Object run(AssignExpression expression) {
		String name = expression.name.lexeme;
		Object value = evaluate(expression.value);
		dist = locals().get(expression);
		Keyword op = expression.operator.keyword;
		
		return execute(name, value, op);
	}
	
	private Object execute(String nameIn, Object valueIn, Keyword opIn) {
		name = nameIn;
		value = EnvisionObject.convert(valueIn);
		op = opIn;
		obj = scope().get(name);
		
		if (obj instanceof ClassInstance) {
			return OperatorOverloadHandler.handleOverload(interpreter, op, (ClassInstance) obj, value);
		}
		else {
			switch (op) {
			case ASSIGN: value = assign(); break;
			case ADD_ASSIGN: value = add(); break;
			case SUBTRACT_ASSIGN: value = sub(); break;
			case MULTIPLY_ASSIGN: value = mul(); break;
			case DIVIDE_ASSIGN: value = div(); break;
			case MODULUS_ASSIGN: value = mod(); break;
			case BITWISE_AND_ASSIGN: value = and(); break;
			case BITWISE_OR_ASSIGN: value = or(); break;
			case BITWISE_XOR_ASSIGN: value = xor(); break;
			case SHIFT_LEFT_ASSIGN: value = shiftL(); break;
			case SHIFT_RIGHT_ASSIGN: value = shiftR(); break;
			case SHIFT_RIGHT_ARITHMETIC_ASSIGN: value = arithShiftR(); break;
			default: // error -- need to implement still
			}
		}
		
		return value;
	}
	
	/*
	if (dist != null) {
		if (value instanceof EnvisionObject) { scope().setAt(dist, name, (EnvisionObject) value); }
		else { scope().setAt(dist, name, ObjectCreator.createObject(name, value)); }
	}
	else {

	}
	*/

	private Object assign() {
		return assign(interpreter, name, obj, value);
	}
	
	public static Object assign(EnvisionInterpreter interpreter, String name, EnvisionObject obj, Object value) {
		Scope s = interpreter.scope();
		//System.out.println(name + " -- " + obj + " -- " + value);
		
		//Attempt to define on the spot
		if (obj == null) {
			if (value instanceof EnvisionVariable) {
				s.define(name, EnvisionDataType.OBJECT, ObjectCreator.createObject(EnvisionObject.convert(value)));
			}
			else if (value instanceof EnvisionObject) {
				s.define(name, EnvisionDataType.OBJECT, (EnvisionObject) value);
			}
			else {
				s.define(name, EnvisionDataType.OBJECT, ObjectCreator.createObject(value));
			}
		}
		//otherwise attempt to reassign the existing value
		else {
			if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
			
			//check type -- only if strong
			if (obj.isStrong()) {
				CastingUtil.checkType(obj, value);
			}
			
			if (obj instanceof EnvisionVariable) {
				if (obj.isStrong()) {
					EnvisionVariable var = (EnvisionVariable) obj;
					var.set(EnvisionObject.convert(value));
				}
				else {
					s.set(name, ObjectCreator.wrap(value));
				}
			}
			else if (value instanceof EnvisionObject) {
				EnvisionObject valObj = (EnvisionObject) value;
				//if (valObj instanceof ClassInstance) { ((ClassInstance) valObj).setName(name); }
				s.set(name, valObj);
			}
			else {
				s.set(name, ObjectCreator.createObject(name, value));
			}
		}
		
		//System.out.println("THE VALUE: " + value);
		
		return value;
	}
	
	private Object add() {
		//check if null or final
		if (obj == null) throw new NullVariableError(name);
		if (obj instanceof EnvisionList) {
			EnvisionList l = (EnvisionList) obj;
			if (value == l) throw new SelfAdditionError((EnvisionList) value);
			l.add(value);
			return l;
		}
		if (!(obj instanceof EnvisionVariable)) throw new NotAVariableError(obj);
		if (obj.isFinal()) throw new FinalVarReassignmentError(obj, value);
	
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		//trying to figure out how to properly handle -- If it's a char then it needs to be converted to a string -- this can mess things up..
		case CHAR: oVal = ((String) oVal) + value; break; 
		case DOUBLE: checkNumber(valueType); oVal = ((Double) oVal) + ((Number) value).doubleValue(); break;
		case INT: checkNumber(valueType); oVal = ((Long) oVal) + ((Number) value).longValue(); break;
		case STRING: oVal = ((String) oVal) + value; break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object sub() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: checkNumber(valueType); oVal = ((Double) oVal) - ((Number) value).doubleValue(); break;
		case INT: checkNumber(valueType); oVal = ((Long) oVal) - ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object mul() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case CHAR: break;
		case DOUBLE: checkNumber(valueType); oVal = ((Double) oVal) * ((Number) value).doubleValue(); break;
		case INT: checkNumber(valueType); oVal = ((Long) oVal) * ((Number) value).longValue(); break;
		case STRING: checkNumber(valueType); oVal = StringUtil.repeatString((String) oVal, ((Number) value).intValue()); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object div() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: checkNumber(valueType); oVal = ((Double) oVal) / ((Number) value).doubleValue(); break;
		case INT: checkNumber(valueType); oVal = ((Long) oVal) / ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object mod() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: checkNumber(valueType); oVal = ((Double) oVal) % ((Number) value).doubleValue(); break;
		case INT: checkNumber(valueType); oVal = ((Long) oVal) % ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object and() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: case INT: checkNumber(valueType); oVal = ((Number) oVal).longValue() & ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object or() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: case INT: checkNumber(valueType); oVal = ((Number) oVal).longValue() | ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object xor() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: case INT: checkNumber(valueType); oVal = ((Number) oVal).longValue() ^ ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object shiftL() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: case INT: checkNumber(valueType); oVal = ((Number) oVal).longValue() << ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object shiftR() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: case INT: checkNumber(valueType); oVal = ((Number) oVal).longValue() >> ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	private Object arithShiftR() {
		//check if null or final
		if (obj == null) { throw new NullVariableError(name); }
		if (!(obj instanceof EnvisionVariable)) { throw new NotAVariableError(obj); }
		if (obj.isFinal()) { throw new FinalVarReassignmentError(obj, value); }
		
		//cast to variable
		EnvisionVariable var = (EnvisionVariable) obj;
		Object oVal = var.get();
		
		//get datatype of incoming value
		EnvisionDataType valueType = EnvisionDataType.getDataType(value);
		
		//determine variable type
		switch (obj.getInternalType()) {
		case DOUBLE: case INT: checkNumber(valueType); oVal = ((Number) oVal).longValue() >>> ((Number) value).longValue(); break;
		default: throw new ArithmeticError("The operation: '" + obj + "' is invalid for the given object: '" + obj + "' (" + obj.getInternalType() + ")!");
		}
		
		//assign the new value
		var.set(oVal);
		
		return oVal;
	}
	
	//---------------------------------------------------------------------------
	
	private void checkNumber(EnvisionDataType type) {
		if (!type.isNumber()) {
			throw new ArithmeticError("Invalid operation: '" + type + "'! Can only operate on numbers!'");
		}
	}
	
	//---------------------------------------------------------------------------
	
	public static Object run(EnvisionInterpreter in, AssignExpression e) {
		return new IE_Assign(in).run(e);
	}
	
}	
