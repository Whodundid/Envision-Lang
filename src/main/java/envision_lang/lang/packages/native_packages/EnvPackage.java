package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.base.AddAttribute;
import envision_lang.lang.packages.native_packages.base.HasAttribute;
import envision_lang.lang.packages.native_packages.base.Millis;
import envision_lang.lang.packages.native_packages.base.Nanos;
import envision_lang.lang.packages.native_packages.base.RemoveAttribute;
import envision_lang.lang.packages.native_packages.base.Sleep;
import envision_lang.lang.packages.native_packages.base.SupportsOP;

/**
 * The basic bundled Envision package encompassing many
 * various specific built-in library functions and values.
 */
public final class EnvPackage extends NativePackage {
	
	public static final String PACKAGE_NAME = "env";
	public static final EnvPackage ENV_PACKAGE = new EnvPackage();
	
	//==============
    // Constructors
    //==============
	
	private EnvPackage() {
		super(PACKAGE_NAME);
	}

	@Override
	public void buildFunctions() {
		define(new Millis());
		define(new Nanos());
		define(new Sleep());
		define(new SupportsOP());
		define(new AddAttribute());
		define(new HasAttribute());
		define(new RemoveAttribute());
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