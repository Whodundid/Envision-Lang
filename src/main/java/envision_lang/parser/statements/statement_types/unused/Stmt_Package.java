package envision_lang.parser.statements.statement_types.unused;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Package extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final Token<?> name;
	public final EList<ParsedStatement> body;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Package(Token<?> start, ParserDeclaration declarationIn, Token<?> nameIn, EList<ParsedStatement> bodyIn) {
		super(start, declarationIn);
		name = nameIn;
		body = bodyIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String b = (body != null && body.isEmpty()) ? "" : " " + body + " ";
		return declaration + "package " + name.getLexeme() + " {" + b + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
//		handler.handlePackageStatement(this);
	}
	
}
