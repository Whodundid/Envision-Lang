package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.exceptions.errors.ArgLengthError;
import envision_lang.lang.exceptions.errors.InvalidArgumentError;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class DebugInfo extends EnvisionFunction {
		
	public DebugInfo() {
		super(EnvisionStaticTypes.VOID_TYPE, "info");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 0) { throw new ArgLengthError(this, 1, 0); }
		
		Object obj = args[0];
		if (!(obj instanceof EnvisionObject)) { throw new InvalidArgumentError(obj, this); }
		
		EnvisionObject o = (EnvisionObject) obj;
		String tab1 = "   ";
		String tab2 = tab1 + tab1;
		
		String out = "\n";
		out += "----------------------------------------------------------------\n";
		out += " DEBUG INFO: '" + o + "'\n";
		out += "----------------------------------------------------------------\n";
		out += tab1 + "General:\n";
		out += tab2 + "Internal Type: " + o.getDatatype() + "\n";
		out += tab2 + "Hex Hash: 0x" + o.getHexHash() + "\n";
		out += tab2 + "Modifiers: ";// + o.getVisibility();
		if (o.isStrong()) out += ", strong";
		if (o.isStatic()) out += ", static";
		if (o.isFinal()) out += ", final";
		out += "\n";
		
		if (o instanceof EnvisionVariable) {
			out += tab1 + "Variable Data:\n";
			out += tab2 + "Value: " + ((EnvisionVariable) o).get() + "\n";
		}
		else if (o instanceof ClassInstance) {
			out += tab1 + "Class Instance Data:\n";
			out += tab2 + "Class: " + ((ClassInstance) o).getEClass() + "\n";
		}
		
		out += "----------------------------------------------------------------\n";
		
		System.out.println(out);
	}
	
}
