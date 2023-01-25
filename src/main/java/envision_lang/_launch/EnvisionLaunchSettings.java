package envision_lang._launch;

import java.util.Collection;
import java.util.List;

import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;
import eutil.debug.Unused;

/**
 * A collection of valid launch arguments to be given to either the Envision
 * language as parameters or to user programs executing during runtime.
 * 
 * @author Hunter Bragg
 */
public class EnvisionLaunchSettings {
	
	/** Arguments passed to the Envision Language. */
	private EList<LaunchSetting> envArgs = new EArrayList<>();
	/** Arguments passed to programs running on the Envision Language. */
	private EList<String> userArgs = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionLaunchSettings() {}
	
	public EnvisionLaunchSettings(String[] in) {
		parseArgs(in);
	}
	
	public EnvisionLaunchSettings(LaunchSetting... argsIn) {
		envArgs.addIfNotContains(argsIn);
	}
	
	public EnvisionLaunchSettings(Collection<String> in) {
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
				LaunchSetting arg = LaunchSetting.matchArg(option);
				if (arg != null) envArgs.addIfNotContains(arg);
				else userArgs.add(s);
			}
			else {
				userArgs.add(s);
			}
		}
	}
	
	public EList<LaunchSetting> getEnvArgs() { return envArgs; }
	public EList<String> getUserArgs() { return userArgs; }
	
	public void addUserArg(String arg) { userArgs.add(arg); }
	public void addLaunchArg(LaunchSetting arg) { envArgs.addIfNotContains(arg); }
	
	//-----------------
	// Launch Arg Enum
	//-----------------
	
	public static enum LaunchSetting {
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
		/** Prints out tokenized values along with their token metadata. */
		TOKENIZE_IN_DEPTH("tokenize_in_depth"),
		/** Prints out parsed statements, File by File. */
		PARSE_STATEMENTS("parse_statements"),
		/** Does not allow Envision code execution to commence. */
		DONT_EXECUTE("dont_execute"),
		//PRINTLN_AS_KEYWORD("println_as_keyword"),
		;
		
		public final String name;
		
		private LaunchSetting(String argName) {
			name = argName;
		}
		
		public static LaunchSetting matchArg(String argNameIn) {
			for (LaunchSetting a : values()) if (a.name.equals(argNameIn)) return a;
			return null;
		}
	}
	
	//-------------------------
	// Static Default Settings
	//-------------------------
	
	public static EnvisionLaunchSettings of(String... args) { return new EnvisionLaunchSettings(args); }
	public static EnvisionLaunchSettings of(LaunchSetting... args) { return new EnvisionLaunchSettings(args); }
	public static EnvisionLaunchSettings of(List<String> args) { return new EnvisionLaunchSettings(args); }
	public static EnvisionLaunchSettings of(EList<String> args) { return new EnvisionLaunchSettings(args); }
	public static EnvisionLaunchSettings preload() { return new EnvisionLaunchSettings(LaunchSetting.PRELOAD_LANGUAGE); }
	public static EnvisionLaunchSettings live() { return new EnvisionLaunchSettings(LaunchSetting.LIVE_MODE); }
	public static EnvisionLaunchSettings tokenize() { return new EnvisionLaunchSettings(LaunchSetting.TOKENIZE); }
	public static EnvisionLaunchSettings parse() { return new EnvisionLaunchSettings(LaunchSetting.PARSE_STATEMENTS); }
	
}
