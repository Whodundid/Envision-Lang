package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.util.VisibilityType;
import envision_lang.parser.GenericParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_GetSet;
import envision_lang.parser.util.DeclarationStage;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_GetSet extends GenericParser {
	
	public static Statement getSet() { return getSet(new ParserDeclaration()); }
	public static Statement getSet(ParserDeclaration declaration) {
		declaration = (declaration == null) ? new ParserDeclaration().setStage(DeclarationStage.VISIBILITY) : declaration;
		
		Stmt_GetSet statement = parseGetSetVis(declaration);
		EArrayList<Token> vars = new EArrayList();
		
		do vars.add(consume(ReservedWord.IDENTIFIER, "Expected a variable name!"));
		while (match(Operator.COMMA));
		statement.setVars(vars);
		
		return statement;
	}
	
	public static Stmt_GetSet parseGetSetVis() { return parseGetSetVis((VisibilityType) null); }
	public static Stmt_GetSet parseGetSetVis(ParserDeclaration dec) { return parseGetSetVis(dec.getVisibility()); }
	public static Stmt_GetSet parseGetSetVis(VisibilityType visIn) {
		//parse for getset modifiers
		VisibilityType curVis = visIn;
		VisibilityType getVis = VisibilityType.SCOPE;
		VisibilityType setVis = VisibilityType.SCOPE;
		boolean get = false, set = false;
		
		//parse for first getset (if there is one)
		if (checkType(VISIBILITY_MODIFIER)) {
			errorIf(curVis != null, "Duplicate visibilty modifier!");
			curVis = VisibilityType.parse(consumeType(VISIBILITY_MODIFIER, "Invalid get/set visibility modifier!"));
		}
		
		if (match(GET)) {
			get = true;
			getVis = curVis;
		}
		else if (match(SET)) {
			set = true;
			setVis = curVis;
		}
		else error("Expected either 'get' or 'set' here!");
		
		//parse for second getset (if there is one)
		if (checkType(VISIBILITY_MODIFIER))
			curVis = VisibilityType.parse(consumeType(VISIBILITY_MODIFIER, "Invalid get/set visibility modifier!"));
		
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
		
		return new Stmt_GetSet(getVis, setVis, get, set);
	}
	
}