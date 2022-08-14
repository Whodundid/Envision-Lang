package envision_lang._launch;

import envision_lang.exceptions.errors.launch.InvalidLaunchArgumentError;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.debug.Broken;
import eutil.debug.Unused;

/**
 * A collection of valid launch arguments to be given to either the Envision
 * language as parameters or to user programs executing during runtime.
 * 
 * @author Hunter Bragg
 */
public class EnvisionLangSettings {
	
	/** All passed arguments to be processed. */
	private final String[] launchArgs;
	/** Arguments passed to the Envision Language. */
	private final EList<LaunchArgs> envArgs = new EArrayList<>();
	/** Arguments passed to programs running on the Envision Language. */
	private final EList<String> userArgs = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionLangSettings(String[] in) {
		parseArgs(in);
		launchArgs = in;
	}
	
	public EnvisionLangSettings(LaunchArgs... argsIn) {
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
	
	public EList<LaunchArgs> getEnvArgs() { return envArgs; }
	public EList<String> getUserArgs() { return userArgs; }
	
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
	
	public static EnvisionLangSettings of(String... args) { return new EnvisionLangSettings(args); }
	public static EnvisionLangSettings of(LaunchArgs... args) { return new EnvisionLangSettings(args); }
	public static EnvisionLangSettings preload() { return new EnvisionLangSettings(LaunchArgs.PRELOAD_LANGUAGE); }
	public static EnvisionLangSettings live() { return new EnvisionLangSettings(LaunchArgs.LIVE_MODE); }
	public static EnvisionLangSettings tokenize() { return new EnvisionLangSettings(LaunchArgs.TOKENIZE); }
	public static EnvisionLangSettings parse() { return new EnvisionLangSettings(LaunchArgs.PARSE_STATEMENTS); }
	
}
