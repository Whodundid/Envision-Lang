package envision_lang.interpreter.statements;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.WorkingDirectory;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.SelfImportError;
import envision_lang.lang.exceptions.errors.UndefinedValueError;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.tokenizer.Token;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_Import {

	public static void run(EnvisionInterpreter interpreter, Stmt_Import s) {
		Expr_Import impE = s.imp;
		Token asName = s.asName;
		String path = impE.path.getLexeme();
		String obj = impE.object.getLexeme();
		String as = (asName != null) ? asName.getLexeme() : null;
		boolean all = s.importAll || obj == null;
		
		//the def_name will be: 'as' if as is not null, OR will be 'obj' is obj is not null
		//ORRR will simply be the 'path' given in the import expression.
		String def_name = (as != null) ? as : (obj != null) ? obj : path;
		
		//check for self importing -- causes infinite recursion
		if (interpreter.fileName.equals(path)) throw new SelfImportError(interpreter.codeFile());
		
		WorkingDirectory dir = interpreter.workingDir();
		EnvisionCodeFile imp = dir.getFile(path);
		
		try {
			if (!imp.isLoaded()) {
				imp.load(dir);
				EnvisionInterpreter.interpret(imp, null);
			}
			
			IScope impScope = imp.scope();
			
			//import literally everything (including the code file itself)
			if (all) {
				//define the code file as an object within this scope
				interpreter.scope().defineImportVal(def_name, StaticTypes.CODE_FILE, imp);
				//define import object's scope level members
				for (var b : impScope.values().entrySet()) {
					var n = b.getKey();
					var e = b.getValue();
					//System.out.println("IMPORTING: " + n + " : " + o.isPublic());
					//if (o.isPublic()) {
					interpreter.scope().define(n, e);
					//}
				}
			}
			//if there's no object to import, simply import the code file as an object inside of this interpreter
			else if (obj == null) {
				//System.out.println("DEFINING CODE FILE");
				interpreter.scope().defineImportVal(def_name, StaticTypes.CODE_FILE, imp);
			}
			//otherwise, import the specific object from the given file (if it exists)
			else {
				var impObject = impScope.get(obj);
				
				if (impObject == null) {
					throw new UndefinedValueError("The specified import target '" +
												  obj + "' does not exist on the file '" +
												  imp.getFileName() + "'!");
				}
				
				interpreter.scope().defineImportVal(def_name, impObject);
			}
		}
		catch (Exception e) {
			throw new EnvisionLangError(e);
		}
	}
	
}
