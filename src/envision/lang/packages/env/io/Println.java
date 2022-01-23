package envision.lang.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.EnvisionStringFormatter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class Println extends EnvisionMethod {
	
	public Println() {
		super(EnvisionDataType.VOID, "println");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		System.out.println(EnvisionStringFormatter.formatPrint(interpreter, args));
	}
	
}
