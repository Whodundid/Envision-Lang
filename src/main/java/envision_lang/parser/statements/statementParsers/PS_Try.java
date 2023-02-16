package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Catch;
import envision_lang.parser.statements.statement_types.Stmt_Try;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class PS_Try extends ParserHead {
	
	public static ParsedStatement tryStatement() {
		Token<?> tryToken = consume(TRY, "Expected 'try' here!");
		Token<?> blockStart = consume(CURLY_L, "Expected block start after try declaration!");
		Stmt_Block tryBlock = new Stmt_Block(blockStart, getBlock());
		
		EList<Stmt_Catch> catches = EList.newList();
		
		ignoreTerminators();
		if (check(CATCH)) {
			do {
				Token<?> catchToken = consume(CATCH, "Expected 'catch' statement block after try block!");
				consume(PAREN_L, "Expected catch expression start!");
								
				Token<?> type, name;
				
				type = consume(IDENTIFIER, "Expected an exception type!");
				
				if (check(IDENTIFIER)) {
					name = consume(IDENTIFIER, "Expected a variable to pair against exception type!");
				}
				else {
					name = type;
					type = null;
				}
				
				consume(PAREN_R, "Expected catch expression end!");
				consume(CURLY_L, "Expected block start after catch declaration!");
				catches.add(new Stmt_Catch(catchToken, type, name, getBlock()));
			}
			while (check(CATCH));
		}
		
		Stmt_Block finallyBlock = null;
		if (match(FINALLY)) {
			Token<?> blockStart2 = consume(CURLY_L, "Expected block start after finally declaration!");
			finallyBlock = new Stmt_Block(blockStart2, getBlock());
		}
		
		return new Stmt_Try(tryToken, tryBlock, catches, finallyBlock);
	}
	
}
