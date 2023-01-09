package envision_lang.interpreter.util.scope;

import java.util.HashMap;
import java.util.Map;

public class Scope implements IScope {
	
	protected IScope parentScope = null;
	public final Map<String, ScopeEntry> values = new HashMap<>();
	public final Map<String, ScopeEntry> importedValues = new HashMap<>();
	
	//--------------
	// Constructors
	//--------------
	
	/** Creates a scope with no immediate parent scope. */
	public Scope() {}
	/** Creates a scope with the specified parent scope. */
	public Scope(IScope parentScopeIn) {
		parentScope = parentScopeIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String toString() { return IScope.asString(this); }
	
	@Override public Map<String, ScopeEntry> values() { return values; }
	@Override public Map<String, ScopeEntry> imports() { return importedValues; }
	
	@Override public IScope getParent() { return parentScope; }
	@Override public void setParent(IScope scopeIn) { parentScope = scopeIn; }
	
}
