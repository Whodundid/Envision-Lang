package envision_lang.lang.classes;

import java.util.HashMap;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.DuplicateOverloadError;
import envision_lang.exceptions.errors.FinalVarReassignmentError;
import envision_lang.exceptions.errors.NotAFunctionError;
import envision_lang.exceptions.errors.UndefinedFunctionError;
import envision_lang.exceptions.errors.objects.ClassCastError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.CastingUtil;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.FunctionPrototype;
import envision_lang.lang.util.ParameterData;
import envision_lang.tokenizer.Operator;

/**
 * An instantiated version of an EnvisionClass.
 */
public class ClassInstance extends EnvisionObject {
	
	/**
	 * The scope of this instance. Directly inherited from the calling
	 * scope and the over-arching class's scope from which this instance
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
		instanceScope = new Scope(derivingClassIn.staticScope);
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
			//Java:Null should not be directly reference-able in Envision, let alone assignable.
			//EnvisionNull.Null should be used to represent actual Null values within Envision.
			if (obj == null) throw new EnvisionLangError("EnvisionObject is Java:Null!");
			
			//check for final value reassignment
			if (isFinal()) throw new FinalVarReassignmentError(this, obj);
			
			//first, start by checking if the assignment value is null
			//in which case, simply assign null to this scope definition
			if (obj == EnvisionNull.NULL) {
				interpreter.scope().set(scopeName, internalType, EnvisionNull.NULL);
				return EnvisionNull.NULL;
			}
			
			//start by determining object types for assignment compatibility
			IDatatype this_type = getDatatype();
			IDatatype asgn_type = obj.getDatatype();
			
			//check for type mismatch
			if (isStrong()) CastingUtil.assert_expected_datatype(this_type, asgn_type);
			
			//check for exact type matches
			//this approach does not require value overwriting within the scope
			if (this_type.compare(asgn_type) && this instanceof EnvisionVariable env_var) {
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
		if (op == Operator.EQUALS) {
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
	
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//check for self type cast
		if (getDatatype().compare(castType)) return this;
		
		//otherwise, not supported -- throw cast error
		throw new ClassCastError(this, castType);
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
	 * The result of this method dynamically casts its output to whatever
	 * type is specified. Invalid types are not checked and relevant cast
	 * errors will be thrown.
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
		
		if (obj == null) throw new UndefinedFunctionError(funcName, this);
		else if (obj instanceof FunctionPrototype iproto) return (E) handlePrototype(iproto, interpreter, args);
		else if (!(obj instanceof EnvisionFunction)) throw new NotAFunctionError(obj);
		
		//execute and return function result -- even if void
		EnvisionFunction func = (EnvisionFunction) obj;
		return func.invoke_r(interpreter, args);
	}
	
	protected EnvisionObject handlePrototype(FunctionPrototype proto, EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (isPrimitive) return handlePrimitive(proto, args);
		return proto.build(this).invoke_r(interpreter, args);
	}
	
	/**
	 * Every native Envision object should directly override this
	 * method to declare their own specific primitive function names.
	 * Furthermore, every native Envision object that overrides this
	 * method should also directly call this method's super in order
	 * to access inherited function declarations.
	 * 
	 * @param proto
	 * @param args
	 * @return
	 */
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		//switch on funcName
		return switch (funcName) {
		case "equals" -> EnvisionBooleanClass.newBoolean(equals(args[0]));
		case "hash" -> EnvisionIntClass.newInt(getObjectHash());
		case "hexHash" -> EnvisionStringClass.newString(getHexHash());
		case "isStatic" -> EnvisionBooleanClass.newBoolean(isStatic());
		case "isFinal" -> EnvisionBooleanClass.newBoolean(isFinal());
		case "toString" -> EnvisionStringClass.newString(toString());
		case "type" -> EnvisionStringClass.newString(getDatatype());
		case "typeString" -> EnvisionStringClass.newString(getTypeString());
		//case "members" -> EnvisionListClass.newList(EnvisionDatatype.STRING_TYPE, instanceScope.getMethods());
		//always error if this point is reached
		default -> throw new UndefinedFunctionError(funcName, this);
		};
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
		EnvisionObject result = executeFunction("toString", interpreter);
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
		EnvisionObject obj = instanceScope.get(name);
		//if function prototype, build dynamic function
		if (obj instanceof FunctionPrototype proto) return proto.build(this);
		//otherwise, just return scope object
		return obj;
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
	public EnvisionObject set(String name, IDatatype type, EnvisionObject in) {
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
			if (opFunc.hasOverload(incoming_params)) throw new DuplicateOverloadError(funcName, incoming_params);
			
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
