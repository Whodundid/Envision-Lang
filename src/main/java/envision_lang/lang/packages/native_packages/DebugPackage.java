package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.debug.DebugInfo;
import envision_lang.lang.packages.native_packages.debug.DebugParsed;
import envision_lang.lang.packages.native_packages.debug.DebugScope;
import envision_lang.lang.packages.native_packages.debug.DebugScopeFull;

public final class DebugPackage extends NativePackage {
	
	public DebugPackage() {
		super("debug");
	}

	@Override
	public void buildFunctions() {
		define(new DebugScope());
		define(new DebugScopeFull());
		define(new DebugInfo());
		define(new DebugParsed());
	}
	
}
