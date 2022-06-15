package envision.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.natives.IDatatype;

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
	
	public void defineOn(EnvisionInterpreter interpreter) {
		//add each object within the incoming package's scope
		Scope incomming_scope = interpreter.internalScope();
		for (var obj : packageScope.values.entrySet()) {
			String import_val_name = obj.getKey();
			IDatatype import_val_type = obj.getValue().getA();
			EnvisionObject import_val_obj = obj.getValue().getB();
			
			incomming_scope.define(import_val_name, import_val_type, import_val_obj);
		}
	}
	
}
