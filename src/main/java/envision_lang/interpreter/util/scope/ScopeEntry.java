package envision_lang.interpreter.util.scope;

import envision_lang.exceptions.errors.StrongVarReassignmentError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;

/**
 * A ScopeEntry stores a specific object that could also have
 * a specific datatype associated with it.
 * 
 * @author Hunter Bragg
 */
public class ScopeEntry {
	
	//========
	// Fields
	//========
	
	/** The specified datatype allowed for the paired object. */
	private IDatatype datatype;
	/** Determines whether or not this datatype can dynamically change over time. */
	private boolean strong = false;
	/** The object being stored at this scope entry. */
	private EnvisionObject object = EnvisionNull.NULL;
	
	//==============
	// Constructors
	//==============
	
	public ScopeEntry(IDatatype datatypeIn) {
		if (datatypeIn == null) throw new IllegalStateException("Scope entry datatype cannot be null!");
		datatype = datatypeIn;
	}
	
	public ScopeEntry(EnvisionObject objectIn) {
		datatype = objectIn.getDatatype();
		object = objectIn;
	}
	
	//=========
	// Getters
	//=========
	
	public IDatatype getDatatype() { return datatype; }
	public boolean isStrong() { return strong; }
	public EnvisionObject getObject() { return object; }
	
	//=========
	// Setters
	//=========
	
	public void setStrong(boolean val) {
		strong = val;
	}
	
	public void set(EnvisionObject objectIn) {
		IDatatype incoming = objectIn.getDatatype();
		boolean compare = StaticTypes.NULL_TYPE.compare(incoming);
		
		if (strong) {
			if (!compare) throw new StrongVarReassignmentError(datatype, incoming);
		}
		else {
			
		}
		
		
	}
	
}
