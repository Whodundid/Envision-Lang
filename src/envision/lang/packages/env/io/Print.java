package envision.lang.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Print extends EnvisionFunction {
	
	public Print() {
		super(Primitives.VOID, "print");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		System.out.print(EnvisionStringFormatter.formatPrint(interpreter, args));
	}
	
}
