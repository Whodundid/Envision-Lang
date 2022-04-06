package envision.lang.util;

import java.util.HashMap;

import envision.lang.EnvisionObject;
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
public class IFunctionPrototype extends EnvisionObject {

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
	 */
	private final HashMap<EnvisionDatatype, EArrayList<ParameterData>> overloads = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	public IFunctionPrototype(String nameIn) {
		this(nameIn, EnvisionDatatype.VAR_TYPE, new ParameterData());
	}
	
	public IFunctionPrototype(String nameIn, Primitives rType) {
		this(nameIn, rType.toDatatype(), new ParameterData());
	}
	
	public IFunctionPrototype(String nameIn, EnvisionDatatype rType) {
		this(nameIn, rType, new ParameterData());
	}
	
	public IFunctionPrototype(String nameIn, Primitives rType, Primitives... paramsIn) {
		this(nameIn, rType.toDatatype(), new ParameterData(paramsIn));
	}
	
	public IFunctionPrototype(String nameIn, EnvisionDatatype rType, Primitives... paramsIn) {
		this(nameIn, rType, new ParameterData(paramsIn));
	}
	
	public IFunctionPrototype(String nameIn, Primitives rType, EnvisionDatatype... paramsIn) {
		this(nameIn, rType.toDatatype(), new ParameterData(paramsIn));
	}
	
	public IFunctionPrototype(String nameIn, EnvisionDatatype rType, EnvisionDatatype... paramsIn) {
		this(nameIn, rType, new ParameterData(paramsIn));
	}
	
	public IFunctionPrototype(String nameIn, Primitives rType, ParameterData paramsIn) {
		this(nameIn, rType.toDatatype(), paramsIn);
	}
	
	public IFunctionPrototype(String nameIn, EnvisionDatatype rType, ParameterData paramsIn) {
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
	
	//-----------
	// Overloads
	//-----------
	
	public IFunctionPrototype addOverload(Primitives rType, Primitives... params) {
		return addOverload(rType.toDatatype(), new ParameterData(params));
	}
	
	public IFunctionPrototype addOverload(EnvisionDatatype rType, Primitives... params) {
		return addOverload(rType, new ParameterData(params));
	}
	
	public IFunctionPrototype addOverload(Primitives rType, EnvisionDatatype... params) {
		return addOverload(rType.toDatatype(), new ParameterData(params));
	}
	
	public IFunctionPrototype addOverload(EnvisionDatatype rType, EnvisionDatatype... params) {
		return addOverload(rType, new ParameterData(params));
	}
	
	public IFunctionPrototype addOverload(Primitives rType, ParameterData params) {
		return addOverload(rType.toDatatype(), params);
	}
	
	public IFunctionPrototype addOverload(EnvisionDatatype rType, ParameterData params) {
		//check if there is a bucket made for the given return type
		EArrayList<ParameterData> bucket = overloads.get(rType);
		//if there is no bucket, make one first
		if (bucket == null) {
			overloads.put(rType, bucket = new EArrayList());
		}
		bucket.add(params);
		return this;
	}
	
	/**
	 * Checks whether or not an overload has been made for this internal prototype
	 * declaration. Uses the given return type to check if the following parameters
	 * actually match.
	 * 
	 * @param rType The overload's return type
	 * @param params The overload's parameters
	 * @return True if there is a parameter with the same return type and parameters
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
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the placeholder function name of this prototype.
	 * 
	 * @return The name of this function
	 */
	public String getFunctionName() { return func_name; }
	
	/**
	 * Returns the placeholder function's return type.
	 * 
	 * @return The return type of this function
	 */
	public EnvisionDatatype getReturnType() { return returnType; }
	
	/**
	 * Returns the placeholder function's parameter data.
	 * 
	 * @return The parameter data of this function.
	 */
	public ParameterData getParams() { return params; }
	
}
