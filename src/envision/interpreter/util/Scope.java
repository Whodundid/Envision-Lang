package envision.interpreter.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.UndefinedValueError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.VariableUtil;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Scope {
	
	private EnvisionInterpreter interpreter;
	protected Scope parentScope = null;
	public final Map<String, Box2<String, EnvisionObject>> values = new HashMap();
	public final Map<String, Box2<String, EnvisionObject>> importedValues = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	public Scope() {}
	
	public Scope(EnvisionInterpreter intIn) {
		interpreter = intIn;
	}
	
	public Scope(Scope parentScopeIn) {
		interpreter = parentScopeIn.interpreter;
		parentScope = parentScopeIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		String out = "Scope:{";
		
		if (values.isEmpty() && importedValues.isEmpty()) { out += " EMPTY "; }
		else {
			if (!values.isEmpty()) {
				String tab = (importedValues.isEmpty()) ? "   " : "      ";
				out += (!importedValues.isEmpty()) ? "\n   Local:\n" : "\n";
				//fields
				out += convertMapping(tab, "Fields", values.entrySet().stream()
																	  .filter(b -> b.getValue().getB().getInternalType().isField())
																	  .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
																	  .iterator()
									 );
				//methods
				out += convertMapping(tab, "Methods", values.entrySet().stream()
						 											   .filter(b -> b.getValue().getB().getInternalType().isMethod())
						 											   .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
						 											   .iterator()
						 			 );
				//classes
				out += convertMapping(tab, "Classes", values.entrySet().stream()
						 											   .filter(b -> !b.getValue().getB().getInternalType().isMethod())
						 											   .filter(b -> !b.getValue().getB().getInternalType().isField())
						 											   .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
						 											   .iterator()
						 			 );
			}
			if (!importedValues.isEmpty()) {
				out += "   Imported:\n";
				int i = 0;
				for (Map.Entry<String, Box2<String, EnvisionObject>> o : importedValues.entrySet()) {
					out += "      " + i++ + ": " + o.getKey() + " = " + o.getValue() + "\n";
				}
			}
		}
		
		return out + "}";
	}
	
	private String convertMapping(String tab, String catName, Iterator<Entry<String, Box2<String, EnvisionObject>>> objects) {
		String out = tab + catName + ":\n";
		for (int i = 0; objects.hasNext(); i++) {
			Entry<String, Box2<String, EnvisionObject>> o = objects.next();
			EnvisionObject obj = o.getValue().getB();
			String objS = "";
			if (obj != null) {
				objS += ", " + obj.getVisibility();
				if (obj.isFinal()) { objS += "_final"; }
				if (obj.isStatic()) { objS += "_static"; }
				if (obj.isStrong()) { objS += "_strong"; }
			}
			Box2<String, EnvisionObject> box = o.getValue();
			out += tab + tab + i + ": " + o.getKey() + " = [" + box.getA() + ", " + box.getB() + objS + "]\n";
		}
		return out;
	}
	
	//---------
	// Methods
	//---------
	
	/** Returns a list of all values on this immediate scope. Does not check parents. */
	public EArrayList<EnvisionObject> values() {
		return values.entrySet().stream()
								.map(b -> b.getValue().getB())
								.collect(EArrayList.toEArrayList());
	}
	
	/** Returns a list of all fields on this immediate scope. Does not check parents. */
	public EArrayList<EnvisionObject> fields() {
		return values.entrySet().stream()
								.filter(b -> b.getValue().getB().getInternalType().isField())
								.map(b -> b.getValue().getB())
								.collect(EArrayList.toEArrayList());
	}
	
	/** Returns a list of all methods on this immediate scope. Does not check parents. */
	public EArrayList<EnvisionMethod> methods() {
		return values.entrySet().stream()
								.filter(b -> b.getValue().getB().getInternalType().isMethod())
								.map(b -> b.getValue().getB())
								.map(b -> (EnvisionMethod) b)
								.collect(EArrayList.toEArrayList());
	}
	
	/** Defines a variable on this scope. */
	public EnvisionObject define(String name, EnvisionObject object) { return define(name, (object != null) ? object.getTypeString() : "null", object); }
	public EnvisionObject define(String name, EnvisionDataType type, EnvisionObject object) { return define(name, (type != null) ? type.type : "null", object); }
	
	/** Defines a variable on this scope. */
	public EnvisionObject define(String name, String type, EnvisionObject object) {
		if (interpreter.codeFile().getName().equals(name) && !(object instanceof EnvisionClass)) {
			throw new EnvisionError("Only classes are permitted to be defined under the same name as the containing file! (" + name + ")");
		}
		values.put(name, new Box2(type, object));
		return object;
	}
	
	/** Defines a variable on this scope. */
	public EnvisionObject defineImportVal(String name, EnvisionObject object) { return defineImportVal(name, (object != null) ? object.getTypeString() : "null", object); }
	public EnvisionObject defineImportVal(String name, EnvisionDataType type, EnvisionObject object) { return defineImportVal(name, type.type, object); }
	
	/** Defines a variable on this scope. */
	public EnvisionObject defineImportVal(String name, String type, EnvisionObject object) {
		importedValues.put(name, new Box2(type, object));
		return object;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns true if a value under the given name is present. */
	public boolean exists(String name) {
		return get(name) != null;
	}
	
	/** Returns a value associated with the given name identifier. */
	public EnvisionObject get(String name) {
		Box2<String, EnvisionObject> box = getI(name);
		return (box != null) ? box.getB() : null;
	}
	
	public Box2<String, EnvisionObject> getTyped(String name) {
		return getI(name);
	}
	
	/** Attempts to return an object of the same name from this scope as well as any encompassing parent scopes. */
	private Box2<String, EnvisionObject> getI(String name) throws EnvisionError {
		Box2<String, EnvisionObject> obj = values.get(name);
		if (obj == null) { obj = importedValues.get(name); }
		
		if (obj == null) {
			Scope curScope = parentScope;
			
			while (curScope != null) {
				obj = curScope.getI(name);
				if (obj == null) { obj = curScope.getI(name); }
				
				if (obj == null) {
					curScope = curScope.getParentScope();
				}
				else { break; }
			}
		}
		
		return obj;
	}
	
	/** Returns the internal datatype associated with the given identifier. */
	public EnvisionDataType getInternalType(String name) throws EnvisionError {
		EnvisionObject o = get(name);
		if (o != null) { return o.getInternalType(); }
		return null;
	}
	
	/** Returns the string representation of the given identifier. */
	public String getType(String name) throws EnvisionError {
		EnvisionObject o = get(name);
		return VariableUtil.getTypeString(o);
	}
	
	
	public void set(String name, EnvisionObject value) { set(name, (value != null) ? value.getTypeString() : "null", value); }
	public void set(String name, EnvisionDataType type, EnvisionObject value) { set(name, type.type, value); }
	
	/** Modifies the value of an already existing object within this scope.
	 *  If the object is not actually defined, and error is thrown instead. */
	public void set(String name, String type, EnvisionObject value) {
		/*
		Scope curScope = this;
		EnvisionObject obj = values.get(name);
		
		if (obj == null) {
			curScope = parentScope;
			
			while (curScope != null) {
				obj = curScope.get(name);
				if (obj == null) {
					curScope = curScope.getParentScope();
				}
				else { break; }
			}
		}
		
		if (obj == null) { throw new UndefinedValueError(name); }
		curScope.values.put(name, value);
		*/
		Box2<String, EnvisionObject> b = getI(name);
		if (b == null) { throw new UndefinedValueError(name); }
		b.setB(value);
	}
	
	/** Modifies an already defined object at a specific depth. */
	public void setAt(int dist, String name, EnvisionObject value) { parentAt(dist).set(name, value); }
	
	/** Returns an already defined object at a specific depth. */
	public EnvisionObject getAt(int dist, String name) {
		Box2<String, EnvisionObject> b = parentAt(dist).values.get(name);
		return (b != null) ? b.getB() : null;
	}
	
	/** Defines an object at a specific depth. */
	//public EnvisionObject defineAt(int dist, String name, EnvisionObject object) {
	//	return parentAt(dist).values.put(name, object);
	//}
	
	/** Gets a parent level scope containing this one at a specific parent level. */
	public Scope parentAt(int dist) {
		Scope s = this;
		for (int i = 0; i < dist; i++) {
			s = s.parentScope;
		}
		return s;
	}
	
	/** Returns the immediate parent scope. If the parent is null, this is the global file scope. */
	public Scope getParentScope() { return parentScope; }
	
	public void setParentScope(Scope in) { parentScope = in; }
	
}
