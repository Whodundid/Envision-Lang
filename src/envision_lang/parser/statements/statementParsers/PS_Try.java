package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Catch;
import envision_lang.parser.statements.statement_types.Stmt_Try;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PS_Try extends GenericParser {
	
	public static Statement tryStatement() {
		consume(CURLY_L, "Expected block start after try declaration!");
		Stmt_Block tryBlock = new Stmt_Block(getBlock());
		
		EArrayList<Stmt_Catch> catches = new EArrayList();
		
		consumeEmptyLines();
		
		do {
			consume(CATCH, "Expected 'catch' statement block after try block!");
			
			consume(PAREN_L, "Expected catch expression start!");
			Token exceptionType = consume(IDENTIFIER, "Expected an exception type!");
			Token var = consume(IDENTIFIER, "Expected a variable to pair against exception type!");
			consume(PAREN_R, "Expected catch expression end!");
			
			consume(CURLY_L, "Expected block start after catch declaration!");
			catches.add(new Stmt_Catch(exceptionType, var, getBlock()));
		}
		while (check(CATCH));
		
		consumeEmptyLines();
		
		Stmt_Block finallyBlock = null;
		if (match(FINALLY)) {
			consumeEmptyLines();
			consume(CURLY_L, "Expected block start after finally declaration!");
			finallyBlock = new Stmt_Block(getBlock());
		}
		
		return new Stmt_Try(tryBlock, catches, finallyBlock);
	}
	
}
