package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.lang.util.VisibilityType;
import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.BlockStatement;

public class PS_Visibility extends ParserStage {
	
	/**
	 * Attempts to parse a visibility modifer from tokens.
	 * @return Statement
	 */
	public static Statement handleVisibility() { return handleVisibility(null); }
	public static Statement handleVisibility(ParserDeclaration declaration) {
		VisibilityType visibility = VisibilityType.parse(consumeType(VISIBILITY_MODIFIER, "Expected a visibility modifier!").keyword);
		declaration = (declaration != null) ? declaration.setVisibility(visibility) : new ParserDeclaration(visibility);
		
		errorIf(checkType(VISIBILITY_MODIFIER), "Can only have one visibility modifier!");
		if (check(SCOPE_LEFT)) { return new BlockStatement(handleBlock(declaration)); }
		if (match(PACKAGE)) { return packageDeclaration(declaration); }
		
		return handleDataModifier(declaration);
	}
	
}
