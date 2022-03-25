package envision.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.packages.EnvisionLangPackage;

public class IOPackage extends EnvisionLangPackage {
	
	public IOPackage() {
		super("io");
	}

	@Override
	public void buildMethods() {
		packageScope.defineFunction(new Read());
		packageScope.defineFunction(new Print());
		packageScope.defineFunction(new Println());
	}

	@Override
	public void buildFields() {
	}

	@Override
	public void buildClasses(EnvisionInterpreter interpreter) {
		
	}
	
	@Override
	public void buildPackages(EnvisionInterpreter interpreter) {
		
	}
	
}
