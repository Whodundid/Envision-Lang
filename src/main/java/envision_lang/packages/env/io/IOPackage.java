package envision_lang.packages.env.io;

import envision_lang.packages.EnvisionLangPackage;

public class IOPackage extends EnvisionLangPackage {
	
	public IOPackage() {
		super("io");
	}

	@Override
	public void buildFunctions() {
		define(new Read());
		define(new Print());
		define(new Println());
		define(new Printf());
	}
	
}
