package envision.lang.packages.env.io;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.datatypes.util.DataTypeUtil;

import java.util.Scanner;

public class Read extends EnvisionFunction {
	
	//I might need to use Strings to handle internal object types..
	
	public Read() {
		super(Primitives.STRING, "read");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		Scanner reader = new Scanner(System.in);
		String input = reader.next();
		reader.close();
		
		Object rVal = input;
		
		switch (DataTypeUtil.getStringDataType(input)) {
		case NULL: rVal = null; break;
		case BOOLEAN: rVal = Boolean.parseBoolean(input); break;
		case CHAR: rVal = input.charAt(0); break;
		case LONG: rVal = Long.parseLong(input); break;
		case DOUBLE: rVal = Double.parseDouble(input); break;
		default: rVal = input;
		}
		
		ret(rVal);
	}
	
}
