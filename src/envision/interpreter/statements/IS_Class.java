package envision.interpreter.statements;

import envision.exceptions.errors.UndefinedTypeError;
import envision.exceptions.errors.classErrors.InvalidClassStatement;
import envision.exceptions.errors.objects.FinalExtensionError;
import envision.exceptions.errors.objects.NotAClassError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.interpreter.util.TypeManager;
import envision.interpreter.util.creationUtil.MethodCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.DataModifier;
import envision.lang.util.structureTypes.InheritableObject;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.BlockStatement;
import envision.parser.statements.types.ClassStatement;
import envision.parser.statements.types.EnumStatement;
import envision.parser.statements.types.ExpressionStatement;
import envision.parser.statements.types.GetSetStatement;
import envision.parser.statements.types.MethodDeclarationStatement;
import envision.parser.statements.types.ModularMethodStatement;
import envision.parser.statements.types.VariableStatement;
import eutil.datatypes.EArrayList;

public class IS_Class extends StatementExecutor<ClassStatement> {

	public IS_Class(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(ClassStatement statement) {
		ParserDeclaration dec = statement.declaration;
		
		String name = statement.name.lexeme;
		EArrayList<VarExpression> supers = statement.superclasses;
		EArrayList<Statement> body = statement.body;
		EArrayList<Statement> staticMembers = statement.staticMembers;
		EArrayList<MethodDeclarationStatement> constructors = statement.constructors;
		
		//create the class framework
		EnvisionClass theClass = new EnvisionClass(name);
		Scope classScope = new Scope(scope());
		
		//set body and scope
		theClass.setScope(classScope);
		theClass.setStatics(staticMembers);
		theClass.setBody(body);
		theClass.setConstructors(constructors);
		
		//check that each stated super class is valid
		for (VarExpression s : supers) {
			EnvisionClass superClass = getSuper(s.getName());
			theClass.addParent(superClass);
		}
		
		//set modifiers
		theClass.setVisibility(dec.getVisibility());
		for (DataModifier d : dec.getMods()) { theClass.setModifier(d, true); }
		
		//go through statements and process valid ones (Variable declarations, Method declarations, Objects)
		for (Statement s : body) { checkValid(s); }
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
		
		//build constructor methods
		for (MethodDeclarationStatement constructor : constructors) {
			EnvisionMethod con = MethodCreator.buildMethod(interpreter, constructor, classScope);
			theClass.addConstructor(con);
		}
		
		//System.out.println("the body: " + body);
		//System.out.println("static members: " + staticMembers);
		//System.out.println("constructors: " + constructors);
		//System.out.println();
		
		//define it
		scope().define(name, name, theClass);
		interpreter.getTypeManager().defineType(name, theClass);
		theClass.assignConstruct(new ClassConstruct(interpreter, theClass));
	}
	
	private EnvisionClass getSuper(String name) {
		if (name == null) return null;
		
		EnvisionDataType type = EnvisionDataType.getDataType(name);
		boolean primitive = type != null && type != EnvisionDataType.NULL;
		TypeManager man = interpreter.getTypeManager();
		EnvisionObject obj = (primitive) ? man.getPrimitiveType(type) : man.getTypeClass(name);
		
		//check not null
		if (obj == null) throw new UndefinedTypeError(name);
		//check that it's actually a class
		if (!(obj instanceof EnvisionClass)) throw new NotAClassError(obj);
		
		EnvisionClass superClass = (EnvisionClass) obj;
		
		//check that the super class is not final
		if (superClass.isFinal()) throw new FinalExtensionError(superClass);
		return superClass;
	}
	
	private void checkValid(Statement s) {
		if (s instanceof ClassStatement) { return; }
		if (s instanceof BlockStatement) { return; }
		if (s instanceof EnumStatement) { return; }
		if (s instanceof MethodDeclarationStatement) { return; }
		if (s instanceof ModularMethodStatement) return;
		if (s instanceof VariableStatement) { return; }
		if (s instanceof ExpressionStatement) { return; }
		if (s instanceof GetSetStatement) { return; }
		throw new InvalidClassStatement(s);
	}
	
	public static void run(EnvisionInterpreter in, ClassStatement s) {
		new IS_Class(in).run(s);
	}
	
}