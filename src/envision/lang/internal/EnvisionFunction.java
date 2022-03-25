package envision.lang.internal;

import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.DuplicateFunctionError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.NegativeArgumentLengthError;
import envision.exceptions.errors.NoOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.CastingUtil;
import envision.interpreter.util.scope.Scope;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Parameter;
import envision.lang.util.ParameterData;
import envision.lang.util.Primitives;
import envision.parser.statements.Statement;
import envision.tokenizer.Operator;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

public class EnvisionFunction extends ClassInstance {
	
	/**
	 * The name of this function.
	 */
	protected String functionName;
	
	/**
	 * The return type of this function.
	 */
	protected EnvisionDatatype returnType;
	
	/**
	 * The parameter datatypes that this function accepts.
	 */
	protected ParameterData params = new ParameterData();
	
	/**
	 * The entire list of statements encapsulated within this function.
	 * Every time the function is invoked, this list of statements is
	 * executed.
	 */
	protected EArrayList<Statement> statements = new EArrayList();
	
	/**
	 * The scope that this function was derived from.
	 * 
	 * <p>
	 * Note: all values native to the parent scope from which this
	 * function's scope was derived from will be visible to this function
	 * in some capacity.
	 */
	protected Scope functionScope;
	
	/**
	 * If declared inside of a class, this is the class that houses this
	 * function.
	 */
	protected EnvisionClass parentClass = null;
	
	/**
	 * A list of all function overloads which stem directly from this base
	 * function.
	 * 
	 * <p>
	 * Note: There is no guarantee that this base function will be the
	 * version without parameters. Instead, it should be assumed that the
	 * base function will always be the very first interpreted instance
	 * that was parsed within the given scope. Any additionally parsed
	 * functions with the same name will be given to this base function as
	 * an overload.
	 */
	protected EArrayList<EnvisionFunction> overloads = new EArrayList();
	
	/**
	 * If this is an operator overload function, this is the operator that
	 * this function specifically overloads.
	 */
	protected Operator operatorOverload;
	
	/**
	 * True if this is a constructor for a class.
	 */
	protected boolean isConstructor = false;
	
	/**
	 * True if this is an operator overload function.
	 */
	protected boolean isOperatorOverload = false;
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Creates a new EnvisionFunction with the given name. This function
	 * is protected because it should only ever be used for internal
	 * function creation purposes.
	 * 
	 * @param nameIn The function name
	 */
	protected EnvisionFunction(String nameIn) {
		this(EnvisionDatatype.VAR_TYPE, nameIn, new ParameterData());
	}
	
	/**
	 * Creates a new function defintion with the given return type and
	 * function name. Parameters are set to empty by default.
	 * 
	 * @param rt
	 * @param funcNameIn
	 */
	public EnvisionFunction(Primitives rt, String nameIn) {
		this(rt.toDatatype(), nameIn, new ParameterData());
	}
	
	/**
	 * Creates a new function defintion with the given return type and
	 * function name. Parameters are set to empty by default.
	 * 
	 * @param rt     The return type of the function
	 * @param nameIn The name of the function
	 */
	public EnvisionFunction(EnvisionDatatype rt, String nameIn) {
		this(rt, nameIn, new ParameterData());
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(Primitives rt, String nameIn, Primitives... paramsIn) {
		this(rt, nameIn, new ParameterData(paramsIn));
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(Primitives rt, String nameIn, EnvisionDatatype... paramsIn) {
		this(rt, nameIn, new ParameterData(paramsIn));
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(Primitives rt, String nameIn, ParameterData paramsIn) {
		this(rt.toDatatype(), nameIn, paramsIn);
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(EnvisionDatatype rt, String nameIn, Primitives... paramsIn) {
		this(rt, nameIn, new ParameterData(paramsIn));
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(EnvisionDatatype rt, String nameIn, EnvisionDatatype... paramsIn) {
		this(rt, nameIn, new ParameterData(paramsIn));
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(EnvisionDatatype rt, String nameIn, ParameterData paramsIn) {
		super(EnvisionFunctionClass.FUNC_CLASS);
		returnType = rt;
		functionName = nameIn;
		setParams(paramsIn);
	}
	
	/**
	 * Creates a new function definition with new parameters from an
	 * existing function declaration. Existing function overloads are not
	 * copied.
	 * 
	 * This is primarily used to help create function overloads.
	 * 
	 * @param func     The original function declaration
	 * @param paramsIn The new function parameters
	 */
	public EnvisionFunction(EnvisionFunction mIn, ParameterData paramsIn) {
		super(EnvisionFunctionClass.FUNC_CLASS);
		functionName = mIn.functionName;
		returnType = mIn.returnType;
		setParams(paramsIn);
		isConstructor = mIn.isConstructor;
		isOperatorOverload = mIn.isOperatorOverload;
		operatorOverload = mIn.operatorOverload;
		functionScope = mIn.functionScope;
	}
	
	/** Exclusive to class constructors. */
	public EnvisionFunction(ParameterData paramsIn) {
		super(EnvisionFunctionClass.FUNC_CLASS);
		setParams(paramsIn);
		functionName = "init";
		isConstructor = true;
	}
	
	/** Exclusive to operator overloads. */
	public EnvisionFunction(Operator operatorIn, ParameterData paramsIn) {
		super(EnvisionFunctionClass.FUNC_CLASS);
		setParams(paramsIn);
		functionName = "OPERATOR_" + operatorIn.chars;
		operatorOverload = operatorIn;
		isOperatorOverload = true;
	}
	
	/**
	 * Creates a copy of an existing EnvisionFunction declaration. This
	 * shallow copies body statements as well as function overloads.
	 * 
	 * @param toCopy The function to copy
	 */
	private EnvisionFunction(EnvisionFunction toCopy) {
		super(EnvisionFunctionClass.FUNC_CLASS);
		functionName = toCopy.functionName;
		statements = toCopy.statements;
		returnType = toCopy.returnType;
		setParams(toCopy.params);
		isConstructor = toCopy.isConstructor;
		isOperatorOverload = toCopy.isOperatorOverload;
		operatorOverload = toCopy.operatorOverload;
		functionScope = toCopy.functionScope;
		
		for (EnvisionFunction overload : toCopy.overloads) {
			overloads.add(copy(overload));
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return functionName + params + getHexHash();
	}
	
	@Override
	public EnvisionFunction copy() {
		return new EnvisionFunction(this);
	}
	
	@Override
	public boolean equals(Object in) {
		if (!(in instanceof EnvisionFunction)) return false;
		
		EnvisionFunction m = (EnvisionFunction) in;
		return m.functionName.equals(functionName) && m.params.compare(params)
				&& m.isConstructor == isConstructor && m.isOperatorOverload == isOperatorOverload;
	}
	
	//----------------
	// Invoke Methods
	//----------------
	
	
	
	//-------------------
	// Overload Handling
	//-------------------
	
	/**
	 * Adds a new function overload with the given return type and
	 * parameter types.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(Primitives rt, Primitives... paramsIn) {
		return addOverload(rt.toDatatype(), paramsIn);
	}
	
	/**
	 * Adds a new function overload with the given return type and
	 * parameter types.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(EnvisionDatatype rt, Primitives... paramsIn) {
		ParameterData params = new ParameterData(paramsIn);
		if (hasOverload(params)) throw new DuplicateFunctionError(functionName, params);
		overloads.add(makeOverload(rt, params, statements));
		return this;
	}
	
	/**
	 * Adds a new function overload with the given return type and
	 * parameter types.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(EnvisionDatatype rt, EnvisionDatatype... paramsIn) {
		ParameterData params = new ParameterData(paramsIn);
		if (hasOverload(params)) throw new DuplicateFunctionError(functionName, params);
		overloads.add(makeOverload(rt, params, statements));
		return this;
	}
	
	/**
	 * Adds a new function overload with the given return type and
	 * parameter types.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(Primitives rt, ParameterData paramsIn) {
		return addOverload(rt.toDatatype(), paramsIn);
	}
	
	/**
	 * Adds a new function overload with the given return type and
	 * parameter types.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(EnvisionDatatype rt, ParameterData paramsIn) {
		if (hasOverload(params)) throw new DuplicateFunctionError(functionName, params);
		overloads.add(makeOverload(rt, params, statements));
		return this;
	}
	
	/**
	 * Adds a new function overload with the given parameter data and
	 * function body statements. This overload will share the same return
	 * type as the function for which it overrides.
	 * 
	 * @param paramsIn The overloaded parameters
	 * @param bodyIn   The overload function's body
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(ParameterData paramsIn, EArrayList<Statement> bodyIn) {
		return addOverload(returnType, paramsIn, bodyIn);
	}
	
	/**
	 * Adds a new function overload with the given return type, parameter
	 * data, and function body statements.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * @param bodyIn   The overload function's body
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(Primitives rt, ParameterData paramsIn,
			EArrayList<Statement> bodyIn) {
		return addOverload(rt.toDatatype(), paramsIn, bodyIn);
	}
	
	/**
	 * Adds a new function overload with the given return type, parameter
	 * data, and function body statements.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * @param bodyIn   The overload function's body
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(EnvisionDatatype rt, ParameterData paramsIn,
			EArrayList<Statement> bodyIn) {
		if (hasOverload(paramsIn)) throw new DuplicateFunctionError(functionName, paramsIn);
		overloads.add(makeOverload(rt, paramsIn, bodyIn));
		return this;
	}
	
	/**
	 * Creates a new overload function with the given return type,
	 * parameter data, and function body.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * @param bodyIn   The overload function's body
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction makeOverload(EnvisionDatatype rt, ParameterData paramsIn,
			EArrayList<Statement> bodyIn) {
		EnvisionFunction overload = new EnvisionFunction(rt, functionName, paramsIn);
		overload.setBody(bodyIn);
		return overload;
	}
	
	/**
	 * Attempts to add the given function as an overload of this function.
	 * This is only possible if both A. The function name matches and B.
	 * the base function does not already have an overload with the given
	 * parameters.
	 * 
	 * @param overload The overload function
	 * 
	 * @return The base function
	 */
	public EnvisionFunction addOverload(EnvisionFunction overload) {
		//only accept functions with the same name
		if (!functionName.equals(overload.functionName))
			throw new InvalidTargetError("The given function: " + overload
					+ " is not valid as an overload function of" + this + "!");
		//don't allow if already existing overloads
		if (hasOverload(overload.params))
			throw new DuplicateFunctionError(functionName, overload.params);
		
		//add the overload
		overloads.add(overload);
		return this;
	}
	
	/**
	 * Returns true if this base function has an overload with the given
	 * parameter data.
	 * 
	 * @param dataIn The overload parameters to search for
	 * @return True if an overload with the given parameters exists
	 */
	public boolean hasOverload(ParameterData dataIn) {
		return (getOverload(dataIn) != null);
	}
	
	/**
	 * Attempts to return an overload with parameters matching the given
	 * arguments.
	 */
	public EnvisionFunction getOverloadFromArgs(EArrayList<EnvisionObject> args) {
		ParameterData callParams = (args != null) ? new ParameterData(args) : new ParameterData();
		
		//check if the base method parameters match the given arguments
		if (params.compare(callParams)) return this;
		
		//if not, check for any overloads
		EnvisionFunction m = null;
		
		m = getOverload(callParams);
		//if there are no overloads with matching parameters -- throw an error
		if (m == null) throw new NoOverloadError(this, callParams);
		
		return m;
	}
	
	/**
	 * Attempts to find an overload of the same method name but with
	 * different parameters.
	 */
	public EnvisionFunction getOverload(ParameterData dataIn) {
		for (EnvisionFunction overload : overloads) {
			if (overload.argLength() == dataIn.size()) {
				if (overload.params.compare(dataIn)) {
					return overload;
				}
			}
		}
		return null;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * The expected number of arguments to be read. 255 is the absolute
	 * max.
	 */
	public int argLength() {
		return 255;
	}
	
	/**
	 * The expected datatypes of the arguments to be read. Return null to
	 * indicate that types do not matter.
	 */
	public ParameterData argTypes() {
		return null;
	}
	
	/**
	 * Called when the language actually runs the related callable object.
	 * Helper version to wrap a singleton argument into an array of length 1.
	 */
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject arg) {
		invoke(interpreter, new EnvisionObject[]{arg});
	}
	
	/**
	 * Should be called initially to check for valid arguments.
	 * If arguments are valid, then the actual invoke method is called.
	 */
	public void invoke_I(String func_name, EnvisionInterpreter interpreter, EnvisionObject[] args) {
		checkArgs(args);
		invoke(interpreter, args);
	}
	
	/**
	 * Checks for argument size and datatype errors.
	 */
	public void checkArgs(EnvisionObject[] incoming_args) {
		int arg_length = argLength();
		ParameterData types = argTypes();
		
		// ensure that the expected number of arguments is possible
		if (arg_length < 0) throw new NegativeArgumentLengthError(this, arg_length);
		
		// if the incomming args are null -- ensure that the argSize is 0 -- otherwise error
		if (incoming_args == null && arg_length > 0) throw new ArgLengthError(this, arg_length, 0);
		
		// ensure that the expected number of arguments is not larger than the maximum allowed even if 
		if (arg_length > 255) throw new ArgLengthError(this, arg_length, 255);
		if (incoming_args.length > 255) throw new ArgLengthError(this, incoming_args.length, 255);
		
		// don't care about types if the expected ones are null
		if (types != null) {
			ParameterData args = new ParameterData(incoming_args);
			
			//compare each parameter to ensure that they are compatible matches
			for (int i = 0, j = 0; i < incoming_args.length; i++) {
				Parameter type = types.get(j);
				Parameter obj = args.get(i);
				
				if (!type.compare(obj)) throw new InvalidDatatypeError(obj, type);
			}
		}
	}
	
	/**
	 * Attempts to run the function with the given arguments.
	 */
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//create a new scope derived from this function's scope
		Scope scope = new Scope(functionScope);
		
		EArrayList<EnvisionObject> callArgs = new EArrayList<EnvisionObject>().addA(args);
		EnvisionFunction m = getOverloadFromArgs(callArgs);
		
		//define parameter values within the current scope
		EArrayList<String> names = m.params.getNames();
		for (int i = 0; i < m.params.size(); i++) {
			String n = names.get(i);
			scope.define(n, callArgs.get(i));
		}
		
		//if this is a constructor, check if any of the arguments are assignment args
		//as in if any argument name matches an existing body level variable -- assign it directly
		if (isConstructor) {
			Scope ps = scope.getParentScope();
			
			for (int i = 0; i < callArgs.size(); i++) {
				String arg_name = m.params.getNames().get(i);
				EnvisionObject arg_obj = callArgs.get(i);
				
				Box2<EnvisionDatatype, EnvisionObject> scopeValue = ps.getTyped(arg_name);
				
				if (scopeValue != null) {
					EnvisionDatatype scope_var_type = scopeValue.getA();
					EnvisionDatatype incomming_type = arg_obj.getDatatype();
					
					if (scope_var_type.compareType(incomming_type)) {
						ps.set(arg_name, arg_obj);
					}
					else if (EnvisionDatatype.isNumber(scopeValue.getA())) {
						EnvisionObject obj = CastingUtil.castToNumber(arg_obj, scopeValue.getA());
						ps.set(arg_name, obj);
					}
					else {
						throw new InvalidArgumentError(functionName, scope_var_type,
								incomming_type);
					}
				}
				
			}
		}
		
		try {
			interpreter.executeBlock(m.statements, scope);
		}
		catch (ReturnValue r) {
			if (isConstructor) throw new ReturnValue(scope.get("this"));
			throw r;
		}
		
		if (isConstructor) throw new ReturnValue(scope.get("this"));
	}
	
	public EnvisionFunction addStatement(Statement statementIn) {
		return statements.addR(statementIn, this);
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * @return The name of this function
	 */
	public String getFunctionName() {
		return functionName;
	}
	
	public Scope getScope() {
		return functionScope;
	}
	
	public ParameterData getParams() {
		return params;
	}
	
	public EArrayList<Statement> getStatements() {
		return statements;
	}
	
	public EArrayList<EnvisionFunction> getOverloads() {
		return overloads;
	}
	
	public EArrayList<Statement> getBody() {
		return statements;
	}
	
	public EArrayList<EnvisionDatatype> getParamTypes() {
		return params.getDataTypes();
	}
	
	public EArrayList<String> getParamNames() {
		return params.getNames();
	}
	
	public EnvisionDatatype getReturnType() {
		return returnType;
	}
	
	public boolean isVoid() {
		return Primitives.getDataType(returnType) == Primitives.VOID;
	}
	
	public boolean isConstructor() {
		return isConstructor;
	}
	
	public boolean isOperator() {
		return isOperatorOverload;
	}
	
	public Operator getOperator() {
		return operatorOverload;
	}
	
	//---------
	// Setters
	//---------
	
	public EnvisionFunction setScope(Scope scopeIn) {
		functionScope = scopeIn;
		return this;
	}
	
	public EnvisionFunction setBody(EArrayList<Statement> in) {
		statements.clear();
		statements.addAll(in);
		return this;
	}
	
	public EnvisionFunction setParams(ParameterData dataIn) {
		params.clear();
		params.set(dataIn);
		return this;
	}
	
	public EnvisionFunction assignParentClass(EnvisionClass parentClassIn) {
		parentClass = parentClassIn;
		return this;
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static EnvisionFunction copy(EnvisionFunction m) {
		return new EnvisionFunction(m);
	}
	
}
