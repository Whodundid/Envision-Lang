package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.lang.util.VisibilityType;
import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.EnumExpression;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.EnumStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Enum extends ParserStage {
	
	public static Statement enumDeclaration() { return enumDeclaration(new ParserDeclaration()); }
	public static Statement enumDeclaration(ParserDeclaration declaration) {
		Token name = consume(IDENTIFIER, "Expected an enum name!");
		
		if (declaration == null) { declaration = new ParserDeclaration(); }
		if (declaration.getVisibility() == null) { declaration.setVisibility(VisibilityType.SCOPE); }
		
		EnumStatement s = new EnumStatement(name, declaration);
		
		if (match(COLON)) {
			do {
				consume(IDENTIFIER, "Expected super enum name.");
				s.addSuper(new VarExpression(previous()));
			}
			while (match(COMMA));
		}
		
		//read in class body
		consume(SCOPE_LEFT, "Expected '{' after enum declaration!");
		
		//parse enum values
		while (!atEnd()) {
			while (match(NEWLINE));
			if (match(SEMICOLON, SCOPE_RIGHT)) { break; }
			
			Token valueName = consume(IDENTIFIER, "Expected an enum value name!");
			EArrayList<Expression> valueArgs = new EArrayList();
			
			if (match(EXPR_LEFT)) {
				if (!check(EXPR_RIGHT)) {
					do {
						valueArgs.add(expression());
					}
					while (match(COMMA));
					
					consume(EXPR_RIGHT, "Expected ')' after enum parameters!");
				}
			}
			
			s.addValue(new EnumExpression(valueName, valueArgs));
			if (check(COMMA)) { advance(); }
		}
		
		if (previous().keyword != SCOPE_RIGHT) {
			//set enum body
			s.setBody(getBlock());
		}
		
		return s;
	}
	
}
