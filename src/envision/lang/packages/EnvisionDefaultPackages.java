package envision.lang.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.packages.env.debug.DebugPackage;
import envision.lang.packages.env.io.IOPackage;
import envision.lang.packages.env.math.MathPackage;
import eutil.datatypes.EArrayList;

public class EnvisionDefaultPackages {
	
	private static final EArrayList<EnvisionLangPackage> packages = new EArrayList();
	
	static {
		packages.add(new DebugPackage());
		packages.add(new IOPackage());
		packages.add(new MathPackage());
	}
	
	public static EnvisionLangPackage getPackage(String name) {
		for (EnvisionLangPackage p : packages) {
			if (p.getName().equals(name)) { return p; }
		}
		return null;
	}
	
	public static void defineAll(EnvisionInterpreter interpreter) {
		for (EnvisionLangPackage p : packages) {
			p.defineOn(interpreter);
		}
	}
	
}
