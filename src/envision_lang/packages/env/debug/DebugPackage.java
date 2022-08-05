package envision_lang.packages.env.debug;

import envision_lang.packages.EnvisionLangPackage;

public class DebugPackage extends EnvisionLangPackage {
	
	public DebugPackage() {
		super("debug");
	}

	@Override
	public void buildFunctions() {
		packageScope.defineFunction(new DebugScope());
		packageScope.defineFunction(new DebugInfo());
		packageScope.defineFunction(new DebugParsed());
	}
	
}
