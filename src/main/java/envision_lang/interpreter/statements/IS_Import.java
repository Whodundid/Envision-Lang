package envision_lang.interpreter.statements;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.WorkingDirectory;
import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.SelfImportError;
import envision_lang.exceptions.errors.UndefinedValueError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.tokenizer.Token;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_Import extends StatementExecutor<Stmt_Import> {

	public IS_Import(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Import s) {
		new IS_Import(in).run(s);
	}

	@Override
	public void run(Stmt_Import s) {
		Expr_Import impE = s.imp;
		Token asName = s.asName;
		String path = impE.path.lexeme;
		String obj = impE.object.lexeme;
		String as = (asName != null) ? asName.lexeme : null;
		boolean all = s.importAll;
		
		//the def_name will be: 'as' if as is not null, OR will be 'obj' is obj is not null
		//ORRR will simply be the 'path' given in the import expression.
		String def_name = (as != null) ? as : (obj != null) ? obj : path;
		
		//check for self importing -- causes infinite recursion
		if (interpreter.fileName.equals(path)) throw new SelfImportError(interpreter.codeFile());
		
		WorkingDirectory dir = workingDir();
		EnvisionCodeFile imp = dir.getFile(path);
		
		try {
			if (!imp.isLoaded()) {
				imp.load(envision(), dir);
				imp.execute();
			}
			
			EnvisionInterpreter impInterpreter = imp.getInterpreter();
			
			//import literally everything (including the code file itself)
			if (all) {
				//define the code file as an object within this scope
				scope().defineImportVal(def_name, StaticTypes.CODE_FILE, imp);
				//define import object's scope level members
				for (var b : impInterpreter.scope().values().entrySet()) {
					var box = b.getValue();
					var n = b.getKey();
					var t = box.getA();
					var o = box.getB();
					//System.out.println("IMPORTING: " + n + " : " + o.isPublic());
					//if (o.isPublic()) {
						scope().define(n, t, o);
					//}
				}
			}
			//if there's no object to import, simply import the code file as an object inside of this interpreter
			else if (obj == null) {
				//System.out.println("DEFINING CODE FILE");
				scope().defineImportVal(def_name, StaticTypes.CODE_FILE, imp);
			}
			//otherwise, import the specific object from the given file (if it exists)
			else {
				var impObject = impInterpreter.scope().get(obj);
				
				if (impObject == null) {
					throw new UndefinedValueError("The specified import target '" +
												  obj + "' does not exist on the file '" +
												  imp.getFileName() + "'!");
				}
				
				scope().defineImportVal(def_name, impObject);
			}
		}
		catch (Exception e) {
			throw new EnvisionLangError(e);
		}
	}
	
}

// Old code for reference

/*
@Override
public void run(Stmt_Import s) {
	Expr_Import impE = s.imp;
	Token asName = s.asName;
	String path = impE.path.lexeme;
	String obj = impE.object.lexeme;
	String as = (asName != null) ? asName.lexeme : null;
	
	System.out.println(path + " : " + obj);
	
	//determine logical file path name
	//path = (path == null) ? obj : path;
	//the def_name will be: 'as' if as is not null, OR will be 'obj' is obj is not null
	//ORRR will simply be the 'path' given in the import expression.
	String def_name = (as != null) ? as : (obj != null) ? obj : path;
	//the object being imported
	//EnvisionObject def_obj = (obj != null) ? scope().get(obj) : null;
	//import object type == the type of the object being imported
	//IDatatype def_type = (def_obj != null) ? def_obj.getDatatype() : null;
	
	//System.out.println(def_name + " : " + def_obj + " : " + def_type);
	
	//check for self importing -- causes infinite recursion
	if (interpreter.fileName.equals(path)) throw new SelfImportError(interpreter.codeFile());
	
	//I am not sure what this is necessarily doing atm.
	//EnvisionLangPackage langPack = EnvisionDefaultPackages.getPackage(path);
	//if (EnvPackage.packageName.equals(path)) {
		//new EnvPackage().defineOn(scope());
		//langPack.defineOn(scope());
	//}
	//else {
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
			//the specific imported object can still intrinsically access
			//all of the defined private fields/values from its originating file.
			
			//If the path is the same as the object being imported,
			//import the whole file.
//			if (EUtil.isEqual(path, obj)) {
//				//define the top level object
//				scope().define(def_name, imp);
//				//define scope level members
//				for (var b : impInterpreter.scope().values().entrySet()) {
//					var box = b.getValue();
//					var n = b.getKey();
//					var t = box.getA();
//					var o = box.getB();
//					System.out.println("IMPORTING: " + n + " : " + o.isPublic());
//					//if (o.isPublic()) {
//						scope().define(n, t, o);
//					//}
//				}
//				//can't fully implement at the moment because of the way that
//				//importing currently creates separate interpreters each with their
//				//own respective base lang packages.
//				//Not to mention this could lead to a world of potential recursive
//				//definitions. IE: 2 files importing each other.
//				//scope().defineImportVal(def_name, def_type, def_obj);
//			}
			//Otherwise, import a specific object from the given file.
			//else if (obj != null) {
				//scope().defineImportVal(def_name, def_type, def_obj);
			//}
//			else {
//				scope().defineImportVal(def_name, StaticTypes.CODE_FILE, imp);
//			}
			
			// don't do this!
			
			//directly import public members into the top level scope
//			for (StorageBox<String, EnvisionObject> b : impInterpreter.scope().values.values()) {
//				EnvisionObject o = b.getB();
//				if (o.isPublic()) {
//					scope().defineImportVal(o.getName(), o);
//				}
//			}
			
			
			//if there's no object to import, simply import the code file as an object inside of this interpreter
			if (obj == null) {
				scope().define(def_name, StaticTypes.CODE_FILE, imp);
			}
			//otherwise, import the specific object from the given file (if it exists)
			else {
				var impObject = impInterpreter.scope().get(obj);
				if (impObject == null) {
					throw new UndefinedValueError("The specified import target '" +
												  obj + "' does not exist on the file '" +
												  imp.getFileName() + "'!");
				}
			}
		}
		catch (Exception e) {
			throw new EnvisionLangError(e);
		}
	//}
}
*/
