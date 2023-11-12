package envision_lang.lang.packages.native_packages.io;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;

public class Println extends EnvisionFunction {
	
	public Println() {
		super(VOID, "println");
		addOverload(VOID, VAR_A);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args, true);
		interpreter.printToConsoleReceiver(l, true);
	}
	
}
