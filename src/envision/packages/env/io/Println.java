package envision.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Println extends EnvisionFunction {
	
	public Println() {
		super(Primitives.VOID, "println");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		System.out.println(EnvisionStringFormatter.formatPrint(interpreter, args));
	}
	
}
