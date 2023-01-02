package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import eutil.datatypes.util.EList;

public class PS_Block extends ParserHead {
	
	public static EList<ParsedStatement> handleBlock() {
		EList<ParsedStatement> b = EList.newList();
		
		consume(CURLY_L, "Expected a '}' to start scope block!");
		
		do {
			ParsedStatement s = declaration();
			
			if (s != null) {
				//unpack blocks
				if (s instanceof Stmt_Block block) {
					for (ParsedStatement bs : block.statements) b.add(bs);
				}
				else b.add(s);
			}
		}
		while (!check(CURLY_R) && !atEnd());
		
		consume(CURLY_R, "Expected a '}' to close current block!");
		return b;
	}
	
}
