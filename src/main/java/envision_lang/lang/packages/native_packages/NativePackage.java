package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.Buildable;
import envision_lang.lang.packages.EnvisionLangPackage;

/**
 * A special type of package that is natively imported at program
 * start.
 */
abstract sealed class NativePackage extends EnvisionLangPackage implements Buildable
	permits DebugPackage, EnvPackage, FilePackage, ImagePackage, IOPackage, MathPackage
{
	
	protected NativePackage(String nameIn) {
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
	
}
