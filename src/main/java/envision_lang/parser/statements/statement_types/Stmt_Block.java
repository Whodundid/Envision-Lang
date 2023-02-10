package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Block extends ParsedStatement {

	//========
	// Fields
	//========
	
	public final EList<ParsedStatement> statements = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Block(Token<?> start) { this(start, null); }
	public Stmt_Block(Token<?> start, EList<ParsedStatement> in) {
		super(start);
		if (in != null) statements.addAll(in);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String b = "{";
		if (statements.isNotEmpty()) { b += " "; }
		for (int i = 0; i < statements.size(); i++) {
			ParsedStatement s = statements.get(i);
			b += s + ((i < statements.size() - 1) ? " " : "");
		}
		return b + ((statements.isEmpty()) ? "" : " ") + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleBlockStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addStatement(ParsedStatement in) {
		statements.add(in);
	}
	
}
