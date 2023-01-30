package envision_lang.lang.packages.native_packages.debug;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.packages.EnvisionLangPackage;
import eutil.strings.EStringBuilder;

public class DebugScopeFull extends EnvisionFunction {
	
	public DebugScopeFull() {
		super(EnvisionStaticTypes.VOID_TYPE, "scopeFull");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 0) {
			IScope s = interpreter.scope();
			//IScope p = s.getParent();
			var out = new EStringBuilder("\n");
			out.a("------------------------------------------------------------");
			out.a("\nFULL SCOPE DEBUG (Local)\n", IScope.printFullStack(s));
			out.a("\n------------------------------------------------------------");
			System.out.println(out.toString());
		}
		else {
			EnvisionObject o = args[0];
			if (o instanceof EnvisionClass inst) {
				IDatatype type = inst.getDatatype();
				IScope inst_scope = inst.getClassScope();
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nFULL CLASS SCOPE: (", type, " : ", o, ")\n", IScope.printFullStack(inst_scope));
				out.a("\n------------------------------------------------------------");
				System.out.println(out.toString());
			}
			else if (o instanceof ClassInstance inst) {
				IDatatype type = inst.getDatatype();
				IScope inst_scope = inst.getScope();
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nFULL CLASS INSTANCE SCOPE: (", type, " : ", o, ")\n", IScope.printFullStack(inst_scope));
				out.a("\n------------------------------------------------------------");
				System.out.println(out.toString());
			}
			else if (o instanceof EnvisionCodeFile code) {
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nFULL CODE FILE SCOPE: (", code, " : ", o, ")\n", IScope.printFullStack(code.scope()));
				out.a("\n------------------------------------------------------------");
				System.out.println(out.toString());
			}
			else if (o instanceof EnvisionLangPackage pkg) {
				var out = new EStringBuilder("\n");
				out.a("------------------------------------------------------------");
				out.a("\nFULL PACKAGE SCOPE: (", pkg.getPackageName(), " : ", o, ")\n", IScope.printFullStack(pkg.getScope()));
				out.a("\n------------------------------------------------------------");
				System.out.println(out.toString());
			}
			else {
				var out = new EStringBuilder("\n");
				out.a("\nSCOPE DEBUG ERROR! -- Can't show the scope of a '", o, "'!");
				System.out.println(out.toString());
			}
		}
	}
	
}
