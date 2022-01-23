package envision.lang.util.structureTypes;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.parser.statements.Statement;
import envision.parser.statements.types.MethodDeclarationStatement;
import eutil.datatypes.EArrayList;

public abstract class InheritableObject extends EnvisionObject {
	
	protected EArrayList<InheritableObject> parents = new EArrayList();
	protected EArrayList<EnvisionObject> staticMembers = new EArrayList();
	protected EArrayList<Statement> staticStatements = new EArrayList();
	protected EArrayList<Statement> bodyStatements = new EArrayList();
	protected EArrayList<MethodDeclarationStatement> constructorStatements = new EArrayList();
	//protected EArrayList<MethodDeclarationStatement> methods = new EArrayList();
	
	//---------------------------------------------------------------------------
	
	protected InheritableObject(EnvisionDataType internalTypeIn, String nameIn) {
		super(internalTypeIn, nameIn);
	}
	
	//---------------------------------------------------------------------------
	
	public InheritableObject addParent(InheritableObject in) { parents.addNullContains(in); return this; }
	public InheritableObject addStaticStatement(Statement s) { staticStatements.add(s); return this; }
	public InheritableObject addBodyStatement(Statement s) { bodyStatements.add(s); return this; }
	public InheritableObject addConstructorStatement(MethodDeclarationStatement c) { constructorStatements.add(c); return this; }
	
	//---------------------------------------------------------------------------
	
	public EArrayList<InheritableObject> getParents() { return parents; }
	public EArrayList<EnvisionObject> getStaticMembers() { return staticMembers; }
	public EArrayList<Statement> getStaticStatements() { return staticStatements; }
	public EArrayList<Statement> getBody() { return bodyStatements; }
	//public EArrayList<MethodDeclarationStatement> getMethods() { return methods; }

	//---------------------------------------------------------------------------
	
	public InheritableObject setParents(EArrayList<InheritableObject> in) { parents = in; return this; }
	public InheritableObject setStatics(EArrayList<Statement> in) { staticStatements = in; return this; }
	public InheritableObject setBody(EArrayList<Statement> in) { bodyStatements = in; return this; }
	public InheritableObject setConstructors(EArrayList<MethodDeclarationStatement> in) { constructorStatements = in; return this; }
	
	//public InheritableObject setMethods(EArrayList<MethodDeclarationStatement> in) { methods = in; return this; }
	//public InheritableObject addMethod(MethodDeclarationStatement m) { methods.add(m); return this; }
	
	//---------------------------------------------------------------------------
	
}
