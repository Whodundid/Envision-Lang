package envision_lang.parser.statements;

import envision_lang.parser.util.ParserDeclaration;

public interface Statement {
	
	public void execute(StatementHandler handler);
	public default ParserDeclaration getDeclaration() { return null; }
	public default Statement copy() { return null; }
	
}
