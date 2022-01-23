package envision.parser.statements.types;

import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class ClassStatement implements Statement {

	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<VarExpression> superclasses = new EArrayList<VarExpression>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	public final EArrayList<Statement> staticMembers = new EArrayList<Statement>();
	public final EArrayList<MethodDeclarationStatement> methods = new EArrayList<MethodDeclarationStatement>();
	public final EArrayList<MethodDeclarationStatement> constructors = new EArrayList<MethodDeclarationStatement>();
	
	public ClassStatement(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public ClassStatement setSupers(EArrayList<VarExpression> supersIn) {
		superclasses.clear();
		superclasses.addAll(supersIn);
		return this;
	}
	
	public ClassStatement setBody(EArrayList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public ClassStatement setStaticMembers(EArrayList<Statement> staticsIn) {
		staticMembers.clear();
		staticMembers.addAll(staticsIn);
		return this;
	}
	
	public ClassStatement setMethods(EArrayList<MethodDeclarationStatement> methodsIn) {
		methods.clear();
		methods.addAll(methodsIn);
		return this;
	}
	
	public ClassStatement setConstructors(EArrayList<MethodDeclarationStatement> constructorsIn) {
		constructors.clear();
		constructors.addAll(constructorsIn);
		return this;
	}
	
	public ClassStatement addSuper(VarExpression in) { superclasses.add(in); return this; }
	public ClassStatement addStatement(Statement in) { body.add(in); return this; }
	
	@Override
	public String toString() {
		String s = (superclasses.isEmpty()) ? "" : " " + superclasses.toString();
		String b = "\n\tbody:", c = (constructors.isNotEmpty() ? "\n\tconstructors:" : "");
		for (int i = 0; i < constructors.size(); i++) c += "\n\t\t" + constructors.get(i);
		for (int i = 0; i < body.size(); i++) b += "\n\t\t" + body.get(i);
		return declaration + "class '" + name.lexeme + "'" + s + c + b;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleClassStatement(this);
	}
	
}
