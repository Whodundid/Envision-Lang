package envision_lang.lang.packages.env.math;

import envision_lang.lang.packages.EnvisionLangPackage;

public class MathPackage extends EnvisionLangPackage {
	
	//public static final MathPackage base = new MathPackage();
	
	public MathPackage() {
		super("math");
	}

	@Override
	public void buildFunctions() {
		define(new Sqrt());
		define(new Pow());
		define(new Log());
		define(new Ceil());
		define(new Floor());
		define(new RandInt());
		define(new RandDouble());
		define(new RandStr());
		define(new RandName());
	}
	
}