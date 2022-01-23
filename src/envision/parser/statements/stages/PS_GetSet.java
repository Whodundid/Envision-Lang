package envision.parser.statements.stages;

import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.DeclarationStage;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.GetSetStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_GetSet extends ParserStage {
	
	public static Statement getSet() { return getSet(new ParserDeclaration()); }
	public static Statement getSet(ParserDeclaration declaration) {
		declaration = (declaration == null) ? new ParserDeclaration().setStage(DeclarationStage.VISIBILITY) : declaration;
		
		boolean get = match(Keyword.GET);
		boolean set = match(Keyword.SET);
		EArrayList<Token> vars = new EArrayList();
		
		//check for 'set get' order
		if (match(Keyword.GET)) {
			if (get) error("Invalid statement declaration!");
			get = true;
		}
		
		do {
			vars.add(consume(Keyword.IDENTIFIER, "Expected a variable name!"));
		}
		while (match(Keyword.COMMA));
		
		return new GetSetStatement(declaration, get, set, vars);
	}
	
}
