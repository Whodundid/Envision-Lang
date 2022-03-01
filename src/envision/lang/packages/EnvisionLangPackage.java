package envision.lang.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;

/** A special type of package that is natively imported at program start. */
public abstract class EnvisionLangPackage extends EnvisionObject {
	
	EArrayList<EnvisionObject> objects = new EArrayList();
	
	public EnvisionLangPackage(String nameIn) {
		super(new EnvisionDatatype(Primitives.PACKAGE), nameIn);
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
	
	protected EnvisionLangPackage addPackage(EnvisionInterpreter interpreter, EnvisionLangPackage packageIn) {
		objects.add(packageIn);
		packageIn.build(interpreter);
		objects.addAll(packageIn.objects);
		return this;
	}
	
	protected EnvisionLangPackage addAll(EArrayList<EnvisionObject> in) {
		objects.addAll(in);
		return this;
	}
	
	protected EnvisionLangPackage add(EnvisionObject... in) {
		for (EnvisionObject o : in) { objects.addIfNotNull(o); }
		return this;
	}
	
	public void defineOn(EnvisionInterpreter interpreter) {
		build(interpreter);
		var scope = interpreter.scope();
		objects.forEach(scope::defineImportVal);
	}
	
}
