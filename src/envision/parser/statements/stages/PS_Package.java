package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.PackageStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Package extends ParserStage {
	
	public static Statement packageDeclaration() { return packageDeclaration(new ParserDeclaration()); }
	public static Statement packageDeclaration(ParserDeclaration declaration) {
		Token packageName = consume(IDENTIFIER, "Expected a package name!");
		consume(SCOPE_LEFT, "Expected a '{' after package declaration!");
		
		EArrayList<Statement> body = getBlock();
		
		return new PackageStatement(declaration, packageName, body);
	}
	
}
