package envision.parser.statements.types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class GetSetStatement implements Statement {

	public final ParserDeclaration declaration;
	public final boolean get, set;
	public final EArrayList<Token> vars;
	
	public GetSetStatement(ParserDeclaration declarationIn, boolean getIn, boolean setIn, EArrayList<Token> varsIn) {
		declaration = declarationIn;
		get = getIn;
		set = setIn;
		vars = varsIn;
	}
	
	@Override
	public String toString() {
		String out = "";
		if (get) out += "get ";
		if (set) out += "set ";
		out += vars;
		return out;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleGetSetStatement(this);
	}
	
}
