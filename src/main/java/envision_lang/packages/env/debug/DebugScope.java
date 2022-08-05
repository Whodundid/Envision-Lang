package envision_lang.packages.env.debug;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.StaticTypes;

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
