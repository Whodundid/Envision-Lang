package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.VariableDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_VarDef extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final EList<VariableDeclaration> vars = EList.newList();
	public final Stmt_GetSet getset;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_VarDef(Token<?> start, ParserDeclaration declarationIn) { this(start, declarationIn, null); }
	public Stmt_VarDef(Token<?> start, ParserDeclaration declarationIn, Stmt_GetSet getsetIn) {
		super(start, declarationIn);
		getset = getsetIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "" + declaration + vars;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleVariableStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addVar(Token nameIn, ParsedExpression expressionIn) {
		vars.add(new VariableDeclaration(nameIn, expressionIn));
	}

}
