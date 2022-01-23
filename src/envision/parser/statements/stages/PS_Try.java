package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.types.BlockStatement;
import envision.parser.statements.types.CatchStatement;
import envision.parser.statements.types.TryStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Try extends ParserStage {
	
	public static Statement tryStatement() {
		consume(Keyword.SCOPE_LEFT, "Expected block start after try declaration!");
		BlockStatement tryBlock = new BlockStatement(getBlock());
		
		EArrayList<CatchStatement> catches = new EArrayList();
		
		while (match(NEWLINE));
		
		do {
			consume(Keyword.CATCH, "Expected 'catch' statement block after try block!");
			
			consume(Keyword.EXPR_LEFT, "Expected catch expression start!");
			Token exceptionType = consume(IDENTIFIER, "Expected an exception type!");
			Token var = consume(IDENTIFIER, "Expected a variable to pair against exception type!");
			consume(Keyword.EXPR_RIGHT, "Expected catch expression end!");
			
			consume(Keyword.SCOPE_LEFT, "Expected block start after catch declaration!");
			catches.add(new CatchStatement(exceptionType, var, getBlock()));
		}
		while (check(CATCH));
		
		while (match(NEWLINE));
		
		BlockStatement finallyBlock = null;
		if (match(Keyword.FINALLY)) {
			while (match(NEWLINE));
			consume(Keyword.SCOPE_LEFT, "Expected block start after finally declaration!");
			finallyBlock = new BlockStatement(getBlock());
		}
		
		return new TryStatement(tryBlock, catches, finallyBlock);
	}
	
}
