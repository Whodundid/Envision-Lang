package envision_lang.interpreter.util.scope;

/**
 * A ScopeEntry stores a specific object that could also have
 * a specific datatype associated with it.
 * 
 * @author Hunter Bragg
 */
public class ScopeEntry {
	
	/** The specified datatype allowed for the paired object. */
	private String datatype;
	private Object storred_object;
	
	//--------------
	// Constructors
	//--------------
	
	public ScopeEntry(Object objectIn) { this(null, objectIn); }
	public ScopeEntry(String datatypeIn, Object objectIn) {
		datatype = datatypeIn;
		storred_object = objectIn;
	}
	
	//---------
	// Getters
	//---------
	
	public String getDatatype() { return datatype; }
	public Object getObject() { return storred_object; }
	
	//---------
	// Setters
	//---------
	
	public ScopeEntry set(Object objectIn) { return set(null, objectIn); }
	public ScopeEntry set(String datatypeIn, Object objectIn) {
		datatype = datatypeIn;
		storred_object = objectIn;
		return this;
	}
	
}
