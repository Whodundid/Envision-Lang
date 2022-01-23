package envision.lang.packages.env;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.packages.EnvisionLangPackage;
import envision.lang.packages.env.base.Millis;
import envision.lang.packages.env.base.Nanos;
import envision.lang.packages.env.base.Sleep;
import envision.lang.packages.env.base.SupportsOP;
import envision.lang.packages.env.debug.DebugPackage;
import envision.lang.packages.env.file.FilePackage;
import envision.lang.packages.env.io.IOPackage;
import envision.lang.packages.env.math.MathPackage;

/**
 * The basic bundled Envision package emcompassing many
 * various specific built-in library functions and values.
 */
public class EnvPackage extends EnvisionLangPackage {
	
	public static final String packageName = "env";
	
	public EnvPackage() {
		super(packageName);
	}

	@Override
	public void buildMethods() {
		add(new Millis());
		add(new Nanos());
		add(new Sleep());
		add(new SupportsOP());
	}

	@Override
	public void buildFields() {
	}

	@Override
	public void buildClasses(EnvisionInterpreter interpreter) {
		
	}
	
	@Override
	public void buildPackages(EnvisionInterpreter interpreter) {
		DebugPackage debug = new DebugPackage();
		IOPackage io = new IOPackage();
		MathPackage math = new MathPackage();
		FilePackage file = new FilePackage();
		//NetPackage net = new NetPackage();
		
		addPackage(interpreter, debug);
		addPackage(interpreter, io);
		addPackage(interpreter, math);
		addPackage(interpreter, file);
		//addPackage(net);
		
		
	}
	
	//public static String packageName() { return packageName; }
	
}