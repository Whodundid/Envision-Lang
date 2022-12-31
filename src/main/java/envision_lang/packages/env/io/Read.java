package envision_lang.packages.env.io;

import java.util.Scanner;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.StaticTypes;
import eutil.datatypes.util.JavaDatatype;

public class Read extends EnvisionFunction {
	
	public Read() {
		super(StaticTypes.STRING_TYPE, "read");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var l = EnvisionStringFormatter.formatPrint(interpreter, args, true);
		interpreter.envision().getConsoleHandler().println(l);
		
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		reader.reset();
		String input = reader.next();
		
		Object rVal = input;
		
		switch (JavaDatatype.getStringDataType(input)) {
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
