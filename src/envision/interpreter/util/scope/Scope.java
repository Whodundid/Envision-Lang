package envision.interpreter.util.scope;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.UndefinedValueError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionChar;
import envision.lang.datatypes.EnvisionString;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class Scope {
	
	private EnvisionInterpreter interpreter;
	protected Scope parentScope = null;
	public final Map<String, Box2<EnvisionDatatype, EnvisionObject>> values = new HashMap();
	public final Map<String, Box2<EnvisionDatatype, EnvisionObject>> importedValues = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
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
		StringBuilder out = new StringBuilder("Scope:{");
		
		if (values.isEmpty() && importedValues.isEmpty()) {
			out.append(" EMPTY ");
		}
		else {
			if (!values.isEmpty()) {
				String tab = (importedValues.isEmpty()) ? "   " : "      ";
				out.append((!importedValues.isEmpty()) ? "\n   Local:\n" : "\n");
				// packages
				out.append(convertMapping(tab, "Packages",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isPackage())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// fields
				out.append(convertMapping(tab, "Fields",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isField())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// methods
				out.append(convertMapping(tab, "Methods",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isFunction())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// classes
				out.append(convertMapping(tab, "Classes",
						values.entrySet().stream()
						 				 .filter(b -> b.getValue().getB().getDatatype().isClass())
						 				 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
			}
			if (!importedValues.isEmpty()) {
				out.append("   Imported:\n");
				int i = 0;
				for (Map.Entry<String, Box2<EnvisionDatatype, EnvisionObject>> o : importedValues
						.entrySet()) {
					out.append("      " + i++ + ": " + o.getKey() + " = " + o.getValue() + "\n");
				}
			}
		}
		
		return out.append("}").toString();
	}
	
	private String convertMapping(String tab, String catName, Iterator<Entry<String, Box2<EnvisionDatatype, EnvisionObject>>> objects) {
		String out = tab + catName + ":\n";
		for (int i = 0; objects.hasNext(); i++) {
			Entry<String, Box2<EnvisionDatatype, EnvisionObject>> o = objects.next();
			EnvisionObject obj = o.getValue().getB();
			String objS = "";
			if (obj != null) {
				objS += ", " + obj.getVisibility().name();
				if (obj.isFinal()) objS += "_final";
				if (obj.isStatic()) objS += "_static";
				if (obj.isStrong()) objS += "_strong";
			}
			Box2<EnvisionDatatype, EnvisionObject> box = o.getValue();
			String obj_output = null;
			if (obj instanceof EnvisionString) obj_output = "\"" + obj_output + "\"";
			else if (obj instanceof EnvisionChar) obj_output = "'" + obj_output + "'";
			else obj_output = String.valueOf(obj);
			out += tab + tab + i + ": " + o.getKey() + " = [" + box.getA() + ", " + obj_output + objS + "]\n";
		}
		return out;
	}
	
	//---------
	// Methods
	//---------
	
	public Stream<EnvisionObject> getObjectsAsStream() {
		return values.entrySet().stream().map(b -> b.getValue().getB());
	}
	
	public EArrayList<EnvisionObject> getFields() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isField())
								   .sorted((a, b) -> a.getName().compareTo(b.getName()))
								   .collect(EArrayList.toEArrayList());
	}
	
	public EArrayList<EnvisionObject> getMethods() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isFunction())
								   .sorted((a, b) -> a.getName().compareTo(b.getName()))
				   				   .collect(EArrayList.toEArrayList());
	}
	
	public EArrayList<EnvisionObject> getClasses() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isClass())
								   .sorted((a, b) -> a.getName().compareTo(b.getName()))
				   				   .collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Returns a list of all values on this immediate scope. Does not
	 * check parents.
	 */
	public EArrayList<EnvisionObject> values() {
		return values.entrySet().stream().map(b -> b.getValue().getB())
				.collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Returns a list of all fields on this immediate scope. Does not
	 * check parents.
	 */
	public EArrayList<EnvisionObject> fields() {
		return values.entrySet().stream()
				.filter(b -> !b.getValue().getB().getDatatype().isFunction())
				.map(b -> b.getValue().getB()).collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Returns a list of all methods on this immediate scope. Does not
	 * check parents.
	 */
	public EArrayList<EnvisionFunction> methods() {
		return values.entrySet().stream()
				.filter(b -> b.getValue().getB().getDatatype().isFunction())
				.map(b -> b.getValue().getB()).map(b -> (EnvisionFunction) b)
				.collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Defines a variable on this scope.
	 */
	public EnvisionObject define(Token name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : EnvisionDatatype.prim_null();
		return define(name, type, object);
	}
	
	public EnvisionObject define(Token name, Primitives typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? new EnvisionDatatype(typeIn) : EnvisionDatatype.prim_null();
		return define(name, type, object);
	}
	
	/**
	 * Defines a variable on this scope.
	 */
	public EnvisionObject define(Token name, EnvisionDatatype type, EnvisionObject object) {
		return define(name.lexeme, type, object);
	}
	
	public EnvisionObject define(String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : EnvisionDatatype.prim_null();
		return define(name, type, object);
	}
	
	public EnvisionObject define(String name, Primitives typeIn, EnvisionObject object) {
		return define(name, new EnvisionDatatype(typeIn), object);
	}
	
	public EnvisionObject define(String name, EnvisionDatatype type, EnvisionObject obj) {
		if (interpreter.codeFile().getName().equals(name) && !(obj instanceof EnvisionClass)) {
			throw new EnvisionError(
					"Only classes are permitted to be defined under the same name as the containing file! ("
					+ name + ")");
		}
		values.put(name, new Box2<EnvisionDatatype, EnvisionObject>(type, obj));
		return obj;
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	public EnvisionObject defineAt(int dist, Token name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : EnvisionDatatype.prim_null();
		return defineAt(dist, name.lexeme, type, object);
	}
	
	public EnvisionObject defineAt(int dist, String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : EnvisionDatatype.prim_null();
		return defineAt(dist, name, type, object);
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	public EnvisionObject defineAt(int dist, Token name, Primitives typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? new EnvisionDatatype(typeIn) : EnvisionDatatype.prim_null();
		return defineAt(dist, name.lexeme, type, object);
	}
	
	public EnvisionObject defineAt(int dist, String name, Primitives typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? new EnvisionDatatype(typeIn) : EnvisionDatatype.prim_null();
		return defineAt(dist, name, type, object);
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	public EnvisionObject defineAt(int dist, Token name, EnvisionDatatype typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? typeIn : EnvisionDatatype.prim_null();
		return defineAt(dist, name.lexeme, type, object);
	}
	
	public EnvisionObject defineAt(int dist, String name, EnvisionDatatype typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? typeIn : EnvisionDatatype.prim_null();
		parentAt(dist).values.put(name, new Box2<EnvisionDatatype, EnvisionObject>(type, object));
		return object;
	}
	
	/**
	 * Defines an imported variable on this scope.
	 */
	public EnvisionObject defineImportVal(EnvisionObject obj) {
		return defineImportVal(obj.getName(), obj.getDatatype(), obj);
	}
	
	/**
	 * Defines an imported variable on this scope.
	 */
	public EnvisionObject defineImportVal(String name, EnvisionDatatype type, EnvisionObject object) {
		importedValues.put(name, new Box2(type, object));
		return object;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns true if a value under the given name is present. */
	public boolean exists(Token name) {
		return get(name) != null;
	}
	
	/**
	 * Returns true if a variable under the given 'name' is locally
	 * defined within this immediate scope.
	 * <p>
	 * Note: this method completely ignores parent scopes.
	 * 
	 * @param name The variable name to be searched for
	 * @return True if a variable under the given 'name' exists
	 */
	public boolean existsLocally(Token name) {
		return values.containsKey(name.lexeme);
	}
	
	public boolean existsLocally(String name) {
		return values.containsKey(name);
	}
	
	/**
	 * Returns a value associated with the given name identifier.
	 * 
	 * @param name The variable name to be searched for
	 * @return A variable under the given 'name' if it exists
	 */
	public EnvisionObject get(Token name) {
		var box = getI(name);
		return (box != null) ? box.getB() : null;
	}
	
	public EnvisionObject get(String name) {
		var box = getI(name);
		return (box != null) ? box.getB() : null;
	}
	
	public Box2<EnvisionDatatype, EnvisionObject> getTyped(Token name) {
		return getI(name.lexeme);
	}
	
	public Box2<EnvisionDatatype, EnvisionObject> getTyped(String name) {
		return getI(name);
	}
	
	private Box2<EnvisionDatatype, EnvisionObject> getI(Token name) throws EnvisionError {
		return getI(name.lexeme);
	}
	
	/**
	 * Attempts to return an object of the same name from this scope as
	 * well as any encompassing parent scopes.
	 */
	private Box2<EnvisionDatatype, EnvisionObject> getI(String name) throws EnvisionError {
		var obj = values.getOrDefault(name, importedValues.get(name));
		
		if (obj == null) {
			Scope curScope = parentScope;
			
			while (curScope != null) {
				obj = curScope.getI(name);
				if (obj == null) obj = curScope.getI(name);
				if (obj == null) curScope = curScope.getParentScope();
				else break;
			}
		}
		
		return obj;
	}
	
	/**
	 * Returns the internal datatype associated with the given identifier.
	 */
	public EnvisionDatatype getInternalType(Token name) throws EnvisionError {
		EnvisionObject o = get(name);
		return (o != null) ? o.getDatatype() : null;
	}
	
	/**
	 * Returns the string representation of the given identifier.
	 */
	public EnvisionDatatype getType(Token name) throws EnvisionError {
		EnvisionObject o = get(name.lexeme);
		return o.getDatatype();
	}
	
	/**
	 * Returns the string representation of the given identifier.
	 */
	public EnvisionDatatype getType(String name) throws EnvisionError {
		EnvisionObject o = get(name);
		return o.getDatatype();
	}
	
	public void set(Token name, EnvisionObject value) {
		set(name.lexeme, (value != null) ? value.getDatatype() : EnvisionDatatype.prim_null(), value);
	}
	
	public void set(String name, EnvisionObject value) {
		set(name, (value != null) ? value.getDatatype() : EnvisionDatatype.prim_null(), value);
	}
	
	public void set(Token name, Primitives type, EnvisionObject value) {
		set(name.lexeme, (type != null) ? new EnvisionDatatype(type) : EnvisionDatatype.prim_null(), value);
	}
	
	public void set(String name, Primitives type, EnvisionObject value) {
		set(name, (type != null) ? new EnvisionDatatype(type) : EnvisionDatatype.prim_null(), value);
	}
	
	/**
	 * Modifies the value of an already existing object within this scope.
	 * If the object is not actually defined, and error is thrown instead.
	 */
	public void set(Token name, EnvisionDatatype type, EnvisionObject value) {
		var b = getI(name.lexeme);
		if (b == null) throw new UndefinedValueError(name);
		b.setB(value);
	}
	
	/**
	 * Modifies the value of an already existing object within this scope.
	 * If the object is not actually defined, and error is thrown instead.
	 */
	public void set(String name, EnvisionDatatype type, EnvisionObject value) {
		var b = getI(name);
		if (b == null) throw new UndefinedValueError(name);
		b.setB(value);
	}
	
	/**
	 * Modifies an already defined object at a specific depth.
	 */
	public void setAt(int dist, Token name, EnvisionObject value) {
		parentAt(dist).set(name.lexeme, value);
	}
	
	/**
	 * Modifies an already defined object at a specific depth.
	 */
	public void setAt(int dist, String name, EnvisionObject value) {
		parentAt(dist).set(name, value);
	}
	
	/**
	 * Returns an already defined object at a specific depth.
	 */
	public EnvisionObject getAt(int dist, Token name) {
		var b = parentAt(dist).values.get(name.lexeme);
		return (b != null) ? b.getB() : null;
	}
	
	/**
	 * Returns an already defined object at a specific depth.
	 */
	public EnvisionObject getAt(int dist, String name) {
		var b = parentAt(dist).values.get(name);
		return (b != null) ? b.getB() : null;
	}
	
	/**
	 * Ignores parent scopes when searching for a specific variable under
	 * the given 'name'.
	 * <p>
	 * Returns null if the variable is not defined within this immediate
	 * scope.
	 * 
	 * @param name The name of a variable to be searched for
	 * @return A variable under the given name
	 */
	public EnvisionObject getLocal(Token name) {
		var typedBox = values.get(name.lexeme);
		return (typedBox != null) ? typedBox.getB() : null;
	}
	
	/**
	 * Ignores parent scopes when searching for a specific variable under
	 * the given 'name'.
	 * <p>
	 * Returns null if the variable is not defined within this immediate
	 * scope.
	 * 
	 * @param name The name of a variable to be searched for
	 * @return A variable under the given name
	 */
	public EnvisionObject getLocal(String name) {
		var typedBox = values.get(name);
		return (typedBox != null) ? typedBox.getB() : null;
	}
	
	/**
	 * Ignores parent scopes when searching for a specific variable under
	 * the given 'name'.
	 * <p>
	 * Returns null if the variable is not defined within this immediate
	 * scope.
	 * 
	 * @param name The name of a variable to be searched for
	 * @return A variable and its type under the given name
	 */
	public EnvisionObject getTypedLocal(Token name) {
		var typedBox = values.get(name.lexeme);
		return (typedBox != null) ? typedBox.getB() : null;
	}
	
	/**
	 * Ignores parent scopes when searching for a specific variable under
	 * the given 'name'.
	 * <p>
	 * Returns null if the variable is not defined within this immediate
	 * scope.
	 * 
	 * @param name The name of a variable to be searched for
	 * @return A variable and its type under the given name
	 */
	public EnvisionObject getTypedLocal(String name) {
		var typedBox = values.get(name);
		return (typedBox != null) ? typedBox.getB() : null;
	}
	
	public EnvisionObject modifyDatatype(String name, EnvisionDatatype newType) {
		var typedBox = getI(name);
		
		//assign new type
		if (typedBox != null) {
			typedBox.setA(newType);
		}
		
		return (typedBox != null) ? typedBox.getB() : null;
	}
	
	/**
	 * Gets a parent level scope containing this one at a specific parent
	 * level.
	 * 
	 * @param The depth of the desired parent scope
	 * @return The scope at the specified parent level
	 */
	public Scope parentAt(int dist) {
		Scope s = this;
		for (int i = 0; i < dist; i++) {
			s = s.parentScope;
		}
		return s;
	}
	
	/**
	 * Returns the immediate parent scope. If the parent scope is null,
	 * this is the global file scope.
	 * 
	 * @return The immediate parent scope of this scope
	 */
	public Scope getParentScope() {
		return parentScope;
	}
	
	public void setParentScope(Scope in) {
		parentScope = in;
	}
	
}
