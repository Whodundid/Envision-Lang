package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.types.ImportExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.ImportStatement;
import envision.tokenizer.Token;
import eutil.strings.StringUtil;

public class PS_Import extends ParserStage {
	
	/**
	 * Parses an expression which will map to a specific package or file to be imported.
	 * If the import statement declares the 'as' keyword then the object or file being
	 * imported will be referenced as the following given token.
	 * @return ImportStatement
	 */
	public static Statement handleImport() {
		String m = "Expected a valid identifier for an import path name!";
		String path = consume(IDENTIFIER, m).lexeme;
		while (match(PERIOD)) {
			path += ".";
			path += consume(IDENTIFIER, m).lexeme;
		}
		
		String object = null;
		if (path.contains(".")) {
			int pos = StringUtil.findStartingIndex(path, ".", true);
			object = path.substring(pos + 1);
			path = path.substring(0, pos);
		}
		else {
			object = path;
			path = null;
		}
		
		Token asName = null;
		if (match(AS)) {
			asName = consume(IDENTIFIER, "Expected a valid identifier for which to map an import into!");
		}
		return new ImportStatement(new ImportExpression(path, object), asName);
	}
	
}
