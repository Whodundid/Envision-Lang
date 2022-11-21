package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Generic;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_Generic extends BasicStatement {
	
	public final EArrayList<Expr_Generic> generics = new EArrayList<>();
	
	public Stmt_Generic(Token start) { this(start, new EArrayList<>()); }
	public Stmt_Generic(Token start, EArrayList<Expr_Generic> genericsIn) {
		super(start);
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
