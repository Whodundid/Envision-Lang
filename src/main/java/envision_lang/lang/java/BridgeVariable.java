package envision_lang.lang.java;

import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import eutil.datatypes.util.JavaDatatype;

/**
 * Holds the value of an Envision variable as well as it's linked
 * native Java object which directly backs it.
 * 
 * @author Hunter Bragg
 */
public class BridgeVariable {

	//--------
	// Fields
	//--------
	
	public EnvisionObject envisionObject;
	public Object javaObject;
	public IDatatype envisionType;
	public JavaDatatype javaType;
	
	//--------------
	// Constructors
	//--------------
	
	public BridgeVariable(JavaDatatype javaTypeIn, Object javaObjectIn) {
		javaType = javaTypeIn;
		javaObject = javaObjectIn;
		
		envisionType = IDatatype.fromJavaType(javaType);
		
		// don't allow null envision types to advance
		if (envisionType == null)
			throw new EnvisionLangError("No valid Envision datatype mapping for Java type: [" + javaTypeIn + "] !");
		
		envisionObject = ObjectCreator.createObject(envisionType, javaObjectIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "bridgeVar[" + javaType + ":" + javaObject + " <==> " + envisionType + ":" + envisionObject + "]";
	}
	
	//---------
	// Getters
	//---------
	
	public JavaDatatype getJavaType() { return javaType; }
	public IDatatype getEnvisionType() { return envisionType; }

	public EnvisionObject getEnvisionObject() { return envisionObject; }
	public Object getJavaObject() { return javaObject; }
	
	//---------
	// Setters
	//---------
	
	public void setBoth(Object javaObjectIn, EnvisionObject envisionObjectIn) {
		javaObject = javaObjectIn;
		envisionObject = envisionObjectIn;
	}
	
	public void set(Object in) {
		// magic needs to happen here..
		
		javaObject = in;
		envisionObject = ObjectCreator.createObject(envisionType, in);
	}
	
}
