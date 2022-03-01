package envision.parser.statements.statements;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statementUtil.VariableDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class VariableStatement implements Statement {
	
	public final EArrayList<VariableDeclaration> vars = new EArrayList();
	public final ParserDeclaration declaration;
	public final GetSetStatement getset;
	
	public VariableStatement(ParserDeclaration declarationIn) { this(declarationIn, null); }
	public VariableStatement(ParserDeclaration declarationIn, GetSetStatement getsetIn) {
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
