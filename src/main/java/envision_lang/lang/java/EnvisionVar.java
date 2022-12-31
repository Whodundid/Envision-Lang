package envision_lang.lang.java;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;

/**
 * Holds the value of an Envision 'var' variable as well as it's linked
 * native Java object which directly backs it.
 * 
 * @author Hunter Bragg
 */
public class EnvisionVar {

	//--------
	// Fields
	//--------
	
	public EnvisionObject envisionObject;
	public Object javaObject;
	public IDatatype datatype;
	public boolean isStrong = false;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionVar(IDatatype typeIn) { this (typeIn, EnvisionNull.NULL, null); }
	public EnvisionVar(IDatatype typeIn, EnvisionObject envisionObjectIn, Object javaObjectIn) {
		envisionObject = envisionObjectIn;
		javaObject = javaObjectIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "var[" + envisionObject + " :: " + javaObject + "]";
	}
	
	//---------
	// Getters
	//---------
	
	public EnvisionObject getEnvisionObject() { return envisionObject; }
	public Object getJavaObject() { return javaObject; }
	public boolean isStrong() { return isStrong; }
	
	//---------
	// Setters
	//---------
	
	public void setBoth(Object javaObjectIn, EnvisionObject envisionObjectIn) {
		javaObject = javaObjectIn;
		envisionObject = envisionObjectIn;
	}
	
	public void set(Object in) {
		// magic needs to happen here..
	}
	
	public void setStrong() { setStrong(true); }
	public void setStrong(boolean val) { isStrong = val; }
	
}
