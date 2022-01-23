package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.DeclarationStage;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.BlockStatement;
import envision.tokenizer.Token;

public class PS_Type extends ParserStage {
	
	public static Statement handleType() { return handleType(new ParserDeclaration()); }
	public static Statement handleType(ParserDeclaration declaration) {
		declaration = (declaration != null) ? declaration : new ParserDeclaration().setStage(DeclarationStage.GENERICS);
		errorIf(checkNext(SEMICOLON, NEWLINE, EOF), "Incomplete statement declaration!");
		
		//check if expression
		if (checkNext(EXPR_LEFT, PERIOD, BRACKET_LEFT, OPERATOR_EXPR)) {
			//first check if this could potentially be a constructor
			if (ParserStage.curClassName != null && check(IDENTIFIER) && current().lexeme.equals(ParserStage.curClassName.lexeme)) {
				return methodDeclaration(false, declaration);
			}
			//otherwise parse an expression
			return expressionStatement();
		}
		if (checkNextType(ASSIGNMENT) && declaration.getMods().isEmpty()) { return expressionStatement(); }
		
		//Determine the type of the given token
		Token type = null;
		
		if (check(IDENTIFIER)) { type = consume(IDENTIFIER, "Expected a return type!"); }
		if (check(THIS)) { type = consume(THIS, "Expected a return type!"); }
		if (checkType(DATATYPE)) { type = consumeType(DATATYPE, "Expected a return type!"); }
		
		errorIf(type == null, "Expected a non null statement type!");
		declaration.setReturnType(type);
		
		//check for variable parameters
		if (check(LESS_THAN)) {
			for (Token t : getParameters(type)) declaration.addParameter(t);
		}
		
		declaration.setStage(DeclarationStage.TYPE);
		
		//determine next
		if (check(EXPR_RIGHT)) { return expressionStatement(); }
		if (check(SCOPE_LEFT)) { return new BlockStatement(handleBlock(declaration)); }
		if (match(OPERATOR_)) { return methodDeclaration(true, declaration); }
		if (check(LESS_THAN, COMMA, SEMICOLON, NEWLINE, EOF) || checkType(ASSIGNMENT) || checkType(OPERATOR)) {
			return varDeclaration(null, declaration);
		}
		
		return methodDeclaration(declaration);
	}
	
}
