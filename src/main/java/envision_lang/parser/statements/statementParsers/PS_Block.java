package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import eutil.datatypes.EArrayList;

public class PS_Block extends GenericParser {
	
	public static EArrayList<Statement> handleBlock() {
		EArrayList<Statement> b = new EArrayList<>();
		
		consume(CURLY_L, "Expected a '}' to start scope block!");
		
		do {
			Statement s = declaration();
			
			if (s != null) {
				//unpack blocks
				if (s instanceof Stmt_Block block) {
					for (Statement bs : block.statements) b.add(bs);
				}
				else b.add(s);
			}
		}
		while (!check(CURLY_R) && !atEnd());
		
		consume(CURLY_R, "Expected a '}' to close current block!");
		return b;
	}
	
}
