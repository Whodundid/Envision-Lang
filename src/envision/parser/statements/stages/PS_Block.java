package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.BlockStatement;
import eutil.datatypes.EArrayList;

public class PS_Block extends ParserStage {
	
	public static EArrayList<Statement> handleBlock() { return handleBlock(new ParserDeclaration()); }
	public static EArrayList<Statement> handleBlock(ParserDeclaration declaration) {
		//parser.pd("\n------------------------------------Entered Handle Block--------------------------------------------\n");
		
		EArrayList<Statement> b = new EArrayList();
		ParserDeclaration last = declaration;
		//parser.pd("Last block: " + last);
		
		consume(SCOPE_LEFT, "Expected a '}' to start scope block!");
		
		//parser.pd("\n        CURRENT: " + current().lexeme);
		//parser.pd("        The dec: " + last + "\n");
		
		do {
			Statement s = declaration(new ParserDeclaration(last));
			
			if (s != null) {
				//unpack blocks
				if (s instanceof BlockStatement) {
					for (Statement bs : ((BlockStatement) s).statements) {
						b.add(bs);
					}
				}
				else {
					//parser.pd("\n                 Statement dec: " + s.getDeclaration());
					//parser.pd("                 Current: '" + current().lexeme + "'");
					b.add(s);
					//parser.pd("                 Adding Statement: " + s + "\n");
				}
			}
		}
		while (!check(SCOPE_RIGHT) && !atEnd());
		
		consume(SCOPE_RIGHT, "Expected a '}' to close current block!");
		return b;
	}
	
}
