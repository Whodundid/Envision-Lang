package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Package;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EList;

public class PS_Package extends GenericParser {
	
	public static Statement packageDeclaration() { return packageDeclaration(new ParserDeclaration()); }
	public static Statement packageDeclaration(ParserDeclaration declaration) {
		Token packageName = consume(IDENTIFIER, "Expected a package name!");
		consume(CURLY_L, "Expected a '{' after package declaration!");
		
		EList<Statement> body = getBlock();
		
		return new Stmt_Package(declaration, packageName, body);
	}
	
}
