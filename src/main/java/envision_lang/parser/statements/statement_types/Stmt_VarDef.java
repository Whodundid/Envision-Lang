package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.VariableDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_VarDef extends BasicStatement {
	
	public final EArrayList<VariableDeclaration> vars = new EArrayList();
	public final ParserDeclaration declaration;
	public final Stmt_GetSet getset;
	
	public Stmt_VarDef(Token start, ParserDeclaration declarationIn) { this(start, declarationIn, null); }
	public Stmt_VarDef(Token start, ParserDeclaration declarationIn, Stmt_GetSet getsetIn) {
		super(start);
		declaration = declarationIn;
		getset = getsetIn;
	}
	
	public void addVar(Token nameIn, Expression expressionIn) {
		vars.add(new VariableDeclaration(nameIn, expressionIn));
	}
	
	@Override
	public String toString() {
		return "" + declaration + vars;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleVariableStatement(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
