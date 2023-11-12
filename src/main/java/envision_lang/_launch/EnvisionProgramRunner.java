package envision_lang._launch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.workingDirectory.InterpreterCreationError;
import envision_lang.lang.language_errors.error_types.workingDirectory.NoMainError;
import envision_lang.lang.packages.EnvisionLangPackage;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class EnvisionProgramRunner {
    
    //========
    // Fields
    //========
    
    /** The logger instance for Program Runners. */
    private final Logger logger = LoggerFactory.getLogger(EnvisionProgramRunner.class);
    
    /** The program to be run. */
    private final EnvisionProgram program;
    /** The primary interpreter instance to execute the bound program. */
    private EnvisionInterpreter interpreter;
    
    /** The time that this program was started. */
    private long start_time;
    /** True if this runner has started running the bound program. */
    private boolean hasStarted = false;
    /**
     * Flag to indicate whether or not this runner is actively running a
     * program.
     */
    private boolean isRunning = false;
    /**
     * Flag to indicate whether this runner has hit a block statement during
     * execution.
     */
    private boolean isBlocked = false;
    /**
     * Flag to indicate whether or not the program bound by this runner has
     * completed execution.
     */
    private boolean hasFinished = false;
    /**
     * Flag to indicate whether or not an error was thrown during execution.
     */
    private boolean hasError = false;
    
    //==============
    // Constructors
    //==============
    
    public EnvisionProgramRunner(EnvisionProgram programIn) throws Exception {
        program = programIn;
        
        build();
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void build() throws Exception {
        var dir = program.getWorkingDir();
        
        EList<EnvisionLangPackage> packages = EList.newList();
        
        // load build packages into dir
        try {
            packages.addAll(program.getBundledPackages());
            packages.forEach(dir::addBuildPackage);
        }
        catch (Exception err) {
            logger.error("Failed to build program!", err);
            throw err;
            // handle working directory error
        }
        
        var launchSettings = program.getLaunchArgs();
        
        // get user args
        var programArgs = (launchSettings != null) ? launchSettings.getUserArgs() : new EArrayList<String>();
        
        // check main
        EnvisionCodeFile main = dir.getMain();
        
        // check null main
        if (main == null) throw new NoMainError(dir);
        
        // throw interpret error if the file could not be loaded
        if (!main.load(dir, program.isBlockStatementParsingEnabled())) { throw new InterpreterCreationError(); }
        
        // create the interpreter
        interpreter = EnvisionInterpreter.build(program, programArgs);
        // load any program bundled envision java objects into the main's interpreter scope
        for (var box : program.getJavaObjectsToWrap()) {
            String name = box.getA();
            Object obj = box.getB();
            interpreter.injectJavaObject(name, obj);
        }
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Start the execution of the bound program.
     * 
     * @throws Exception
     */
    public void start(String... args) throws Exception {
        // error out if we are currently running as everything will break otherwise
        if (isRunning) throw new IllegalStateException("Program is already running!");
        
        // get the program's working dir
        var dir = program.getWorkingDir();
        
        // apply launch environment settings (if any)
        program.applyEnvSettings();
        
        // check if file tokens will be displayed
        if (program.tokenize()) dir.debugTokenize();
        // check if file tokens and their metadata should be displayed
        if (program.tokenizeInDepth()) dir.debugTokenizeInDepth();
        // check if parsed file statements will be displayed
        if (program.parseStatements()) dir.debugParsedStatements();
        
        // if disabled, don't continue with program execution
        if (!program.executeCode()) return;
        
        // if there were any user arguments passed, inject them into the interpreter
        if (args.length > 0) {
            interpreter.setupWithUserArguments(EList.of(args));
        }
        
        // track program start time
        start_time = System.currentTimeMillis();
        
        isRunning = true;
        hasStarted = true;
        hasFinished = false;
        
        // start execution of the program at the main file
        executeNextInstruction();
    }
    
    /**
     * Runs the next instruction of the bound program until either a blocking
     * statement is reached or the program terminates normally.
     */
    public void executeNextInstruction() {
        // error out if we are not running in the first place as everything will break otherwise
        if (!isRunning) throw new IllegalStateException("Program is not running!");
        // if execution is currently blocked, wait until it relinquishes state
        if (isBlocked) return;
        
        try {
            // actually execute the program
            interpreter.executeNext();
        }
        catch (EnvisionLangError e) {
            if (program.getErrorCallback() != null) program.getErrorCallback().onEnvisionError(e);
            hasError = true;
        }
        catch (Exception e) {
            if (program.getErrorCallback() != null) program.getErrorCallback().onJavaException(e);
            hasError = true;
        }
        
        // if this statement is reached, check if there are more instructions
        // to run, and if not, terminate the program.
        if (hasError || !hasNextInstruction()) {
            terminate();
        }
        else {
            isBlocked = true;
        }
    }
    
    /**
     * Immediately stops the execution of the bound program.
     */
    public void terminate() {
        if (!isRunning) throw new IllegalStateException("Program is not running!");
        
        // stop the interpreter
        interpreter.terminate();
        
        // debug log the program's total running time
        logger.debug("ENVISION-END: {} ms", (System.currentTimeMillis() - start_time));
        
        isRunning = false;
        hasStarted = false;
        isBlocked = false;
        hasFinished = true;
    }
    
    //=========
    // Getters
    //=========
    
    /** Returns the program being executed by this runner. */
    public EnvisionProgram getProgram() { return program; }
    
    public EnvisionInterpreter getInterpreter() { return interpreter; }
    
    public boolean hasNextInstruction() {
        return (isRunning && interpreter != null && interpreter.hasNext());
    }
    
    public boolean isRunning() { return isRunning; }
    public boolean hasStarted() { return hasStarted; }
    public boolean isBlocked() { return isBlocked; }
    public boolean hasFinished() { return hasFinished; }
    public boolean hasError() { return hasError; }
    
}
