package envision.packages.env;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.util.EnvisionDatatype;
import envision.packages.EnvisionLangPackage;
import envision.packages.env.base.Millis;
import envision.packages.env.base.Nanos;
import envision.packages.env.base.Sleep;
import envision.packages.env.base.SupportsOP;
import envision.packages.env.debug.DebugPackage;
import envision.packages.env.file.FilePackage;
import envision.packages.env.io.IOPackage;
import envision.packages.env.math.MathPackage;

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
		packageScope.defineFunction(new Millis());
		packageScope.defineFunction(new Nanos());
		packageScope.defineFunction(new Sleep());
		packageScope.defineFunction(new SupportsOP());
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
		
		packageScope.define(debug.getPackageName(), EnvisionDatatype.PACKAGE_TYPE, debug);
		packageScope.define(io.getPackageName(), EnvisionDatatype.PACKAGE_TYPE, io);
		packageScope.define(math.getPackageName(), EnvisionDatatype.PACKAGE_TYPE, math);
		packageScope.define(file.getPackageName(), EnvisionDatatype.PACKAGE_TYPE, file);
		
		addPackage(interpreter, debug);
		addPackage(interpreter, io);
		addPackage(interpreter, math);
		addPackage(interpreter, file);
		//addPackage(net);
		
		
	}
	
}