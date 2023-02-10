package envision_lang.interpreter.util.scope;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.language_errors.error_types.StrongVarReassignmentError;
import envision_lang.lang.language_errors.error_types.objects.CopyNotSupportedError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.EnvisionVisibilityModifier;
import eutil.strings.EStringBuilder;

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
	private boolean strong;
	/** The object being stored at this scope entry. */
	private EnvisionObject object = EnvisionNull.NULL;
	/** The visibility of this object on this scope. */
	private EnvisionVisibilityModifier visibility = EnvisionVisibilityModifier.SCOPE;
	
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
	
	/**
	 * Package-Private constructor for internal convenience.
	 * 
	 * @implNote This constructor is hidden because there is no inherent
	 *           guarantee that the given datatype and object type actually
	 *           have anything in common with one another. In the event that an
	 *           invalid datatype is paired against an unrelated object type,
	 *           awkward variable datatype inconsistencies will arise when
	 *           being interpreted later.
	 * 			
	 * @param datatypeIn The datatype of this entry
	 * @param objectIn   The object to store at this entry
	 */
	ScopeEntry(IDatatype datatypeIn, EnvisionObject objectIn) {
		datatype = datatypeIn;
		object = objectIn;
	}

	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		var sb = new EStringBuilder("[");
		sb.a(datatype);
		if (strong) sb.a(" : ", strong);
		sb.a(" : ", object);
		sb.a("]");
		return sb.toString();
	}
	
	//=========
	// Methods
	//=========
	
	/**
	 * Performs a shallow copy of this scope entry. Effectively, this means
	 * that the underlying EnvisionObject for which this entry contains will
	 * not be copied, but rather the object will be referenced.
	 * 
	 * @return A shallow copy of this ScopeEntry
	 */
	public ScopeEntry shallowCopy() {
		ScopeEntry r = new ScopeEntry(datatype);
		r.strong = strong;
		r.visibility = visibility;
		r.object = object;
		return r;
	}
	
	/**
	 * Performs a full copy of this scope entry including creating a deep copy
	 * of the underlying EnvisionObject that this entry contains.
	 * 
	 * @return A deep copy of this ScopeEntry
	 */
	public ScopeEntry deepCopy() throws CopyNotSupportedError {
		ScopeEntry r = new ScopeEntry(datatype);
		r.strong = strong;
		r.visibility = visibility;
		
		if (object != null) {
			r.object = object.copy();
		}
		
		return r;
	}
	
	//=========
	// Getters
	//=========
	
	public IDatatype getDatatype() { return datatype; }
	public boolean isFieldType() { return datatype.isField(); }
	public boolean isFunctionType() { return datatype.isFunction(); }
	public boolean isNumberType() { return datatype.isNumber(); }
	public boolean isClassType() { return datatype.isClass(); }
	public boolean isStringType() { return datatype.isString(); }
	public boolean isPackageType() { return datatype.isPackage(); }
	public boolean isVoidType() { return datatype.isVoid(); }
	public boolean isStrong() { return strong; }
	public EnvisionVisibilityModifier getVisibility() { return visibility; }
	public EnvisionObject getObject() { return object; }
	
	//=========
	// Setters
	//=========
	
	public void setStrong(boolean val) {
		strong = val;
	}
	
	void modifyDatatype(IDatatype type) {
		datatype = type;
	}

	void setVisibility(EnvisionVisibilityModifier visIn) {
		visibility = visIn;
	}
	
	public void set(EnvisionObject objectIn) {
		IDatatype incoming = objectIn.getDatatype();
		boolean compare = EnvisionStaticTypes.NULL_TYPE.compare(incoming);
		
		if (strong) {
			if (!compare) throw new StrongVarReassignmentError(datatype, incoming);
		}
		else {
			// this is probably not complete
			object = objectIn;
		}
		
		
	}
	
	public EnvisionObject setR(EnvisionObject objectIn) {
		set(objectIn);
		return objectIn;
	}
	
	public ScopeEntry setRT(EnvisionObject objectIn) {
		set(objectIn);
		return this;
	}
	
}
