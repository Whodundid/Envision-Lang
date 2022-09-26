package envision_lang._launch;

import java.util.Collection;
import java.util.List;

import eutil.datatypes.EArrayList;
import eutil.debug.Broken;
import eutil.debug.Unused;

/**
 * A collection of valid launch arguments to be given to either the Envision
 * language as parameters or to user programs executing during runtime.
 * 
 * @author Hunter Bragg
 */
public class EnvisionLangSettings {
	
	/** Arguments passed to the Envision Language. */
	private EArrayList<LaunchArgs> envArgs = new EArrayList<>();
	/** Arguments passed to programs running on the Envision Language. */
	private EArrayList<String> userArgs = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionLangSettings() {}
	
	public EnvisionLangSettings(String[] in) {
		parseArgs(in);
	}
	
	public EnvisionLangSettings(LaunchArgs... argsIn) {
		envArgs.addIfNotContains(argsIn);
	}
	
	public EnvisionLangSettings(Collection<String> in) {
		parseArgs(in.toArray(new String[0]));
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
				if (arg != null) envArgs.addIfNotContains(arg);
				else userArgs.add(s);
			}
			else {
				userArgs.add(s);
			}
		}
	}
	
	public EArrayList<LaunchArgs> getEnvArgs() { return envArgs; }
	public EArrayList<String> getUserArgs() { return userArgs; }
	
	public void addUserArg(String arg) { userArgs.add(arg); }
	public void addLaunchArg(LaunchArgs arg) { envArgs.addIfNotContains(arg); }
	
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
		DEBUG_MODE("debugMode"),
		/** 'Talk' directly to the interpreter. VERY BUGGY! */
		LIVE_MODE("liveMode"),
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
	public static EnvisionLangSettings of(List<String> args) { return new EnvisionLangSettings(args); }
	public static EnvisionLangSettings preload() { return new EnvisionLangSettings(LaunchArgs.PRELOAD_LANGUAGE); }
	public static EnvisionLangSettings live() { return new EnvisionLangSettings(LaunchArgs.LIVE_MODE); }
	public static EnvisionLangSettings tokenize() { return new EnvisionLangSettings(LaunchArgs.TOKENIZE); }
	public static EnvisionLangSettings parse() { return new EnvisionLangSettings(LaunchArgs.PARSE_STATEMENTS); }
	
}
