package envision.parser.statements;

import envision.parser.util.ParserDeclaration;

public interface Statement {
	
	public default ParserDeclaration getDeclaration() { return null; }
	public void execute(StatementHandler handler);
	public default Statement copy() { return null; }
	
}
