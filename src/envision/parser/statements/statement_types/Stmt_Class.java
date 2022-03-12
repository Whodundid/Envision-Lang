package envision.parser.statements.statement_types;

import envision.parser.expressions.expression_types.Expr_Var;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_Class implements Statement {

	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<Expr_Var> parentclasses = new EArrayList<Expr_Var>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	public final EArrayList<Statement> staticMembers = new EArrayList<Statement>();
	public final EArrayList<Stmt_FuncDef> methods = new EArrayList<Stmt_FuncDef>();
	public final EArrayList<Stmt_FuncDef> initializers = new EArrayList<Stmt_FuncDef>();
	
	public Stmt_Class(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public Stmt_Class setSupers(EArrayList<Expr_Var> supersIn) {
		parentclasses.clear();
		parentclasses.addAll(supersIn);
		return this;
	}
	
	public Stmt_Class setBody(EArrayList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public Stmt_Class setStaticMembers(EArrayList<Statement> staticsIn) {
		staticMembers.clear();
		staticMembers.addAll(staticsIn);
		return this;
	}
	
	public Stmt_Class setMethods(EArrayList<Stmt_FuncDef> methodsIn) {
		methods.clear();
		methods.addAll(methodsIn);
		return this;
	}
	
	public Stmt_Class setInitializers(EArrayList<Stmt_FuncDef> constructorsIn) {
		initializers.clear();
		initializers.addAll(constructorsIn);
		return this;
	}
	
	public Stmt_Class addSuper(Expr_Var in) { parentclasses.add(in); return this; }
	public Stmt_Class addStatement(Statement in) { body.add(in); return this; }
	
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
