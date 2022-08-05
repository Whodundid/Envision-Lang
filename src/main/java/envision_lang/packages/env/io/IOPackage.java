package envision_lang.packages.env.io;

import envision_lang.packages.EnvisionLangPackage;

public class IOPackage extends EnvisionLangPackage {
	
	public IOPackage() {
		super("io");
	}

	@Override
	public void buildFunctions() {
		packageScope.defineFunction(new Read());
		packageScope.defineFunction(new Print());
		packageScope.defineFunction(new Println());
	}
	
}
