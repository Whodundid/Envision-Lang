package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.parser.ParserStage;
import envision.parser.expressions.types.GenericExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.DeclarationStage;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;

public class PS_Generics extends ParserStage {
	
	public static Statement handleGenerics() { return handleGenerics(new ParserDeclaration()); }
	public static Statement handleGenerics(ParserDeclaration declaration) {
		declaration = (declaration != null) ? declaration : new ParserDeclaration().setStage(DeclarationStage.DATAMODS);
		
		//EArrayList<GenericExpression> generics = new EArrayList();
		
		if (check(Keyword.LESS_THAN)) {
			consume(Keyword.LESS_THAN, "Expceted '<' for generic declaration start!");
			if (!check(Keyword.GREATER_THAN)) {
				do {
					Token generic = consume(IDENTIFIER, "Expected generic type!");
					Token extension = null;
					if (match(Keyword.COLON)) {
						extension = getAdvance();
					}
					declaration.addGeneric(new GenericExpression(generic, extension));
				}
				while (match(COMMA));
			}
		}
		
		declaration.setStage(DeclarationStage.GENERICS);
		
		//check for appropriate continuing statement
		if (check(INIT)) return PS_Method.methodDeclaration(false, declaration);
		if (match(FUNCTION)) return PS_Method.methodDeclaration(false, declaration);
		if (match(OPERATOR_)) { return PS_Method.methodDeclaration(true, declaration); }
		//if (match(PACKAGE)) { }
		if (match(CLASS)) { return PS_Class.classDeclaration(declaration); }
		if (check(IDENTIFIER, THIS) || checkType(DATATYPE)) { return PS_Type.handleType(declaration); }
		
		System.out.println(current().line);
		error("Invalid statement declaration!");
		return null;
	}
	
}
