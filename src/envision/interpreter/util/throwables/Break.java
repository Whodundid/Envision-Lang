package envision.interpreter.util.throwables;

/** Thrown whenever a break statement is executed within some kind of loop or switch statement. */
public class Break extends RuntimeException {
	
	public static final Break instance = new Break();
	
	private Break() {}
	
}
