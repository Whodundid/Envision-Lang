package envision.packages.env.debug;

import envision._launch.EnvisionCodeFile;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.IDatatype;
import envision.lang.util.StaticTypes;

public class DebugScope extends EnvisionFunction {
	
	public DebugScope() {
		super(StaticTypes.VOID_TYPE, "scope");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 0) {
			Scope s = interpreter.scope();
			Scope p = s.getParentScope();
			String header = "\n";
			header += "------------------------------------------------------------";
			header += "\nSCOPE DEBUG (Local)\n" + s + ((p != null) ? "\n" + p : "");
			header += "\n------------------------------------------------------------";
			System.out.println(header);
		}
		else {
			EnvisionObject o = args[0];
			if (o instanceof EnvisionClass) {
				System.out.println("\nSCOPE DEBUG: Cannot show the scope of classes, pass a class instance instead!");
			}
			if (o instanceof ClassInstance) {
				ClassInstance inst = (ClassInstance) o;
				IDatatype type = inst.getDatatype();
				Scope inst_scope = inst.getScope();
				String header = "\n";
				header += "------------------------------------------------------------";
				header += "\nSCOPE DEBUG: (" + type + " : " + o + ")\n" + inst_scope + "\n" + inst_scope.getParentScope();
				header += "\n------------------------------------------------------------";
				System.out.println(header);
			}
			if (o instanceof EnvisionCodeFile) {
				EnvisionCodeFile inst = (EnvisionCodeFile) o;
				String header = "\n";
				header += "------------------------------------------------------------";
				header += "\nSCOPE DEBUG: (" + inst + " : " + o + ")\n" + ((EnvisionCodeFile) o).getInterpreter().scope() + "\n";
				header += "------------------------------------------------------------";
				System.out.println(header);
			}
		}
	}
	
}
