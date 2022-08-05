package envision_lang.packages;

import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;

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
		super(Primitives.PACKAGE);
		packageName = packageNameIn;
		packageScope = new Scope();
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Adds another package onto this package's scope.
	 * 
	 * @param pkg The package to add
	 */
	public void addPackage(EnvisionPackage pkg) {
		if (pkg == null) throw new NullPointerException();
		
		//define the package as a referencable object
		packageScope.define(pkg.packageName, pkg.internalType, pkg);
		
		//add each object within the incoming package's scope
		Scope incomming_scope = pkg.packageScope;
		for (var obj : incomming_scope.values.entrySet()) {
			String import_val_name = obj.getKey();
			IDatatype import_val_type = obj.getValue().getA();
			EnvisionObject import_val_obj = obj.getValue().getB();
			
			packageScope.define(import_val_name, import_val_type, import_val_obj);
		}
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
