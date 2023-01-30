package envision_lang.lang.packages;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.EnvisionFunction;

/**
 * A special type of package that is natively imported at program
 * start.
 */
public abstract class EnvisionLangPackage extends EnvisionPackage implements Buildable {
	
	public EnvisionLangPackage(String nameIn) {
		super(nameIn);
		
		build();
	}
	
	protected void build() {
		packageScope.clear();
		
		buildFunctions();
		buildFields();
		buildClasses();
		buildPackages();
	}
	
	protected void define(EnvisionObject object) {
		if (object instanceof EnvisionFunction func) packageScope.defineFunction(func);
		else if (object instanceof EnvisionClass clz) packageScope.defineClass(clz);
	}
	
	protected void define(String name, EnvisionObject object) {
		packageScope.define(name, object);
	}
	
	public void defineOn(EnvisionInterpreter interpreter) {
		defineOn(interpreter.internalScope());
	}
	
	public void defineOn(IScope scope) {
		//add each object within the incoming package's scope
		for (var obj : packageScope.values.entrySet()) {
			String import_val_name = obj.getKey();
			ScopeEntry import_entry = obj.getValue();
			
			scope.defineImportVal(import_val_name, import_entry);
		}
		scope.defineImportVal(packageName, this);
	}
	
}
