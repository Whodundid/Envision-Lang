package envision_lang.lang.functions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.CastingUtil;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionVoid;
import envision_lang.lang.language_errors.error_types.ArgLengthError;
import envision_lang.lang.language_errors.error_types.DuplicateOverloadError;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.language_errors.error_types.InvalidDatatypeError;
import envision_lang.lang.language_errors.error_types.InvalidTargetError;
import envision_lang.lang.language_errors.error_types.NegativeArgumentLengthError;
import envision_lang.lang.language_errors.error_types.NoOverloadError;
import envision_lang.lang.natives.EnvisionParameter;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.util.EList;

public class EnvisionFunction extends ClassInstance {
	
    //========
    // Fields
    //========
    
	/**
	 * The name of this function.
	 */
	protected String functionName;
	
	/**
	 * The return type of this function.
	 */
	protected IDatatype returnType;
	
	/**
	 * The parameter datatypes that this function accepts.
	 */
	protected ParameterData parameters = ParameterData.EMPTY_PARAMS;
	
	/**
	 * The entire list of statements encapsulated within this function.
	 * Every time the function is invoked, this list of statements is
	 * executed.
	 */
	protected EList<ParsedStatement> statements = EList.newList();
	
	/**
	 * The scope that this function was derived from.
	 * 
	 * <p>
	 * Note: all values native to the parent scope from which this
	 * function's scope was derived from will be visible to this function
	 * in some capacity.
	 */
	//protected Scope functionScope;
	
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
	protected EList<EnvisionFunction> overloads = EList.newList();
	
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
	
	/**
	 * The original pre-overloaded function of this one.
	 * <p>
	 * Otherwise known as this function's 'super'.
	 * <p>
	 * Unless this function is overriding an existing function, this field will
	 * generally be null.
	 */
	protected EnvisionFunction superFunction = null;
	
	//==============
    // Constructors
    //==============
	
	/**
	 * No argument constructor intended for purely internal use!
	 */
	protected EnvisionFunction() {
	    super(EnvisionFunctionClass.FUNC_CLASS);
	}
	
	/**
	 * Creates a new EnvisionFunction with the given name. This function
	 * is protected because it should only ever be used for internal
	 * function creation purposes.
	 * 
	 * @param nameIn The function name
	 */
	protected EnvisionFunction(String nameIn) {
		this(EnvisionStaticTypes.VAR_TYPE, nameIn, ParameterData.EMPTY_PARAMS);
	}
	
	/**
	 * Creates a new function definition with the given return type and
	 * function name. Parameters are set to empty by default.
	 * 
	 * @param rt
	 * @param funcNameIn
	 */
	public EnvisionFunction(IDatatype rt, String nameIn) {
		this(rt.toDatatype(), nameIn, ParameterData.EMPTY_PARAMS);
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(IDatatype rt, String nameIn, IDatatype... paramsIn) {
		this(rt, nameIn, ParameterData.from(paramsIn));
	}
	
	/**
	 * Creates a new function definition with the given return type,
	 * function name, and given parameters.
	 * 
	 * @param rt       The function's return type
	 * @param nameIn   The function's name
	 * @param paramsIn The function's parameters
	 */
	public EnvisionFunction(IDatatype rt, String nameIn, ParameterData paramsIn) {
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
		instanceScope = mIn.instanceScope;
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
		functionName = "OPERATOR_" + operatorIn.operatorString;
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
		setParams(toCopy.parameters);
		isConstructor = toCopy.isConstructor;
		isOperatorOverload = toCopy.isOperatorOverload;
		operatorOverload = toCopy.operatorOverload;
		instanceScope = toCopy.instanceScope;
		
		for (EnvisionFunction overload : toCopy.overloads) {
			overloads.add(overload.copy());
		}
	}
	
	//===========
    // Overrides
    //===========
	
	@Override
	public String toString() {
		return functionName + "(" + parameters + ")" + getHexHash();
	}
	
	@Override
	public EnvisionFunction copy() {
		return new EnvisionFunction(this);
	}
	
	@Override
	public boolean equals(Object in) {
		if (!(in instanceof EnvisionFunction)) return false;
		
		EnvisionFunction m = (EnvisionFunction) in;
		return m.functionName.equals(functionName) &&
			   m.parameters.compare(parameters) &&
			   m.isConstructor == isConstructor &&
			   m.isOperatorOverload == isOperatorOverload;
	}
	
	//===================
	// Overload Handling
	//===================
	
	/**
	 * Adds a new function overload with the given return type and
	 * parameter types.
	 * 
	 * @param rt       The overload function's return type
	 * @param paramsIn The overloaded parameters
	 * 
	 * @return The new overload function
	 */
	public EnvisionFunction addOverload(IDatatype rt, IDatatype... paramsIn) {
		ParameterData params = ParameterData.from(paramsIn);
		if (hasOverload(params)) throw new DuplicateOverloadError(functionName, params);
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
	public EnvisionFunction addOverload(IDatatype rt, ParameterData paramsIn) {
		return addOverload(rt, paramsIn);
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
	public EnvisionFunction addOverload(ParameterData paramsIn, EList<ParsedStatement> bodyIn) {
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
	public EnvisionFunction addOverload(IDatatype rt, ParameterData paramsIn, EList<ParsedStatement> bodyIn) {
		if (hasOverload(paramsIn)) throw new DuplicateOverloadError(functionName, paramsIn);
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
	public EnvisionFunction makeOverload(IDatatype rt, ParameterData paramsIn, EList<ParsedStatement> bodyIn) {
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
		if (hasOverload(overload.parameters))
			throw new DuplicateOverloadError(functionName, overload.parameters);
		
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
		return getOverload(dataIn) != null;
	}
	
	/**
	 * Attempts to return an overload with parameters matching the given
	 * arguments.
	 */
	public EnvisionFunction getOverloadFromArgs(EnvisionObject[] args) {
		ParameterData callParams = (args != null) ? ParameterData.from(args) : ParameterData.EMPTY_PARAMS;
		
		//check if the base method parameters match the given arguments
		if (parameters.compare(callParams)) return this;
		
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
			if (overload.parameters.length() != dataIn.length()) continue;
			if (!overload.parameters.compare(dataIn)) continue;
			return overload;
		}
		return null;
	}
	
	//=========
    // Methods
    //=========
	
	/**
	 * Compares the incoming function name against this function's name.
	 * Returns true if they match.
	 * 
	 * @param funcName The incoming function name
	 * @return True if matching
	 */
	public boolean compare(String funcName) {
		return this.functionName.equals(funcName);
	}
	
	/**
	 * Directly compares this function's name and parameters to the
	 * incoming name and parameters. Returns true if they match
	 * 
	 * @param funcName The incoming function name
	 * @param params The incoming parameters
	 * @return True if matching
	 */
	public boolean compare(String funcName, ParameterData params) {
		return this.functionName.equals(funcName) && this.parameters.compare(params);
	}
	
	/**
	 * The expected number of arguments to be read. 255 is the absolute
	 * max.
	 */
	public int argLength() {
		return parameters.length();
	}
	
	/**
	 * The expected datatypes of the arguments to be read. Return null to
	 * indicate that types do not matter.
	 */
	public ParameterData argTypes() {
		return null;
	}
	
	/**
	 * Executes and returns the result of this function call. If the function
	 * does not actually return anything, return VOID instead.
	 * 
	 * @param <E>         An expected function return type
	 * @param interpreter The interpreter to execute the function with
	 * @param arg         The single argument to pass to the function
	 * 
	 * @return The result of the function's execution or VOID by default
	 */
	public <E extends EnvisionObject> E invoke_r(EnvisionInterpreter interpreter, EnvisionObject arg) {
		return invoke_r(interpreter, new EnvisionObject[]{arg});
	}
	
	/**
	 * Executes and returns the result of this function call. If the function
	 * does not actually return anything, return VOID instead.
	 * 
	 * @param <E>         An expected function return type
	 * @param interpreter The interpreter to execute the function with
	 * @param args        The arguments to pass to the function
	 * 
	 * @return The result of the function's execution or VOID by default
	 */
	public <E extends EnvisionObject> E invoke_r(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		// actually begin executing the function
		try {
			invoke(interpreter, args);
		}
		catch (ReturnValue r) {
			// return any value produced by the function's execution
			return (E) r.result;
		}
		
		// if no value was produced, return void by default
		return (E) EnvisionVoid.VOID;
	}
	
	/**
	 * Executes this function call with zero passed arguments.
	 * 
	 * @param interpreter The interpreter to execute the function with
	 */
	public void invoke(EnvisionInterpreter interpreter) {
		invoke(interpreter, new EnvisionObject[0]);
	}
	
	/**
	 * Executes this function call with one passed argument.
	 * 
	 * @param interpreter The interpreter to execute the function with
	 * @param arg         The single arguments to be passed to the function
	 */
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject arg) {
		invoke(interpreter, new EnvisionObject[]{arg});
	}
	
	/**
	 * Executes this function call with an array of passed arguments.
	 * 
	 * @param interpreter The interpreter to execute the function with
	 * @param args        The arguments to be passed to the function
	 */
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		checkArgs(args);
		invoke_i(interpreter, args);
	}
	
	/**
	 * Checks for argument size and invalid argument vs. parameter type errors.
	 */
	protected void checkArgs(EnvisionObject[] args) {
		int arg_length = argLength();
		ParameterData types = argTypes();
		
		EnvisionObject[] incoming_args = args;
		if (incoming_args == null) incoming_args = new EnvisionObject[0];
		
		// ensure that the expected number of arguments is possible
		if (arg_length < 0) throw new NegativeArgumentLengthError(this, arg_length);
		
		// ensure that the expected number of arguments is not larger than the maximum allowed
		if (arg_length > 255) throw new ArgLengthError(this, arg_length, 255);
		if (incoming_args.length > 255) throw new ArgLengthError(this, incoming_args.length, 255);
		
		// don't care about types if the expected ones are null
		if (types == null) return;
		
		// perform type checking and verification across each incoming argument vs. each expected parameter type
		ParameterData parsedArgs = ParameterData.from(incoming_args);
		
		// compare each parameter to ensure that they are compatible matches
		for (int i = 0, j = 0; i < incoming_args.length; i++) {
			EnvisionParameter type = types.get(j);
			EnvisionParameter obj = parsedArgs.get(i);
			
			// if they're not compatible, throw error
			if (!type.compare(obj)) throw new InvalidDatatypeError(type, obj);
		}
		
		// ensure that there is a valid function overload to support the given arguments
		EnvisionFunction overload = getOverload(parsedArgs);
		// if there are no overloads with matching parameters -- throw an error
		if (overload == null) throw new NoOverloadError(this, parsedArgs);
	}
	
	/**
	 * Attempts to run the function with the given arguments.
	 */
	protected void invoke_i(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		// create a new scope derived from this function's scope
		IScope scope = new Scope(instanceScope);
		
		EnvisionFunction m = getOverloadFromArgs(args);
		
		// define parameter values within the current scope
		String[] parameterNames = m.parameters.getNames();
		for (int i = 0; i < m.parameters.length(); i++) {
			String n = parameterNames[i];
			scope.define(n, args[i]);
		}
		
		// if this is a constructor, check if any of the arguments are assignment args
		// Essentially, if any argument name matches an existing class level variable -- assign it directly
		if (isConstructor) {
			IScope ps = scope.getParent();
			
			for (int i = 0; i < args.length; i++) {
				String arg_name = parameterNames[i];
				EnvisionObject arg_obj = args[i];
				
				ScopeEntry scopeValue = ps.getTyped(arg_name);
				if (scopeValue == null) continue;
				
				IDatatype scope_var_type = scopeValue.getDatatype();
				IDatatype incoming_type = arg_obj.getDatatype();
				
				// always allow vars
				if (EnvisionStaticTypes.VAR_TYPE.compare(scope_var_type)) {
					ps.set(arg_name, arg_obj);
				}
				// allow if the types are direct matches
				else if (scope_var_type.compare(incoming_type)) {
					ps.set(arg_name, arg_obj);
				}
				// allow if expected type is a number and the incoming type is a type of number
				else if (IDatatype.isNumber(scopeValue.getDatatype())) {
					EnvisionObject obj = CastingUtil.castToNumber(arg_obj, scopeValue.getDatatype());
					ps.set(arg_name, obj);
				}
				else throw new InvalidArgumentError(functionName, scope_var_type, incoming_type);
			}
		}
		
		// handle block execution and any return values thrown from the executed block
		interpreter.executeStatementsForReturns(m.statements, scope);
		
		// if this point is reached and the function is a constructor, return 'this' by default
		if (isConstructor) ret(scope.get("this"));
	}
	
	//=========
    // Getters
    //=========
	
	/**
	 * @return The name of this function
	 */
	public String getFunctionName() { return functionName; }
	public ParameterData getParams() { return parameters; }
	public EList<ParsedStatement> getStatements() { return statements; }
	public EList<EnvisionFunction> getOverloads() { return overloads; }
	public EList<ParsedStatement> getBody() { return statements; }
	public IDatatype[] getParamTypes() { return parameters.getDataTypes(); }
	public String[] getParamNames() { return parameters.getNames(); }
	public EnvisionFunction getSuper() { return superFunction; }
	
	public boolean isVoid() { return returnType.isVoid(); }
	public boolean isConstructor() { return isConstructor; }
	public boolean isOperator() { return isOperatorOverload; }
	
	public IDatatype getReturnType() { return returnType; }
	public Operator getOperator() { return operatorOverload; }
	
	//=========
    // Setters
    //=========
	
	public EnvisionFunction setBody(EList<ParsedStatement> in) {
		statements.clear();
		statements.addAll(in);
		return this;
	}
	
	public EnvisionFunction setParams(ParameterData dataIn) {
		parameters = ParameterData.from(dataIn);
		return this;
	}
	
	public EnvisionFunction assignParentClass(EnvisionClass parentClassIn) {
		parentClass = parentClassIn;
		return this;
	}
	
	public void setSuper(EnvisionFunction func) {
		superFunction = func;
	}
	
	public EnvisionFunction addStatement(ParsedStatement statementIn) {
		return statements.addR(statementIn, this);
	}
	
	//================
    // Static Methods
    //================
	
	public static EnvisionFunction copy(EnvisionFunction m) {
		return new EnvisionFunction(m);
	}
	
}