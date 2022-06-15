package envision.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.StaticTypes;

public class Println extends EnvisionFunction {
	
	public Println() {
		super(StaticTypes.VOID_TYPE, "println");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args);
		interpreter.envision().getConsoleHandler().println(l);
	}
	
}
