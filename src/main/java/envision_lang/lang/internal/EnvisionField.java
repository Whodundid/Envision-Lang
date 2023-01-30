package envision_lang.lang.internal;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.exceptions.errors.NullVariableError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.util.DataModifierHandler;

/**
 * A field that marks a datatype and modifiers within a scope.
 * 
 * @author Hunter Bragg
 */
public class EnvisionField {
	
	//--------
	// Fields
	//--------
	
	private String fieldName;
	private IDatatype datatype = Primitives.VAR; // defaults to VAR
	private EnvisionObject value = EnvisionNull.NULL; // defaults to NULL
	
	private final DataModifierHandler modifierHandler = new DataModifierHandler();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionField(String nameIn) { this(nameIn, Primitives.VAR, EnvisionNull.NULL); }
	public EnvisionField(String nameIn, IDatatype typeIn) { this(nameIn, typeIn, EnvisionNull.NULL); }
	public EnvisionField(String nameIn, IDatatype typeIn, EnvisionObject valueIn) {
		fieldName = nameIn;
		datatype = typeIn;
		value = valueIn;
	}
	
	//---------
	// Getters
	//---------
	
	public EnvisionObject get() {
		return value;
	}
	
	//---------
	// Setters
	//---------
	
	public void set(EnvisionObject valueIn) {
		//error on null values -- this should never be Java::Null in Envision!
		if (valueIn == null) throw new NullVariableError();
		
		//always allow Envision::Null
		if (valueIn == EnvisionNull.NULL) {
			value = valueIn;
			return;
		}
		
		//make sure that the type coming in matches the expected type
		IDatatype in = valueIn.getDatatype();
		if (!datatype.compare(in)) throw new InvalidDatatypeError(datatype, in);
		
		value = valueIn;
	}
	
}
