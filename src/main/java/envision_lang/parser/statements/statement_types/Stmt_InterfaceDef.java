package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Generic;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
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
