package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.debug.*;

public final class DebugPackage extends NativePackage {
	
	public DebugPackage() {
		super("debug");
	}

	@Override
	public void buildFunctions() {
	    define(new AssertEquals());
	    define(new AssertFalse());
	    define(new AssertNotEquals());
	    define(new AssertNotNull());
	    define(new AssertNull());
	    define(new AssertTrue());
		define(new DebugScope());
		define(new DebugScopeFull());
		define(new DebugInfo());
		define(new DebugParsed());
		define(new DebugStackFrames());
	}
	
}
