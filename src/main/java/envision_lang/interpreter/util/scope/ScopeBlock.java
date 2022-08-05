package envision_lang.interpreter.util.scope;

/**
 * A ScopeBlock is intended to simplify scope value lookups and
 * to compartmentalize variables under specific visibilities
 * into their own specific, not-connected groups.
 * 
 * @author Hunter Bragg
 */
public class ScopeBlock {
	
	//private final HashMap<>
	
	/**
	 * Returns the scope visibility of this object.
	 * 
	 * SHOULD NOT BE HERE!
	 */
	//public VisibilityType getVisibility();
	
	/**
	 * THIS SHOULD NOT BE HERE DUE TO INCONSISTENCIES BETWEEN SCOPE DEFINED VISIBILITY
	 * AND OBJECT INSTNACE VISIBILITY! THIS MUST BE REMOVED AND HANDLED ENTIRELY WITHIN SCOPES!
	 * 
	 * @param type
	 * @return
	 */
	//public IObjectBase setVisibility(VisibilityType type);
	//public default IObjectBase setPublic() { return setVisibility(VisibilityType.PUBLIC); }
	//public default IObjectBase setProtected() { return setVisibility(VisibilityType.PROTECTED); }
	//public default IObjectBase setPrivate() { return setVisibility(VisibilityType.PRIVATE); }
	//public default IObjectBase setRestricted() { return setVisibility(VisibilityType.RESTRICTED); }
	
	//ALL VISIBILITY STUFF MUST BE MOVED TO SCOPES
	
	//public default boolean isPublic() { return getVisibility() == VisibilityType.PUBLIC; }
	//public default boolean isProtected() { return getVisibility() == VisibilityType.PROTECTED; }
	//public default boolean isPrivate() { return getVisibility() == VisibilityType.PRIVATE; }
	//public default boolean isRestricted() { return getVisibility() == VisibilityType.RESTRICTED; }
	
}
