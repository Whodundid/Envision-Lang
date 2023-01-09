package envision_lang.packages.env.io;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.StaticTypes;

public class Printf extends EnvisionFunction {
	
	public Printf() {
		super(StaticTypes.VOID_TYPE, "printf");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args, true);
		System.out.print(l);
	}
	
}
