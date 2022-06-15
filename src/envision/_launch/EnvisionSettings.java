package envision._launch;

import envision.exceptions.errors.launch.InvalidLaunchArgumentError;
import eutil.datatypes.EArrayList;
import eutil.debug.Broken;
import eutil.debug.Unused;

/**
 * A collection of valid launch arguments to be given to either the Envision
 * language as parameters or to user programs executing during runtime.
 * 
 * @author Hunter Bragg
 */
public class EnvisionSettings {
	
	/** All passed arguments to be processed. */
	private final String[] launchArgs;
	/** Arguments passed to the Envision Language. */
	private final EArrayList<LaunchArgs> envArgs = new EArrayList();
	/** Arguments passed to programs running on the Envision Language. */
	private final EArrayList<String> userArgs = new EArrayList();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionSettings(String[] in) {
		parseArgs(in);
		launchArgs = in;
	}
	
	public EnvisionSettings(LaunchArgs... argsIn) {
		launchArgs = new String[0];
		envArgs.addIfNotContains(argsIn);
	}
	
	//---------
	// Methods
	//---------
	
	/** Separates valid launch arguments from standard user program arguments. */
	private void parseArgs(String[] in) {
		for (String s : in) {
			if (s.startsWith("-") && s.length() > 1) {
				String option = s.substring(1); //strip '-'
				LaunchArgs arg = LaunchArgs.matchArg(option);
				if (arg == null) throw new InvalidLaunchArgumentError(s);
				envArgs.add(arg);
			}
			else {
				userArgs.add(s);
			}
		}
	}
	
	public EArrayList<LaunchArgs> getEnvArgs() { return envArgs; }
	public EArrayList<String> getUserArgs() { return userArgs; }
	
	//-----------------
	// Launch Arg Enum
	//-----------------
	
	public static enum LaunchArgs {
		@Unused
		CLASS_FILE_STATEMENTS("allowClassFileStatements"),
		@Unused
		CLASS_BODY_STATEMENTS("allowClassBodyStatements"),
		/** Loads primary Envision system classes up-front before executing Envision code. */
		@Broken
		PRELOAD_LANGUAGE("preloadLang"),
		/** Displays various debug outputs from the language. 'Very poorly defined!' */
		DEBUG_MODE("enableDebugMode"),
		/** 'Talk' directly to the interpreter. VERY BUGGY! */
		LIVE_MODE("enableLiveMode"),
		/** Prints out tokenized values, File by File. */
		TOKENIZE("tokenize"),
		/** Prints out parsed statements, File by File. */
		PARSE_STATEMENTS("parse_statements"),
		/** Does not allow Envision code execution to commence. */
		DONT_EXECUTE("dont_execute"),
		//PRINTLN_AS_KEYWORD("println_as_keyword"),
		;
		
		public final String name;
		
		private LaunchArgs(String argName) {
			name = argName;
		}
		
		public static LaunchArgs matchArg(String argNameIn) {
			for (LaunchArgs a : values()) if (a.name.equals(argNameIn)) return a;
			return null;
		}
	}
	
	//-------------------------
	// Static Default Settings
	//-------------------------
	
	public static EnvisionSettings of(String... args) { return new EnvisionSettings(args); }
	public static EnvisionSettings of(LaunchArgs... args) { return new EnvisionSettings(args); }
	public static EnvisionSettings preload() { return new EnvisionSettings(LaunchArgs.PRELOAD_LANGUAGE); }
	public static EnvisionSettings live() { return new EnvisionSettings(LaunchArgs.LIVE_MODE); }
	public static EnvisionSettings tokenize() { return new EnvisionSettings(LaunchArgs.TOKENIZE); }
	public static EnvisionSettings parse() { return new EnvisionSettings(LaunchArgs.PARSE_STATEMENTS); }
	
}
