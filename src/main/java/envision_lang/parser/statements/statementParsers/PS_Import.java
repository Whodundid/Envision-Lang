package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.tokenizer.Token;
import eutil.strings.StringUtil;

public class PS_Import extends GenericParser {
	
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
		return new Stmt_Import(new Expr_Import(path, object), asName);
	}
	
}