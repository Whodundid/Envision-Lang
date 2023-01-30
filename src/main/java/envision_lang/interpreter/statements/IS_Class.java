package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.FunctionCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.classes.ClassConstruct;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.exceptions.errors.classErrors.InvalidClassStatement;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.DataModifier;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.natives.Primitives;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Class;
import envision_lang.parser.statements.statement_types.Stmt_EnumDef;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.statements.statement_types.Stmt_GetSet;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.util.ParserDeclaration;
import eutil.datatypes.util.EList;

public class IS_Class extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_Class statement) {
		ParserDeclaration dec = statement.getDeclaration();
		
		IDatatype name = NativeTypeManager.datatypeOf(statement.name.getLexeme());
		
		//EArrayList<Expr_Var> supers = statement.parentclasses;
		EList<ParsedStatement> body = statement.body;
		EList<ParsedStatement> staticMembers = statement.staticMembers;
		EList<Stmt_FuncDef> constructors = statement.initializers;
		
		// create the class framework
		EnvisionClass theClass = new EnvisionClass(name.getStringValue());
		IScope classScope = new Scope(interpreter.scope());
		
		// set body and scope
		theClass.setScope(classScope);
		//theClass.setStatics(staticMembers);
		theClass.setBody(body);
		//theClass.setConstructors(constructors);
		
		// check that each stated super class is valid
		//for (VarExpression s : supers) {
			//EnvisionClass superClass = getSuper(new EnvisionDatatype(s.getName()));
			//theClass.addParent(superClass);
		//}
		
		// set modifiers
		theClass.setVisibility(dec.getVisibility());
		for (DataModifier d : dec.getMods()) theClass.setModifier(d, true);
		
		// go through statements and process valid ones (Variable declarations, Method declarations, Objects)
		for (ParsedStatement s : body) checkValid(s);
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
		interpreter.executeBlock(staticMembers, classScope);
		
		// build constructor functions -- inherently static
		for (Stmt_FuncDef constructor : constructors) {
			EnvisionFunction con = FunctionCreator.buildFunction(interpreter, constructor, classScope);
			theClass.addConstructor(con);
			// define the constructor as a static member on the class scope
			classScope.define("init", Primitives.FUNCTION, con);
		}
		
		// define it
		interpreter.scope().define(name.getStringValue(), name, theClass);
		interpreter.getTypeManager().defineType(name, theClass);
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
		if (s instanceof Stmt_EnumDef) return;
		if (s instanceof Stmt_FuncDef) return;
		if (s instanceof Stmt_VarDef) return;
		if (s instanceof Stmt_Expression) return;
		if (s instanceof Stmt_GetSet) return;
		throw new InvalidClassStatement(s);
	}
	
}