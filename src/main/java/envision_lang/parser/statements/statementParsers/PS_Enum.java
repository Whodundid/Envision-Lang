package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.util.VisibilityType;
import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_EnumDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Enum extends GenericParser {
	
	public static Statement enumDeclaration() { return enumDeclaration(new ParserDeclaration()); }
	public static Statement enumDeclaration(ParserDeclaration declaration) {
		Token name = consume(IDENTIFIER, "Expected an enum name!");
		
		if (declaration == null) { declaration = new ParserDeclaration(); }
		if (declaration.getVisibility() == null) { declaration.applyVisibility(VisibilityType.SCOPE); }
		
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
			while (match(NEWLINE));
			if (match(SEMICOLON, CURLY_R)) { break; }
			
			Token valueName = consume(IDENTIFIER, "Expected an enum value name!");
			EArrayList<Expression> valueArgs = new EArrayList();
			
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
			if (check(COMMA)) { advance(); }
		}
		
		if (previous().keyword != CURLY_R) {
			//set enum body
			s.setBody(getBlock());
		}
		
		return s;
	}
	
}
