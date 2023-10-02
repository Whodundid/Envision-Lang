package envision_lang.debug;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;

public class DebugParserPrinter {
	
    private DebugParserPrinter() {}
    
	public static void displayParsedStatements(EnvisionCodeFile codeFile) {
		displayParsedStatements(codeFile.getFileName(), codeFile.getStatements());
	}
	
	public static void displayParsedStatements(String fileName, EList<ParsedStatement> statements) {
		var out = new EStringBuilder("\n");
		out.println("'", fileName, "' Parsed Statements:");
		var lines = new EStringBuilder();
		int cur = 1;
		for (ParsedStatement s : statements) {
			lines.a('\t');
			lines.a(cur++);
			lines.a(". ");
			lines.a("\t");
			if (s instanceof Stmt_Expression expStmt)
				lines.a(expStmt.expression.getClass().getSimpleName());
			else
				lines.a(s.getClass().getSimpleName());
			lines.a(" : ");
			lines.a(s.toString());
			lines.a("\n");
		}
		out.print(lines.toString());
		out.println();
		
		System.out.println(out);
	}
	
}
