package envision_lang.lang.packages;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.Primitives;

/**
 * A package is a structure within Envision which bundles a scope of
 * variables, classes, members, and other objects together inside of a
 * portable object. Packages are also the primary means of importing
 * data from one scope into another.
 * 
 * @author Hunter Bragg
 */
public class EnvisionLangPackage extends EnvisionObject {
	
	//--------
	// Fields
	//--------
	
	protected final String packageName;
	protected Scope packageScope;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionLangPackage(String packageNameIn) {
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
	public final void addPackage(EnvisionLangPackage pkg) {
		if (pkg == null) throw new NullPointerException();
		
		//define the package as a referencable object
		packageScope.define(pkg.packageName, pkg.internalType, pkg);
		
		//add each object within the incoming package's scope
		Scope incomming_scope = pkg.packageScope;
		for (var obj : incomming_scope.values.entrySet()) {
			String import_val_name = obj.getKey();
			ScopeEntry import_entry = obj.getValue();
			
			packageScope.define(import_val_name, import_entry);
		}
	}
	
	public final void defineOn(EnvisionInterpreter interpreter) {
		defineOn(interpreter.internalScope());
	}
	
	public final void defineOn(IScope scope) {
		//add each object within the incoming package's scope
		for (var obj : packageScope.values.entrySet()) {
			String import_val_name = obj.getKey();
			ScopeEntry import_entry = obj.getValue();
			
			scope.defineImportVal(import_val_name, import_entry);
		}
		scope.defineImportVal(packageName, this);
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
	
	public EnvisionLangPackage setScope(Scope in) {
		packageScope = in;
		return this;
	}
	
}
