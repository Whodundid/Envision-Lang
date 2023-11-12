package envision_lang._launch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision_lang._launch.EnvisionEnvironmnetSettings.EnvironmentSetting;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.java.NativeDatatypeMapper;
import envision_lang.lang.java.NativeFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.UndefinedFunctionError;
import envision_lang.lang.language_errors.error_types.workingDirectory.BadDirError;
import envision_lang.lang.packages.EnvisionLangPackage;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;
import eutil.debug.PlannedForRefactor;
import eutil.file.EFileUtil;

/**
 * A portable, abstract representation of a program that can be run within
 * the Envision Scripting Language.
 * 
 * @author Hunter Bragg
 */
public class EnvisionProgram {
    
    //========
    // Fields
    //========
    
    /** Logger instance for the EnvisionProgram class. */
    public final Logger logger = LoggerFactory.getLogger(EnvisionProgram.class);
    
    /** The name of this program. */
    private String programName;
    /** The physical location of this program on the file system. */
    private File programDir;
    /** The location of the main file to be used. */
    private File mainFile;
    /** The Envision Scripting Language's active program working directory. */
    private WorkingDirectory dir;
    /** The settings to run the Envision Scripting Language with. */
    private EnvisionEnvironmnetSettings settings;
    /** The main code file where program execution will start by default. */
    private EnvisionCodeFile mainCodeFile;
    /** The extracted scope of the main code file. */
    private IScope mainFileScope;
    /** Packages to be loaded upon program compilation. */
    private EList<EnvisionLangPackage> bundledProgramPackages = EList.newList();
    /**
     * Objects in Java to wrap and translate directly into Envision upon
     * program start.
     */
    private BoxList<String, Object> javaObjectsToWrap = BoxList.newList();
    
    /**
     * A handler which is referenced when an EnvisionError is thrown.
     */
    private EnvisionLangErrorCallBack errorCallback = null;
    
    /**
     * A receiver for all Envision code outputs which would normally be
     * intended for some form of standard console output.
     * <p>
     * Note: if this receiver is null, the default Java Out PrintStream will be
     * used instead.
     */
    @PlannedForRefactor(reason = "Should not be static!")
    private EnvisionConsoleOutputReceiver consoleReceiver = null;
    
    /**
     * Due to the nature of how Java loads programs on a per-class basis, the
     * simple act of class loading will significantly slow down overall
     * performance on start-up runs. This boolean flag is intended to improve
     * the Envision Language's program runtime consistency by requiring all
     * Envision java code files to be fully loaded into the Java Class Loader
     * before any Envision code is executed. It should be noted that enabling
     * this setting will significantly increase the amount of time required
     * before any Envision code actually starts executing as the language now
     * needs to be loaded in full. The trade-off, however, is that any Envision
     * code that is executed after the language is loaded should execute much
     * more smoothly and responsively.
     */
    @Broken
    private boolean preloadLanguage = false;
    
    /**
     * If enabled, bundled program code files will be tokenized and displayed
     * in the order they are read.
     */
    private boolean tokenize = false;
    
    /**
     * If enabled, bundled program code files will be tokenized and displayed
     * in the order they are read along with each token's full metadata as
     * well.
     */
    private boolean tokenizeInDepth = false;
    
    /**
     * If enabled, bundled program code files will be tokenized and then
     * attempt to be parsed into logical Envision statements and displayed.
     */
    private boolean parseStatements = false;
    
    /**
     * If disabled, no program code will actually be executed at run time. This
     * flag is primarily used for debugging and testing.
     */
    private boolean executeCode = true;
    
    /**
     * Enables the ability for commands to 'block' script execution until
     * manually called for.
     */
    private boolean enableBlockingStatements = false;
    
    /**
     * Enables the ability to append a '#' at the beginning of a statement
     * declaration to mark the next parsed statement as a 'blocking' statement.
     * If blocking statements are enabled and one has just finished being
     * executed, the Envision interpreter will halt script execution and
     * preserve the current memory and instruction stacks and will wait until
     * the script is manually restarted.
     */
    private boolean enableBlockStatementParsing = false;
    
    //==============
    // Constructors
    //==============
    
    /**
     * Creates an EnvisionProgram using the given 'lines' as its primary
     * execution target.
     * <p>
     * NOTE: The built program will not refer back to an actual file on the
     * host system as it will exist entirely within the scope of this execution
     * environment instead.
     * 
     * @param programNameIn The name of the program
     * @param linesIn       The Envision program lines to execute
     */
    public EnvisionProgram(String programNameIn, String lineIn) {
        logger.trace("Starting program build for: " + programNameIn);
        lineIn = lineIn.replace("\t", "");
        String[] subLines = lineIn.split("\n");
        
        programName = programNameIn;
        EList<String> lines = EList.of(subLines);
        
        // build directory around user.dir
        logger.trace("Creating working directory for: " + programName);
        dir = new WorkingDirectory();
        
        logger.trace("Injecting Envision program line(s) into new sudo main file.");
        mainCodeFile = wrapLinesIntoCodeFile(lines);
        mainCodeFile.forceValid();
        dir.setMainFile(mainCodeFile);
        
        logger.trace("Extracting main file program scope");
        mainFileScope = mainCodeFile.scope();
        logger.trace("Completed program build for: " + programNameIn);
    }
    
    /**
     * Creates an EnvisionProgram using the given 'lines' as its primary
     * execution target.
     * <p>
     * NOTE: The built program will not refer back to an actual file on the
     * host system as it will exist entirely within the scope of this execution
     * environment instead.
     * 
     * @param programNameIn The name of the program
     * @param linesIn       The Envision program lines to execute
     */
    public EnvisionProgram(String programNameIn, EList<String> linesIn) {
        logger.trace("Starting program build for: " + programNameIn);
        programName = programNameIn;
        
        // build directory around user.dir
        logger.trace("Creating working directory for: " + programName);
        dir = new WorkingDirectory();
        
        logger.trace("Injecting Envision program line(s) into new sudo main file.");
        mainCodeFile = wrapLinesIntoCodeFile(linesIn);
        mainCodeFile.forceValid();
        dir.setMainFile(mainCodeFile);
        
        logger.trace("Extracting main file program scope");
        mainFileScope = mainCodeFile.scope();
        logger.trace("Completed program build for: " + programNameIn);
    }
    
    /**
     * Creates a new EnvisionProgram using the specified code file as its main.
     * 
     * @param programNameIn The name of the program
     * @param mainIn The code file to use as its main file
     */
    public EnvisionProgram(String programNameIn, EnvisionCodeFile mainIn) {
        logger.trace("Starting program build for: " + programNameIn);
        programName = programNameIn;
        
        // build directory around user.dir
        logger.trace("Setting working directory for: " + programName + " to the given main code file's directory");
        dir = mainIn.getWorkingDir();
        mainCodeFile = mainIn;
        
        logger.trace("Extracting main file program scope");
        mainFileScope = mainCodeFile.scope();
        logger.trace("Completed program build for: " + programNameIn);
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
        // use Java program dir as default dir path
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
        this((baseDirectory != null) ? new File(baseDirectory, programNameIn)
                                     : new File(System.getProperty("user.dir"), programNameIn));
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
    
    /**
     * Creates a new EnvisionProgram with the given program name which
     * physically is/will be located at the specified File path.
     * <p>
     * Note: a default main file will be created if one does not already exist
     * within the given directory.
     * 
     * @param programDirIn The directory to use for the program
     * @param buildPackages Any packages to additionally load upon program execution
     */
    public EnvisionProgram(File programDirIn, EList<EnvisionLangPackage> buildPackages) {
        logger.trace("Started program build from directory: " + programDirIn);
        programDir = programDirIn;
        bundledProgramPackages.addAll(buildPackages);
        
        // if null -- error on invalid program path
        if (programDir == null) throw new EnvisionLangError("Invalid Envision program path!");
        
        // assign default program dir and main path to Java's working dir
        if (!EFileUtil.fileExists(programDir)) createDir();
        
        // wrap the program dir and parse
        logger.trace("Creating working directory for: " + programName);
        dir = new WorkingDirectory(programDir);
        dir.discoverFiles();
        
        // create default main if one is not found within the program dir
        if (dir.getMain() == null) {
            logger.trace("Main file not found for: " + programName + ", attempting to create one...");
            mainFile = new File(programDir, "main.nvis");
            try {
                mainFile.createNewFile();
            }
            catch (Exception e) {
                logger.error("Failed to create main file for program: " + programName, e);
                throw new RuntimeException(e);
            }
            logger.trace("Created new main file for: " + programName + " as '" + mainFile + "'");
        }
        
        // attempt to check if the working dir is actually valid
        if (!dir.isValid()) throw new BadDirError(dir);
        
        logger.trace("Extracting main file program scope");
        mainCodeFile = dir.getMain();
        mainFileScope = mainCodeFile.scope();
        logger.trace("Completed program build from directory: " + programDirIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return programName;
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Wraps the given java object into an Envision object and creates a
     * reference of it under the given name in the top level program scope.
     * <p>
     * This method will also create a native envision class type for the given
     * object if one does not already exist.
     * 
     * @param fieldName The name to refer to the given wrapped object with
     * @param object    The object to wrap
     */
    public void addJavaObjectToProgram(String fieldName, Object object) {
        javaObjectsToWrap.add(fieldName, object);
    }
    
    /**
     * Call an EnvisionFunction that resides somewhere within this program's
     * scope.
     * <p>
     * This method will attempt to both find and execute the target function
     * within the Envision language itself.
     * <p>
     * Given Java parameter values will be converted into Envision equivalents.
     * <p>
     * Any value returned by the executed EnvisionFunction will be returned as
     * the equivalent Java object representation.
     * 
     * @param funcName
     * @param args
     */
    public Object callEnvisionFunction(EnvisionInterpreter interpreter, String funcName, Object... args) {
        var function = mainFileScope.getFunction(funcName);
        
        // fail if not found
        if (function == null) throw new UndefinedFunctionError(funcName);
        
        // if the function is a native function, then this is easy, just grab
        // the function's native datatype mapper and let it do the rest
        if (function instanceof NativeFunction nf) {
            NativeDatatypeMapper mapper = nf.getDatatypeMapper();
            var mappedArgs = mapper.mapToEnvision(args);
            return nf.invoke_r(interpreter, mappedArgs);
        }
        else {
            // otherwise.. I really don't know....
        }
        
        return null;
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void createDir() {
        try {
            if (!EFileUtil.fileExists(programDir) && !programDir.mkdirs()) {
                throw new RuntimeException("Could not create script dir!");
            }
        }
        catch (Exception t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }
    
    private EnvisionCodeFile wrapLinesIntoCodeFile(EList<String> linesToWrap) {
        return new EnvisionCodeFile(linesToWrap);
    }
    
    public void applyEnvSettings() throws Exception {
        if (settings == null) return;
        
        for (var arg : settings.getEnvArgs()) {
            switch (arg) {
            case PRELOAD_LANGUAGE:
                preloadLanguage = true;
                for (int i = 0; i < 10; i++) EnvisionLoader.loadLang();
                break;
            case TOKENIZE: tokenize = true; break;
            case TOKENIZE_IN_DEPTH: tokenizeInDepth = true; break;
            case PARSE_STATEMENTS: parseStatements = true; break;
            case DONT_EXECUTE: executeCode = false; break;
            case ENABLE_BLOCK_STATEMENT_PARSING: enableBlockStatementParsing = true; break;
            case ENABLE_BLOCKING_STATEMENTS: enableBlockingStatements = true; break;
            default: break;
            }
        }
    }
    
    //=========
    // Getters
    //=========
    
    /**
     * @return The scope of the main code file
     */
    public IScope getProgramScope() { return mainFileScope; }
    /**
     * @return The Java File path of this script's main.
     */
    public File getMainFile() { return mainFile; }
    /**
     * Returns this script's main file as an EnvisionCodeFile. Note: The script
     * must be built in order for this to not return null.
     */
    public EnvisionCodeFile getMainCodeFile() { return (dir != null) ? dir.getMain() : null; }
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
    /** Packages to be loaded upon program compilation. */
    public EList<EnvisionLangPackage> getBundledPackages() { return bundledProgramPackages; }
    /**
     * Objects in Java to wrap and translate directly into Envision upon
     * program start.
     */
    public BoxList<String, Object> getJavaObjectsToWrap() { return javaObjectsToWrap; }
    /** The name of this program. */
    public String getProgramName() { return programName; }
    
    public EnvisionLangErrorCallBack getErrorCallback() {
        return errorCallback;
    }
    
    public EnvisionConsoleOutputReceiver getConsoleReceiver() {
        return consoleReceiver;
    }
    
    public boolean tokenize() { return tokenize; }
    public boolean tokenizeInDepth() { return tokenizeInDepth; }
    public boolean parseStatements() { return parseStatements; }
    public boolean executeCode() { return executeCode; }
    public boolean areBlockingStatementsEnabled() { return enableBlockingStatements; }
    public boolean isBlockStatementParsingEnabled() { return enableBlockStatementParsing; }
    
    //=========
    // Setters
    //=========
    
    /**
     * Sets the launch arguments to run the Envision Scripting Language with.
     * 
     * @param settingsIn The settings to run with this program
     */
    public void setLaunchSettings(EnvironmentSetting... settingsIn) {
        settings = EnvisionEnvironmnetSettings.of(settingsIn);
    }

    /**
     * Sets the launch arguments to run the Envision Scripting Language with.
     * 
     * @param settingsIn The settings to run with this program
     */
    public void setLaunchArgs(EnvisionEnvironmnetSettings settingsIn) { settings = settingsIn; }
    /**
     * Enables the ability for commands to 'block' script execution until
     * manually called for.
     */
    public void setEnableBlockStatements(boolean val) { enableBlockingStatements = val; }
    /**
     * If set true, enables the ability to append a '#' at the beginning of a
     * statement declaration to mark the next parsed statement as a 'blocking'
     * statement. If blocking statements are enabled and one has just finished
     * being executed, the Envision interpreter will halt script execution and
     * preserve the current memory and instruction stacks and will wait until
     * the script is manually restarted.
     */
    public void setEnableBlockStatementParsing(boolean val) { enableBlockStatementParsing = val; }
    /**
     * If enabled, the entire language will be loaded into the JVM before any
     * actual Envision code is executed.
     * <p>
     * NOTE: Once program execution starts, changes to this value will not be
     * reflected in running code.
     * @param val The value to set.
     */
    public void setPreloadLanguage(boolean val) { preloadLanguage = val; }
    /**
     * If enabled, bundled program code files will be tokenized and displayed
     * in the order they are read.
     */
    public void setDisplayTokens(boolean val) { tokenize = val; }
    /**
     * If enabled, bundled program code files will be tokenized and displayed
     * in the order they are read along with each token's full metadata as
     * well.
     */
    public void setDisplayTokensInDepth(boolean val) { tokenizeInDepth = val; }
    /**
     * If enabled, bundled program code files will be tokenized and then
     * attempt to be parsed into logical Envision statements and displayed.
     */
    public void setParseStatements(boolean val) { parseStatements = val; }
    /**
     * If disabled, no program code will actually be executed at run time. This
     * flag is primarily used for debugging and testing.
     */
    public void setEnableCodeExecution(boolean val) { executeCode = val; }
    
    /**
     * Assigns the specified class as the designated error callback for which
     * any (and all) thrown errors will be sent to during Envision's execution.
     *  
     * @param <T> A class extending EnvisionErrorCallback
     * @param callbackIn The specified class to be sent thrown errors
     * @return This Envision instance
     */
    public <T extends EnvisionLangErrorCallBack> void setErrorCallback(T callbackIn) {
        errorCallback = callbackIn;
    }
    
    public <T extends EnvisionConsoleOutputReceiver> void setConsoleReceiver(T receiverIn) {
        consoleReceiver = receiverIn;
    }
    
}
