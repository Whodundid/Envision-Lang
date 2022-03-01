package envision.lang.packages;

import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

/**
 *  A package is a structure within Envision which bundles a scope of variables, classes, members, and
 *  other objects together inside of a portable object. Packages are also the primary means of
 *  importing data from one scope into another.
 */
public class EnvisionPackage extends EnvisionObject {
	
	Scope packageScope;
	
	public EnvisionPackage(String packageName) {
		super(new EnvisionDatatype(Primitives.PACKAGE), packageName);
	}
	
	//---------
	// Methods
	//---------
	
	//---------
	// Getters
	//---------
	
	public Scope getScope() { return packageScope; }
	
	//---------
	// Setters
	//---------
	
	public EnvisionPackage setScope(Scope in) { packageScope = in; return this; }
	
}
