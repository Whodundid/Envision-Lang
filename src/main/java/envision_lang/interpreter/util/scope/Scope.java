package envision_lang.interpreter.util.scope;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.UndefinedValueError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionChar;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.StaticTypes;
import envision_lang.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;

public class Scope {
	
	//private EnvisionInterpreter interpreter;
	protected Scope parentScope = null;
	public final Map<String, Box2<IDatatype, EnvisionObject>> values = new HashMap();
	public final Map<String, Box2<IDatatype, EnvisionObject>> importedValues = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	//public Scope(EnvisionInterpreter intIn) {
	//	interpreter = intIn;
	//}
	
	public Scope() {}
	public Scope(Scope parentScopeIn) {
		//interpreter = parentScopeIn.interpreter;
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
										 .sorted(name_sorter).iterator()));
				// fields
				out.append(convertMapping(tab, "Fields",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isField())
										 .sorted(name_sorter).iterator()));
				// methods
				out.append(convertMapping(tab, "Functions",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isFunction())
										 .sorted(name_sorter).iterator()));
				// classes
				out.append(convertMapping(tab, "Classes",
						values.entrySet().stream()
						 				 .filter(b -> b.getValue().getB().getDatatype().isClass())
						 				 .sorted(name_sorter).iterator()));
			}
			if (!importedValues.isEmpty()) {
				out.append("   Imported:\n");
				int i = 0;
				for (Map.Entry<String, Box2<IDatatype, EnvisionObject>> o : importedValues.entrySet()) {
					out.append("      " + i++ + ": " + o.getKey() + " = " + o.getValue() + "\n");
				}
			}
		}
		
		return out.append("}").toString();
	}
	
	private Comparator<? super Entry<String, Box2<IDatatype, EnvisionObject>>> name_sorter =
		(a, b) -> a.getKey().compareTo(b.getKey())
	;
	
	private String convertMapping(String tab, String catName, Iterator<Entry<String, Box2<IDatatype, EnvisionObject>>> objects) {
		String out = tab + catName + ":\n";
		for (int i = 0; objects.hasNext(); i++) {
			Entry<String, Box2<IDatatype, EnvisionObject>> o = objects.next();
			EnvisionObject obj = o.getValue().getB();
			StringBuilder objS = new StringBuilder();
			if (obj != null) {
				//objS += ", " + obj.getVisibility().name();
				if (obj.isFinal()) objS.append("_final");
				if (obj.isStatic()) objS.append("_static");
				if (obj.isStrong()) objS.append("_strong");
			}
			Box2<IDatatype, EnvisionObject> box = o.getValue();
			String obj_output = obj.getVisibility().lexeme;
			if (obj instanceof EnvisionString) obj_output += "\"" + obj_output + "\"";
			else if (obj instanceof EnvisionChar) obj_output += "'" + obj_output + "'";
			else obj_output += String.valueOf(obj);
			out += tab + tab + i + ": " + o.getKey() + " = [" + box.getA() + ", " + obj_output + objS + "]\n";
		}
		return out;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Empties the immediate contents of this scope including all imported
	 * values.
	 */
	public void clear() {
		values.clear();
		importedValues.clear();
	}
	
	protected Stream<EnvisionObject> getObjectsAsStream() {
		return values.entrySet().stream().map(b -> b.getValue().getB());
	}
	
	public List<EnvisionObject> getObjectsAsList() {
		return getObjectsAsStream().toList();
	}
	
	public List<EnvisionObject> getFields() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isField()).toList();
	}
	
	public List<EnvisionFunction> getMethods() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isFunction())
								   .map(o -> (EnvisionFunction) o)
				   				   .toList();
	}
	
	public List<EnvisionClass> getClasses() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isClass())
								   .map(o -> (EnvisionClass) o)
				   				   .toList();
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
	
	public BoxList<String, EnvisionObject> named_fields() {
		BoxList<String, EnvisionObject> named_fields = new BoxList();
		values.entrySet().stream()
						 .filter(b -> !b.getValue().getB().getDatatype().isFunction())
						 .forEach(b -> named_fields.add(b.getKey(), b.getValue().getB()));
		return named_fields;
	}
	
	/**
	 * Returns a list of all methods on this immediate scope. Does not
	 * check parents.
	 */
	public EArrayList<EnvisionFunction> functions() {
		return values.entrySet().stream()
				.filter(b -> b.getValue().getB().getDatatype().isFunction())
				.map(b -> b.getValue().getB()).map(b -> (EnvisionFunction) b)
				.collect(EArrayList.toEArrayList());
	}
	
	public EnvisionObject define(String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : StaticTypes.NULL_TYPE;
		return define(name, type, object);
	}
	
	public EnvisionObject define(String name, IDatatype type, EnvisionObject obj) {
		/*
		if (interpreter.codeFile().getFileName().equals(name) && !(obj instanceof EnvisionClass)) {
			throw new EnvisionError(
					"Only classes are permitted to be defined under the same name as the containing file! ("
							+ name + ")");
		}
		*/
		values.put(name, new Box2<IDatatype, EnvisionObject>(type, obj));
		return obj;
	}
	
	/**
	 * Defines the given function within this scope.
	 * 
	 * @param func The function to be defined
	 * @return The defined function
	 */
	public EnvisionFunction defineFunction(EnvisionFunction func) {
		values.put(func.getFunctionName(), new Box2<IDatatype, EnvisionObject>(func.getDatatype(), func));
		return func;
	}
	
	/**
	 * Defines a function prototype within this scope.
	 * 
	 * @param prototype The function prototype to be defined
	 * @return The defined prototype
	 */
	public FunctionPrototype defineFunctionPrototype(FunctionPrototype prototype) {
		var boxedType = new Box2<IDatatype, EnvisionObject>(prototype.getDatatype(), prototype);
		values.put(prototype.getFunctionName(), boxedType);
		return prototype;
	}
	
	/**
	 * Defines a new class onto this scope.
	 * 
	 * @param the_class The class being defined
	 * @return The class being defined
	 */
	public EnvisionClass defineClass(EnvisionClass the_class) {
		Box2<IDatatype, EnvisionObject> typedObject = new Box2();
		typedObject.set(the_class.getDatatype(), the_class);
		values.put(the_class.getClassName(), typedObject);
		return the_class;
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	public EnvisionObject defineAt(int dist, String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : StaticTypes.NULL_TYPE;
		return defineAt(dist, name, type, object);
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	public EnvisionObject defineAt(int dist, String name, IDatatype typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? typeIn : StaticTypes.NULL_TYPE;
		parentAt(dist).values.put(name, new Box2<IDatatype, EnvisionObject>(type, object));
		return object;
	}
	
	/**
	 * Defines an imported variable on this scope.
	 */
	public EnvisionObject defineImportVal(String name, IDatatype typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? typeIn : StaticTypes.NULL_TYPE;
		importedValues.put(name, new Box2(type, object));
		return object;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns true if a value under the given name is present.
	 */
	public boolean exists(String name) {
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
	public boolean existsLocally(String name) {
		return values.containsKey(name);
	}
	
	/**
	 * Returns a value associated with the given name identifier.
	 * 
	 * @param name The variable name to be searched for
	 * @return A variable under the given 'name' if it exists
	 */
	public EnvisionObject get(String name) {
		var box = getI(name);
		return (box != null) ? box.getB() : null;
	}
	
	public Box2<IDatatype, EnvisionObject> getTyped(Token name) {
		return getI(name.lexeme);
	}
	
	public Box2<IDatatype, EnvisionObject> getTyped(String name) {
		return getI(name);
	}
	
	/**
	 * Attempts to return an object of the same name from this scope as
	 * well as any encompassing parent scopes.
	 */
	private Box2<IDatatype, EnvisionObject> getI(String name) throws EnvisionLangError {
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
	public IDatatype getInternalType(String name) throws EnvisionLangError {
		EnvisionObject o = get(name);
		return o.getDatatype();
	}
	
	public void set(String name, EnvisionObject value) {
		if (value == null) throw new NullVariableError();
		set(name, value.getDatatype(), value);
	}
	
	/**
	 * Modifies the value of an already existing object within this scope.
	 * If the object is not actually defined, and error is thrown instead.
	 */
	public void set(String name, IDatatype type, EnvisionObject value) {
		var b = getI(name);
		if (b == null) throw new UndefinedValueError(name);
		if (value == null) throw new NullVariableError();
		b.setB(value);
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
	public Box2<IDatatype, EnvisionObject> getTypedLocal(String name) {
		return values.get(name);
	}
	
	public EnvisionObject modifyDatatype(String name, IDatatype newType) {
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
