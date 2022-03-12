package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.parser.util.VariableDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_VarDef implements Statement {
	
	public final EArrayList<VariableDeclaration> vars = new EArrayList();
	public final ParserDeclaration declaration;
	public final Stmt_GetSet getset;
	
	public Stmt_VarDef(ParserDeclaration declarationIn) { this(declarationIn, null); }
	public Stmt_VarDef(ParserDeclaration declarationIn, Stmt_GetSet getsetIn) {
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
	
}
