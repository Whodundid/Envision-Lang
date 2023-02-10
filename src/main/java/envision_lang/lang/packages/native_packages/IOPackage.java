package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.io.Print;
import envision_lang.lang.packages.native_packages.io.Printf;
import envision_lang.lang.packages.native_packages.io.Println;
import envision_lang.lang.packages.native_packages.io.Read;

public final class IOPackage extends NativePackage {
	
	public IOPackage() {
		super("io");
	}

	@Override
	public void buildFunctions() {
		define(new Read());
		define(new Print());
		define(new Println());
		define(new Printf());
	}
	
}
