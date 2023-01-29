package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_SwitchCase;
import envision_lang.parser.statements.statement_types.Stmt_SwitchDef;
import envision_lang.tokenizer.Token;
import eutil.EUtil;
import eutil.datatypes.util.EList;

public class PS_Switch extends ParserHead {
	
	public static ParsedStatement switchStatement() {
		//ignoreNL();
		Token<?> switchToken = consume(SWITCH, "Expected 'switch' here!");
		//ignoreNL();
		consume(PAREN_L, "Expected '(' after while declaration!");
		//ignoreNL();
		ParsedExpression switchExpression = ExpressionParser.parseExpression();
		//ignoreNL();
		consume(PAREN_R, "Expected ')' after while condition!");
		//ignoreNL();
		consume(CURLY_L, "Expected '{' after switch declaration!");

		EList<Stmt_SwitchCase> cases = EList.newList();
		Stmt_SwitchCase defaultCase = null;
		boolean hasDefault = false;
		
		//ignoreNL();
		
		if (!check(CURLY_R)) {
			while (check(CASE, DEFAULT) && !atEnd()) {
				
				Token<?> caseToken = null;
				Token<?> caseName = null;
				boolean isDefault = match(DEFAULT);
				
				//prevent multiple defaults
				if (hasDefault && isDefault) error("Switch already has a default case!");
				
				if (!isDefault) {
					//ignoreNL();
					caseToken = consume(CASE, "Expected a case statement!");
					//ignoreNL();
					caseName = consumeType(LITERAL, "Expected a case name!");
					//prevent duplicate cases
					if (hasCase(cases, caseName)) {
						decrementParsingIndex();
						error("Duplicate case: '" + caseName + "' in the current switch statement!");
					}
				}
				
				EList<ParsedStatement> body = EList.newList();
				
				//ignoreNL();
				if (check(COLON)) {
					consume(COLON, "Expected a ':' after case name!");
					while (!check(CASE, DEFAULT, CURLY_R) && !atEnd()) {
						//ignoreNL();
						match(CASE, DEFAULT, CURLY_R);
						ParsedStatement s = declaration();
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
				
				Stmt_SwitchCase theCase = new Stmt_SwitchCase(caseToken, caseName, body, isDefault);
				if (isDefault) defaultCase = theCase;
				cases.add(theCase);
				
				if (isDefault) break;
			}
		}
		
		//ignoreNL();
		consume(CURLY_R, "Expected a '}' to close switch statement!");
		
		return new Stmt_SwitchDef(switchToken, switchExpression, cases, defaultCase);
	}
	
	private static boolean hasCase(EList<Stmt_SwitchCase> cases, Token<?> t) {
		if (t == null) throw new EnvisionLangError("Switch Error: Token is null!");
		for (Stmt_SwitchCase c : cases) {
			if (EUtil.isEqual(c.caseName.getLexeme(), t.getLexeme())) return true;
		}
		return false;
	}
	
}
