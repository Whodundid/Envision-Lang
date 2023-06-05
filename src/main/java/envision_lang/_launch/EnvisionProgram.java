package envision_lang._launch;

import java.io.File;
import java.io.IOException;

import envision_lang.lang.java.EnvisionJavaObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.workingDirectory.BadDirError;
import envision_lang.lang.packages.EnvisionLangPackage;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;

/**
 * A portable, abstract representation of a program that can be run within
 * the Envision Scripting Language.
 * 
 * @author Hunter Bragg
 */
public class EnvisionProgram {
	
	//--------
	// Fields
	//--------
	
	/** The physical location of this program on the file system. */
	private File programDir;
	/** The location of the main file to be used. */
	private File mainFile;
	/** The Envision Scripting Language's active program working directory. */
	private WorkingDirectory dir;
	/** The settings to run the Envision Scripting Language with. */
	private EnvisionEnvironmnetSettings settings;
	
	private EList<EnvisionLangPackage> bundledProgramPackages = EList.newList();
	private EList<EnvisionJavaObject> envisionJavaObjects = EList.newList();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionProgram() {
		try {
			File.createTempFile("", "dir");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new EnvisionProgram with the given program name.
	 * <p>
	 * Note: a default program directory will be created under the active Java
	 * program dir using the given program name and a default main file will be
	 * created within said directory.
	 * 
	 * @param programNameIn The name for the program
	 */
	public EnvisionProgram(String programNameIn) {
		//use Java program dir as default dir path
		this(new File(System.getProperty("user.dir"), programNameIn));
	}
	
	/**
	 * Creates a new EnvisionProgram with the given program name within the
	 * given base directory. Note: a default main file will be created if one
	 * does not already exist under the given program directory.
	 * 
	 * @param programNameIn The name for the program
	 * @param baseDirectory The directory to create the program within
	 */
	public EnvisionProgram(String programNameIn, File baseDirectory) {
		this((baseDirectory != null) ? new File(baseDirectory, programNameIn) :
									   new File(System.getProperty("user.dir"), programNameIn));
	}
	
	/**
	 * Creates a new EnvisionProgram with the given program name which
	 * physically is/will be located at the specified File path.
	 * <p>
	 * Note: a default main file will be created if one does not already exist
	 * within the given directory.
	 * 
	 * @param programDirIn The directory to use for the program
	 */
	public EnvisionProgram(File programDirIn) {
		this(programDirIn, EList.newList());
	}
	
	public EnvisionProgram(File programDirIn, EList<EnvisionLangPackage> buildPackages) {
		programDir = programDirIn;
		bundledProgramPackages.addAll(buildPackages);
		
		//if null -- error on invalid program path
		if (programDir == null) throw new EnvisionLangError("Invalid Envision program path!");
		
		//assign default program dir and main path to Java's working dir
		if (!EFileUtil.fileExists(programDir)) createDir();
		
		//wrap the program dir and parse
		dir = new WorkingDirectory(programDir);
		dir.discoverFiles();
		
		//create default main if one is not found within the program dir
		if (dir.getMain() == null) {
			mainFile = new File(programDir, "main.nvis");
			try {
				mainFile.createNewFile();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		//attempt to check if the working dir is actually valid
		if (!dir.isValid()) throw new BadDirError(dir);
	}
	
	//=========
	// Methods
	//=========
	
	public void addJavaObjectToProgram(EnvisionJavaObject object) {
		envisionJavaObjects.add(object);
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void createDir() {
		try {
			if (!EFileUtil.fileExists(programDir) && !programDir.mkdirs())
				throw new RuntimeException("Could not create script dir!");
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * @return The Java File path of this script's main.
	 */
	public File getMainFile() { return mainFile; }
	
	/**
	 * Returns this script's main file as an EnvisionCodeFile. Note: The script
	 * must be built in order for this to not return null.
	 */
	public EnvisionCodeFile getMainCodeFile() {
		return (dir != null) ? dir.getMain() : null;
	}
	
	/**
	 * Returns this script's working directory. If this script has not been
	 * built, null is returned instead.
	 * 
	 * @return The working directory
	 */
	public WorkingDirectory getWorkingDir() { return dir; }
	
	/**
	 * Returns the set of launch arguments for which to run the Envision
	 * Scripting Language with.
	 * 
	 * @return The launch settings for this program
	 */
	public EnvisionEnvironmnetSettings getLaunchArgs() { return settings; }
	
	public EList<EnvisionLangPackage> getBundledPackages() { return bundledProgramPackages; }
	public EList<EnvisionJavaObject> getEnvisionJavaObjects() { return envisionJavaObjects; }
	
	//---------
	// Setters
	//---------
	
	/**
	 * Sets the launch arguments to run the Envision Scripting Language with.
	 * 
	 * @param settingsIn The settings to run with this program
	 */
	public void setLaunchArgs(EnvisionEnvironmnetSettings settingsIn) {
		settings = settingsIn;
	}
	
}
