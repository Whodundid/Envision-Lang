package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.tokenizer.Token;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class PS_Import extends ParserHead {
	
	/**
	 * Parses an expression which will map to a specific package or file to be
	 * imported. If the import statement declares the 'as' keyword then the
	 * object or file being imported will be referenced as the following given
	 * token.
	 * 
	 * @return ImportStatement
	 */
	public static ParsedStatement handleImport() {
		Token<?> importToken = consume(IMPORT, "Expected 'import' here!");
		String m = "Expected a valid identifier for an import path name!";
		boolean all = false;
		
		Token<?> pathStart = consume(IDENTIFIER, m);
		
		EStringBuilder path = new EStringBuilder(pathStart.getLexeme());
		while (match(PERIOD)) {
			path.append(".");
			
			if (match(MUL)) {
				path.append("*");
				all = true;
				break;
			}
			else {
				path.append(consume(IDENTIFIER, m).getLexeme());
			}
		}
		
		String object = null;
		if (path.contains(".")) {
			int pos = EStringUtil.findStartingIndex(path.toString(), ".", true);
			object = path.substring(pos + 1);
			path.setSubstring(0, pos);
		}
		
		Token<?> pathToken = Token.create(path.toString(), pathStart.getLineNum());
		Token<?> objectToken = Token.create(object, pathStart.getLineNum());
		
		Token<?> asName = null;
		if (match(AS)) {
			asName = consume(IDENTIFIER, "Expected a valid identifier for which to map an import into!");
		}
		
		return new Stmt_Import(importToken, new Expr_Import(pathToken, objectToken), asName, all);
	}
	
}
