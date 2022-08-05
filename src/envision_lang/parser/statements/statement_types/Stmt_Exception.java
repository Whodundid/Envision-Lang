package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

/** Declares the start of an exception object.
 *  Exceptions are defined in much the same way as a class is and can have their own members/methods/etc too.
 *  The primary difference between the two is that an exception can be thrown where as a class cannot. */
public class Stmt_Exception implements Statement {
	
	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<Expr_Var> superclasses = new EArrayList<Expr_Var>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	
	public Stmt_Exception(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public Stmt_Exception setSupers(EArrayList<Expr_Var> supersIn) {
		superclasses.clear();
		superclasses.addAll(supersIn);
		return this;
	}
	
	public Stmt_Exception setBody(EArrayList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public Stmt_Exception addSuper(Expr_Var in) { superclasses.add(in); return this; }
	public Stmt_Exception addStatement(Statement in) { body.add(in); return this; }
	
	@Override
	public String toString() {
		String s = (superclasses.isEmpty()) ? "" : " " + superclasses.toString();
		String b = "";
		for (int i = 0; i < body.size(); i++) {
			b += body.get(i);
		}
		return declaration + " exception " + name.lexeme + s + " {\n" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleExceptionStatement(this);
	}
	
}
