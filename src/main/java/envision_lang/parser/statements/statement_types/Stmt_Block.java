package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
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
	
	public Stmt_Block(Token<?> start) { this(null, start, null); }
	public Stmt_Block(Token<?> start, EList<ParsedStatement> in) { this(null, start, in); }
	public Stmt_Block(ParserDeclaration dec, Token<?> start) { this(dec, start, null); }
	public Stmt_Block(ParserDeclaration dec, Token<?> start, EList<ParsedStatement> in) {
		super(start, dec);
		if (in != null) statements.addAll(in);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String b = "BLOCK{";
		if (statements.isNotEmpty()) { b += " "; }
		for (int i = 0; i < statements.size(); i++) {
			ParsedStatement s = statements.get(i);
			b += s + ((i < statements.size() - 1) ? "; " : ";");
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
