package envision_lang.parser.statements.statementParsers.unused;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.natives.EnvisionVisibilityModifier;
import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.expressions.expression_types.unused.Expr_Enum;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.unused.Stmt_EnumDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class PS_Enum extends ParserHead {
	
	public static ParsedStatement enumDeclaration() { return enumDeclaration(new ParserDeclaration()); }
	public static ParsedStatement enumDeclaration(ParserDeclaration declaration) {
		Token<?> name = consume(IDENTIFIER, "Expected an enum name!");
		
		if (declaration == null) declaration = new ParserDeclaration();
		if (declaration.getVisibility() == null) declaration.applyVisibility(EnvisionVisibilityModifier.SCOPE);
		
		Stmt_EnumDef s = new Stmt_EnumDef(name, declaration);
		
		if (match(COLON)) {
			do {
				consume(IDENTIFIER, "Expected super enum name.");
				s.addSuper(new Expr_Var(previous()));
			}
			while (match(COMMA));
		}
		
		//read in class body
		consume(CURLY_L, "Expected '{' after enum declaration!");
		
		//parse enum values
		while (!atEnd()) {
			if (match(SEMICOLON, CURLY_R)) break;
			
			Token<?> valueName = consume(IDENTIFIER, "Expected an enum value name!");
			EList<ParsedExpression> valueArgs = EList.newList();
			
			if (match(PAREN_L)) {
				if (!check(PAREN_R)) {
					do {
						valueArgs.add(ExpressionParser.parseExpression());
					}
					while (match(COMMA));
					
					consume(PAREN_R, "Expected ')' after enum parameters!");
				}
			}
			
			s.addValue(new Expr_Enum(valueName, valueArgs));
			if (check(COMMA)) advance();
		}
		
		if (previous().getKeyword() != CURLY_R) {
			//set enum body
			s.setBody(getBlock());
		}
		
		return s;
	}
	
}
