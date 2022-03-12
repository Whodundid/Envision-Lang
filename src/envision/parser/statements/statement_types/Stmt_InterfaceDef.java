package envision.parser.statements.statement_types;

import envision.parser.expressions.expression_types.Expr_Generic;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class Stmt_InterfaceDef implements Statement {
	
	public final ParserDeclaration declaration;
	public final EArrayList<Expr_Generic> generics = new EArrayList();
	
	public Stmt_InterfaceDef(ParserDeclaration declarationIn) { declaration = declarationIn; }
	public Stmt_InterfaceDef(ParserDeclaration declarationIn, EArrayList<Expr_Generic> genericsIn) {
		declaration = declarationIn;
		generics.addAll(genericsIn);
	}
	
	public Stmt_InterfaceDef addGeneric(Expr_Generic in) {
		generics.add(in);
		return this;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleInterfaceStatement(this);
	}
	
}
