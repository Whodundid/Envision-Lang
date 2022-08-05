package envision_lang.packages.env.io;

import java.util.Scanner;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.util.StaticTypes;
import eutil.datatypes.util.DataTypeUtil;

public class Read extends EnvisionFunction {
	
	public Read() {
		super(StaticTypes.STRING_TYPE, "read");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
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
		
		ret(ObjectCreator.wrap(rVal));
	}
	
}
