package envision.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.StaticTypes;

public class Print extends EnvisionFunction {
	
	public Print() {
		super(StaticTypes.VOID_TYPE, "print");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args);
		interpreter.envision().getConsoleHandler().print(l);
	}
	
}
