package envision.lang.packages.env.debug;

import envision.EnvisionCodeFile;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class DebugScope extends EnvisionMethod {
	
	public DebugScope() {
		super(EnvisionDataType.VOID, "scope");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
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
			Object o = args[0];
			if (o instanceof EnvisionClass) {
				System.out.println("\nSCOPE DEBUG: Cannot show the scope of classes, pass a class instance instead!");
			}
			if (o instanceof ClassInstance) {
				ClassInstance inst = (ClassInstance) o;
				String header = "\n";
				header += "------------------------------------------------------------";
				header += "\nSCOPE DEBUG: (" + inst.getName() + " : " + o + ")\n" + ((ClassInstance) o).getScope() + "\n" + ((ClassInstance) o).getScope().getParentScope();
				header += "\n------------------------------------------------------------";
				System.out.println(header);
			}
			if (o instanceof EnvisionCodeFile) {
				EnvisionCodeFile inst = (EnvisionCodeFile) o;
				String header = "\n";
				header += "------------------------------------------------------------";
				header += "\nSCOPE DEBUG: (" + inst.getName() + " : " + o + ")\n" + ((EnvisionCodeFile) o).getInterpreter().scope() + "\n";
				header += "------------------------------------------------------------";
				System.out.println(header);
			}
		}
	}
	
}
