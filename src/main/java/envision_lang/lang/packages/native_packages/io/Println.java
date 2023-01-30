package envision_lang.lang.packages.native_packages.io;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class Println extends EnvisionFunction {
	
	public Println() {
		super(EnvisionStaticTypes.VOID_TYPE, "println");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args, true);
		System.out.println(l);
	}
	
}
