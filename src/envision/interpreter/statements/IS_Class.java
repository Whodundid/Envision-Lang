package envision.interpreter.statements;

import envision.exceptions.errors.classErrors.InvalidClassStatement;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.FunctionCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.interpreter.util.scope.Scope;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.lang.util.data.DataModifier;
import envision.lang.util.structureTypes.InheritableObject;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.BlockStatement;
import envision.parser.statements.statement_types.ClassStatement;
import envision.parser.statements.statement_types.EnumStatement;
import envision.parser.statements.statement_types.ExpressionStatement;
import envision.parser.statements.statement_types.GetSetStatement;
import envision.parser.statements.statement_types.MethodDeclarationStatement;
import envision.parser.statements.statement_types.ModularMethodStatement;
import envision.parser.statements.statement_types.VariableStatement;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class IS_Class extends StatementExecutor<ClassStatement> {

	public IS_Class(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(ClassStatement statement) {
		ParserDeclaration dec = statement.declaration;
		
		EnvisionDatatype name = new EnvisionDatatype(statement.name.lexeme);
		//EArrayList<VarExpression> supers = statement.parentclasses;
		EArrayList<Statement> body = statement.body;
		EArrayList<Statement> staticMembers = statement.staticMembers;
		EArrayList<MethodDeclarationStatement> constructors = statement.initializers;
		
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
		for (DataModifier d : dec.getMods()) { theClass.setModifier(d, true); }
		
		//go through statements and process valid ones (Variable declarations, Method declarations, Objects)
		for (Statement s : body) checkValid(s);
		//for (Statement s : staticMembers) { checkValid(s); }
		
		//gather visible parent members
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
		
		//execute the static members against the class's scope
		executeBlock(staticMembers, classScope);
		
		//build constructor methods -- inherently static
		for (MethodDeclarationStatement constructor : constructors) {
			EnvisionFunction con = FunctionCreator.buildMethod(interpreter, constructor, classScope);
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
		if (s instanceof ClassStatement) return;
		if (s instanceof BlockStatement) return;
		if (s instanceof EnumStatement) return;
		if (s instanceof MethodDeclarationStatement) return;
		if (s instanceof ModularMethodStatement) return;
		if (s instanceof VariableStatement) return;
		if (s instanceof ExpressionStatement) return;
		if (s instanceof GetSetStatement) return;
		throw new InvalidClassStatement(s);
	}
	
	public static void run(EnvisionInterpreter in, ClassStatement s) {
		new IS_Class(in).run(s);
	}
	
}