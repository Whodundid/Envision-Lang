package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;

import envision.parser.GenericParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.BlockStatement;
import eutil.datatypes.EArrayList;

public class PS_Block extends GenericParser {
	
	public static EArrayList<Statement> handleBlock() {
		EArrayList<Statement> b = new EArrayList();
		
		consume(CURLY_L, "Expected a '}' to start scope block!");
		
		do {
			Statement s = declaration();
			
			if (s != null) {
				//unpack blocks
				if (s instanceof BlockStatement block) {
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
