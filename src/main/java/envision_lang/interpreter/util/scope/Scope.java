package envision_lang.interpreter.util.scope;

import java.util.HashMap;
import java.util.Map;

import envision_lang.lang.EnvisionObject;

public class Scope implements IScope {
	
	protected IScope parentScope = null;
	public final Map<String, ScopeEntry> values = new HashMap<>();
	public final Map<String, ScopeEntry> importedValues = new HashMap<>();
	//private final EList<String> valueNames = EList.newList();
	//private final EList<String> importedValueNames = EList.newList();
	
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
	
	@Override
	public EnvisionObject define_i(String name, ScopeEntry entry) {
		values.put(name, entry);
		//if (!valueNames.contains(name)) valueNames.add(name);
		return entry.getObject();
	}
	
	@Override
	public EnvisionObject defineImportVal_i(String name, ScopeEntry entry) {
		importedValues.put(name, entry);
		//if (!importedValueNames.contains(name)) importedValueNames.add(name);
		return entry.getObject();
	}
	
	@Override
	public boolean doesValueExist(String name) {
//		if (name == null) return false;
//		for (int i = 0; i < valueNames.size(); i++) {
//			String s = valueNames.get(i);
//			if (s.equals(name)) return true;
//		}
		return false;
	}
	
	@Override
	public boolean doesImportedValueExist(String name) {
//		if (name == null) return false;
//		for (int i = 0; i < importedValueNames.size(); i++) {
//			String s = importedValueNames.get(i);
//			if (s.equals(name)) return true;
//		}
		return false;
	}
	
	@Override public IScope getParent() { return parentScope; }
	@Override public void setParent(IScope scopeIn) { parentScope = scopeIn; }
	
}
