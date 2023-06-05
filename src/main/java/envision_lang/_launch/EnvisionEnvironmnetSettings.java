package envision_lang._launch;

import java.util.Collection;
import java.util.List;

import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

/**
 * A collection of valid environment arguments to be given to either the Envision
 * language as parameters or to user programs executing during runtime.
 * 
 * @author Hunter Bragg
 */
public class EnvisionEnvironmnetSettings {
	
	/** Arguments passed to the Envision Language. */
	private EList<EnvironmentSetting> envArgs = new EArrayList<>();
	/** Arguments passed to programs running on the Envision Language. */
	private EList<String> userArgs = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionEnvironmnetSettings() {}
	
	public EnvisionEnvironmnetSettings(String[] in) {
		parseArgs(in);
	}
	
	public EnvisionEnvironmnetSettings(EnvironmentSetting... argsIn) {
		envArgs.addIfNotContains(argsIn);
	}
	
	public EnvisionEnvironmnetSettings(Collection<String> in) {
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
				EnvironmentSetting arg = EnvironmentSetting.matchArg(option);
				if (arg != null) envArgs.addIfNotContains(arg);
				else userArgs.add(s);
			}
			else {
				userArgs.add(s);
			}
		}
	}
	
	public EList<EnvironmentSetting> getEnvArgs() { return envArgs; }
	public EList<String> getUserArgs() { return userArgs; }
	
	public void addUserArg(String arg) { userArgs.add(arg); }
	public void addLaunchArg(EnvironmentSetting arg) { envArgs.addIfNotContains(arg); }
	
	//-----------------
	// Launch Arg Enum
	//-----------------
	
	public static enum EnvironmentSetting {
		//@Unused
		//CLASS_FILE_STATEMENTS("allowClassFileStatements"),
		//@Unused
		//CLASS_BODY_STATEMENTS("allowClassBodyStatements"),
		/** Loads primary Envision system classes up-front before executing Envision code. */
		@Broken
		PRELOAD_LANGUAGE("preloadLang"),
		/** Displays various debug outputs from the language. 'Very poorly defined!' */
		@Broken(reason="I am not sure what this setting is supposed to even accomplish anymore")
		DEBUG_MODE("debugMode"),
		/** 'Talk' directly to the interpreter. VERY BUGGY! */
		@Broken
		LIVE_MODE("liveMode"),
		/** Prints out tokenized values, File by File. */
		TOKENIZE("tokenize"),
		/** Prints out tokenized values along with their token metadata. */
		TOKENIZE_IN_DEPTH("tokenize_in_depth"),
		/** Prints out parsed statements, File by File. */
		PARSE_STATEMENTS("parse_statements"),
		/** Does not allow Envision code execution to commence. */
		DONT_EXECUTE("dont_execute"),
		/** Enables blocking statements. */
		ENABLE_BLOCKING_STATEMENTS("enable_blocking_statements"),
		/** Enables the parsing of blocking statements by starting them with a '#'. */
		ENABLE_BLOCK_STATEMENT_PARSING("enable_block_statement_parsing"),
		//PRINTLN_AS_KEYWORD("println_as_keyword"),
		;
		
		public final String name;
		
		private EnvironmentSetting(String argName) {
			name = argName;
		}
		
		public static EnvironmentSetting matchArg(String argNameIn) {
			for (EnvironmentSetting a : values()) if (a.name.equals(argNameIn)) return a;
			return null;
		}
	}
	
	//-------------------------
	// Static Default Settings
	//-------------------------
	
	public static EnvisionEnvironmnetSettings of(String... args) { return new EnvisionEnvironmnetSettings(args); }
	public static EnvisionEnvironmnetSettings of(EnvironmentSetting... args) { return new EnvisionEnvironmnetSettings(args); }
	public static EnvisionEnvironmnetSettings of(List<String> args) { return new EnvisionEnvironmnetSettings(args); }
	public static EnvisionEnvironmnetSettings of(EList<String> args) { return new EnvisionEnvironmnetSettings(args); }
	public static EnvisionEnvironmnetSettings preload() { return new EnvisionEnvironmnetSettings(EnvironmentSetting.PRELOAD_LANGUAGE); }
	public static EnvisionEnvironmnetSettings live() { return new EnvisionEnvironmnetSettings(EnvironmentSetting.LIVE_MODE); }
	public static EnvisionEnvironmnetSettings tokenize() { return new EnvisionEnvironmnetSettings(EnvironmentSetting.TOKENIZE); }
	public static EnvisionEnvironmnetSettings parse() { return new EnvisionEnvironmnetSettings(EnvironmentSetting.PARSE_STATEMENTS); }
	
}
