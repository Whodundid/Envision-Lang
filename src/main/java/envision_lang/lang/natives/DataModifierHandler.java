package envision_lang.lang.natives;

public class DataModifierHandler {
	
	private int modifiers;
	
	public DataModifierHandler() {}
	public DataModifierHandler(int modifiersIn) {
		modifiers = modifiersIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	public String toString() {
		return String.format("MODS_[0b%8s]", Integer.toBinaryString(modifiers)).replace(" ", "0");
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns true if this object has the given data modifier.
	 */
	public boolean hasModifier(DataModifier mod) {
		return ((modifiers & (1L << mod.byteVal))) != 0;
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
	
	/**
	 * Returns the integer containing all byte modifiers on this specific object.
	 */
	public int getModifiers() {
		return modifiers;
	}
	
	/**
	 * @return This object's visibility
	 */
	public EnvisionVisibilityModifier getVisibility() {
		if (isRestricted()) return EnvisionVisibilityModifier.RESTRICTED;
		if (isPublic()) return EnvisionVisibilityModifier.PUBLIC;
		if (isProtected()) return EnvisionVisibilityModifier.PROTECTED;
		if (isPrivate()) return EnvisionVisibilityModifier.PRIVATE;
		return EnvisionVisibilityModifier.SCOPE;
	}
	
	public boolean isAbstract() { return hasModifier(DataModifier.ABSTRACT); }
	public boolean isStatic() { return hasModifier(DataModifier.STATIC); }
	public boolean isFinal() { return hasModifier(DataModifier.FINAL); }
	public boolean isStrong() { return hasModifier(DataModifier.STRONG); }
	
	public boolean isRestricted() { return hasModifier(DataModifier.RESTRICTED); }
	public boolean isPrivate() { return hasModifier(DataModifier.PRIVATE); }
	public boolean isProtected() { return hasModifier(DataModifier.PROTECTED); }
	public boolean isPublic() { return hasModifier(DataModifier.PUBLIC); }
	/** Returns true if there are no visibility modifiers set. */
	public boolean isScopeVisibility() { return ((modifiers >> 4) & 0xf) == 0; }
	
	//---------
	// Setters
	//---------
	
	public void setModifiers(int modifiersIn) {
		modifiers = modifiersIn;
	}
	
	public DataModifierHandler setModifier(DataModifier mod, boolean val) {
		if (val) addModifier(mod);
		else removeModifier(mod);
		return this;
	}
	
	/**
	 * Sets all bitwise visibility modifiers to zero.
	 */
	private void resetVisibility() {
		removeModifier(DataModifier.RESTRICTED);
		removeModifier(DataModifier.PRIVATE);
		removeModifier(DataModifier.PROTECTED);
		removeModifier(DataModifier.PUBLIC);
	}
	
	/**
	 * Assigns data-modifiers which pertain to the given Visibility type.
	 * 
	 * @param visIn Visibility to assign
	 * @return This EnvisionObject
	 */
	public DataModifierHandler setVisibility(EnvisionVisibilityModifier visIn) {
		//clear out current visibility bitwise mods
		resetVisibility();
		//assign specific visibility bitwise mods from visibility type
		switch (visIn) {
		case RESTRICTED: 	addModifier(DataModifier.RESTRICTED); 	break;
		case PRIVATE: 		addModifier(DataModifier.PRIVATE);		break;
		case PROTECTED: 	addModifier(DataModifier.PROTECTED);	break;
		case PUBLIC: 		addModifier(DataModifier.PUBLIC);		break;
		//*scope visibility does not assign bitwise mods*
		default: 													break;
		}
		
		return this;
	}
	
	public DataModifierHandler setStatic() { addModifier(DataModifier.STATIC); return this; }
	public DataModifierHandler setFinal() { addModifier(DataModifier.FINAL); return this; }
	public DataModifierHandler setStrong() { addModifier(DataModifier.STRONG); return this; }
	
	public DataModifierHandler setRestricted() { return setVisibility(EnvisionVisibilityModifier.RESTRICTED); }
	public DataModifierHandler setPrivate() { return setVisibility(EnvisionVisibilityModifier.PRIVATE); }
	public DataModifierHandler setProtected() { return setVisibility(EnvisionVisibilityModifier.PROTECTED); }
	public DataModifierHandler setPublic() { return setVisibility(EnvisionVisibilityModifier.PUBLIC); }
	public DataModifierHandler setScopeVisibility() { return setVisibility(EnvisionVisibilityModifier.SCOPE); }
	
}
