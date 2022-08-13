package envision_lang.interpreter.statements;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.WorkingDirectory;
import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.SelfImportError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.StaticTypes;
import envision_lang.packages.env.EnvPackage;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.tokenizer.Token;
import eutil.EUtil;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_Import extends StatementExecutor<Stmt_Import> {

	public IS_Import(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Import s) {
		Expr_Import impE = s.imp;
		Token asName = s.asName;
		String path = impE.path;
		String obj = impE.object;
		String as = (asName != null) ? asName.lexeme : null;
		
		//determine logical file path name
		path = (path == null) ? obj : path;
		//the def_name will be: 'as' if as is not null, OR will be 'obj' is obj is not null
		//ORRR will simply be the 'path' given in the import expression.
		String def_name = (as != null) ? as : (obj != null) ? obj : path;
		//the object being imported
		EnvisionObject def_obj = (obj != null) ? scope().get(obj) : null;
		//import object type == the type of the object being imported
		IDatatype def_type = (def_obj != null) ? def_obj.getDatatype() : null;
		
		//check for self importing -- causes infinite recursion
		if (interpreter.fileName.equals(path)) throw new SelfImportError(interpreter.codeFile());
		
		//I am not sure what this is necessarily doing atm.
		//EnvisionLangPackage langPack = EnvisionDefaultPackages.getPackage(path);
		if (EnvPackage.packageName.equals(path)) {
			//new EnvPackage().defineOn(scope());
			//langPack.defineOn(scope());
		}
		else {
			WorkingDirectory dir = workingDir();
			EnvisionCodeFile imp = dir.getFile(path);
			
			try {
				imp.load(envision(), dir);
				imp.execute();
				EnvisionInterpreter impInterpreter = imp.getInterpreter();
				
				//Importing a value merely grants local visibility from one file
				//to another. This is simply stating that any object being imported
				//still has complete visibility to its originating file. For
				//instance, if a file has multiple private values defined within it and
				//only a specific (public) value is being imported from it,
				//the specific imported object can still intrinsicly access
				//all of the defined private fields/values from its originating file.
				
				//If the path is the same as the object being imported,
				//import the whole file.
				if (EUtil.isEqual(path, obj)) {
					//define the top level object
					scope().define(def_name, imp);
					//define scope level members
					for (var b : impInterpreter.scope().values.entrySet()) {
						var box = b.getValue();
						var n = b.getKey();
						var t = box.getA();
						var o = box.getB();
						System.out.println("IMPORTING: " + n + " : " + o.isPublic());
						if (o.isPublic()) {
							scope().define(n, t, o);
						}
					}
					//can't fully implement at the moment because of the way that
					//importing currently creates separate interpreters each with their
					//own respective base lang packages.
					//Not to mention this could lead to a world of potential recursive
					//definitions. IE: 2 files importing each other.
					//scope().defineImportVal(def_name, def_type, def_obj);
				}
				//Otherwise, import a specific object from the given file.
				else if (obj != null) {
					scope().defineImportVal(def_name, def_type, def_obj);
				}
				else {
					scope().defineImportVal(def_name, StaticTypes.CODE_FILE, imp);
				}
				
				// don't do this!
				/*
				//directly import public members into the top level scope
				for (StorageBox<String, EnvisionObject> b : impInterpreter.scope().values.values()) {
					EnvisionObject o = b.getB();
					if (o.isPublic()) {
						scope().defineImportVal(o.getName(), o);
					}
				}
				*/
			}
			catch (Exception e) {
				throw new EnvisionLangError(e);
			}
		}
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Import s) {
		new IS_Import(in).run(s);
	}
	
}