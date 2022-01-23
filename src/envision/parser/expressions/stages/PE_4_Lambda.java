package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.LambdaExpression;

public class PE_4_Lambda extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_5_Term.run();
		
		while (match(LAMBDA)) {
			/*
			if (c instanceof CompoundExpression) {
				compound = (CompoundExpression) c;
			}
			else if (c instanceof GroupingExpression) {
				GroupingExpression g = (GroupingExpression) c;
				
				if (g.expression instanceof CompoundExpression) { compound = (CompoundExpression) g.expression; }
				else { compound.add(g.expression); }
			}
			else compound.add(c);
			*/
			e = new LambdaExpression(e, expression());
		}
		
		return e;
	}
	
}
