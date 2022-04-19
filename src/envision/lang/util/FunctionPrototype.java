package envision.lang.util;

import java.util.HashMap;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import eutil.datatypes.EArrayList;

/**
 * A low-level function placeholder that is intended to be handled
 * natively within primitive object types.
 * <p>
 * Due to the fact that internal Envision objects can actually never
 * be directly overwritten, there is no reason to have actual
 * EnvisionFunctions directly backing the internal primitive member
 * functions.
 * <p>
 * This design choice is intended to provide a significant performance
 * optimization by removing the need to create actual internal member
 * function instances.
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
	private final EnvisionDatatype returnType;
	
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
	private final HashMap<EnvisionDatatype, EArrayList<ParameterData>> overloads = new HashMap();
	
	private final EArrayList<ParameterData> overload_params = new EArrayList();
	
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
	 * Envision. Normally, primitive objects will not need to dynamically built
	 * their primitive functions as they are intended to be executed internally by
	 * Java, translated into Envision wrapper objects, and subsequently returned to
	 * the Envision scope.
	 * <p>
	 * Note: this backing class can be intentionally left null in order to prevent
	 * specific function extractions for internally protected members.
	 */
	private Class<? extends InstanceFunction> func_class;
	
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
	private ClassInstance instance;
	
	/**
	 * In the event that a primitive object's function needs to be dynamically
	 * built, this will be the constructed function instance capable of being used
	 * within Envision.
	 */
	private InstanceFunction built_func;
	
	/*
	 * Unsure if needed for right now
	 */
	private InstanceFunction flash_func;
	
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
		this(nameIn, EnvisionDatatype.VAR_TYPE, new ParameterData());
	}
	
	public FunctionPrototype(String nameIn, Primitives rType) {
		this(nameIn, rType.toDatatype(), new ParameterData());
	}
	
	public FunctionPrototype(String nameIn, EnvisionDatatype rType) {
		this(nameIn, rType, new ParameterData());
	}
	
	public FunctionPrototype(String nameIn, Primitives rType, Primitives... paramsIn) {
		this(nameIn, rType.toDatatype(), new ParameterData(paramsIn));
	}
	
	public FunctionPrototype(String nameIn, EnvisionDatatype rType, Primitives... paramsIn) {
		this(nameIn, rType, new ParameterData(paramsIn));
	}
	
	public FunctionPrototype(String nameIn, Primitives rType, EnvisionDatatype... paramsIn) {
		this(nameIn, rType.toDatatype(), new ParameterData(paramsIn));
	}
	
	public FunctionPrototype(String nameIn, EnvisionDatatype rType, EnvisionDatatype... paramsIn) {
		this(nameIn, rType, new ParameterData(paramsIn));
	}
	
	public FunctionPrototype(String nameIn, Primitives rType, ParameterData paramsIn) {
		this(nameIn, rType.toDatatype(), paramsIn);
	}
	
	public FunctionPrototype(String nameIn, EnvisionDatatype rType, ParameterData paramsIn) {
		super(EnvisionDatatype.FUNC_TYPE);
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
	public InstanceFunction build(ClassInstance instIn) {
		//check if already built and return built_func
		if (built) return built_func;
		
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
			built_func.setInst(instance);
			if (!acc) con.setAccessible(false);
			
			//mark as built and return newly created function
			built = true;
			return built_func;
		}
		//realistically, this error could only possibly be thrown is a mismatching
		//function class or an invalid class instance were passed to this prototype.
		catch (Exception e) {
			throw new EnvisionError(e);
		}
	}
	
	//-----------
	// Overloads
	//-----------
	
	public FunctionPrototype addOverload(Primitives rType, Primitives... params) {
		return addOverload(rType.toDatatype(), new ParameterData(params));
	}
	
	public FunctionPrototype addOverload(EnvisionDatatype rType, Primitives... params) {
		return addOverload(rType, new ParameterData(params));
	}
	
	public FunctionPrototype addOverload(Primitives rType, EnvisionDatatype... params) {
		return addOverload(rType.toDatatype(), new ParameterData(params));
	}
	
	public FunctionPrototype addOverload(EnvisionDatatype rType, EnvisionDatatype... params) {
		return addOverload(rType, new ParameterData(params));
	}
	
	public FunctionPrototype addOverload(Primitives rType, ParameterData params) {
		return addOverload(rType.toDatatype(), params);
	}
	
	public FunctionPrototype addOverload(EnvisionDatatype rType, ParameterData params) {
		//check if there is a bucket made for the given return type
		EArrayList<ParameterData> bucket = overloads.get(rType);
		//if there is no bucket, make one first
		if (bucket == null) {
			overloads.put(rType, bucket = new EArrayList());
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
	public boolean hasOverload(EnvisionDatatype rType, ParameterData params) {
		EArrayList<ParameterData> bucket = overloads.get(rType);
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
		ParameterData params = new ParameterData(argsIn);
		return hasOverload(params);
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
	public EnvisionDatatype getReturnType() {
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
	public FunctionPrototype setInstance(ClassInstance inst) {
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
	public FunctionPrototype assignDynamicClass(Class<? extends InstanceFunction> classIn) {
		func_class = classIn;
		return this;
	}
	
}
