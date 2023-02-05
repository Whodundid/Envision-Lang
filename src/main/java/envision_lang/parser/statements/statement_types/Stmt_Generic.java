package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.unused.Expr_Generic;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Generic extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final EList<Expr_Generic> generics = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Generic(Token start) { this(start, null); }
	public Stmt_Generic(Token start, EList<Expr_Generic> genericsIn) {
		super(start);
		if (genericsIn != null) generics.addAll(genericsIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void execute(StatementHandler handler) {
//		handler.handleGenericStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public Stmt_Generic addGeneric(Expr_Generic in) {
		generics.add(in);
		return this;
	}
	
}
