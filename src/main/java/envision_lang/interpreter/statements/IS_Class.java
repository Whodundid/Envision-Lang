package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creation_util.FunctionCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.classes.ClassConstruct;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.error_types.classErrors.InvalidClassStatement;
import envision_lang.lang.natives.EnvisionDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.UserDefinedTypeManager;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Class;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.util.ParserDeclaration;
import eutil.datatypes.util.EList;

public class IS_Class extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_Class statement) {
		ParserDeclaration dec = statement.getDeclaration();
		UserDefinedTypeManager typeMan = interpreter.getTypeManager();
		String className = statement.name.getLexeme();
		EnvisionDatatype userType = typeMan.getOrCreateDatatypeFor(className);
		
		//EArrayList<Expr_Var> supers = statement.parentclasses;
		EList<ParsedStatement> body = statement.body;
		EList<ParsedStatement> staticMembers = statement.staticMembers;
		EList<Stmt_FuncDef> constructors = statement.initializers;
		
		// create the class framework
		EnvisionClass theClass = new EnvisionClass(userType);
		
		// set body and scope
		IScope classScope = new Scope(interpreter.scope());
		theClass.setScope(classScope);
		theClass.setBody(body);
		//theClass.setStatics(staticMembers);
		//theClass.setConstructors(constructors);
		
		// check that each stated super class is valid
		//for (VarExpression s : supers) {
			//EnvisionClass superClass = getSuper(new EnvisionDatatype(s.getName()));
			//theClass.addParent(superClass);
		//}
		
		// set modifiers
		theClass.setVisibility(dec.getVisibility());
		
		var mods = dec.getMods();
		int modSize = mods.size();
		for (int i = 0; i < modSize; i++) {
			theClass.setModifier(mods.get(i), true);
		}
		
		// go through statements and process valid ones (Variable declarations, Method declarations, Objects)
		int bodySize = body.size();
		for (int i = 0; i < bodySize; i++) {
			checkValid(body.get(i));
		}
		//for (Statement s : staticMembers) checkValid(s);
		
		// gather visible parent members
		/*
		for (InheritableObject sc : theClass.getParents()) {
			//get visible staticmembers
			for (Statement s : sc.getBody()) {
				ParserDeclaration sDec = s.getDeclaration();
				if (sDec != null) {
					if (sDec.isPublic() || sDec.isProtected()) { theClass.addStaticStatement(s); }
				}
			}
			//get visible members
			for (Statement s : sc.getBody()) {
				ParserDeclaration sDec = s.getDeclaration();
				if (sDec != null) {
					if (sDec.isPublic() || sDec.isProtected()) { theClass.addBodyStatement(s); }
				}
			}
		}
		*/
		
		// execute the static members against the class's scope
		
		interpreter.executeStatements(staticMembers, classScope);
		
		// build constructor functions -- inherently static
		int conSize = constructors.size();
		for (int i = 0; i < conSize; i++) {
			Stmt_FuncDef constructor = constructors.get(i);
			EnvisionFunction con = FunctionCreator.buildFunction(interpreter, constructor, classScope);
			theClass.addConstructor(con);
			// define the constructor as a static member on the class scope
			classScope.define("init", Primitives.FUNCTION, con);
		}
		
		// define it
		interpreter.scope().define(className, userType, theClass);
		typeMan.defineUserClass(theClass);
		theClass.assignConstruct(new ClassConstruct(interpreter, theClass));
	}
	
	/*
	private EnvisionClass getSuper(EnvisionDatatype name) {
		if (name == null) return null;
		
		PrimitiveDatatypes type = PrimitiveDatatypes.getDataType(name);
		boolean primitive = type != null && type != PrimitiveDatatypes.NULL;
		TypeManager man = interpreter.getTypeManager();
		EnvisionObject obj = (primitive) ? man.getPrimitiveType(type) : man.getTypeClass(name);
		
		//check not null
		if (obj == null) throw new UndefinedTypeError(name.getType());
		//check that it's actually a class
		if (!(obj instanceof EnvisionClass)) throw new NotAClassError(obj);
		
		EnvisionClass superClass = (EnvisionClass) obj;
		
		//check that the super class is not final
		if (superClass.isFinal()) throw new FinalExtensionError(superClass);
		return superClass;
	}
	*/
	
	private static void checkValid(ParsedStatement s) {
		if (s instanceof Stmt_Class) return;
		if (s instanceof Stmt_Block) return;
//		if (s instanceof Stmt_EnumDef) return;
		if (s instanceof Stmt_FuncDef) return;
		if (s instanceof Stmt_VarDef) return;
		if (s instanceof Stmt_Expression) return;
//		if (s instanceof Stmt_GetSet) return;
		throw new InvalidClassStatement(s);
	}
	
}