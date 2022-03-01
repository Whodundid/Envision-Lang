package envision.bytecode.translator;

import envision.EnvisionCodeFile;
import envision.lang.EnvisionObject;
import envision.parser.statements.Statement;
import eutil.datatypes.EArrayList;

/**
 * Converts valid parsed Envision statements into compiled
 * bytecode equivalents.
 * 
 * @author Hunter
 */
public class BytecodeTranslator {
	
	private final EnvisionCodeFile codeFile;
	private final EArrayList<Statement> statements;
	
	private EArrayList<EnvisionObject> localFields = new EArrayList();
	
	public BytecodeTranslator(EnvisionCodeFile codeFileIn) {
		codeFile = codeFileIn;
		statements = codeFile.getStatements();
	}
	
	public void translate() {
		if (codeFile.isParsed()) {
			//Scope scope = codeFile.getInterpreter().scope();
			
			//EArrayList<EnvisionObject> fields = scope.getFields();
			//EArrayList<EnvisionObject> methods = scope.getMethods();
			//EArrayList<EnvisionObject> classes = scope.getClasses();
			
			//System.out.println(fields);
			//System.out.println(methods);
			//System.out.println(classes);
			
			//for (Statement s : statements) {
				
			//}
		}
	}
	
	
	
}
