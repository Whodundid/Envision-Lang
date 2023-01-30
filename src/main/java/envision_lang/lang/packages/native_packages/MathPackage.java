package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.math.Ceil;
import envision_lang.lang.packages.native_packages.math.Floor;
import envision_lang.lang.packages.native_packages.math.Log;
import envision_lang.lang.packages.native_packages.math.Pow;
import envision_lang.lang.packages.native_packages.math.RandDouble;
import envision_lang.lang.packages.native_packages.math.RandInt;
import envision_lang.lang.packages.native_packages.math.RandName;
import envision_lang.lang.packages.native_packages.math.RandStr;
import envision_lang.lang.packages.native_packages.math.Sqrt;

public final class MathPackage extends NativePackage {
	
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