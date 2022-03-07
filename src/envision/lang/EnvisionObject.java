package envision.lang;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.RestrictedAccessError;
import envision.exceptions.errors.UndefinedMethodError;
import envision.exceptions.errors.classErrors.UndefinedConstructorError;
import envision.exceptions.errors.objects.NoInternalMethodsError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionList;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;
import envision.lang.util.VisibilityType;
import envision.lang.util.data.DataModifier;
import envision.lang.util.data.ParameterData;
import envision.parser.util.ParserDeclaration;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

import java.util.HashMap;

/**
 * The underlying parent object class for which all Envision:Java
 * objects inherit from.
 * 
 * @author Hunter Bragg
 */
public class EnvisionObject {

	//--------
	// Fields
	//--------
	
	/**
	 * The default name assigned to any object that is not explicitly
	 * given a name.
	 */
	public static final String DEFAULT_NAME = "noname";
	
	/** 'This' object instance. */
	protected final EnvisionObject t = this;
	/** The name of this object. */
	protected String name;
	/**
	 * The underlying datatype of this object.
	 * Even though Envision:Java supports dynamic variable datatypes,
	 * Java does not. As such, every dynamic type must be converted down
	 * to an exact referencable datatype.
	 */
	protected EnvisionDatatype internalType;
	/**
	 * The visibility type of this object. Used in imports/classes for
	 * scope visibility. Defaults to current scope.
	 */
	protected VisibilityType visibility = VisibilityType.SCOPE;
	/**
	 * A byte-representation of all active modifiers for this specific object.
	 * Modifiers may include types such as 'static', '
	 */
	protected int modifiers = 0b00000000;
	/** A list of all methods on this specific object. */
	protected HashMap<String, InternalMethod> internalMethods = new HashMap();
	/** True if this object has object methods. */
	protected boolean hasObjectMethods = true;
	
	/**
	 * Wrapped with an underlying Java object for special cross language purposes.
	 * <p>
	 * For instance, File handling in Envision makes direct use of Java's 'File' object.
	 * The Java 'File' object is then wrapped directly into an EnvisionObject so that
	 * it can be used normally within the Envision:Java language.
	 */
	protected Object javaObject;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionObject() { this(EnvisionDatatype.prim_var(), DEFAULT_NAME); }
	public EnvisionObject(Primitives datatypeIn) { this(new EnvisionDatatype(datatypeIn), DEFAULT_NAME); }
	public EnvisionObject(String nameIn) { this(EnvisionDatatype.prim_var(), nameIn); }
	public EnvisionObject(EnvisionDatatype typeIn) { this(typeIn, DEFAULT_NAME); }
	public EnvisionObject(EnvisionDatatype datatypeIn, String nameIn) {
		internalType = datatypeIn;
		name = nameIn;
		registerInternalMethods();
	}
	
	private EnvisionObject(String nameIn, Object javaObjectIn) {
		internalType = EnvisionDatatype.prim_var();
		name = nameIn;
		javaObject = javaObjectIn;
	}
	
	public static EnvisionObject javaObjectWrapper(String varNameIn, Object javaObjectIn) {
		return new EnvisionObject(varNameIn, javaObjectIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return internalType + "_" + getHexHash();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void im(InternalMethod m) {
		if (m == null) return;
		
		String iMethName = m.getIMethodName();
		
		//overwrite existing internal methods under the same -- no way to perform super method calls :(
		internalMethods.put(iMethName, m);
	}
	
	/**
	 * Used to bind any and all internal object methods.
	 * <p>
	 * This method should be overriden in child classes to specify their own
	 * specific internal methods.
	 */
	protected void registerInternalMethods() {
		im(new InternalMethod(BOOLEAN, "equals", VAR) { protected void body(Object[] a) { ret(t.equals(a[0])); }});
		im(new InternalMethod(INT, "hash") { protected void body(Object[] a) { ret(t.getObjectHash()); }});
		im(new InternalMethod(STRING, "hexHash") { protected void body(Object[] a) { ret(t.getHexHash()); }});
		im(new InternalMethod(BOOLEAN, "isStatic") { protected void body(Object[] a) { ret(t.isStatic()); }});
		im(new InternalMethod(BOOLEAN, "isFinal") { protected void body(Object[] a) { ret(t.isFinal()); }});
		im(new InternalMethod(STRING, "name") { protected void body(Object[] a) { ret(t.getName()); }});
		im(new InternalMethod(STRING, "toString") { protected void body(Object[] a) { ret(t.toString()); }});
		im(new InternalMethod(STRING, "type") { protected void body(Object[] a) { ret(t.getDatatype().getType()); }});
		im(new InternalMethod(STRING, "typeString") { protected void body(Object[] a) { ret(t.internalType + "_" + t.getHexHash()); }});
		im(new InternalMethod(STRING, "visibility") { protected void body(Object[] a) { ret(t.getVisibility().toString()); }});
		im(new InternalMethod(VOID, "setStrong", BOOLEAN) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				if ((boolean) cv(a[0])) t.setStrong();
				else t.removeModifier(DataModifier.STRONG);
			}
		});
		im(new InternalMethod(LIST, "methods") {
			protected void body(Object[] a) {
				EnvisionList l = new EnvisionList(STRING);
				internalMethods.keySet().iterator().forEachRemaining(l::add);
				ret(l);
			}
		});
		im(new InternalMethod(BOOLEAN, "setVis", STRING) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				String type = (String) cv(a[0]);
				switch (type) {
				case "public": setPublic(); break;
				case "protected": setProtected(); break;
				case "private": setPrivate(); break;
				case "scope": setVisibility(VisibilityType.SCOPE); break;
				default: throw new InvalidArgumentError(type, getIMethodName());
				}
			}
		});
		im(new InternalMethod(BOOLEAN, "setFinal", BOOLEAN) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				if ((boolean) cv(a[0])) t.setFinal();
				else t.removeModifier(DataModifier.FINAL);
			}
		});
	}
	
	/** Returns an InternalMethod with the same name and parameters. */
	public InternalMethod getInternalMethod(String name, Object[] args) {
		ParameterData passParams = new ParameterData(ObjectCreator.createArgs(args));
		
		InternalMethod m = internalMethods.get(name);
		if (m == null) return null;
		
		return (m.comapreParams(passParams)) ? m : null;
	}
	
	//---------------------------------------------------------------------------------------
	// Abstracts : While EnvisionObject is not abstract, these are intended to be overriden.
	//---------------------------------------------------------------------------------------
	
	/**
	 * Creates a copy of this object. The copy will have the same
	 * visibility, modifiers, and JavaObject (if applicable).
	 * 
	 * @return A copy of this object
	 */
	public EnvisionObject copy() {
		EnvisionObject obj = new EnvisionObject(internalType, name);
		obj.visibility = visibility;
		obj.modifiers = modifiers;
		obj.hasObjectMethods = hasObjectMethods;
		obj.javaObject = javaObject;
		return obj;
	}
	
	/**
	 * Attempts to run an internal method of the same name on this specific object.
	 * Internal methods are native to every EnvisionObject and furthermore any object
	 * that extends off of EnvisionObject.
	 * 
	 * @param methodName The method to be executed
	 * @param interpreter The active interpreter instance
	 * @param args Any arguments to be passed to the internal method
	 */
	public void runInternalMethod(String methodName, EnvisionInterpreter interpreter, Object[] args) {
		if (isNull()) throw new NullVariableError(methodName);
		if (!hasObjectMethods) throw new NoInternalMethodsError(this);
		
		//check for a matching internal method of the same name
		InternalMethod m = getInternalMethod(methodName, args);
		if (m != null) m.invoke(interpreter, args);
		else throw new UndefinedMethodError(methodName, getClass().getSimpleName());
	}
	
	public void runObjctConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (isNull()) throw new NullVariableError();
		
		EnvisionObject obj = runConstructor(interpreter, args);
		if (obj != null) ret(obj);
		throw new UndefinedConstructorError("No visible constructor with given parameters: " +
				EUtil.map(args, o -> ObjectCreator.wrap(o).getTypeString()));
	}
	
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) return new EnvisionObject();
		throw new EnvisionError("TEMP: Object creation -- This should be impossible!");
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Applys each value on the given parser declaration to this object.
	 */
	public void applyDeclaration(ParserDeclaration declaration) {
		if (declaration != null) {
			modifiers = 0;
			for (DataModifier m : declaration.getMods()) modifiers |= m.byteVal;
			
			setVisibility(declaration.getVisibility());
		}
	}
	
	/**
	 * Returns true if this object has the given data modifier.
	 */
	public boolean hasModifier(DataModifier mod) {
		return (modifiers & mod.byteVal) == mod.byteVal;
	}
	
	/**
	 * Applies the given modifier to this object.
	 */
	public void addModifier(DataModifier mod) {
		modifiers |= mod.byteVal;
	}
	
	/**
	 * Removes the given modifier from this object.
	 */
	public void removeModifier(DataModifier mod) {
		modifiers ^= mod.byteVal;
	}
	
	/**
	 * Returns true if this object represents a null reference. Note, this
	 * does not actually indicate that this object is Java::Null, only
	 * Envision::Null.
	 */
	public boolean isNull() {
		if (internalType == null) throw new EnvisionError("Null object internal type!");
		return internalType.isNull();
	}
	
	//---------
	// Getters
	//---------
	
	public int getObjectHash() { return hashCode(); }
	public String getHexHash() { return Integer.toHexString(hashCode()); }
	public EnvisionDatatype getDatatype() { return internalType; }
	public Primitives getPrimitiveType() { return internalType.getPrimitiveType(); }
	public String getTypeString() { return internalType.getType(); }
	public String getName() { return name; }
	public boolean isStatic() { return hasModifier(DataModifier.STATIC); }
	public boolean isFinal() { return hasModifier(DataModifier.FINAL); }
	public boolean isPublic() { return visibility == VisibilityType.PUBLIC; }
	public boolean isProtected() { return visibility == VisibilityType.PROTECTED; }
	public boolean isPrivate() { return visibility == VisibilityType.PRIVATE; }
	public boolean isRestricted() { return visibility == VisibilityType.RESTRICTED; }
	public boolean isStrong() { return hasModifier(DataModifier.STRONG); }
	public VisibilityType getVisibility() { return visibility; }
	public boolean hasObjectMethods() { return hasObjectMethods; }
	//public EArrayList<DataModifier> getModifiers() { return modifiers; }
	
	/** Special getter used to grab a wrapped Object in Java into Envision. */
	public Object getJavaObject() { return javaObject; }
	
	//---------
	// Setters
	//---------
	
	public EnvisionObject setName(String nameIn) { name = nameIn; return this; }
	public EnvisionObject setVisibility(VisibilityType in) { visibility = in; return this; }
	public EnvisionObject setHasObjectMethods(boolean val) { hasObjectMethods = val; return this; }
	
	public EnvisionObject setPublic() { visibility = VisibilityType.PUBLIC; return this; }
	public EnvisionObject setProtected() { visibility = VisibilityType.PROTECTED; return this; }
	public EnvisionObject setPrivate() { visibility = VisibilityType.PRIVATE; return this; }
	public EnvisionObject setRestricted() { visibility = VisibilityType.RESTRICTED; return this; }
	
	public EnvisionObject setStatic() { addModifier(DataModifier.STATIC); return this; }
	public EnvisionObject setFinal() { addModifier(DataModifier.FINAL); return this; }
	public EnvisionObject setStrong() { addModifier(DataModifier.STRONG); return this; }
	
	public EnvisionObject setModifier(DataModifier mod, boolean val) {
		if (val) addModifier(mod);
		else removeModifier(mod);
		return this;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	protected Object ret(Object value) {
		EnvisionObject r = null;
		if (value instanceof EnvisionObject env_obj) r = env_obj;
		else r = ObjectCreator.createObject(DEFAULT_NAME, EnvisionDatatype.dynamicallyDetermineType(value), value, false);
		throw new ReturnValue(r);
	}
	
	protected Object ret(Object value, Primitives type) {
		EnvisionObject r = null;
		if (value instanceof EnvisionObject env_obj) r = env_obj;
		else r = ObjectCreator.createObject(DEFAULT_NAME, new EnvisionDatatype(type), value, false);
		throw new ReturnValue(r);
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static EnvisionObject getBase() {
		return new EnvisionObject();
	}
	
	public static boolean isNull(EnvisionObject in) {
		return in.isNull();
	}
	
	public static Object convert(Object in) {
		return (in instanceof EnvisionVariable) ? ((EnvisionVariable) in).get() : in;
	}
	
	protected static Object cv(Object in) {
		return convert(in);
	}
	
	//--------------------------
	// Default Internal Methods
	//--------------------------
	
	private static final EArrayList<InternalMethod> default_object_methods = new EArrayList();
	
	static {
		//create a single 
	}
	
}
