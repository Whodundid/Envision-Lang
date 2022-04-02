package envision.packages.env;

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
	public static final EnvPackage ENV_PACKAGE = new EnvPackage();
	
	//--------------
	// Constructors
	//--------------
	
	private EnvPackage() {
		super(packageName);
	}

	@Override
	public void buildFunctions() {
		packageScope.defineFunction(new Millis());
		packageScope.defineFunction(new Nanos());
		packageScope.defineFunction(new Sleep());
		packageScope.defineFunction(new SupportsOP());
	}
	
	@Override
	public void buildPackages() {
		DebugPackage debug = new DebugPackage();
		IOPackage io = new IOPackage();
		MathPackage math = new MathPackage();
		FilePackage file = new FilePackage();
		//NetPackage net = new NetPackage();
		
		addPackage(debug);
		addPackage(io);
		addPackage(math);
		addPackage(file);
		//addPackage(net);
	}
	
}