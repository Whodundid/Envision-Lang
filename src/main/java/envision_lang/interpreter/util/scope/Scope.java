package envision_lang.interpreter.util.scope;

import java.util.HashMap;
import java.util.Map;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.IDatatype;
import eutil.datatypes.Box2;

public class Scope implements IScope {
	
	protected IScope parentScope = null;
	public final Map<String, Box2<IDatatype, EnvisionObject>> values = new HashMap<>();
	public final Map<String, Box2<IDatatype, EnvisionObject>> importedValues = new HashMap<>();
	
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
	
	@Override public Map<String, Box2<IDatatype, EnvisionObject>> values() { return values; }
	@Override public Map<String, Box2<IDatatype, EnvisionObject>> imports() { return importedValues; }
	
	@Override public IScope getParent() { return parentScope; }
	@Override public void setParent(IScope scopeIn) { parentScope = scopeIn; }
	
}
