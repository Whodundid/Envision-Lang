package envision_lang;

import java.util.Map;

import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class ScopeManager implements IScope {
	
	private IScope scope;
	
	public ScopeManager() { this(new Scope()); }
	public ScopeManager(IScope scopeIn) { scope = scopeIn; }
	@Override public String toString() { return scope.toString(); }

	public ScopeManager def(String name, EnvisionObject o) { scope.define(name, o); return this; }
	public ScopeManager defBool(String name, boolean val) { return def(name, EnvisionBooleanClass.valueOf(val)); } 
	public ScopeManager defChar(String name, char val) { return def(name, EnvisionCharClass.valueOf(val)); }
	public ScopeManager defInt(String name, long val) { return def(name, EnvisionIntClass.valueOf(val)); }
	public ScopeManager defDouble(String name, double val) { return def(name, EnvisionDoubleClass.valueOf(val)); }
	public ScopeManager defString(String name, String val) { return def(name, EnvisionStringClass.valueOf(val)); }
	
	public ScopeManager defVar(String name, Object val) {
		IDatatype type = IDatatype.dynamicallyDetermineType(val);
		define(name, EnvisionStaticTypes.VAR_TYPE, ObjectCreator.createObject(type, val));
		return this;
	}

	@Override public Map<String, ScopeEntry> values() { return scope.values(); }
	@Override public Map<String, ScopeEntry> imports() { return scope.imports(); }
	@Override public IScope getParent() { return scope.getParent(); }
	@Override public void setParent(IScope scopeIn) { scope.setParent(scopeIn); }
	
	@Override public boolean doesValueExist(String name) { return scope.doesValueExist(name); }
	@Override public boolean doesImportedValueExist(String name) { return scope.doesImportedValueExist(name); }
	
	@Override public EnvisionObject define_i(String name, ScopeEntry entry) { return scope.define_i(name, entry); }
	@Override public EnvisionObject defineImportVal_i(String name, ScopeEntry entry) { return scope.defineImportVal_i(name, entry); }
}