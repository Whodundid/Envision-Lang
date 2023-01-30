package envision_lang.lang.util;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.InvalidParameterError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.StaticTypes;
import eutil.strings.EStringUtil;

/**
 * A object which comprises function parameters within the Envision::Java
 * scripting language.
 * 
 * @author Hunter Bragg
 */
public class ParameterData {
	
	public static final ParameterData EMPTY_PARAMS = new ParameterData();
	
	private EnvisionParameter[] params;
	private String[] parameterNames;
	private IDatatype[] parameterTypes;
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Builds an empty set of parameters.
	 */
	private ParameterData() {
		params = new EnvisionParameter[0];
		parameterNames = new String[0];
		parameterTypes = new IDatatype[0];
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param dataIn
	 */
	private ParameterData(ParameterData dataIn) {
		int size = dataIn.length();
		params = new EnvisionParameter[size];
		parameterNames = new String[size];
		parameterTypes = new IDatatype[size];
		
		for (int i = 0; i < size; i++) {
			EnvisionParameter p = dataIn.get(i);
			IDatatype type = p.datatype;
			String name = p.name;
			
			params[i] = new EnvisionParameter(type, name);
			parameterNames[i] = name;
			parameterTypes[i] = type;
		}
	}
	
	private ParameterData(EnvisionParameter[] paramsIn) {
		params = (paramsIn != null) ? paramsIn : new EnvisionParameter[0];
		parameterNames = new String[params.length];
		parameterTypes = new IDatatype[params.length];
	}
	
	private ParameterData(IDatatype... typesIn) {
		int size = typesIn.length;
		params = new EnvisionParameter[size];
		parameterNames = new String[size];
		parameterTypes = new IDatatype[size];
		
		for (int i = 0; i < size; i++) {
			IDatatype type = typesIn[i];
			
			// check for valid parameter datatype
			if (type == null) throw new InvalidParameterError("Java::NULL is not a valid parameter type!");
			if (StaticTypes.NULL_TYPE.compare(type)) throw new InvalidParameterError("Envision::NULL is not a valid parameter type!");
			
			String name = "";
			EnvisionParameter param = new EnvisionParameter(type, name);
			
			params[i] = param;
			parameterNames[i] = name;
			parameterTypes[i] = type;
		}
	}
	
	private ParameterData(EnvisionObject... objsIn) {
		int size = objsIn.length;
		params = new EnvisionParameter[size];
		parameterNames = new String[size];
		parameterTypes = new IDatatype[size];
		
		for (int i = 0; i < size; i++) {
			EnvisionObject obj = objsIn[i];
			
			// check for valid parameter datatype
			if (obj == null) throw new InvalidParameterError("Java::NULL is not a valid parameter type!");
			if (obj instanceof EnvisionNull) throw new InvalidParameterError("Envision::NULL is not a valid parameter type!");
			
			IDatatype type = obj.getDatatype();
			String name = "";
			
			EnvisionParameter param = new EnvisionParameter(type, name);
			
			params[i] = param;
			parameterNames[i] = name;
			parameterTypes[i] = type;
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return EStringUtil.toString(parameterTypes);
	}
	
	//---------
	// Methods
	//---------
	
	public int length() {
		return params.length;
	}
	
	public boolean compare(ParameterData dataIn) {
		if (dataIn.length() == length()) {
			for (int i = 0; i < length(); i++) {
				EnvisionParameter a = get(i);
				EnvisionParameter b = dataIn.get(i);
				
				if (a.datatype.getPrimitive() == Primitives.NUMBER && b.isNumber()) return true;
				if (a.datatype.isVar()) continue;
				if (!a.compare(b)) return false;
			}
			return true;
		}
		// handle array types
		else if (params.length > 0 && parameterTypes[0].isArrayType()) {
			EnvisionParameter param = params[0];
			IDatatype type = parameterTypes[0];
			Primitives base = type.getPrimitive().getNonArrayType();
			
			if (base == null) throw new EnvisionLangError("Parameter type parsing failed! '" + param + "'");
			if (base == Primitives.VAR) return true;
			
			int size = length();
			for (int i = 0; i < size; i++) {
				EnvisionParameter pIn = dataIn.get(i);
				
				if (base == Primitives.NUMBER && pIn.isNumber()) return true;
				if (!type.compare(pIn.datatype)) return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	//---------
	// Getters
	//---------
	
	public EnvisionParameter get(int i) { return params[i]; }
	public String[] getNames() { return parameterNames; }
	public IDatatype[] getDataTypes() { return parameterTypes; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static ParameterData from(EnvisionParameter... params) {
		if (params.length == 0) return EMPTY_PARAMS;
		return new ParameterData(params);
	}
	
	public static ParameterData from(IDatatype... types) {
		if (types.length == 0) return EMPTY_PARAMS;
		return new ParameterData(types);
	}
	
	public static ParameterData from(EnvisionObject... objects) {
		if (objects.length == 0) return EMPTY_PARAMS;
		return new ParameterData(objects);
	}
	
	public static ParameterData from(ParameterData dataIn) {
		if (dataIn == EMPTY_PARAMS) return EMPTY_PARAMS;
		if (dataIn.length() == 0) return EMPTY_PARAMS;
		return new ParameterData(dataIn);
	}
	
}
