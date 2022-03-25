package envision.packages.env.debug;

import envision.interpreter.EnvisionInterpreter;
import envision.packages.EnvisionLangPackage;

public class DebugPackage extends EnvisionLangPackage {
	
	public DebugPackage() {
		super("debug");
	}

	@Override
	public void buildMethods() {
		packageScope.defineFunction(new DebugScope());
		packageScope.defineFunction(new DebugInfo());
		packageScope.defineFunction(new DebugParsed());
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
