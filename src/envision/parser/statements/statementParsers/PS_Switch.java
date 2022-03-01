package envision.parser.statements.statementParsers;

import static envision.tokenizer.KeywordType.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.exceptions.EnvisionError;
import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.CaseStatement;
import envision.parser.statements.statements.SwitchStatement;
import envision.tokenizer.Token;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class PS_Switch extends GenericParser {
	
	public static Statement switchStatement() {
		while (match(NEWLINE));
		consume(PAREN_L, "Expected '(' after while declaration!");
		while (match(NEWLINE));
		Expression switchExpression = ExpressionParser.parseExpression();
		while (match(NEWLINE));
		consume(PAREN_R, "Expected ')' after while condition!");
		while (match(NEWLINE));
		consume(CURLY_L, "Expected '{' after switch declaration!");

		EArrayList<CaseStatement> cases = new EArrayList();
		CaseStatement defaultCase = null;
		boolean hasDefault = false;
		
		while (match(NEWLINE));
		
		if (!check(CURLY_R)) {
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
					while (!check(CASE, DEFAULT, CURLY_R) && !atEnd()) {
						match(CASE, DEFAULT, CURLY_R);
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
		consume(CURLY_R, "Expected a '}' to close switch statement!");
		
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
