package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_SwitchCase;
import envision_lang.parser.statements.statement_types.Stmt_SwitchDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.EUtil;
import eutil.datatypes.util.EList;

public class PS_Switch extends ParserHead {
	
	public static ParsedStatement switchStatement(ParserDeclaration dec) {
		Token<?> switchToken = consume(SWITCH, "Expected 'switch' here!");
		consume(PAREN_L, "Expected '(' after while declaration!");
		ParsedExpression switchExpression = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected ')' after while condition!");
		consume(CURLY_L, "Expected '{' after switch declaration!");

		EList<Stmt_SwitchCase> cases = EList.newList();
		Stmt_SwitchCase defaultCase = null;
		boolean hasDefault = false;
		
		if (!check(CURLY_R)) {
			while (check(CASE, DEFAULT) && !atEnd()) {
				
				Token<?> caseToken = null;
				Token<?> caseName = null;
				boolean isDefault = match(DEFAULT);
				
				//prevent multiple defaults
				if (hasDefault && isDefault) error("Switch already has a default case!");
				
				if (!isDefault) {
					caseToken = consume(CASE, "Expected a case statement!");
					caseName = consumeType(LITERAL, "Expected a case name!");
					//prevent duplicate cases
					if (hasCase(cases, caseName)) {
						decrementParsingIndex();
						error("Duplicate case: '" + caseName + "' in the current switch statement!");
					}
				}
				
				EList<ParsedStatement> body = EList.newList();
				
				if (check(COLON)) {
					consume(COLON, "Expected a ':' after case name!");
					while (!check(CASE, DEFAULT, CURLY_R) && !atEnd()) {
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
		
		consume(CURLY_R, "Expected a '}' to close switch statement!");
		
		var stmt = new Stmt_SwitchDef(switchToken, switchExpression, cases, defaultCase);
		
		if (dec != null) stmt.setBlockStatement(dec.isBlockingStatement());
		
		return stmt;
	}
	
	private static boolean hasCase(EList<Stmt_SwitchCase> cases, Token<?> t) {
		if (t == null) throw new EnvisionLangError("Switch Error: Token is null!");
		for (Stmt_SwitchCase c : cases) {
			if (EUtil.isEqual(c.caseName.getLexeme(), t.getLexeme())) return true;
		}
		return false;
	}
	
}
