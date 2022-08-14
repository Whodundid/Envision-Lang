package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Generic;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class Stmt_Generic implements Statement {
	
	public final EList<Expr_Generic> generics = new EArrayList<>();
	
	public Stmt_Generic() {}
	public Stmt_Generic(EList<Expr_Generic> genericsIn) {
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
