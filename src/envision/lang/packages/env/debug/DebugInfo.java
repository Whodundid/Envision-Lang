package envision.lang.packages.env.debug;

import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class DebugInfo extends EnvisionFunction {
		
	public DebugInfo() {
		super(Primitives.VOID, "info");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { throw new ArgLengthError(this, 0, 1); }
		
		Object obj = args[0];
		if (!(obj instanceof EnvisionObject)) { throw new InvalidArgumentError(obj, this); }
		
		EnvisionObject o = (EnvisionObject) obj;
		String tab1 = "   ";
		String tab2 = tab1 + tab1;
		
		String out = "\n";
		out += "----------------------------------------------------------------\n";
		out += " DEBUG INFO: '" + o.getName() + "'\n";
		out += "----------------------------------------------------------------\n";
		out += tab1 + "General:\n";
		out += tab2 + "Internal Type: " + o.getDatatype() + "\n";
		out += tab2 + "Hex Hash: 0x" + o.getHexHash() + "\n";
		out += tab2 + "Modifiers: " + o.getVisibility();
		if (o.isStrong()) { out += ", strong"; }
		if (o.isStatic()) { out += ", static"; }
		if (o.isFinal()) { out += ", final"; }
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
