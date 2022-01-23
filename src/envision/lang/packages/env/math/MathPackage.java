package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.packages.EnvisionLangPackage;

public class MathPackage extends EnvisionLangPackage {
	
	//public static final MathPackage base = new MathPackage();
	
	public MathPackage() {
		super("math");
	}

	@Override
	public void buildMethods() {
		add(new Sqrt());
		add(new Pow());
		add(new Log());
		add(new Ceil());
		add(new Floor());
		add(new RandInt());
		add(new RandDouble());
		add(new RandStr());
		add(new RandName());
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