package envision_lang.interpreter.util.scope;

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
import envision_lang.lang.natives.StaticTypes;
import envision_lang.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringBuilder;

public interface IScope {
	
	/** Returns the immediate local values of this scope. */
	Map<String, Box2<IDatatype, EnvisionObject>> values();
	/** Returns the values that have been imported into this scope from another. */
	Map<String, Box2<IDatatype, EnvisionObject>> imports();
	
	//---------
	// Parents
	//---------
	
	/**
	 * Returns the immediate parent scope. If the parent scope is null,
	 * this is the global file scope.
	 * 
	 * @return The immediate parent scope of this scope
	 */
	IScope getParent();
	
	/**
	 * Sets the immediate parent scope. If the parent scope is null,
	 * this is the global file scope.
	 * 
	 * @param scopeIn the incoming parent scope
	 */
	void setParent(IScope scopeIn);
	
	/**
	 * Gets a parent level scope containing this one at a specific parent
	 * level.
	 * 
	 * @param The depth of the desired parent scope
	 * @return The scope at the specified parent level
	 */
	default IScope parentAt(int dist) {
		IScope s = this;
		for (int i = 0; i < dist; i++) {
			s = s.getParent();
		}
		return s;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns true if a value under the given name is present.
	 */
	default boolean exists(String name) {
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
	default boolean existsLocally(String name) {
		return values().containsKey(name);
	}
	
	//----------------------
	// Variable Definitions
	//----------------------
	
	default EnvisionObject define(String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : StaticTypes.NULL_TYPE;
		return define(name, type, object);
	}
	
	default EnvisionObject define(String name, IDatatype type, EnvisionObject obj) {
		/*
		if (interpreter.codeFile().getFileName().equals(name) && !(obj instanceof EnvisionClass)) {
			throw new EnvisionError(
					"Only classes are permitted to be defined under the same name as the containing file! ("
							+ name + ")");
		}
		*/
		values().put(name, new Box2<>(type, obj));
		return obj;
	}
	
	/**
	 * Defines the given function within this scope.
	 * 
	 * @param func The function to be defined
	 * @return The defined function
	 */
	default EnvisionFunction defineFunction(EnvisionFunction func) {
		values().put(func.getFunctionName(), new Box2<>(func.getDatatype(), func));
		return func;
	}
	
	/**
	 * Defines a function prototype within this scope.
	 * 
	 * @param prototype The function prototype to be defined
	 * @return The defined prototype
	 */
	default FunctionPrototype defineFunctionPrototype(FunctionPrototype prototype) {
		var boxedType = new Box2<IDatatype, EnvisionObject>(prototype.getDatatype(), prototype);
		values().put(prototype.getFunctionName(), boxedType);
		return prototype;
	}
	
	/**
	 * Defines a new class onto this scope.
	 * 
	 * @param the_class The class being defined
	 * @return The class being defined
	 */
	default EnvisionClass defineClass(EnvisionClass the_class) {
		Box2<IDatatype, EnvisionObject> typedObject = new Box2();
		typedObject.set(the_class.getDatatype(), the_class);
		values().put(the_class.getClassName(), typedObject);
		return the_class;
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	default EnvisionObject defineAt(int dist, String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : StaticTypes.NULL_TYPE;
		return defineAt(dist, name, type, object);
	}
	
	/**
	 * Defines an object at a specific depth.
	 */
	default EnvisionObject defineAt(int dist, String name, IDatatype typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? typeIn : StaticTypes.NULL_TYPE;
		parentAt(dist).values().put(name, new Box2<>(type, object));
		return object;
	}
	
	/**
	 * Defines an imported variable on this scope.
	 */
	default EnvisionObject defineImportVal(String name, EnvisionObject object) {
		var type = (object != null) ? object.getDatatype() : StaticTypes.NULL_TYPE;
		return defineImportVal(name, type, object);
	}
	
	/**
	 * Defines an imported variable on this scope.
	 */
	default EnvisionObject defineImportVal(String name, IDatatype typeIn, EnvisionObject object) {
		var type = (typeIn != null) ? typeIn : StaticTypes.NULL_TYPE;
		imports().put(name, new Box2(type, object));
		return object;
	}
	
	//----------------
	// Mapped Getters
	//----------------
	
	/**
	 * Returns a list of all values on this immediate scope. Does not
	 * check parents.
	 */
	default EArrayList<EnvisionObject> locals() {
		return values().entrySet().stream()
								  .map(b -> b.getValue().getB())
								  .collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Returns a list of all fields on this immediate scope. Does not
	 * check parents.
	 */
	default EArrayList<EnvisionObject> fields() {
		return values().entrySet().stream()
								  .filter(b -> !b.getValue().getB().getDatatype().isFunction())
								  .map(b -> b.getValue().getB()).collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Returns a BoxList containing all values and their associated field name
	 * within this immediate scope. Does not check parent scopes.
	 */
	default BoxList<String, EnvisionObject> named_fields() {
		BoxList<String, EnvisionObject> named_fields = new BoxList<>();
		values().entrySet().stream()
						   .filter(b -> !b.getValue().getB().getDatatype().isFunction())
						   .forEach(b -> named_fields.add(b.getKey(), b.getValue().getB()));
		return named_fields;
	}
	
	/**
	 * Returns a list of all functions on this immediate scope. Does not
	 * check parents.
	 */
	default EArrayList<EnvisionFunction> functions() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().getB().getDatatype().isFunction())
								  .map(b -> b.getValue().getB()).map(b -> (EnvisionFunction) b)
								  .collect(EArrayList.toEArrayList());
	}
	
	/**
	 * Clears this immediate scope of all defined values as well as any
	 * imported ones.
	 */
	default void clear() {
		values().clear();
		imports().clear();
	}
	
	default void clearAll() {
		//clear locally
		clear();
		
		//now clear all parent scopes
		IScope p = getParent();
		while (p != null) {
			p.clear();
			p = p.getParent();
		}
	}
	
	default Stream<EnvisionObject> getObjectsAsStream() {
		return values().entrySet().stream().map(b -> b.getValue().getB());
	}
	
	default List<EnvisionObject> getObjectsAsList() {
		return getObjectsAsStream().toList();
	}
	
	default List<EnvisionObject> getFields() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isField()).toList();
	}
	
	default List<EnvisionFunction> getFunctions() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isFunction())
								   .map(o -> (EnvisionFunction) o)
				   				   .toList();
	}
	
	default List<EnvisionClass> getClasses() {
		return getObjectsAsStream().filter(o -> o.getDatatype().isClass())
								   .map(o -> (EnvisionClass) o)
				   				   .toList();
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns a value associated with the given name identifier.
	 * 
	 * @param name The variable name to be searched for
	 * @return A variable under the given 'name' if it exists
	 */
	default EnvisionObject get(String name) {
		var box = getTyped(name);
		return (box != null) ? box.getB() : null;
	}
	
	/**
	 * Attempts to return an object of the same name from this scope as
	 * well as any encompassing parent scopes.
	 * 
	 * (extracts the lexeme of a Token for the name)
	 */
	default Box2<IDatatype, EnvisionObject> getTyped(Token name) {
		return getTyped(name.lexeme);
	}
	
	/**
	 * Attempts to return an object of the same name from this scope as
	 * well as any encompassing parent scopes.
	 */
	default Box2<IDatatype, EnvisionObject> getTyped(String name) {
		var obj = values().getOrDefault(name, imports().get(name));
		
		if (obj == null) {
			IScope curScope = getParent();
			
			while (curScope != null) {
				obj = curScope.getTyped(name);
				if (obj == null) obj = curScope.getTyped(name);
				if (obj == null) curScope = curScope.getParent();
				else break;
			}
		}
		
		return obj;
	}
	
	/**
	 * Returns the internal datatype associated with the given identifier.
	 */
	default IDatatype getInternalType(String name) throws EnvisionLangError {
		EnvisionObject o = get(name);
		return o.getDatatype();
	}
	
	/**
	 * Returns an already defined object at a specific depth.
	 */
	default EnvisionObject getAt(int dist, String name) {
		var b = parentAt(dist).values().get(name);
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
	default EnvisionObject getLocal(String name) {
		var typedBox = values().get(name);
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
	default Box2<IDatatype, EnvisionObject> getTypedLocal(String name) {
		return values().get(name);
	}
	
	//---------
	// Setters
	//---------
	
	default void set(String name, EnvisionObject value) {
		if (value == null) throw new NullVariableError();
		set(name, value.getDatatype(), value);
	}
	
	/**
	 * Modifies the value of an already existing object within this scope.
	 * If the object is not actually defined, and error is thrown instead.
	 */
	default void set(String name, IDatatype type, EnvisionObject value) {
		var b = getTyped(name);
		if (b == null) throw new UndefinedValueError(name);
		if (value == null) throw new NullVariableError();
		b.setB(value);
	}
	
	/**
	 * Modifies an already defined object at a specific depth.
	 */
	default void setAt(int dist, String name, EnvisionObject value) {
		parentAt(dist).set(name, value);
	}
	
	default EnvisionObject modifyDatatype(String name, IDatatype newType) {
		var typedBox = getTyped(name);
		
		//assign new type
		if (typedBox != null) {
			typedBox.setA(newType);
		}
		
		return (typedBox != null) ? typedBox.getB() : null;
	}
	
	//-------------------
	// Utility Functions
	//-------------------
	
	/**
	 * Converts this scope to an easier to understand visual representation.
	 * 
	 * @return ToString for IScope objects
	 */
	static String asString(IScope scopeIn) {
		EStringBuilder out = new EStringBuilder("Scope:{");
		
		var values = scopeIn.values();
		var importedValues = scopeIn.imports();
		
		if (values.isEmpty() && importedValues.isEmpty()) {
			out.a(" EMPTY ");
		}
		else {
			if (!importedValues.isEmpty()) {
				out.a("\n   Imported:\n");
				int i = 0;
				for (Map.Entry<String, Box2<IDatatype, EnvisionObject>> o : importedValues.entrySet()) {
					out.a("      ", i++, ": ", o.getKey(), " = ", o.getValue(), "\n");
				}
			}
			
			if (!values.isEmpty()) {
				String tab = (importedValues.isEmpty()) ? "   " : "      ";
				out.a((!importedValues.isEmpty()) ? "\n   Local:\n" : "\n");
				// packages
				out.a(convertMapping(tab, "Packages",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isPackage())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// fields
				out.a(convertMapping(tab, "Fields",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isField())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// methods
				out.a(convertMapping(tab, "Functions",
						values.entrySet().stream()
										 .filter(b -> b.getValue().getB().getDatatype().isFunction())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// classes
				out.a(convertMapping(tab, "Classes",
						values.entrySet().stream()
						 				 .filter(b -> b.getValue().getB().getDatatype().isClass())
						 				 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
			}
		}
		
		return out.a("}").toString();
	}
	
	private static String convertMapping(String tab, String catName, Iterator<Entry<String, Box2<IDatatype, EnvisionObject>>> objects) {
		//if there aren't any values for the given type, return empty
		if (!objects.hasNext()) return "";
		
		EStringBuilder out = EStringBuilder.of(tab, catName, ":\n");
		for (int i = 0; objects.hasNext(); i++) {
			Entry<String, Box2<IDatatype, EnvisionObject>> o = objects.next();
			EnvisionObject obj = o.getValue().getB();
			
			EStringBuilder objS = new EStringBuilder();
			if (obj != null) {
				//objS += ", " + obj.getVisibility().name();
				if (obj.isFinal()) objS.a("_final");
				if (obj.isStatic()) objS.a("_static");
				if (obj.isStrong()) objS.a("_strong");
			}
			
			Box2<IDatatype, EnvisionObject> box = o.getValue();
			EStringBuilder obj_output = EStringBuilder.of(obj.getVisibility().lexeme);
			if (obj instanceof EnvisionString) obj_output.a("\"", obj_output, "\"");
			else if (obj instanceof EnvisionChar) obj_output.a("'", obj_output, "'");
			else obj_output.a(obj);
			
			out.a(tab, tab, i, ": ", o.getKey(), " = [", box.getA(), ", ", obj_output, objS, "]\n");
		}
		
		return out.toString();
	}
	
}
