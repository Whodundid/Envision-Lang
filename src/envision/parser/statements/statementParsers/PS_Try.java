package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.BlockStatement;
import envision.parser.statements.statement_types.CatchStatement;
import envision.parser.statements.statement_types.TryStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Try extends GenericParser {
	
	public static Statement tryStatement() {
		consume(CURLY_L, "Expected block start after try declaration!");
		BlockStatement tryBlock = new BlockStatement(getBlock());
		
		EArrayList<CatchStatement> catches = new EArrayList();
		
		consumeEmptyLines();
		
		do {
			consume(CATCH, "Expected 'catch' statement block after try block!");
			
			consume(PAREN_L, "Expected catch expression start!");
			Token exceptionType = consume(IDENTIFIER, "Expected an exception type!");
			Token var = consume(IDENTIFIER, "Expected a variable to pair against exception type!");
			consume(PAREN_R, "Expected catch expression end!");
			
			consume(CURLY_L, "Expected block start after catch declaration!");
			catches.add(new CatchStatement(exceptionType, var, getBlock()));
		}
		while (check(CATCH));
		
		consumeEmptyLines();
		
		BlockStatement finallyBlock = null;
		if (match(FINALLY)) {
			consumeEmptyLines();
			consume(CURLY_L, "Expected block start after finally declaration!");
			finallyBlock = new BlockStatement(getBlock());
		}
		
		return new TryStatement(tryBlock, catches, finallyBlock);
	}
	
}
