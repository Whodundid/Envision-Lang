package envision.lang.packages.env.debug;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.packages.EnvisionLangPackage;

public class DebugPackage extends EnvisionLangPackage {
	
	public DebugPackage() {
		super("debug");
	}

	@Override
	public void buildMethods() {
		add(new DebugScope());
		add(new DebugInfo());
		add(new DebugParsed());
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
