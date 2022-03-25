package envision.packages;

import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.util.Primitives;

/**
 * A package is a structure within Envision which bundles a scope of
 * variables, classes, members, and other objects together inside of a
 * portable object. Packages are also the primary means of importing
 * data from one scope into another.
 * 
 * @author Hunter Bragg
 */
public class EnvisionPackage extends EnvisionObject {
	
	//--------
	// Fields
	//--------
	
	protected final String packageName;
	protected Scope packageScope;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionPackage(String packageNameIn) {
		super(Primitives.PACKAGE.toDatatype());
		packageName = packageNameIn;
	}
	
	//---------
	// Getters
	//---------
	
	public String getPackageName() {
		return packageName;
	}
	
	public Scope getScope() {
		return packageScope;
	}
	
	//---------
	// Setters
	//---------
	
	public EnvisionPackage setScope(Scope in) {
		packageScope = in;
		return this;
	}
	
}
