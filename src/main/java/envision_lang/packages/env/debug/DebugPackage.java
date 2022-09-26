package envision_lang.packages.env.debug;

import envision_lang.packages.EnvisionLangPackage;

public class DebugPackage extends EnvisionLangPackage {
	
	public DebugPackage() {
		super("debug");
	}

	@Override
	public void buildFunctions() {
		define(new DebugScope());
		define(new DebugInfo());
		define(new DebugParsed());
	}
	
}
