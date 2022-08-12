package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.util.DataModifier;

public class EnvisionField {
	
	private int modifiers;
	private IDatatype datatype;
	private EnvisionObject value;
	
	public EnvisionField() { this(Primitives.VAR, EnvisionNull.NULL); }
	public EnvisionField(IDatatype typeIn) { this(typeIn, EnvisionNull.NULL); }
	public EnvisionField(IDatatype typeIn, EnvisionObject valueIn) {
		datatype = typeIn;
		value = valueIn;
	}
	
	/**
	 * Returns true if this object has the given data modifier.
	 */
	public boolean hasModifier(DataModifier mod) {
		return ((modifiers & (1L << mod.byteVal))) != 0;
	}
	
	/**
	 * Returns the integer containing all byte modifiers on this specific object.
	 */
	public int getModifiers() {
		return modifiers;
	}
	
	/**
	 * Applies the given modifier to this object.
	 */
	public void addModifier(DataModifier mod) {
		modifiers |= mod.byteVal;
	}
	
	/**
	 * Removes the given modifier from this object.
	 */
	public void removeModifier(DataModifier mod) {
		modifiers &= ~mod.byteVal;
	}
	
	//---------
	// Getters
	//---------
	
	public void setModifiers(int modifiersIn) {
		modifiers = modifiersIn;
	}
	
	//System.out.println("HAS " + this.getHexHash() + " : " + String.format("0b%8s", Integer.toBinaryString(modifiers)).replace(" ", "0"));
}
