package envision.lang.classes;

import java.util.HashMap;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.DuplicateFunctionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.NotAFunctionError;
import envision.exceptions.errors.UndefinedFunctionError;
import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.CastingUtil;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBoolean;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.internal.EnvisionFunction;
import envision.lang.internal.EnvisionNull;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.ParameterData;
import envision.tokenizer.Operator;

/**
 * An instantiated version of an EnvisionClass.
 */
public class ClassInstance extends EnvisionObject {
	
	/**
	 * The scope of this instance. Directly inherited from the calling
	 * scope and the overarching class's scope from which this instance
	 * was defined from.
	 */
	protected final Scope instanceScope;
	
	/**
	 * Used for operator overloading. In the event that a class defines
	 * specific methods detailing operator functionality, the operators which
	 * have been overloaded will be statically referenced here.
	 */
	protected HashMap<Operator, EnvisionFunction> operators = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	public ClassInstance(EnvisionClass derivingClassIn, Scope instanceScopeIn) {
		super(derivingClassIn.getDatatype());
		internalClass = derivingClassIn;
		instanceScope = instanceScopeIn;
		registerNatives();
	}
	
	/**
	 * Creates a new ClassInstance without a direct instance scope. The
	 * object's instance scope is derived directly from the parent class's
	 * scope.
	 * 
	 * @param derivingClassIn
	 */
	protected ClassInstance(EnvisionClass derivingClassIn) {
		super(derivingClassIn.getDatatype());
		internalClass = derivingClassIn;
		instanceScope = new Scope(derivingClassIn.staticClassScope);
		registerNatives();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String getTypeString() {
		return internalClass.getTypeString();
	}
	
	@Override
	public String toString() {
		return getDatatype() + "_" + Integer.toHexString(hashCode());
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Called during instance initialization.
	 */
	protected void registerNatives() {};
	
	/**
	 * Used to natively handle operator overloads within class instances.
	 * Each time an expression is processed that directly references this
	 * object instance, this method is called and given the operator in
	 * question, and the object the operator is being applied to in the
	 * expression.
	 * <p>
	 * If no direct override to this method has been provided, this method
	 * will attempt to find and execute a single EnvisionFunction within
	 * this instance's scope which matches the given operator overload
	 * parameters. In the even that no operator overload function is
	 * found, then an UnsupportedOverloadError will be thrown to indicate
	 * the incompatibility between this class instance and the given
	 * expression.
	 * <p>
	 * Primitives and natively defined classes such as: EnvisionBoolean,
	 * EnvisionChar, EnvisionInt, EnvisionDouble, EnvisionString,
	 * EnvisionList, EnvisionFile directly override this method to provide
	 * their own specific operator overload handles for various
	 * expressions.
	 * <p>
	 * All expressions within Envision:Java are processed in a
	 * left-to-right manner, meaning that an operator expression will
	 * ALWAYS have the following structure: 'this' OPERATOR 'obj'. Where
	 * 'this' is this object instance, OPERATOR is the operator being
	 * used, and 'obj' is the object for which the given operator is being
	 * applied to under the context of object instance.
	 * 
	 * @throws UnsupportedOverloadError Thrown when the given operator
	 *                                  'op' is not supported by this
	 *                                  class instance.
	 * 
	 * @param interpreter The interpreter for which this expression is being evaluated against
	 * @param scopeName The name of 'this' object within the current scope
	 * @param op  The operator being applied
	 * @param obj The object for which the given operator is being applied
	 *            to
	 * @return The result of the expression
	 */
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//natively support assignment '='
		if (op == Operator.ASSIGN) {
			//as assignment can involve complete scope overwrites, assignment
			//operations must be directly handled using the interpreter's scope
			//to account for scope visibility and naming
			
			//if the assignment object is Java:Null, this is an error and must be thrown.
			//Java:Null should not be directly referencable in Envision, let alone assignable.
			//EnvisionNull.Null should be used to represent actual Null values within Envision.
			if (obj == null) throw new EnvisionError("EnvisionObject is Java:Null!");
			
			//check for final value reassignment
			if (isFinal()) throw new FinalVarReassignmentError(this, obj);
			
			//first, start by checking if the assignment value is null
			//in which case, simply assign null to this scope definition
			if (obj == EnvisionNull.NULL) {
				interpreter.scope().set(scopeName, internalType, EnvisionNull.NULL);
				return EnvisionNull.NULL;
			}
			
			//start by determining object types for assignment compatibility
			EnvisionDatatype this_type = getDatatype();
			EnvisionDatatype asgn_type = obj.getDatatype();
			
			//check for type mismatch
			if (isStrong()) CastingUtil.assert_expected_datatype(this_type, asgn_type);
			
			//check for exact type matches
			//this approach does not require value overwriting within the scope
			if (this_type.compareType(asgn_type) && this instanceof EnvisionVariable env_var) {
				env_var.set(obj);
				return this;
			}
			
			//assign new value in scope
			interpreter.scope().set(scopeName, asgn_type, obj);
			
			//effectively this action can ultimately result in 'this' object's death
			//within the JVM as 'this' object is removed from the scope's value
			return obj;
		}
		
		//natively support comparison '=='
		if (op == Operator.COMPARE) {
			//each native primitive type should have their own method 
			//of 'equals' to account for cross language design barriers
			return EnvisionBooleanClass.newBoolean(equals(obj));
		}
		
		//natively support not equals '!='
		if (op == Operator.NOT_EQUALS) {
			//each native primitive type should have their own method 
			//of 'equals' to account for cross language design barriers
			return EnvisionBooleanClass.newBoolean(!equals(obj));
		}
		
		throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
	}
	
	/**
	 * Returns the first function matching the given name. If no function
	 * with that name exists, null is returned instead.
	 * 
	 * @param funcName The name of the function
	 * @return The matching function
	 */
	public EnvisionFunction getFunction(String funcName) {
		return instanceScope.functions().getFirst(f -> f.compare(funcName));
	}
	
	/**
	 * Returns a function on this instance's scope matching the given name
	 * and parameters (if it exists).
	 * 
	 * @param funcName The name of the function
	 * @param params   The parameters of the function
	 * @return The matching function
	 */
	public EnvisionFunction getFunction(String funcName, ParameterData params) {
		return instanceScope.functions().getFirst(f -> f.compare(funcName, params));
	}
	
	/**
	 * Attempts to execute and return a defined function within this
	 * instance's scope. If there is no function defined with the given
	 * name in this scope, an UndefinedFunctionError is thrown instead.
	 * Otherwise, if there is an object with the given name but it is not
	 * a function, a NotAFunctionError is thrown. No arguments are passed
	 * using this version.
	 * <p>
	 * Note: This method can throw any of the errors involved with
	 * standard function execution within Envision:Java. As such, it is
	 * important to fully understand the function being called including
	 * the parameter types and arguments that are expected.
	 * 
	 * @param funcName    The name of the function to call
	 * @param interpreter The interpreter executing the function's
	 *                    statements
	 * 
	 * @param <E>         The expected return type of the function
	 * 
	 * @return The result of the function's execution, could be void
	 * 
	 * @throws UndefinedFunctionError Thrown if there is no function with
	 *                                the given name in this scope
	 * 								
	 * @throws NotAFunctionError      Thrown if there is an object with
	 *                                the given name, but it's not a
	 *                                function
	 */
	public <E extends EnvisionObject> E executeFunction
		(String funcName, EnvisionInterpreter interpreter)
			throws UndefinedFunctionError, NotAFunctionError
	{
		return executeFunction(funcName, interpreter, new EnvisionObject[0]);
	}
	
	/**
	 * Attempts to execute and return a defined function within this
	 * instance's scope. If there is no function defined with the given
	 * name in this scope, an UndefinedFunctionError is thrown instead.
	 * Otherwise, if there is an object with the given name but it is not
	 * a function, a NotAFunctionError is thrown.
	 * <p>
	 * Note: This method can throw any of the errors involved with
	 * standard function execution within Envision:Java. As such, it is
	 * important to fully understand the function being called including
	 * the parameter types and arguments that are expected.
	 * 
	 * @param funcName    The name of the function to call
	 * @param interpreter The interpreter executing the function's
	 *                    statements
	 * @param args        The arguments to pass to the function
	 * 
	 * @param <E>         The expected return type of the function
	 * 
	 * @return The result of the function's execution, could be void
	 * 
	 * @throws UndefinedFunctionError Thrown if there is no function with
	 *                                the given name in this scope
	 * 								
	 * @throws NotAFunctionError      Thrown if there is an object with
	 *                                the given name, but it's not a
	 *                                function
	 */
	public <E extends EnvisionObject> E executeFunction
		(String funcName, EnvisionInterpreter interpreter, EnvisionObject[] args)
			throws UndefinedFunctionError, NotAFunctionError
	{
		//attempt to get function with given name from scope
		EnvisionObject obj = instanceScope.get(funcName);
		if (obj == null) throw new UndefinedFunctionError(funcName);
		if (!(obj instanceof EnvisionFunction)) throw new NotAFunctionError(obj);
		
		//execute and return function result -- even if void
		EnvisionFunction func = (EnvisionFunction) obj;
		E result = func.invoke_r(interpreter, args);
		
		//check for null values
		//if (result.getDatatype().equals(EnvisionDatatype.NULL_TYPE)) {
		//	if (func.getReturnType().equals(EnvisionDatatype.STRING_TYPE)) {
		//		return (E) EnvisionStringClass.newString(EnvisionDatatype.STRING_TYPE.getType());
		//	}
		//}
		
		return result;
	}
	
	/**
	 * Directly executes this instance's toString function and returns the
	 * result as an EnvisionString.
	 * 
	 * @param interpreter The interpreter executing the function's
	 *                    statements
	 * @return The result of this instance's toString function
	 */
	public EnvisionString executeToString(EnvisionInterpreter interpreter) {
		return executeFunction("toString", interpreter, new EnvisionObject[0]);
	}
	
	/**
	 * Directly executes this instance's toString function and returns the
	 * result as a Java:String.
	 * 
	 * @param interpreter The interpreter executing the function's
	 *                    statements
	 * @return The result of this instance's toString function
	 */
	public String executeToString_i(EnvisionInterpreter interpreter) {
		EnvisionObject result = executeFunction("toString", interpreter, new EnvisionObject[0]);
		if (result instanceof EnvisionString env_str) return env_str.string_val;
		return result.toString();
	}
	
	public EnvisionBoolean executeEquals(EnvisionInterpreter interpreter, EnvisionObject obj) {
		return executeEquals(interpreter, new EnvisionObject[]{obj});
	}
	
	public EnvisionBoolean executeEquals(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		return executeFunction("equals", interpreter, args);
	}
	
	public boolean executeEquals_i(EnvisionInterpreter interpreter, EnvisionObject obj) {
		return executeEquals(interpreter, new EnvisionObject[]{obj}).bool_val;
	}
	
	public boolean executeEquals_i(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		return executeEquals(interpreter, args).bool_val;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the scope of this class instance.
	 */
	public Scope getScope() {
		return instanceScope;
	}
	
	/**
	 * Returns a field value from this instance's scope.
	 */
	public EnvisionObject get(String name) {
		return instanceScope.get(name);
	}
	
	/**
	 * Returns the class for which this instance is derived from.
	 */
	public EnvisionClass getEClass() {
		return internalClass;
	}
	
	//---------
	// Setters
	//---------
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	public EnvisionObject set(String name, EnvisionObject in) {
		instanceScope.set(name, in);
		return in;
	}
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	public EnvisionObject set(String name, EnvisionDatatype type, EnvisionObject in) {
		instanceScope.set(name, type, in);
		return in;
	}

	/**
	 * Adds an operator overload to this instance which defines behavior
	 * for how to respond when this class instance is used in conjunction
	 * within expression statements.
	 * 
	 * @param opIn The operator being overloaded (ex: +, *, ++, etc.)
	 * @param func The method which defines the operator overload behavior
	 * @return this (the instance)
	 */
	public void addOperatorOverload(Operator operator, EnvisionFunction op) {
		if (operators.containsKey(operator)) {
			EnvisionFunction opFunc = operators.get(operator);
			String funcName = opFunc.getFunctionName();
			ParameterData incoming_params = op.getParams();
			
			//prevent duplicate overload
			if (opFunc.hasOverload(incoming_params)) throw new DuplicateFunctionError(funcName, incoming_params);
			
			opFunc.addOverload(op);
		}
		operators.put(operator, op);
	}
	
	/**
	 * Returns the operator overload method corresponding to the given
	 * operator. Returns null if there is no overload.
	 */
	public EnvisionFunction getOperator(Operator op) {
		return operators.get(op);
	}
	
	/**
	 * Returns the list of all operators being overloaded with their
	 * corresponding overload method.
	 */
	public HashMap<Operator, EnvisionFunction> getOperators() {
		return operators;
	}
	
	/**
	 * Returns true if this class instance (and more specifically the
	 * class for which this was derived from) supports the given operator
	 * overload.
	 */
	public boolean supportsOperator(Operator op) {
		return (op != null) ? operators.containsKey(op) : false;
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
}
