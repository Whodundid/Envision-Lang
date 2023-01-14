package envision_lang.interpreter.util.scope;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.UndefinedValueError;
import envision_lang.exceptions.errors.objects.CopyNotSupportedError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionChar;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.tokenizer.Token;
import eutil.datatypes.BoxList;
import eutil.datatypes.util.EList;
import eutil.debug.Inefficient;
import eutil.math.ENumUtil;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public interface IScope {
	
	/** Returns the immediate local values of this scope. */
	Map<String, ScopeEntry> values();
	/** Returns the values that have been imported into this scope from another. */
	Map<String, ScopeEntry> imports();
	
	/** Returns true if an entry exists under the given name. */
	boolean doesValueExist(String name);
	/** Returns true if an imported entry exists under the given name. */
	boolean doesImportedValueExist(String name);
	
	EnvisionObject define_i(String name, ScopeEntry entry);
	EnvisionObject defineImportVal_i(String name, ScopeEntry entry);
	
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
	
	//===========
	// Overrides
	//===========
	
	/**
	 * Creates a deep copy of this immediate scope. The parent scope is not deep
	 * copied, but it is still set as the cloned scope's immediate parent.
	 * 
	 * @return A deep copy of this scope
	 */
	default IScope copy() {
		// create clone using the same parent scope
		IScope c = new Scope(getParent());
		
		// for each element on this scope, create an entirely new deep copy of each (if possible)
		for (var o : values().entrySet()) {
			String name = o.getKey();
			ScopeEntry entry = o.getValue();
			
			// only copy if the underlying object can actually be copied
			ScopeEntry copy = null;
			try {
				copy = entry.deepCopy();
			}
			catch (CopyNotSupportedError e) {
				// objects that can't be copied will be ignored
			}
			
			if (copy != null) {
				c.define(name, copy);
			}
		}
		
		// shallow copy imports
		for (var o : imports().entrySet()) {
			String name = o.getKey();
			ScopeEntry entry = o.getValue();
			ScopeEntry shallowCopy = entry.shallowCopy();
			
			c.defineImportVal(name, shallowCopy);
		}
		
		return c;
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
	
	default EnvisionObject define(String name, IDatatype typeIn, EnvisionObject obj) {
		var type = (typeIn != null) ? typeIn : StaticTypes.NULL_TYPE;
		var entry = new ScopeEntry(type, obj);
		return define(name, entry);
	}
	
	default EnvisionObject define(String name, ScopeEntry entry) {
		if (name == null) throw new IllegalArgumentException("Scope object cannot have a 'NULL' name!");
		if (entry == null) throw new IllegalArgumentException("Scope entries cannot be entirely 'NULL' !");
		return define_i(name, entry);
	}
	
	/**
	 * Attempts to define the following object onto this scope only if there is
	 * not an object already defined under the same name.
	 * 
	 * @param name The name to define this object under
	 * @param type The Envision datatype of the object
	 * @param obj  The actual object to define
	 * 
	 * @implNote NOTE: use of this method should be significantly limited as it
	 *           is DRAMATICALLY slower than a standard 'define' operation!
	 */
	@Inefficient(reason="The action of simply looking up a name to check for existing presence is VERY slow!")
	default void defineIfNotPresent(String name, IDatatype type, EnvisionObject obj) {
		boolean existCheck = doesValueExist(name);
		if (!existCheck) define(name, type, obj);
	}
	
	/**
	 * Defines the given function within this scope.
	 * 
	 * @param func The function to be defined
	 * @return The defined function
	 */
	default EnvisionFunction defineFunction(EnvisionFunction func) {
		define(func.getFunctionName(), func.getDatatype(), func);
		return func;
	}
	
	/**
	 * Defines a function prototype within this scope.
	 * 
	 * @param prototype The function prototype to be defined
	 * @return The defined prototype
	 */
	default FunctionPrototype defineFunctionPrototype(FunctionPrototype prototype) {
		define(prototype.getFunctionName(), prototype.getDatatype(), prototype);
		return prototype;
	}
	
	/**
	 * Defines a new class onto this scope.
	 * 
	 * @param the_class The class being defined
	 * @return The class being defined
	 */
	default EnvisionClass defineClass(EnvisionClass the_class) {
		define(the_class.getClassName(), the_class.getDatatype(), the_class);
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
		parentAt(dist).define(name, type, object);
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
		var entry = new ScopeEntry(type, object);
		return defineImportVal(name, entry);
	}
	
	/**
	 * Defines an imported variable on this scope.
	 */
	default EnvisionObject defineImportVal(String name, ScopeEntry entry) {
		if (name == null) throw new IllegalArgumentException("Scope object cannot have a 'NULL' name!");
		if (entry == null) throw new IllegalArgumentException("Scope entries cannot be entirely 'NULL' !");
		return defineImportVal_i(name, entry);
	}
	
	//----------------
	// Mapped Getters
	//----------------
	
	default Stream<EnvisionObject> objectsAsStream() {
		return values().entrySet().stream()
								  .map(b -> b.getValue())
								  .map(b -> b.getObject());
	}
	
	default Stream<ScopeEntry> objectEntriesAsStream() {
		return values().entrySet().stream()
								  .map(b -> b.getValue());
	}
	
	/**
	 * Returns a list of all values on this immediate scope. Does not
	 * check parents.
	 */
	default EList<EnvisionObject> objects() {
		return values().entrySet().stream()
								  .map(b -> b.getValue())
								  .map(b -> b.getObject())
								  .collect(EList.toEList());
	}
	
	default EList<ScopeEntry> object_entries() {
		return values().entrySet().stream()
								  .map(b -> b.getValue())
								  .collect(EList.toEList());
	}
	
	default EList<EnvisionClass> classes() {
		return values().entrySet().stream()
								  .map(b -> b.getValue())
								  .filter(b -> b.isClassType())
								  .map(b -> b.getObject())
								  .map(b -> (EnvisionClass) b)
								  .collect(EList.toEList());
	}
	
	default EList<ScopeEntry> class_entries() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isClassType())
								  .map(b -> b.getValue())
								  .collect(EList.toEList());
	}
	
	/**
	 * Returns a list of all fields on this immediate scope. Does not
	 * check parents.
	 */
	default EList<EnvisionObject> fields() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isFieldType())
								  .map(b -> b.getValue())
								  .map(b -> b.getObject())
								  .collect(EList.toEList());
	}
	
	/**
	 * Returns a list of all fields on this immediate scope. Does not
	 * check parents.
	 */
	default EList<ScopeEntry> field_entries() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isFieldType())
								  .map(b -> b.getValue())
								  .collect(EList.toEList());
	}
	
	/**
	 * Returns a BoxList containing all values and their associated field name
	 * within this immediate scope. Does not check parent scopes.
	 */
	default BoxList<String, ScopeEntry> named_fields() {
		BoxList<String, ScopeEntry> named_fields = new BoxList<>();
		values().entrySet().stream()
						   .filter(b -> b.getValue().isFieldType())
						   .forEach(b -> named_fields.add(b.getKey(), b.getValue()));
		return named_fields;
	}
	
	/**
	 * Returns a list of all functions on this immediate scope. Does not
	 * check parents.
	 */
	default EList<EnvisionFunction> functions() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isFunctionType())
								  .map(b -> b.getValue())
								  .map(b -> b.getObject())
								  .map(b -> (EnvisionFunction) b)
								  .collect(EList.toEList());
	}
	
	/**
	 * Returns a list of all functions on this immediate scope. Does not
	 * check parents.
	 */
	default EList<ScopeEntry> function_entries() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isFunctionType())
								  .map(b -> b.getValue())
								  .collect(EList.toEList());
	}
	
	/**
	 * Returns a list of all functions on this immediate scope. Does not
	 * check parents.
	 */
	default EList<EnvisionFunction> operators() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isFunctionType())
								  .map(b -> b.getValue())
								  .map(b -> b.getObject())
								  .map(b -> (EnvisionFunction) b)
								  .filter(b -> b.isOperator())
								  .collect(EList.toEList());
	}
	
	/**
	 * Returns a list of all functions on this immediate scope. Does not
	 * check parents.
	 */
	default EList<ScopeEntry> operator_entries() {
		return values().entrySet().stream()
								  .filter(b -> b.getValue().isFunctionType())
								  .filter(b -> ((EnvisionFunction) b.getValue().getObject()).isOperator())
								  .map(b -> b.getValue())
								  .collect(EList.toEList());
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
		return (box != null) ? box.getObject() : null;
	}
	
	/**
	 * Attempts to return a function from within this scope under the given name.
	 * WARNING! UNSAFE type cast!
	 * 
	 * @param funcName The name of the function to find
	 * @return The function under the given name
	 */
	default EnvisionFunction getFunction(String funcName) {
		var func = get(funcName);
		return (EnvisionFunction) func;
	}
	
	/**
	 * Attempts to return an object of the same name from this scope as
	 * well as any encompassing parent scopes.
	 * 
	 * (extracts the lexeme of a Token for the name)
	 */
	default ScopeEntry getTyped(Token name) {
		return getTyped(name.getLexeme());
	}
	
	/**
	 * Attempts to return an object of the same name from this scope as
	 * well as any encompassing parent scopes.
	 */
	default ScopeEntry getTyped(String name) {
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
		return parentAt(dist).get(name);
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
		return (typedBox != null) ? typedBox.getObject() : null;
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
	default ScopeEntry getTypedLocal(String name) {
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
		b.set(value);
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
			typedBox.modifyDatatype(newType);
		}
		
		return (typedBox != null) ? typedBox.getObject() : null;
	}
	
	//-------------------
	// Utility Functions
	//-------------------
	
	public static String printFullStack(IScope scopeIn) {
		if (scopeIn == null) return null;
		
		var sb = new EStringBuilder("FULL SCOPE: {\n");
		
		class Indenter {
			int level = 0;
			private String get() { return EStringUtil.repeatString(" ", level * 4); }
			public String addIndent() { ENumUtil.clamp(level++, 0, level); return get(); }
			public String subIndent() { ENumUtil.clamp(level--, 0, level); return get(); }
			public String indent(String in) {
				String[] lines = in.replace("\t", "    ").split("\n");
				var builder = new EStringBuilder();
				if (lines.length == 1) builder.a(get(), lines[0]);
				else for (String l : lines) builder.a(get(), l, "\n");
				return builder.toString();
			}
		}
		
		var indenter = new Indenter();
		indenter.addIndent();
		
		IScope cur = scopeIn;
		int depth = 0;
		do {
			sb.a(indenter.indent("DEPTH=" + depth), "\n");
			sb.a(indenter.indent(asString(cur)));
			depth++;
			cur = cur.getParent();
			if (cur != null) sb.println();
		}
		while (cur != null);
		indenter.subIndent();
		sb.a("}");
		
		return sb.toString();
	}
	
	/**
	 * Converts this scope to an easier to understand visual representation.
	 * 
	 * @return ToString for IScope objects
	 */
	public static String asString(IScope scopeIn) {
		return asString(scopeIn, false);
	}
	
	/**
	 * Converts this scope to an easier to understand visual representation.
	 * 
	 * @return ToString for IScope objects
	 */
	public static String asString(IScope scopeIn, boolean printParent) {
		EStringBuilder out = new EStringBuilder("Scope: {");
		
		var values = scopeIn.values();
		var importedValues = scopeIn.imports();
		
		if (values.isEmpty() && importedValues.isEmpty()) {
			out.a(" EMPTY ");
		}
		else {
			if (!importedValues.isEmpty()) {
				out.a("\n   Imported:\n");
				int i = 0;
				var importsSorted = importedValues.entrySet().stream()
															 .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
															 .collect(EList.toEList());
				
				for (var o : importsSorted) {
					out.a("      ", i++, ": ", o.getKey(), " = ", o.getValue(), "\n");
				}
			}
			
			if (!values.isEmpty()) {
				String tab = (importedValues.isEmpty()) ? "   " : "      ";
				out.a((!importedValues.isEmpty()) ? "\n   Local:\n" : "\n");
				// packages
				out.a(convertMapping(tab, "Packages",
						values.entrySet().stream()
										 .filter(b -> b.getValue().isPackageType())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// fields
				out.a(convertMapping(tab, "Fields",
						values.entrySet().stream()
										 .filter(b -> b.getValue().isFieldType())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// methods
				out.a(convertMapping(tab, "Functions",
						values.entrySet().stream()
										 .filter(b -> b.getValue().isFunctionType())
										 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
				// classes
				out.a(convertMapping(tab, "Classes",
						values.entrySet().stream()
						 				 .filter(b -> b.getValue().isClassType())
						 				 .sorted((a, b) -> a.getKey().compareTo(b.getKey())).iterator()));
			}
		}
		
		out.a("}");
		
		if (printParent && scopeIn.getParent() != null) {
			out.a("\nParent " + asString(scopeIn.getParent(), false));
		}
		
		return out.toString();
	}
	
	private static String convertMapping(String tab, String catName, Iterator<Entry<String, ScopeEntry>> objects) {
		//if there aren't any values for the given type, return empty
		if (!objects.hasNext()) return "";
		
		EStringBuilder out = EStringBuilder.of(tab, catName, ":\n");
		for (int i = 0; objects.hasNext(); i++) {
			Entry<String, ScopeEntry> o = objects.next();
			ScopeEntry entry = o.getValue();
			EnvisionObject obj = entry.getObject();
			
			EStringBuilder objS = new EStringBuilder();
			if (obj != null) {
				//objS += ", " + obj.getVisibility().name();
				if (obj.isFinal()) objS.a("_final");
				if (obj.isStatic()) objS.a("_static");
				if (obj.isStrong()) objS.a("_strong");
			}
			
			EStringBuilder obj_output = EStringBuilder.of(obj.getVisibility().lexeme);
			if (obj instanceof EnvisionString) obj_output.a("\"", obj_output, "\"");
			else if (obj instanceof EnvisionChar) obj_output.a("'", obj_output, "'");
			else obj_output.a(obj);
			String var_actual_type = (StaticTypes.VAR_TYPE.compare(entry.getDatatype())) ? (obj != null) ? ", <" + obj.getDatatype() + ">" : "" : "";
			String objHash = (obj != null) ? ", " + obj.getHexHash() : "";
			
			out.a(tab, tab, i, ": ", o.getKey(), " = [", entry.getDatatype(), var_actual_type, objHash, ", ", obj_output, objS, "]\n");
		}
		
		return out.toString();
	}
	
}
