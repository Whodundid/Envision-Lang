package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class Stmt_Class extends BasicStatement {

	public final Token<?> name;
	public final ParserDeclaration declaration;
	public final EList<Expr_Var> parentclasses = new EArrayList<>();
	public final EList<Statement> body = new EArrayList<>();
	public final EList<Statement> staticMembers = new EArrayList<>();
	public final EList<Stmt_FuncDef> methods = new EArrayList<>();
	public final EList<Stmt_FuncDef> initializers = new EArrayList<>();
	
	public Stmt_Class(Token<?> start, Token<?> nameIn, ParserDeclaration declarationIn) {
		super(start);
		name = nameIn;
		declaration = declarationIn;
	}
	
	public Stmt_Class setSupers(EList<Expr_Var> supersIn) {
		parentclasses.clear();
		parentclasses.addAll(supersIn);
		return this;
	}
	
	public Stmt_Class setBody(EList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public Stmt_Class setStaticMembers(EList<Statement> staticsIn) {
		staticMembers.clear();
		staticMembers.addAll(staticsIn);
		return this;
	}
	
	public Stmt_Class setMethods(EList<Stmt_FuncDef> methodsIn) {
		methods.clear();
		methods.addAll(methodsIn);
		return this;
	}
	
	public Stmt_Class setInitializers(EList<Stmt_FuncDef> constructorsIn) {
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
		return declaration + "class '" + name.getLexeme() + "'" + p + c + b + s;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleClassStatement(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
