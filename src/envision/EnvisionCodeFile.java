package envision;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.workingDirectory.InvalidCodeFileError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.Primitives;
import envision.parser.EnvisionParser;
import envision.parser.statements.Statement;
import envision.tokenizer.Token;
import envision.tokenizer.Tokenizer;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

import java.io.File;
import java.io.IOException;

/** A special type of EnvisionObject which bundles the data comprising a code file along with its statement contents. */
public class EnvisionCodeFile extends EnvisionObject {
	
	/** The name of the code file itself. */
	private final String fileName;
	/** The physical File object pointing back to the actual file on the system. */
	private final File theFile;
	/** Each line of the file. */
	private EArrayList<String> lines = new EArrayList();
	/** The tokenized version of the file. */
	private EArrayList<Token> tokens = new EArrayList();
	/** The tokenized version of each line. */
	private EArrayList<EArrayList<Token>> lineTokens = new EArrayList();
	/** The parsed Statements from the code file. */
	private EArrayList<Statement> statements = new EArrayList();
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
	private EnvisionInterpreter interpreter;
	/** The Tokenizer associated with this specific code file. */
	private Tokenizer tokenizer;
	/** The paired WorkingDirectory for this CodeFile. */
	private WorkingDirectory workingDir;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionCodeFile(String fileName) { this(new File(fileName)); }
	public EnvisionCodeFile(File in) {
		super(Primitives.CODE_FILE);
		theFile = in;
		
		if (isValid = checkFile()) {
			fileName = StringUtil.subStringAfter(theFile.getPath().replace("\\", "."), ".").replace(".nvis", "");
		}
		else {
			fileName = "NO_NAME";
		}
		
		setName(fileName);
		
		isMain = theFile.getName().toLowerCase().equals("main.nvis");
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
		statements = EnvisionParser.parse(this);
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
		
		if (Envision.debugMode) debugOutput();
		
		//prep interpreter
		try {
			workingDir = dir;
			interpreter = new EnvisionInterpreter(this);
			dir.getBuildPackages().forEach(p -> p.defineOn(interpreter));
			isLoaded = true;
		}
		catch (EnvisionError e) {
			e.printStackTrace();
		}
		
		return isLoaded;
	}
	
	public void execute() throws Exception {
		execute(new EArrayList<String>());
	}
	
	public void execute(EArrayList<String> programArgs) throws Exception {
		if (isLoaded) interpreter.interpret(workingDir);
	}
	
	//---------
	// Getters
	//---------
	
	public EnvisionObject getValue(String identifier) {
		if (interpreter != null) return interpreter.scope().get(identifier);
		return null;
	}
	
	/** Returns true if this code file is actually valid and can be executed. */
	public boolean isValid() { return isValid; }
	public boolean isMain() { return isMain; }
	public boolean isLoaded() { return isLoaded; }
	public boolean isTokenized() { return isTokenized; }
	public boolean isParsed() { return isParsed; }
	
	public String getFileName() { return fileName; }
	public File getSystemFile() { return theFile; }
	public EArrayList<String> getLines() { return lines; }
	public EArrayList<Token> getTokens() { return tokens; }
	public EArrayList<EArrayList<Token>> getLineTokens() { return lineTokens; }
	public EArrayList<Statement> getStatements() { return statements; }
	public EnvisionInterpreter getInterpreter() { return interpreter; }
	
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
		
		System.out.println("'" + getFileName() + "' Tokens:");
		int i = 1;
		for (EArrayList<Token> lines : lineTokens) {
			StringBuilder line = new StringBuilder(String.valueOf(i++));
			line.append("\t");
			for (Token t : lines) line.append(t.toString()).append(" ");
			if (lines.isNotEmpty()) line.deleteCharAt(line.length() - 1);
			System.out.println(line);
		}
		System.out.println();
	}
	
	public void displayParsedStatements() throws Exception {
		//if not tokenized, attempt to tokenize
		if (!isTokenized) tokenizeFile();
		//if not parsed, attempt to parse
		if (!isParsed) parseFile();
		
		System.out.println("'" + getFileName() + "' Parsed Statements:");
		StringBuilder lines = new StringBuilder();
		int cur = 1;
		for (Statement s : statements) {
			lines.append(cur++);
			lines.append(". ");
			lines.append("\t");
			lines.append(s.toString());
			lines.append("\n");
		}
		System.out.println(lines.toString());
		System.out.println();
	}
	
}
