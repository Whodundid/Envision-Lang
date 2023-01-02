package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Catch extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final Token<?> type;
	public final Token<?> var;
	public final EList<ParsedStatement> body;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Catch(Token<?> start, Token<?> typeIn, Token<?> varIn, EList<ParsedStatement> bodyIn) {
		super(start);
		type = typeIn;
		var = varIn;
		body = bodyIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String b = (body != null && body.isEmpty()) ? "" : " " + body + " ";
		return "catch (" + type.getLexeme() + " " + var.getLexeme() + ") {" + b + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleCatchStatement(this);
	}
	
}
