package envision_lang._launch;

import java.io.File;
import java.io.IOException;

import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.workingDirectory.InvalidCodeFileError;
import envision_lang.lang.natives.Primitives;
import envision_lang.parser.EnvisionLangParser;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.tokenizer.Token;
import envision_lang.tokenizer.Tokenizer;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

/**
 * A special type of EnvisionObject which bundles the data composing a code
 * file along with its statement contents.
 * 
 * @author Hunter Bragg
 */
public class EnvisionCodeFile extends EnvisionObject {
	
	//========
	// Fields
	//========
	
	/** The name of the code file itself. */
	private final String fileName;
	/** The full qualified name of this code file. */
	private final String fullFileName;
	/** The physical File object pointing back to the actual file on the system. */
	private final File theFile;
	/** Each line of the file. */
	private EList<String> lines = EList.newList();
	/** The tokenized version of the file. */
	private EList<Token<?>> tokens = EList.newList();
	/** The tokenized version of each line. */
	private BoxList<Integer, EList<Token<?>>> lineTokens = new BoxList<>();
	/** The parsed Statements from the code file. */
	private EList<ParsedStatement> statements = EList.newList();
	/** True if this file is actually a proper Envision code file. */
	private final boolean isValid;
	/** True if this file has already been parsed by the Envision Parser. */
	private boolean isParsed = false;
	/** True if this file has been loaded by the Envision Interpreter. */
	private boolean isLoaded = false;
	/** True if this file is named 'main'. */
	private boolean isMain = false;
	/** True if this file has been successfully tokenized. */
	private boolean isTokenized = false;
	/** The Interpreter associated with this specific code file. */
	private IScope codeFileScope;
	/** The Tokenizer associated with this specific code file. */
	private Tokenizer tokenizer;
	/** The paired WorkingDirectory for this CodeFile. */
	private WorkingDirectory workingDir;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionCodeFile(String fileName) { this(new File(fileName)); }
	public EnvisionCodeFile(File in) {
		super(Primitives.CODE_FILE.toDatatype());
		theFile = in;
		
		if (isValid = checkFile()) {
			fullFileName = EStringUtil.subStringAfter(theFile.getPath().replace("\\", "."), ".").replace(".nvis", "");
		}
		else {
			fullFileName = "NO_NAME";
		}
		
		isMain = theFile.getName().toLowerCase().equals("main.nvis");
		fileName = theFile.getName().replace(".nvis", "");
	}
	
	/**
	 * Returns true if and only if the paired file is not null, actually exists, is not a folder,
	 * and finally is the correct '.nvis' file type.
	 * 
	 * @return true if the file is a valid Envision Code File
	 */
	private boolean checkFile() {
		if (theFile == null || !theFile.exists() || theFile.isDirectory()) return false;
		return theFile.getName().endsWith(".nvis");
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return fileName;
	}
	
	//---------
	// Methods
	//---------
	
	public void tokenizeFile() throws IOException {
		if (tokenizer == null) tokenizer = new Tokenizer(this);
		if (tokenizer.hasFile()) {
			tokenizer.tokenizeFile();
			lineTokens = tokenizer.getLineTokens();
			tokens = tokenizer.getTokens();
			lines = tokenizer.getLines();
			isTokenized = true;
		}
	}
	
	public void parseFile() throws Exception {
		statements = EnvisionLangParser.parse(this);
		isParsed = true;
	}
	
	/**
	 * Builds a new EnvisionInterpreter on this specific code file around the given working directory.
	 * The working directory cannot be null!
	 * 
	 * @param dir a valid working directory
	 * @return true if successfully loaded
	 * @throws Exception
	 */
	public boolean load(WorkingDirectory dir) throws Exception {
		isLoaded = false;
		
		//if not tokenized, attempt to tokenize
		if (!isTokenized) tokenizeFile();
		if (!isParsed) parseFile();
		
		//if (isLoaded) throw new InvalidCodeFileError(fileName, "Is already loaded and cannot be loaded again!");
		if (dir == null) throw new InvalidCodeFileError(fileName, "There is no paired working directory!");
		if (!isTokenized) throw new InvalidCodeFileError(fileName, "Has not been successfully tokenized!");
		if (!isParsed) throw new InvalidCodeFileError(fileName, "Has not been successfully parsed!");
		if (statements == null) throw new InvalidCodeFileError(fileName, "Has somehow been parsed but does not have a valid statement list!");
		
		//if (EnvisionLang.debugMode) debugOutput();
		
		//prep interpreter
		try {
			workingDir = dir;
			dir.getBuildPackages().forEach(p -> p.defineOn(codeFileScope));
			isLoaded = true;
		}
		catch (EnvisionLangError e) {
			e.printStackTrace();
		}
		
		return isLoaded;
	}
	
	//=========
	// Getters
	//=========
	
	public EnvisionObject getValue(String identifier) {
		return (codeFileScope != null) ? codeFileScope.get(identifier) : null;
	}
	
	/** Returns true if this code file is actually valid and can be executed. */
	public boolean isValid() { return isValid; }
	public boolean isMain() { return isMain; }
	public boolean isLoaded() { return isLoaded; }
	public boolean isTokenized() { return isTokenized; }
	public boolean isParsed() { return isParsed; }
	
	public String getFileName() { return fileName; }
	public String getFullFileName() { return fullFileName; }
	public File getSystemFile() { return theFile; }
	public EList<String> getLines() { return lines; }
	public EList<Token<?>> getTokens() { return tokens; }
	public BoxList<Integer, EList<Token<?>>> getLineTokens() { return lineTokens; }
	public EList<ParsedStatement> getStatements() { return statements; }
	public WorkingDirectory getWorkingDir() { return workingDir; }
	
	/** Returns the scope of this code file's interpreter scope. */
	public IScope scope() { return codeFileScope; }
	
	//-----------------------------------------------------
	
	private void debugOutput() {
		String s = "   ", ss = s + s, sss = ss + s;
		System.out.println(this);
		System.out.println("     Lines"); lines.forEach(o -> System.out.println("          " + o)); System.out.println();
		System.out.println("     tokens"); tokens.forEach(o -> System.out.println("          " + o)); System.out.println();
		System.out.println(s + "statements"); statements.forEach(o -> System.out.println(ss + o + "\n" + sss + o.getClass().getSimpleName())); System.out.println();
	}
	
	public void displayTokens() throws IOException {
		//if not tokenized, attempt to tokenize
		if (!isTokenized) tokenizeFile();
		
		var sb = new EStringBuilder();
		sb.println("'" + getFileName() + "' Tokens:");
		for (int i = 0; i < lineTokens.size(); i++) {
			var line = lineTokens.get(i);
			var toPrint = new EStringBuilder(line.getA());
			toPrint.append("\t");
			for (var t : line.getB()) toPrint.append(t.toString()).append(" ");
			if (lines.isNotEmpty()) toPrint.deleteCharAt(toPrint.length() - 1);
			if (i < lineTokens.size() - 1) sb.println(toPrint);
			else sb.print(toPrint);
		}
		sb.println();
		
		System.out.println(sb);
	}
	
	/**
	 * Prints out this file's tokens with formatted metadata.
	 * 
	 * @throws IOException
	 */
	public void displayTokensInDepth() throws IOException {
		//if not tokenized, attempt to tokenize
		if (!isTokenized) tokenizeFile();
		
		class Longest {
			String longestToken() { return EStringUtil.getLongest(tokens); }
			String longestKeyword() { return EStringUtil.getLongest(tokens.map(o -> o.getKeyword())); }
			String longestLexeme() { return EStringUtil.getLongest(tokens.map(o -> o.getLexeme())); }
			String longestLiteral() { return EStringUtil.getLongest(tokens.map(o -> o.getLiteral())); }
			String longestLineNum() { return EStringUtil.getLongest(tokens.map(o -> o.getLineNum())); }
			String longestLineIndex() { return EStringUtil.getLongest(tokens.map(o -> o.getLineIndex())); }
			String longestLineTokenIndex() { return EStringUtil.getLongest(tokens.map(o -> o.getLineTokenIndex())); }
		}
		
		var TLen = new Longest();
		
		var sb = new EStringBuilder();
		sb.println("'" + getFileName() + "' Tokens:");
		
		int longestIndex = EStringUtil.getLongestLength("  i=" + String.valueOf(tokens.size()), "Index");
		int longestLine = EStringUtil.getLongestLength(TLen.longestLineNum(), "Line");
		int longestToken = EStringUtil.getLongestLength(TLen.longestToken(), "Token");
		int longestKeyword = EStringUtil.getLongestLength(TLen.longestKeyword(), "Keyword");
		int longestLexeme = EStringUtil.getLongestLength(TLen.longestLexeme(), "Lexeme");
		int longestLiteral = EStringUtil.getLongestLength(TLen.longestLiteral(), "Literal");
		int longestTokenIndex = EStringUtil.getLongestLength(TLen.longestLineIndex(), "Line Index");
		int longestLineTokenIndex = EStringUtil.getLongestLength(TLen.longestLineTokenIndex(), "Line Token Index");
		
		String indexFormat = "%-" + longestIndex + "s  | ";
		String lineFormat = "%-" + longestLine + "s | ";
		String tokenFormat = "%-" + longestToken + "s | ";
		String keywordFormat = "%-" + longestKeyword + "s | ";
		String lexemeFormat = "%-" + longestLexeme + "s | ";
		String literalFormat = "%-" + longestLiteral + "s | ";
		String lineIndexFormat = "%-" + longestTokenIndex + "s | ";
		String lineTokenIndexFormat = "%-" + longestLineTokenIndex + "s";
		
		String hIndex = String.format("\t| " + indexFormat, "Index");
		String hLine = String.format(lineFormat, "Line");
		String hToken = String.format(tokenFormat, "Token");
		String hKeyword = String.format(keywordFormat, "Keyword");
		String hLexeme = String.format(lexemeFormat, "Lexeme");
		String hLiteral = String.format(literalFormat, "Literal");
		String hLineIndex = String.format(lineIndexFormat, "Line Index");
		String hLineTokenIndex = String.format(lineTokenIndexFormat, "Line Token Index") + " |";
		
		var header = new EStringBuilder();
		header.a(hIndex, hLine, hToken, hKeyword, hLexeme, hLiteral, hLineIndex, hLineTokenIndex);
		var dashes = "\t|" + EStringUtil.repeatString("-", header.length() - 3) + "|";
		sb.println(dashes);
		sb.println(header);
		sb.println(dashes);
		
		int lastLineNum = 1;
		
		for (int i = 0; i < tokens.size(); i++) {
			var token = tokens.get(i);
			
			if (lastLineNum != token.getLineNum()) {
				lastLineNum = token.getLineNum();
				//sb.println(dashes);
			}
			
			String fIndex = String.format("\t| " + indexFormat, i);
			String fLine = String.format(lineFormat, token.getLineNum());
			String fToken = String.format(tokenFormat, token);
			String fKeyword = String.format(keywordFormat, token.getKeyword());
			String fLexeme = String.format(lexemeFormat, token.getLexeme());
			String fLiteral = String.format(literalFormat, token.getLiteral());
			String fLineIndex = String.format(lineIndexFormat, token.getLineIndex());
			String fLineTokenIndex = String.format(lineTokenIndexFormat, token.getLineTokenIndex()) + " |";
			
			sb.println(fIndex, fLine, fToken, fKeyword, fLexeme, fLiteral, fLineIndex, fLineTokenIndex);
		}
		
		sb.println(dashes);
		
		System.out.println(sb);
	}
	
	public void displayParsedStatements() throws Exception {
		//if not tokenized, attempt to tokenize
		if (!isTokenized) tokenizeFile();
		//if not parsed, attempt to parse
		if (!isParsed) parseFile();
		
		var out = new EStringBuilder("\n");
		out.println("'" + getFileName() + "' Parsed Statements:");
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
