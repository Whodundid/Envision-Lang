package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.packages.EnvisionLangPackage;

public class MathPackage extends EnvisionLangPackage {
	
	//public static final MathPackage base = new MathPackage();
	
	public MathPackage() {
		super("math");
	}

	@Override
	public void buildMethods() {
		packageScope.defineFunction(new Sqrt());
		packageScope.defineFunction(new Pow());
		packageScope.defineFunction(new Log());
		packageScope.defineFunction(new Ceil());
		packageScope.defineFunction(new Floor());
		packageScope.defineFunction(new RandInt());
		packageScope.defineFunction(new RandDouble());
		packageScope.defineFunction(new RandStr());
		packageScope.defineFunction(new RandName());
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