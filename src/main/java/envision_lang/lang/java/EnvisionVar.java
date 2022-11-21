package envision_lang.lang.java;

import envision_lang.lang.internal.EnvisionNull;

public class EnvisionVar {

	//--------
	// Fields
	//--------
	
	public Object value;
	public boolean isStrong = false;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionVar() { this (EnvisionNull.NULL); }
	public EnvisionVar(Object valueIn) {
		value = valueIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "var[" + value + "]";
	}
	
	//---------
	// Getters
	//---------
	
	public Object get() { return value; }
	public boolean isStrong() { return isStrong; }
	
	//---------
	// Setters
	//---------
	
	public Object set(Object in) { return (value = in); }
	
	public void setString() { setStrong(true); }
	public void setStrong(boolean val) { isStrong = val; }
	
}
