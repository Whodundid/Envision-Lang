package envision.lang.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.packages.EnvisionLangPackage;

public class IOPackage extends EnvisionLangPackage {
	
	public IOPackage() {
		super("io");
	}

	@Override
	public void buildMethods() {
		add(new Read());
		add(new Print());
		add(new Println());
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
