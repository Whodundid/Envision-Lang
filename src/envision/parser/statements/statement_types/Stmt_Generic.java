package envision.parser.statements.statement_types;

import envision.parser.expressions.expression_types.Expr_Generic;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class Stmt_Generic implements Statement {
	
	public final EArrayList<Expr_Generic> generics = new EArrayList();
	
	public Stmt_Generic() {}
	public Stmt_Generic(EArrayList<Expr_Generic> genericsIn) {
		generics.addAll(genericsIn);
	}
	
	public Stmt_Generic addGeneric(Expr_Generic in) {
		generics.add(in);
		return this;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleGenericStatement(this);
	}
	
}
