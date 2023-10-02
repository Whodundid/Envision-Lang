package envision_lang.lang.functions;

import java.util.HashMap;
import java.util.Map;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import eutil.datatypes.util.EList;

/**
 * A low-level function placeholder that is intended to be handled
 * natively within primitive object types.
 * <p>
 * Effectively, a function prototype is a placeholder for an actual
 * EnvisionFunction while still offering the same end-behavior. Given
 * the fact that an object's member functions aren't even guaranteed
 * to be called in the first place (let alone referenced), it can
 * potentially be a significant waste of processing time and resources
 * to actually create full-on EnvisionFunctions for every object.
 * <p>
 * Prototypes, upon being called however, will actually go through the
 * process of building a complete EnvisionFunction. Once built, the
 * constructed EnvisionFunction will then be used moving forward.
 * <p>
 * Another instance in which a prototype will actually build into an
 * EnvisionFunction is when it is extracted into a variable. Given
 * that variables must be actual EnvisionObjects, this prototype must
 * be built into an actual EnvisionFunction so that it can be used
 * within the language.
 * 
 * @author Hunter Bragg
 */
public class FunctionPrototype extends EnvisionObject {

	/**
	 * The function name that this placeholder will take in scopes.
	 */
	private final String func_name;
	
	/**
	 * The return type of this function.
	 */
	private final IDatatype returnType;
	
	/**
	 * The accepted parameters of this function.
	 */
	private final ParameterData params;
	
	/**
	 * Any overloads of this specific function.
	 * These overloads are internally created and managed.
	 * 
	 * Stores overloads in the form of 'return type', '[ParameterData]*'.
	 * Note: Parameter data can be empty -- hence *.
	 */
	private final Map<IDatatype, EList<ParameterData>> overloads = new HashMap<>();
	
	private final EList<ParameterData> overload_params = EList.newList();
	
	/**
	 * Even internal functions must be bound by some pre-declared Java class in and
	 * subsequently built if needed. In the event that a function is extracted from
	 * an object.
	 * 
	 * <blockquote>
	 * <pre>
	 * 		string hello = "Hello"
	 * 		func upper_func = hello.toUpperCase
	 * 
	 * 		// upper_func now contains the 'toUpperCase'
	 * 		// function for the string 'hello'.
	 * 
	 * 		// implicit invocation
	 * 		string upper = upper_func()
	 * 
	 * 		// Prints: 'HELLO'
	 * 		println(upper)
	 * </pre>
	 * </blockquote>
	 * 
	 * <p>
	 * Given the above example: the function 'toUpperCase' is extracted from the
	 * 'hello' string. This function needs to be dynamically built into an actual
	 * EnvisionFunction instance because it is now being referenced directly within
	 * Envision. Normally, primitive objects will not need to dynamically build
	 * their primitive functions as they are intended to be executed internally by
	 * Java, translated into Envision wrapper objects, and subsequently returned to
	 * the Envision scope.
	 * <p>
	 * Note: this backing class can be intentionally left null in order to prevent
	 * specific function extractions for internally protected members.
	 */
	private Class<? extends InstanceFunction<? extends EnvisionObject>> func_class;
	
	/**
	 * Just as a specific object's member function's directly pertain to the object
	 * instance they reside in, dynamically built primitive functions must have an
	 * object instance for which they pertain to. This ClassInstance is the exact
	 * primitive object for which a function (such as this one) directly pertains
	 * to.
	 * <p>
	 * Note: due to this design structure, static functions should not be handled
	 * using the internal prototype design.
	 */
	private EnvisionObject instance;
	
	/**
	 * In the event that a primitive object's function needs to be dynamically
	 * built, this will be the constructed function instance capable of being used
	 * within Envision.
	 */
	private InstanceFunction built_func;
	
	/**
	 * Locally keeps track of whether or not this function prototype has been
	 * dynamically built into an actual EnvisionFunction or not. In the event
	 * that this flag is false, upon Envision code performing the extraction of a primitive
	 * object's member function, this prototype will first need to build the actual
	 * EnvisionFunction instance and then subsequently return it. 
	 */
	private boolean built = false;
	
	//--------------
	// Constructors
	//--------------
	
	public FunctionPrototype(String nameIn) {
		this(nameIn, EnvisionStaticTypes.VAR_TYPE, ParameterData.EMPTY_PARAMS);
	}
	
	public FunctionPrototype(String nameIn, IDatatype rType) {
		this(nameIn, rType, ParameterData.EMPTY_PARAMS);
	}
	
	public FunctionPrototype(String nameIn, IDatatype rType, IDatatype... paramsIn) {
		this(nameIn, rType, ParameterData.from(paramsIn));
	}
	
	public FunctionPrototype(String nameIn, IDatatype rType, ParameterData paramsIn) {
		super(EnvisionStaticTypes.FUNC_TYPE);
		func_name = nameIn;
		returnType = rType;
		params = paramsIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "proto_"+func_name;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns true if this primitive function has been dynamically built into a
	 * full on EnvisionFunction.
	 * 
	 * @return True if built into an EnvisionFunction
	 */
	public boolean isBuilt() {
		return built;
	}

	/**
	 * Executed when initially calling the function. The function is built
	 * and replaces the prototype placeholder within the current scope.
	 * 
	 * @param interpreter
	 * @return The built function
	 */
	public InstanceFunction<? extends EnvisionObject> build(EnvisionObject instIn) {
		//NOTE: building needs to take into account the scope that it is coming from
		
		//check if already built and return built_func
		if (built) return built_func.setInst(instIn);
		
		//check if there is even a dynamic class to build from
		if (func_class == null) {
			built = true;
			return null;
		}
		
		//otherwise, attempt to build the actual function dynamically
		try {
			var con = func_class.getDeclaredConstructor();
			var acc = con.canAccess(null);
			
			if (instIn != null) instance = instIn;
			
			//forcibly gain access and assign
			if (!acc) con.setAccessible(true);
			built_func = con.newInstance();
			EnvisionFunctionClass.FUNC_CLASS.defineFunctionScopeMembers(built_func);
			built_func.setInst(instance);
			if (!acc) con.setAccessible(false);
			
			//mark as built and return newly created function
			built = true;
			return built_func;
		}
		//realistically, this error could only possibly be thrown is a mismatching
		//function class or an invalid class instance were passed to this prototype.
		catch (Exception e) {
			throw new EnvisionLangError(e);
		}
	}
	
	//-----------
	// Overloads
	//-----------
	
	public FunctionPrototype addOverload(IDatatype rType, IDatatype... params) {
		return addOverload(rType, ParameterData.from(params));
	}
	
	public FunctionPrototype addOverload(IDatatype rType, ParameterData params) {
		//check if there is a bucket made for the given return type
		EList<ParameterData> bucket = overloads.get(rType);
		//if there is no bucket, make one first
		if (bucket == null) {
		    bucket = EList.newList();
			overloads.put(rType, bucket);
		}
		bucket.add(params);
		overload_params.add(params);
		return this;
	}
	
	/**
	 * Checks whether or not an overload has been made for this internal prototype
	 * declaration. Uses the given return type to check if the following parameters
	 * actually match.
	 * <p>
	 * Overload parameters are linearly stored/searched for as it is incredibly
	 * unlikely that there will ever be a scenario in which there are enough
	 * function overloads to warrant needing a more complex storage medium or search
	 * algorithm. Furthermore, once a primitive function prototype has been
	 * dynamically built into a full-on EnvisionFunction, primitive functions, in
	 * general, are entirely handled behind the scenes using Java.
	 * 
	 * @param rType  The overload's return type
	 * @param params The overload's parameters
	 * @return True if there is an overload with the same return type and parameters
	 */
	public boolean hasOverload(IDatatype rType, ParameterData params) {
		EList<ParameterData> bucket = overloads.get(rType);
		//if there's no bucket, then there's no overload -- return false
		if (bucket == null) return false;
		
		//linearly search through the parameter bucket for a match
		for (int i = 0; i < bucket.size(); i++) {
			ParameterData pd = bucket.get(i);
			if (pd.compare(params)) return true;
		}
		
		return false;
	}
	
	/**
	 * Checks to see if there is an overload with the given parameters. Does not
	 * care about return type.
	 * 
	 * @param paramsIn
	 * @return
	 */
	public boolean hasOverload(ParameterData paramsIn) {
		//first compare base function parameters
		if (params.compare(paramsIn)) return true;
		//iterate linearly across all parameters in overloads
		return overload_params.anyMatch(p -> p.compare(paramsIn));
	}
	
	/**
	 * Converts the incomming arguments to parameters and then checks to see if
	 * there is an overload with such parameters. Does not care about return type.
	 * 
	 * @param argsIn
	 * @return
	 */
	public boolean hasOverload(EnvisionObject[] argsIn) {
		//convert args to parameterData
		return hasOverload(ParameterData.from(argsIn));
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the placeholder function name of this prototype.
	 * 
	 * @return The name of this function
	 */
	public String getFunctionName() {
		return func_name;
	}
	
	/**
	 * Returns the placeholder function's return type.
	 * 
	 * @return The return type of this function
	 */
	public IDatatype getReturnType() {
		return returnType;
	}
	
	/**
	 * Returns the placeholder function's parameter data.
	 * 
	 * @return The parameter data of this function.
	 */
	public ParameterData getParams() {
		return params;
	}
	
	//---------
	// Setters
	//---------
	
	/**
	 * Assigns the classInstance for which the a dynamically built internal function
	 * will be binded against.
	 * 
	 * @param inst The primitive object for which the dynamically constructed member
	 *             function will be binded to
	 * @return This prototype
	 */
	public FunctionPrototype setInstance(EnvisionObject inst) {
		instance = inst;
		return this;
	}
	
	/**
	 * Used to assign the Java class which can be used to create dynamic instances
	 * of this internal function prototype.
	 * <p>
	 * Note: Do not assign to intentionally prevent this member function from being
	 * extracted and used within Envision.
	 * 
	 * @param classIn The EnvisionFunction class which is used to dynamically back a
	 *                primitive object's member function within Envision.
	 * @return This prototype
	 */
	public FunctionPrototype assignDynamicClass(Class<? extends InstanceFunction<? extends EnvisionObject>> classIn) {
		func_class = classIn;
		return this;
	}
	
}
