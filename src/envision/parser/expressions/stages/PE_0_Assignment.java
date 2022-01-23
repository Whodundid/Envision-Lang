package envision.parser.expressions.stages;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.AssignExpression;
import envision.parser.expressions.types.GetExpression;
import envision.parser.expressions.types.ListIndexExpression;
import envision.parser.expressions.types.ListIndexSetExpression;
import envision.parser.expressions.types.SetExpression;
import envision.parser.expressions.types.VarExpression;
import envision.tokenizer.Keyword.KeywordType;
import envision.tokenizer.Token;

public class PE_0_Assignment extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_1_Or.run();
		
		//System.out.println("cur assign: " + current());
		if (matchType(KeywordType.ASSIGNMENT)) {
			Token operator = previous();
			//System.out.println("the assignment: " + e + " : " + operator);
			Expression value = assignment();
			
			if (e instanceof VarExpression) {
				Token name = ((VarExpression) e).name;
				//parser.pd(name + " " + operator + " " + value);
				return new AssignExpression(name, operator, value);
			}
			//else if (e instanceof ModularExpression) {
			//	return new AssignExpression(null, operator, value, ((ModularExpression) e).modulars);
			//}
			else if (e instanceof GetExpression) {
				GetExpression get = (GetExpression) e;
				return new SetExpression(get.object, get.name, value);
			}
			else if (e instanceof ListIndexExpression) {
				ListIndexExpression lie = (ListIndexExpression) e;
				return new ListIndexSetExpression(lie, value);
			}
			
			setPrevious();
			error("'" + e + "' Invalid assignment target.");
		}
		
		return e;
	}
	
}
