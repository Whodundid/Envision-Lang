package envision.interpreter.util.throwables;

/** A means of returning data across method/class scopes. */
public class ReturnValue extends RuntimeException {
	
	/** The object being returned. */
	public final Object object;
	
	public ReturnValue() { this(null); }
	public ReturnValue(Object objIn) {
		object = objIn;
	}
	
	/** Returns true if the return value is null. */
	public boolean isNull() { return object == null; }
	
}
