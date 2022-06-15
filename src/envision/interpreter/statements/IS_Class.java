package envision.interpreter.statements;

import envision.exceptions.errors.classErrors.InvalidClassStatement;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.FunctionCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.scope.Scope;
import envision.lang.classes.ClassConstruct;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.IDatatype;
import envision.lang.natives.NativeTypeManager;
import envision.lang.natives.Primitives;
import envision.lang.util.DataModifier;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_Block;
import envision.parser.statements.statement_types.Stmt_Class;
import envision.parser.statements.statement_types.Stmt_EnumDef;
import envision.parser.statements.statement_types.Stmt_Expression;
import envision.parser.statements.statement_types.Stmt_FuncDef;
import envision.parser.statements.statement_types.Stmt_GetSet;
import envision.parser.statements.statement_types.Stmt_ModularFuncDef;
import envision.parser.statements.statement_types.Stmt_VarDef;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class IS_Class extends StatementExecutor<Stmt_Class> {

	public IS_Class(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Class statement) {
		ParserDeclaration dec = statement.declaration;
		
		IDatatype name = NativeTypeManager.datatypeOf(statement.name.lexeme);
		//@InDevelopment
		//EArrayList<Expr_Var> supers = statement.parentclasses;
		EArrayList<Statement> body = statement.body;
		EArrayList<Statement> staticMembers = statement.staticMembers;
		EArrayList<Stmt_FuncDef> constructors = statement.initializers;
		
		//create the class framework
		EnvisionClass theClass = new EnvisionClass(name.getType());
		Scope classScope = new Scope(scope());
		
		//set body and scope
		theClass.setScope(classScope);
		theClass.setStatics(staticMembers);
		theClass.setBody(body);
		theClass.setConstructors(constructors);
		
		//check that each stated super class is valid
		//for (VarExpression s : supers) {
			//EnvisionClass superClass = getSuper(new EnvisionDatatype(s.getName()));
			//theClass.addParent(superClass);
		//}
		
		//set modifiers
		theClass.setVisibility(dec.getVisibility());
		for (DataModifier d : dec.getMods()) theClass.setModifier(d, true);
		
		//go through statements and process valid ones (Variable declarations, Method declarations, Objects)
		for (Statement s : body) checkValid(s);
		//for (Statement s : staticMembers) checkValid(s);
		
		//gather visible parent members
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
		
		//execute the static members against the class's scope
		executeBlock(staticMembers, classScope);
		
		//build constructor functions -- inherently static
		for (Stmt_FuncDef constructor : constructors) {
			EnvisionFunction con = FunctionCreator.buildFunction(interpreter, constructor, classScope);
			theClass.addConstructor(con);
			//define the constructor as a static member on the class scope
			classScope.define("init", Primitives.FUNCTION, con);
		}
		
		//System.out.println("the body: " + body);
		//System.out.println("static members: " + staticMembers);
		//System.out.println("constructors: " + constructors);
		//System.out.println();
		
		//define it
		scope().define(name.getType(), name, theClass);
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
	
	private void checkValid(Statement s) {
		if (s instanceof Stmt_Class) return;
		if (s instanceof Stmt_Block) return;
		if (s instanceof Stmt_EnumDef) return;
		if (s instanceof Stmt_FuncDef) return;
		if (s instanceof Stmt_ModularFuncDef) return;
		if (s instanceof Stmt_VarDef) return;
		if (s instanceof Stmt_Expression) return;
		if (s instanceof Stmt_GetSet) return;
		throw new InvalidClassStatement(s);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Class s) {
		new IS_Class(in).run(s);
	}
	
}