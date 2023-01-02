package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Generic;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_InterfaceDef extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final EList<Expr_Generic> generics = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_InterfaceDef(Token start, ParserDeclaration declarationIn) { this(start, declarationIn, null); }
	public Stmt_InterfaceDef(Token start, ParserDeclaration declarationIn, EList<Expr_Generic> genericsIn) {
		super(start, declarationIn);
		if (genericsIn != null) generics.addAll(genericsIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleInterfaceStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addGeneric(Expr_Generic in) {
		generics.add(in);
	}
	
}
