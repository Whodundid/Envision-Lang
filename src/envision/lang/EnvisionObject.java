package envision.lang;

import static envision.lang.util.EnvisionDataType.*;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.RestrictedAccessError;
import envision.exceptions.errors.UndefinedMethodError;
import envision.exceptions.errors.classErrors.UndefinedConstructorError;
import envision.exceptions.errors.objects.NoInternalMethodsError;
import envision.exceptions.errors.objects.NoObjectConstructorsError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.objects.EnvisionList;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalConstructor;
import envision.lang.util.InternalMethod;
import envision.lang.util.VisibilityType;
import envision.lang.util.data.DataModifier;
import envision.lang.util.data.ParameterData;
import envision.lang.variables.EnvisionVariable;
import envision.parser.statements.statementUtil.ParserDeclaration;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

/** The base level object instance for which all Envision objects inherit from. */
public class EnvisionObject {

	protected EnvisionObject t;
	protected String name;
	protected EnvisionDataType internalType;
	protected VisibilityType visibility = VisibilityType.PUBLIC;
	protected int modifiers = 0b00000000;
	
	protected EArrayList<InternalMethod> internalMethods = new EArrayList();
	protected EArrayList<InternalConstructor> internalConstructors = new EArrayList();
	protected boolean hasObjectMethods = true;
	protected boolean hasConstructors = true;
	
	protected Object javaObject;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionObject() { this(EnvisionDataType.OBJECT, "noname"); }
	public EnvisionObject(EnvisionDataType internalTypeIn) { this(internalTypeIn, "noname"); }
	public EnvisionObject(String nameIn) { this(EnvisionDataType.OBJECT, nameIn); }
	public EnvisionObject(EnvisionDataType internalTypeIn, String nameIn) {
		internalType = internalTypeIn;
		name = nameIn;
		t = this;
		registerInternalMethods();
	}
	
	public EnvisionObject(String nameIn, Object javaObjectIn) {
		internalType = EnvisionDataType.OBJECT;
		name = nameIn;
		javaObject = javaObjectIn;
		t = this;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void im(InternalMethod m) {
		if (m == null) return;
		
		//check for existing internal method
		for (int i = 0; i < internalMethods.size(); i++) {
			InternalMethod meth = internalMethods.get(i);
			
			//replace existing methods -- no way to call internal super methods :(
			if (meth.getIMethodName().equals(m.getIMethodName())) {
				internalMethods.set(i, m);
				return;
			}
		}
		
		internalMethods.put(m);
	}
	
	protected void registerInternalMethods() {
		im(new InternalMethod(BOOLEAN, "equals", OBJECT) { protected void body(Object[] a) { ret(t.equals(a[0])); }});
		im(new InternalMethod(INT, "hash") { protected void body(Object[] a) { ret(t.getObjectHash()); }});
		im(new InternalMethod(STRING, "hexHash") { protected void body(Object[] a) { ret(t.getHexHash()); }});
		im(new InternalMethod(BOOLEAN, "isStatic") { protected void body(Object[] a) { ret(t.isStatic()); }});
		im(new InternalMethod(BOOLEAN, "isFinal") { protected void body(Object[] a) { ret(t.isFinal()); }});
		im(new InternalMethod(STRING, "name") { protected void body(Object[] a) { ret(t.getName()); }});
		im(new InternalMethod(STRING, "toString") { protected void body(Object[] a) { ret(t.toString()); }});
		im(new InternalMethod(STRING, "type") { protected void body(Object[] a) { ret(t.getInternalType().type); }});
		im(new InternalMethod(STRING, "typeString") { protected void body(Object[] a) { ret(t.internalType + "_" + t.getHexHash()); }});
		im(new InternalMethod(STRING, "getVis") { protected void body(Object[] a) { ret(t.getVisibility().toString()); }});
		im(new InternalMethod(OBJECT, "setStrong", BOOLEAN) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				if ((boolean) cv(a[0])) t.setStrong();
				else t.removeModifier(DataModifier.STRONG);
			}
		});
		im(new InternalMethod(LIST, "methods") {
			protected void body(Object[] a) {
				EnvisionList l = new EnvisionList(STRING);
				l.addAll(internalMethods.map(m -> m.getIMethodName()));
				ret(l);
			}
		});
		im(new InternalMethod(LIST, "constructors") { protected void body(Object[] a) { ret(new EnvisionList(STRING).addAll(internalConstructors.map(m -> m.getParams().toString()))); }});
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
		
		for (InternalMethod m : internalMethods) {
			if (m.getIMethodName().equals(name) && m.getParams().compare(passParams)) {
				return m;
			}
		}
		
		return null;
	}
	
	//-----------------------
	// Internal Constructors
	//-----------------------
	
	protected void ic(InternalConstructor m) { internalConstructors.add(m); }
	protected void registerInternalConstructors() {
		ic(new InternalConstructor() { protected void body(Object[] a) { ret(new EnvisionObject()); }});
	}
	
	public InternalConstructor getInternalConstructor(String name) {
		//for (InternalConstructor c : internalConstructors) {
			//if (c.getName().equals(name)) { return m; }
		//}
		return null;
	}
	
	//---------------------------------------------------------------------------------------
	// Abstracts : While EnvisionObject is not abstract, these are intended to be overriden.
	//---------------------------------------------------------------------------------------
	
	public EnvisionObject copy() {
		EnvisionObject obj = new EnvisionObject(internalType, name);
		obj.visibility = visibility;
		obj.modifiers = modifiers;
		obj.hasConstructors = hasConstructors;
		obj.hasObjectMethods = hasObjectMethods;
		obj.javaObject = javaObject;
		return obj;
	}
	
	public void runInternalMethod(String methodName, EnvisionInterpreter interpreter, Object[] args) {
		if (isNull()) throw new NullVariableError(methodName);
		if (!hasObjectMethods) throw new NoInternalMethodsError(this);
		
		InternalMethod m = getInternalMethod(methodName, args);
		if (m != null) m.call(interpreter, args);
		//else call_I(interpreter, args);
		else throw new UndefinedMethodError(methodName, getClass().getSimpleName());
	}
	
	public void runObjctConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (isNull()) throw new NullVariableError();
		if (!hasConstructors) throw new NoObjectConstructorsError(this);
		
		EnvisionObject obj = runConstructor(interpreter, args);
		if (obj != null) ret(obj);
		throw new UndefinedConstructorError("No visible constructor with given parameters: " +
				EUtil.map(args, o -> ObjectCreator.wrap(o).getTypeString()));
	}
	
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) return new EnvisionObject();
		throw new EnvisionError("TEMP: Object creation -- This should be impossible!");
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String toString() { return internalType + "_" + getHexHash(); }
	
	//---------
	// Methods
	//---------
	
	/** Applys each value on the given parser declaration to this object. */
	public void applyDeclaration(ParserDeclaration declaration) {
		if (declaration != null) {
			modifiers = 0;
			for (DataModifier m : declaration.getMods()) modifiers |= m.byteVal;
			
			setVisibility(declaration.getVisibility());
		}
	}
	
	/** Returns true if this object has the given data modifier. */
	public boolean hasModifier(DataModifier mod) {
		return (modifiers & mod.byteVal) == 1;
	}
	
	/** Applies the given modifier to this object. */
	public void addModifier(DataModifier mod) {
		modifiers |= mod.byteVal;
	}
	
	/** Removes the given modifier from this object. */
	public void removeModifier(DataModifier mod) {
		modifiers ^= mod.byteVal;
	}
	
	/** Returns true if this object represents a null reference. */
	public boolean isNull() { return internalType.equals(EnvisionDataType.NULL); }
	
	//---------
	// Getters
	//---------
	
	public int getObjectHash() { return hashCode(); }
	public String getHexHash() { return Integer.toHexString(hashCode()); }
	public EnvisionDataType getInternalType() { return internalType; }
	public String getTypeString() { return internalType.type; }
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
	
	public EnvisionObject setStatic() { modifiers |= DataModifier.STATIC.byteVal; return this; }
	public EnvisionObject setFinal() { modifiers |= DataModifier.STATIC.byteVal; return this; }
	public EnvisionObject setStrong() { modifiers |= DataModifier.STATIC.byteVal; return this; }
	
	public EnvisionObject setModifier(DataModifier mod, boolean val) {
		if (val) addModifier(mod);
		else removeModifier(mod);
		return this;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	protected Object ret(Object value) {
		EnvisionObject r = (value instanceof EnvisionObject) ? (EnvisionObject) value : ObjectCreator.createObject(value);
		throw new ReturnValue(r);
	}
	
	protected Object ret(Object value, EnvisionDataType type) {
		EnvisionObject r = (value instanceof EnvisionObject) ? (EnvisionObject) value : ObjectCreator.createObject(type, value);
		throw new ReturnValue(r);
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static EnvisionObject getBase() { return new EnvisionObject(); }
	public static boolean isNull(EnvisionObject in) { return in.isNull(); }
	public static Object convert(Object in) { return (in instanceof EnvisionVariable) ? ((EnvisionVariable) in).get() : in; }
	protected static Object cv(Object in) { return convert(in); }
	
}
