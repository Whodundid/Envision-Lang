package envision_lang.interpreter.util.throwables;

/** Thrown whenever a continue statement is executed within a loop statement. */
public class Continue extends RuntimeException {
	
	public static final Continue instance = new Continue();
	
	private Continue() {}
	
}
