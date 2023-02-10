package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.base.Millis;
import envision_lang.lang.packages.native_packages.base.Nanos;
import envision_lang.lang.packages.native_packages.base.Sleep;
import envision_lang.lang.packages.native_packages.base.SupportsOP;

/**
 * The basic bundled Envision package encompassing many
 * various specific built-in library functions and values.
 */
public final class EnvPackage extends NativePackage {
	
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
		define(new Millis());
		define(new Nanos());
		define(new Sleep());
		define(new SupportsOP());
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