package envision_lang.packages;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;

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
		//add each object within the incoming package's scope
		Scope incoming_scope = interpreter.internalScope();
		for (var obj : packageScope.values.entrySet()) {
			String import_val_name = obj.getKey();
			IDatatype import_val_type = obj.getValue().getA();
			EnvisionObject import_val_obj = obj.getValue().getB();
			
			incoming_scope.define(import_val_name, import_val_type, import_val_obj);
		}
		incoming_scope.define(packageName, this);
	}
	
}
