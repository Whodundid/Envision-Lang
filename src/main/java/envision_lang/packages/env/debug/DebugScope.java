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
import envision_lang.packages.EnvisionLangPackage;
import eutil.strings.EStringBuilder;

public class DebugScope extends EnvisionFunction {
	
	public DebugScope() {
		super(StaticTypes.VOID_TYPE, "scope");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 0) {
			Scope s = interpreter.scope();
			Scope p = s.getParentScope();
			var out = new EStringBuilder("\n");
			out.a("------------------------------------------------------------");
			out.a("\nSCOPE DEBUG (Local)\n", s, ((p != null) ? "\n" + p : ""));
			out.a("\n------------------------------------------------------------");
			interpreter.envision().getConsoleHandler().println(out.toString());
		}
		else {
			EnvisionObject o = args[0];
			if (o instanceof EnvisionClass inst) {
				IDatatype type = inst.getDatatype();
				Scope inst_scope = inst.getClassScope();
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nCLASS SCOPE: (", type, " : ", o, ")\n", inst_scope, "\n", inst_scope.getParentScope());
				out.a("\n------------------------------------------------------------");
				interpreter.envision().getConsoleHandler().println(out.toString());
			}
			else if (o instanceof ClassInstance inst) {
				IDatatype type = inst.getDatatype();
				Scope inst_scope = inst.getScope();
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nCLASS INSTANCE SCOPE: (", type, " : ", o, ")\n", inst_scope, "\n", inst_scope.getParentScope());
				out.a("\n------------------------------------------------------------");
				interpreter.envision().getConsoleHandler().println(out.toString());
			}
			else if (o instanceof EnvisionCodeFile code) {
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nCODE FILE SCOPE: (", code, " : ", o, ")\n", code.getInterpreter().scope(), "\n");
				out.a("------------------------------------------------------------");
				interpreter.envision().getConsoleHandler().println(out.toString());
			}
			else if (o instanceof EnvisionLangPackage pkg) {
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nPACKAGE SCOPE: (", pkg.getPackageName(), " : ", o, ")\n", pkg.getScope(), "\n");
				out.a("------------------------------------------------------------");
				interpreter.envision().getConsoleHandler().println(out.toString());
			}
			else {
				var out = new EStringBuilder("\n");
				out.a("\nSCOPE DEBUG ERROR! -- Can't show the scope of a '", o, "'!");
				interpreter.envision().getConsoleHandler().println(out.toString());
			}
		}
	}
	
}
