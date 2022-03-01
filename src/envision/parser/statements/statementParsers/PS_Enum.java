package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.lang.util.VisibilityType;
import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.expressions.expressions.EnumExpression;
import envision.parser.expressions.expressions.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statements.EnumStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Enum extends GenericParser {
	
	public static Statement enumDeclaration() { return enumDeclaration(new ParserDeclaration()); }
	public static Statement enumDeclaration(ParserDeclaration declaration) {
		Token name = consume(IDENTIFIER, "Expected an enum name!");
		
		if (declaration == null) { declaration = new ParserDeclaration(); }
		if (declaration.getVisibility() == null) { declaration.applyVisibility(VisibilityType.SCOPE); }
		
		EnumStatement s = new EnumStatement(name, declaration);
		
		if (match(COLON)) {
			do {
				consume(IDENTIFIER, "Expected super enum name.");
				s.addSuper(new VarExpression(previous()));
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
			
			s.addValue(new EnumExpression(valueName, valueArgs));
			if (check(COMMA)) { advance(); }
		}
		
		if (previous().keyword != CURLY_R) {
			//set enum body
			s.setBody(getBlock());
		}
		
		return s;
	}
	
}
