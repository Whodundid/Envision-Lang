package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.exceptions.EnvisionError;
import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.CaseStatement;
import envision.parser.statements.types.SwitchStatement;
import envision.tokenizer.Token;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class PS_Switch extends ParserStage {
	
	public static Statement switchStatement() {
		while (match(NEWLINE));
		consume(EXPR_LEFT, "Expected '(' after while declaration!");
		while (match(NEWLINE));
		Expression switchExpression = expression();
		while (match(NEWLINE));
		consume(EXPR_RIGHT, "Expected ')' after while condition!");
		while (match(NEWLINE));
		consume(SCOPE_LEFT, "Expected '{' after switch declaration!");

		EArrayList<CaseStatement> cases = new EArrayList();
		CaseStatement defaultCase = null;
		boolean hasDefault = false;
		
		while (match(NEWLINE));
		
		if (!check(SCOPE_RIGHT)) {
			while (check(CASE, DEFAULT) && !atEnd()) {
				
				Token caseName = null;
				boolean isDefault = match(DEFAULT);
				
				//prevent multiple defaults
				if (hasDefault && isDefault) error("Switch already has a default case!");
				
				if (!isDefault) {
					consume(CASE, "Expected a case statement!");
					while (match(NEWLINE));
					caseName = consumeType(LITERAL, "Expected a case name!");
					//prevent duplicate cases
					if (hasCase(cases, caseName)) {
						setPrevious();
						error("Duplicate case: '" + caseName + "' in the current switch statement!");
					}
				}
				
				while (match(NEWLINE));
				EArrayList<Statement> body = new EArrayList();
				
				if (check(COLON)) {
					consume(COLON, "Expected a ':' after case name!");
					while (!check(CASE, DEFAULT, SCOPE_RIGHT) && !atEnd()) {
						match(CASE, DEFAULT, SCOPE_RIGHT);
						Statement s = declaration();
						body.addIfNotNull(s);
					}
				}
				else error("Expected a ':' after case name!");
				/*
				else {
					consume(LAMBDA, "Expected a lambda function!");
					while (match(NEWLINE));
					Statement s = declaration();
					if (s == null) error("A lambda case statement cannot be null!");
					body.add(s);
				}
				*/
				
				CaseStatement theCase = new CaseStatement(caseName, body, isDefault);
				if (isDefault) defaultCase = theCase;
				cases.add(theCase);
				
				if (isDefault) break;
			}
		}
		
		while (match(NEWLINE));
		consume(SCOPE_RIGHT, "Expected a '}' to close switch statement!");
		
		return new SwitchStatement(switchExpression, cases, defaultCase);
	}
	
	private static boolean hasCase(EArrayList<CaseStatement> cases, Token t) {
		if (t == null) throw new EnvisionError("Switch Error: Token is null!");
		for (CaseStatement c : cases) {
			if (EUtil.isEqual(c.caseName.lexeme, t.lexeme)) return true;
		}
		return false;
	}
	
}
