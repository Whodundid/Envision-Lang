package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.lang.util.data.DataModifier;
import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.DeclarationStage;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.BlockStatement;
import eutil.datatypes.EArrayList;

/**
 * Parses data modifiers from tokens.
 * 
 * The recognized list of data modifiers are:
 * 		static
 * 		final
 * 		strong
 * 		abstract
 * 		override
 * 
 * @author Hunter
 *
 */
public class PS_DataModifiers extends ParserStage {
	
	/**
	 * Attempts to parse data modifiers from tokens.
	 * @return Statement
	 */
	public static Statement handleDataModifier() { return handleDataModifier(new ParserDeclaration()); }
	public static Statement handleDataModifier(ParserDeclaration declaration) {
		declaration = (declaration != null) ? declaration : new ParserDeclaration().setStage(DeclarationStage.VISIBILITY);
		
		//collect modifiers
		EArrayList<DataModifier> modifiers = new EArrayList();
		while (checkType(DATA_MODIFIER)) {
			DataModifier m = DataModifier.of(consumeType(DATA_MODIFIER, "Expected a data modifier!").keyword);
			modifiers.addIf(m != null, m);
		}
		
		//set modifiers
		declaration.applyDataMods(modifiers);
		declaration.setStage(DeclarationStage.DATAMODS);
		
		if (check(GET, SET)) return getSet(declaration);
		if (match(ENUM)) { return enumDeclaration(declaration); }
		if (check(SCOPE_LEFT)) { return new BlockStatement(handleBlock(declaration)); }
		
		return handleGenerics(declaration);
	}
	
}
