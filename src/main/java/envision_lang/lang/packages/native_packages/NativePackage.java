package envision_lang.lang.packages.native_packages;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.packages.Buildable;
import envision_lang.lang.packages.EnvisionLangPackage;

/**
 * A special type of package that is natively imported at program
 * start.
 */
abstract sealed class NativePackage extends EnvisionLangPackage implements Buildable
	permits DebugPackage, EnvPackage, FilePackage, ImagePackage, IOPackage, MathPackage
{
	
	public NativePackage(String nameIn) {
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
	
}
