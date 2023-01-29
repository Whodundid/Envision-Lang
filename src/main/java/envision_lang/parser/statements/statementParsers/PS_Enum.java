package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.util.EnvisionVis;
import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_EnumDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class PS_Enum extends ParserHead {
	
	public static ParsedStatement enumDeclaration() { return enumDeclaration(new ParserDeclaration()); }
	public static ParsedStatement enumDeclaration(ParserDeclaration declaration) {
		//ignoreNL();
		Token<?> name = consume(IDENTIFIER, "Expected an enum name!");
		
		if (declaration == null) declaration = new ParserDeclaration();
		if (declaration.getVisibility() == null) declaration.applyVisibility(EnvisionVis.SCOPE);
		
		Stmt_EnumDef s = new Stmt_EnumDef(name, declaration);
		
		//ignoreNL();
		if (match(COLON)) {
			do {
				//ignoreNL();
				consume(IDENTIFIER, "Expected super enum name.");
				s.addSuper(new Expr_Var(previous()));
				//ignoreNL();
			}
			while (match(COMMA));
		}
		
		//read in class body
		//ignoreNL();
		consume(CURLY_L, "Expected '{' after enum declaration!");
		
		//parse enum values
		//ignoreNL();
		while (!atEnd()) {
			//ignoreNL();
			if (match(SEMICOLON, CURLY_R)) break;
			
			//ignoreNL();
			Token<?> valueName = consume(IDENTIFIER, "Expected an enum value name!");
			EList<ParsedExpression> valueArgs = EList.newList();
			
			//ignoreNL();
			if (match(PAREN_L)) {
				//ignoreNL();
				if (!check(PAREN_R)) {
					do {
						valueArgs.add(ExpressionParser.parseExpression());
						//ignoreNL();
					}
					while (match(COMMA));
					
					//ignoreNL();
					consume(PAREN_R, "Expected ')' after enum parameters!");
				}
			}
			
			s.addValue(new Expr_Enum(valueName, valueArgs));
			//ignoreNL();
			if (check(COMMA)) advance();
			//ignoreNL();
		}
		
		if (previous().getKeyword() != CURLY_R) {
			//set enum body
			s.setBody(getBlock());
		}
		
		return s;
	}
	
}
