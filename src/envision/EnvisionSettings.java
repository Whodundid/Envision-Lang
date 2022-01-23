package envision;

import envision.exceptions.errors.launch.InvalidLaunchArgumentError;
import eutil.datatypes.EArrayList;

/**
 * A collection of valid launch parameters to be given as arguments to the 
 * Envision language and its executing programs during runtime.
 * 
 * @author Hunter Bragg
 *
 */
public class EnvisionSettings {
	
	/** All passed arguments to be processed. */
	private final String[] launchArgs;
	
	/** Arguments passed to the Envision Language. */
	private final EArrayList<LaunchArgs> envArgs = new EArrayList();
	/** Arguments passed to programs running on the Envision Language. */
	private final EArrayList<String> userArgs = new EArrayList();
	
	public EnvisionSettings(String[] in) {
		parseArgs(in);
		launchArgs = in;
	}
	
	public EnvisionSettings(LaunchArgs... argsIn) {
		launchArgs = new String[0];
		envArgs.addIfNotContains(argsIn);
	}
	
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
		CLASS_FILE_STATEMENTS("allowClassFileStatements"),
		CLASS_BODY_STATEMENTS("allowClassBodyStatements"),
		PRELOAD_LANGUAGE("preloadLang"),
		DEBUG_MODE("enableDebugMode"),
		LIVE_MODE("enableLiveMode"),
		TOKENIZE("tokenize"),
		PARSE_STATEMENTS("parse_statements"),
		DONT_EXECUTE("dont_execute"),
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
