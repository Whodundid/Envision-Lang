package envision_lang.packages.env.math;

import envision_lang.packages.EnvisionLangPackage;

public class MathPackage extends EnvisionLangPackage {
	
	//public static final MathPackage base = new MathPackage();
	
	public MathPackage() {
		super("math");
	}

	@Override
	public void buildFunctions() {
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
	
}