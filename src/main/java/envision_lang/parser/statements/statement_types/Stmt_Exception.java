package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/**
 * Declares the start of an exception object. Exceptions are defined in
 * much the same way as a class is and can have their own
 * members/methods/etc too. The primary difference between the two is that
 * an exception can be thrown where as a class cannot.
 */
public class Stmt_Exception extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final Token<?> name;
	public final EList<ParsedStatement> body = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Exception(Token<?> nameIn, ParserDeclaration declarationIn) {
		super(nameIn, declarationIn);
		name = nameIn;
	}
	
	@Override
	public String toString() {
		String b = "";
		for (int i = 0; i < body.size(); i++) {
			b += body.get(i);
		}
		return declaration + " exception " + name.getLexeme() + " {\n" + b + "}";
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleExceptionStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addStatement(ParsedStatement in) { body.add(in); }
	
	public void setBody(EList<ParsedStatement> bodyIn) { body.clearThenAddAll(bodyIn); }
	
}
