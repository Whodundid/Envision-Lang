package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Package;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class PS_Package extends ParserHead {
	
	public static ParsedStatement packageDeclaration() { return packageDeclaration(new ParserDeclaration()); }
	public static ParsedStatement packageDeclaration(ParserDeclaration declaration) {
		//ignoreNL();
		Token<?> packageName = consume(IDENTIFIER, "Expected a package name!");
		//ignoreNL();
		consume(CURLY_L, "Expected a '{' after package declaration!");
		
		EList<ParsedStatement> body = getBlock();
		
		return new Stmt_Package(declaration.getStartToken(), declaration, packageName, body);
	}
	
}
