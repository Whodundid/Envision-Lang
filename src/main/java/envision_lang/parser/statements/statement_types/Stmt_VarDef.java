package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.VariableDeclaration;
import envision_lang.tokenizer.Token;
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
