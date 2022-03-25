package envision.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import eutil.datatypes.EArrayList;

/**
 * A special type of package that is natively imported at program
 * start.
 */
public abstract class EnvisionLangPackage extends EnvisionPackage {
	
	EArrayList<EnvisionObject> objects = new EArrayList();
	
	public EnvisionLangPackage(String nameIn) {
		super(nameIn);
	}
	
	protected void build(EnvisionInterpreter interpreter) {
		buildMethods();
		buildFields();
		buildClasses(interpreter);
		buildPackages(interpreter);
	}
	
	// I really don't like the way this is currently being handled
	// I don't think there should be an interpreter instance being passed around during this point.
	// Rather, I think a package should be able to be structurally assembled and then 'applied'
	// to a specific interpreter instance -- not sure how I want to amend this right now. (7/22/2021)
	
	public abstract void buildMethods();
	public abstract void buildFields();
	public abstract void buildClasses(EnvisionInterpreter interpreter);
	public abstract void buildPackages(EnvisionInterpreter interpreter);
	
	public void defineOn(EnvisionInterpreter interpreter) {
		build(interpreter);
		Scope scope = interpreter.scope();
		for (EnvisionObject obj : objects) {
			scope.defineImportVal(packageName, internalType, obj);
		}
	}
	
}
