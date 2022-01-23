package envision.lang.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class Print extends EnvisionMethod {
	
	public Print() {
		super(EnvisionDataType.VOID, "print");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		System.out.print(EnvisionStringFormatter.formatPrint(interpreter, args));
	}
	
}
