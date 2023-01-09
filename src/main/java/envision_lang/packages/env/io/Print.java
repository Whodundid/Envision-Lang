package envision_lang.packages.env.io;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.StaticTypes;

public class Print extends EnvisionFunction {
	
	public Print() {
		super(StaticTypes.VOID_TYPE, "print");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args, true);
		System.out.print(l);
	}
	
}
