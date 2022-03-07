package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.PackageStatement;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Package extends GenericParser {
	
	public static Statement packageDeclaration() { return packageDeclaration(new ParserDeclaration()); }
	public static Statement packageDeclaration(ParserDeclaration declaration) {
		Token packageName = consume(IDENTIFIER, "Expected a package name!");
		consume(CURLY_L, "Expected a '{' after package declaration!");
		
		EArrayList<Statement> body = getBlock();
		
		return new PackageStatement(declaration, packageName, body);
	}
	
}
