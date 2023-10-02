package envision_lang._launch;

import java.io.File;

import envision_lang.lang.language_errors.error_types.workingDirectory.InvalidCodeFileError;
import envision_lang.lang.language_errors.error_types.workingDirectory.MultipleMainsError;
import envision_lang.lang.packages.EnvisionLangPackage;
import eutil.EUtil;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

/** Handles code file discovery and wrapping. */
public class WorkingDirectory {
	
	/** The program's top level directory. */
	private final File dir;
	/** True if the given directory is not null and actually exists. */
	private final boolean isValid;
	/** All successfully parsed code files. */
	private final EList<EnvisionCodeFile> codeFiles = EList.newList();
	/** The main code file. */
	private EnvisionCodeFile main = null;
	/** Packages to be added to the interpreters at run time. */
	private EList<EnvisionLangPackage> packages = EList.newList();
	
	//--------------
	// Constructors
	//--------------
	
	/** Creates a new WorkingDirectory from this directory. */
	public WorkingDirectory() { this(new File(System.getProperty("user.dir"))); }
	/** Creates a new WorkingDirectory from the given string file path. */
	public WorkingDirectory(String in) { this(new File(in)); }
	
	/**
	 * Creates a new WorkingDirectory from the given file. If the file is not a
	 * directory, then the parent directory will be used instead.
	 */
	public WorkingDirectory(File in) {
		isValid = EUtil.fileExists(in);
		if (!in.isDirectory()) in = in.getParentFile();
		dir = in;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Searches through top and child directories for envision code files.
	 */
	public void discoverFiles() {
		if (!dir.isDirectory()) {
			wrapFile(dir);
			return;
		}
		
		EList<File> start = EList.newList(dir.listFiles());
		EList<File> found = EList.newList();
		EList<File> directories = EList.newList();
		EList<File> workList = EList.newList();
		
		//add all envision code files to be found
		found.addAll(start.filter(f -> f.getName().endsWith(".nvis")));
		//gather all directories from the top level directory
		directories.addAll(start.filter(f -> !f.getName().endsWith(".nvis")));
		//load the work list with every file found on each directory
		directories.filterForEach(f -> f.list() != null, f -> workList.addA(f.listFiles()));
		
		while (workList.isNotEmpty()) {
			found.addAll(workList.filter(f -> f.getName().endsWith(".nvis")));
			
			directories.clear();
			workList.filterForEach(f -> f.list() != null, directories::add);
			
			workList.clear();
			directories.forEach(f -> workList.addA(f.listFiles()));
		}
		
		//wrap each found file within an EnvisionCodeFile object
		for (File f : found) wrapFile(f);
	}
	
	/**
	 * Wraps the given file into an EnvisionCodeFile.
	 */
	private void wrapFile(File in) {
		EnvisionCodeFile codeFile = new EnvisionCodeFile(in);
		if (!codeFile.isValid()) throw new InvalidCodeFileError(codeFile);
		if (codeFile.isMain()) {
			//if there is already a main, throw an error
			if (main != null) throw new MultipleMainsError(this);
			main = codeFile;
		}
		codeFiles.add(codeFile);
	}
	
	/**
	 * Attempts to return a code file of the given name (if it exists).
	 */
	@Broken(reason="Does not account for nested files!")
	public EnvisionCodeFile getFile(String name) {
		for (EnvisionCodeFile f : codeFiles) {
			if (f.getFileName().equals(name)) return f;
		}
		return null;
	}
	
	public WorkingDirectory addBuildPackage(EnvisionLangPackage pIn) {
		packages.add(pIn);
		return this;
	}
	
	/**
	 * This will print out the tokenized version of each code file without actually
	 * executing any code.
	 */
	public void debugTokenize() {
		for (EnvisionCodeFile f : codeFiles) f.displayTokens();
	}
	
	/**
	 * This will print out the tokenized version of each code file without actually
	 * executing any code. This will also show the metadata of each token.
	 */
	public void debugTokenizeInDepth() {
		for (EnvisionCodeFile f : codeFiles) f.displayTokensInDepth();
	}
	
	/**
	 * This will print out the parsed statements of each code file without actually
	 * executing any code.
	 */
	public void debugParsedStatements() throws Exception {
		for (EnvisionCodeFile f : codeFiles) f.displayParsedStatements();
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns true if this WorkingDirectory's parent file is not null and actually exists. */
	public boolean isValid() { return isValid; }
	/** Returns this WorkingDirectory's parent file. */
	public File getDirFile() { return dir; }
	/** Returns the main code file (if there is one) from the parsed directory. */
	public EnvisionCodeFile getMain() { return main; }
	/** Returns all parsed Envision code files from the given directory. */
	public EList<EnvisionCodeFile> getCodeFiles() { return codeFiles; }
	/** Returns all packages to be added at program start. */
	public EList<EnvisionLangPackage> getBuildPackages() { return packages; }
	
}
