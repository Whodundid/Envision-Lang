package envision_lang.parser.statements.statementParsers.unused;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.natives.EnvisionVisibilityModifier;
import envision_lang.parser.ParserHead;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.unused.Stmt_GetSet;
import envision_lang.parser.util.DeclarationStage;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class PS_GetSet extends ParserHead {
	
	public static ParsedStatement getSet() { return getSet(new ParserDeclaration()); }
	public static ParsedStatement getSet(ParserDeclaration declaration) {
		declaration = (declaration == null) ? new ParserDeclaration().setStage(DeclarationStage.VISIBILITY) : declaration;
		
		Stmt_GetSet statement = parseGetSetVis(declaration);
		EList<Token<?>> vars = EList.newList();
		
		do vars.add(consume(ReservedWord.IDENTIFIER, "Expected a variable name!"));
		while (match(Operator.COMMA));
		statement.setVars(vars);
		
		return statement;
	}
	
	public static Stmt_GetSet parseGetSetVis() { return parseGetSetVis((EnvisionVisibilityModifier) null); }
	public static Stmt_GetSet parseGetSetVis(ParserDeclaration dec) { return parseGetSetVis(dec.getVisibility()); }
	public static Stmt_GetSet parseGetSetVis(EnvisionVisibilityModifier visIn) {
		//parse for getset modifiers
		EnvisionVisibilityModifier curVis = visIn;
		EnvisionVisibilityModifier getVis = null;
		EnvisionVisibilityModifier setVis = null;
		boolean get = false, set = false;
		
		//parse for first getset (if there is one)
		if (checkType(VISIBILITY_MODIFIER)) {
			errorIf(curVis != null, "Duplicate visibilty modifier!");
			curVis = EnvisionVisibilityModifier.parse(consumeType(VISIBILITY_MODIFIER, "Invalid get/set visibility modifier!"));
		}
		
		Token<?> start = null;
		
		if (match(GET)) {
			get = true;
			getVis = curVis;
			start = previous();
		}
		else if (match(SET)) {
			set = true;
			setVis = curVis;
			start = previous();
		}
		else error("Expected either 'get' or 'set' here!");
		
		//parse for second getset (if there is one)
		if (checkType(VISIBILITY_MODIFIER))
			curVis = EnvisionVisibilityModifier.parse(consumeType(VISIBILITY_MODIFIER, "Invalid get/set visibility modifier!"));
		
		if (match(GET)) {
			if (get) error("Duplicate 'get' declaration in current variable declaration!");
			get = true;
			getVis = curVis;
		}
		else if (match(SET)) {
			if (set) error("Duplicate 'set' declaration in current variable declaration!");
			set = true;
			setVis = curVis;
		}
		else error("Expected either 'get' or 'set' here!");
		
		return new Stmt_GetSet(start, getVis, setVis);
	}
	
}
