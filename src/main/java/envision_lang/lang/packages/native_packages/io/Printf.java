package envision_lang.lang.packages.native_packages.io;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class Printf extends EnvisionFunction {
	
	public Printf() {
		super(EnvisionStaticTypes.VOID_TYPE, "printf");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args, true);
		interpreter.printToConsoleReceiver(l, false);
	}
	
}
