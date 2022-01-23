package envision.parser.statements.types;

import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

/** Declares the start of an exception object.
 *  Exceptions are defined in much the same way as a class is and can have their own members/methods/etc too.
 *  The primary difference between the two is that an exception can be thrown where as a class cannot. */
public class ExceptionStatement implements Statement {
	
	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<VarExpression> superclasses = new EArrayList<VarExpression>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	
	public ExceptionStatement(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public ExceptionStatement setSupers(EArrayList<VarExpression> supersIn) {
		superclasses.clear();
		superclasses.addAll(supersIn);
		return this;
	}
	
	public ExceptionStatement setBody(EArrayList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public ExceptionStatement addSuper(VarExpression in) { superclasses.add(in); return this; }
	public ExceptionStatement addStatement(Statement in) { body.add(in); return this; }
	
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
