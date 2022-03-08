package envision.parser.statements.statement_types;

import envision.parser.expressions.expression_types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class ClassStatement implements Statement {

	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<VarExpression> parentclasses = new EArrayList<VarExpression>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	public final EArrayList<Statement> staticMembers = new EArrayList<Statement>();
	public final EArrayList<FuncDefStatement> methods = new EArrayList<FuncDefStatement>();
	public final EArrayList<FuncDefStatement> initializers = new EArrayList<FuncDefStatement>();
	
	public ClassStatement(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public ClassStatement setSupers(EArrayList<VarExpression> supersIn) {
		parentclasses.clear();
		parentclasses.addAll(supersIn);
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
	
	public ClassStatement setMethods(EArrayList<FuncDefStatement> methodsIn) {
		methods.clear();
		methods.addAll(methodsIn);
		return this;
	}
	
	public ClassStatement setInitializers(EArrayList<FuncDefStatement> constructorsIn) {
		initializers.clear();
		initializers.addAll(constructorsIn);
		return this;
	}
	
	public ClassStatement addSuper(VarExpression in) { parentclasses.add(in); return this; }
	public ClassStatement addStatement(Statement in) { body.add(in); return this; }
	
	@Override
	public String toString() {
		String p = (parentclasses.isEmpty()) ? "" : " " + parentclasses.toString();
		String b = (body.isNotEmpty()) ? "\n\t\tbody:" : "";
		String c = (initializers.isNotEmpty()) ? "\n\t\tintializers:" : "";
		String s = (staticMembers.isNotEmpty()) ? "\n\t\tstatic:" : "";
		for (int i = 0; i < initializers.size(); i++) c += "\n\t\t\t" + initializers.get(i);
		for (int i = 0; i < body.size(); i++) b += "\n\t\t\t" + body.get(i);
		for (int i = 0; i < staticMembers.size(); i++) s += "\n\t\t\t" + staticMembers.get(i);
		return declaration + "class '" + name.lexeme + "'" + p + c + b + s;
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
