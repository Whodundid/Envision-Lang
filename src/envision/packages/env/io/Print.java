package envision.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Print extends EnvisionFunction {
	
	public Print() {
		super(Primitives.VOID, "print");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		System.out.print(EnvisionStringFormatter.formatPrint(interpreter, args));
	}
	
}
