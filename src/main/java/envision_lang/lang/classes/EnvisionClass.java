package envision_lang.lang.classes;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.functions.InstanceFunction;
import envision_lang.lang.language_errors.error_types.classErrors.NotAConstructorError;
import envision_lang.lang.language_errors.error_types.classErrors.UndefinedConstructorError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.parser.statements.ParsedStatement;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

/**
 * The EnvisionClass is the primary component for which all
 * EnvisionObject instances are instantiated from. EnvisionClass,
 * while technically not an object in itself, provides the underlying
 * blueprint structure for which all objects are created from.
 * 
 * <p>
 * Every EnvisionObject will have a specific, singular, root parent
 * class from which it has been instantiated from.
 * 
 * <p>
 * Classes contain their own static scopes which can house their own
 * static fields/methods/classes/etc. This scope is ultimately derived
 * from the over-arching scope from which this class was originally
 * declared within. This means that any values native to the original
 * defining scope are also visible to this class's static scope in
 * some capacity.
 * 
 * <p>
 * A class must define a specific object type that represents the type
 * of object instances it is capable of creating. As such, a datatype
 * must be passed during class instantiation. This datatype will be used
 * as the underlying datatype for all instances.
 * 
 * @author Hunter Bragg
 */
public class EnvisionClass extends EnvisionObject {
	
    //========
    // Fields
    //========
    
	/**
	 * Denotes whether or not the the natives for this class have been constructed.
	 */
	private boolean nativesRegistered = false;
	
	/**
	 * Instance creation optimization tool.
	 * 
	 * @see ClassConstruct
	 */
	protected ClassConstruct classConstruct;
	
	/**
	 * The constructor function that enables instance creation. Note:
	 * objects without a constructor can still be defined as a default
	 * constructor will be automatically generated.
	 */
	protected EnvisionFunction constructor;
	
	/**
	 * The static scope of this class. Any static
	 * fields/functions/classes/etc. that reside within this class's scope
	 * are located within this. This scope is ultimately derived from the
	 * scope in which the class was originally defined within. This means
	 * that any values native to that original defining scope are also
	 * visible to this static class scope in some capacity.
	 */
	protected IScope staticScope;
	
	/**
	 * The name of the class from which object instances of the same type
	 * can be defined.
	 */
	private String className;
	
	/**
	 * True by default but can be set to false to account for
	 * abstract classes as well as interfaces.
	 */
	protected boolean isInstantiable = true;
	
	/**
	 * A list of all static members on this class.
	 */
	//protected EList<EnvisionObject> staticMembers = new EArrayList<>();
	
	/**
	 * The entire list of static statements that have been declared within
	 * this class declaration.
	 */
	//protected EList<ParsedStatement> staticStatements = new EArrayList<>();
	
	/**
	 * The entire list of non-static statements that have been declared within
	 * this class declaration. These statements are used by class constructs
	 * to help improve instance creation.
	 */
	protected EList<ParsedStatement> bodyStatements = new EArrayList<>();
	
	/**
	 * The entire list of constructor (initializer) statements that have been
	 * declared within this class declaration.
	 */
	//protected EList<Stmt_FuncDef> constructorStatements = new EArrayList<>();
	
	private static final IPrototypeHandler OBJ_PROTOS = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		OBJ_PROTOS.define("equals", BOOLEAN, VAR).assignDynamicClass(IFunc_equals.class);
		OBJ_PROTOS.define("hash", INT).assignDynamicClass(IFunc_hash.class);
		OBJ_PROTOS.define("hexHash", STRING).assignDynamicClass(IFunc_hexHash.class);
		OBJ_PROTOS.define("isStatic", BOOLEAN).assignDynamicClass(IFunc_isStatic.class);
		OBJ_PROTOS.define("isFinal", BOOLEAN).assignDynamicClass(IFunc_isFinal.class);
		OBJ_PROTOS.define("toString", STRING).assignDynamicClass(IFunc_toString.class);
		OBJ_PROTOS.define("type", CLASS).assignDynamicClass(IFunc_type.class);
		OBJ_PROTOS.define("typeString", STRING).assignDynamicClass(IFunc_typeString.class);
		OBJ_PROTOS.define("members", LIST).assignDynamicClass(IFunc_members.class);
	}
	
	//==============
    // Constructors
    //==============
	
	public EnvisionClass(String typeNameIn) {
	    this(IDatatype.of(typeNameIn));
	}
	
	/**
	 * Creates a new EnvisionClass with the given class name.
	 * 
	 * Note: the static class scope is undefined at this stage and must be
	 * assigned separately.
	 * 
	 * @param classNameIn
	 */
	public EnvisionClass(IDatatype typeIn) {
		super(typeIn);
		
		//assign class name
		className = typeIn.getStringValue();
		//assign default empty primitive class scope
		staticScope = new Scope();
		//assign native class object
		internalClass = this;
	}
	
	/**
	 * Creates a class wrapper for a primitive datatype. The static class
	 * scope is automatically defined for a primitive class type.
	 * 
	 * Note: this constructor is intended to be used purely for internal
	 * purposes.
	 * 
	 * @param primitiveType The primitive type being wrapped
	 */
	protected EnvisionClass(Primitives primitiveType) {
		super(primitiveType.toDatatype());
		
		//assign primitive class name
		className = primitiveType.string_value;
		//assign default empty primitive class scope
		staticScope = new Scope();
		//assign native class object
		internalClass = this;
	}
	
	//=========
    // Methods
    //=========
	
	public void initClassNatives() {
		//prevent re-registration
		if (nativesRegistered) return;
		registerStaticNatives();
		nativesRegistered = true;
	}
	
	/**
	 * Called at class-interpreter instantiation to register static class members
	 * outside of the internal construction phase. This approach prevents
	 * an infinite instantiation loop.
	 */
	protected void registerStaticNatives() {
	    // intended to be overridden by children, does nothing by default
	}
	
	/**
	 * Adds a specific constructor to this object.
	 * If no constructor was present before, this new constructor
	 * will override the default constructor for this class.
	 * 
	 * @param conIn
	 * @return
	 */
	public EnvisionClass addConstructor(EnvisionFunction conIn) {
		if (conIn == null) throw new UndefinedConstructorError();
		if (!conIn.isConstructor()) throw new NotAConstructorError(conIn);
		
		//assign this as the parent class
		conIn.assignParentClass(this);
		
		//if there is not a constructor already, assign the constructor to the incoming one.
		if (constructor == null) constructor = conIn;
		//otherwise, add the incoming constructor as an overload
		else constructor.addOverload(conIn);
		
		return this;
	}
	
	/**
	 * Returns true if the given object both A. an Envision ClassInstance
	 * and B. an instance created from this class structure.
	 * 
	 * @param in
	 * @return true if the given object is an instance of this class
	 */
	public boolean isInstanceof(Object in) {
		//only care if the incoming object is actually a class instance
		if (in instanceof ClassInstance inst) {
			EnvisionClass instClass = inst.getEClass();
			int instHash = instClass.getObjectHash();
			//compare hashes and direct class equivalence
			return (instHash == getObjectHash()) && (instClass == this);
		}
		return false;
	}
	
	//===================
	// Protected Methods
	//===================
	
	/**
	 * Creates a new instance of this object.
	 * 
	 * All inheriting classes must directly override this method to
	 * specify the exact process of the child's object instantiation.
	 * 
	 * @param interpreter The active working interpreter
	 * @param args Any arguments to be passed to the new object instance
	 * @return The newly created object instance
	 */
	public ClassInstance newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (classConstruct != null) return classConstruct.buildInstance(interpreter, args);
		return buildInstance(interpreter, args);
	}
	
	/**
	 * Default class construction procedure. This path will only be taken if a
	 * ClassConstruct is not present.
	 * 
	 * @param interpreter The active working interpreter
	 * @param args Any arguments to be passed to the new object instance
	 * @return The newly created object instance
	 */
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		Scope instanceScope = new Scope(staticScope);
		ClassInstance instance = new ClassInstance(this, instanceScope);
		
		//define 'this'
		instanceScope.define("this", instance);
		
		//build the body of the new instance
		interpreter.executeStatements(bodyStatements, instanceScope);
		
		//extract operator overloads from scope
		EList<EnvisionFunction> operators = instanceScope.functions().filter(f -> f.isOperator());
		
		//set the overloaded operators onto the class instance
		for (EnvisionFunction op : operators) {
			instance.addOperatorOverload(op.getOperator(), op);
		}
		
		//execute constructor -- if there is one
		if (constructor != null) {
			constructor.setScope(instanceScope);
			constructor.invoke(interpreter, args);
		}
		
		//define scope members
		defineScopeMembers(instance);
		
		return instance;
	}
	
	/**
	 * Internal function used by native object classes to define all static/member
	 * object functions, variables, etc.
	 * <p>
	 * Note: Class object creation can in theory bypass this method but there will
	 * not be any scope members defined on the child object. Could be useful if a
	 * specific object member should not have accessible child members.
	 * 
	 * @param inst The ClassInstance to define scope members to
	 */
	protected void defineScopeMembers(ClassInstance inst) {
		//define instance members
		OBJ_PROTOS.defineOn(inst);
	}
	
	//=========
    // Getters
    //=========
	
	/**
	 * Abstract classes cannot be directly instantiated and require
	 * a non-abstract inheriting class to implement.
	 * 
	 * @return True if this is an abstract class.
	 */
	public boolean isAbstract() {
		return modifierHandler.isAbstract();
	}
	
	/**
	 * Returns true if this object can actually be instantiated.
	 * The only reason this should ever return false is if this
	 * object represents an abstract class or an interface.
	 * 
	 * @return true if actually instantiable
	 */
	public boolean isInstantiable() {
		return isInstantiable;
	}
	
	/**
	 * @return The name of this class
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * All classes have an internal static scope derived from the original
	 * scope in which they were defined in. This static scope contains all
	 * static fields/functions/classes/etc. that reside within this
	 * class's scope are located within this
	 * 
	 * @return The static scope of this class.
	 */
	public IScope getClassScope() {
		return staticScope;
	}
	
	/**
	 * Returns a field value from this instance's scope.
	 */
	public EnvisionObject get(String name) {
		EnvisionObject obj = staticScope.get(name);
		//if function prototype, build dynamic function
		if (obj instanceof FunctionPrototype proto) return proto.build(this);
		//otherwise, just return scope object
		return obj;
	}
	
	/**
	 * The constructor (initializer) function that is called by default
	 * every time a new instance of this specific class is instantiated.
	 * 
	 * <p>
	 * In the event that no constructor has been defined for this class, a
	 * default constructor will be automatically generated.
	 * 
	 * <p>
	 * This constructor function also contains all overloaded constructor
	 * functions.
	 * 
	 * @return The constructor function for this class
	 */
	public EnvisionFunction getConstructor() {
		return constructor;
	}
	
	/**
	 * The instance creation optimization tool for this specific class.
	 * 
	 * @return The construct for this class
	 */
	public ClassConstruct getClassConstruct() {
		return classConstruct;
	}
	
	//=========
    // Setters
    //=========
	
	/**
	 * Used to manually set whether or not this object should be instantiable.
	 * Abstract classes and interfaces will want to set insantiability to false
	 * to prevent an abstract class instance from being created.
	 * 
	 * @param val The boolean value to be assigned
	 */
	public void setInstantiable(boolean val) {
		isInstantiable = val;
	}
	
	/**
	 * Assigns this class's static class scope.
	 * 
	 * @param in The incoming static scope
	 * @return This class
	 */
	public EnvisionClass setScope(IScope in) {
		staticScope = in;
		return this;
	}
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	public EnvisionObject set(String name, EnvisionObject in) {
		staticScope.set(name, in);
		return in;
	}
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	public EnvisionObject set(String name, IDatatype type, EnvisionObject in) {
		staticScope.setFast(name, type, in);
		return in;
	}
	
	/**
	 * Assign's this class's instance creation optimization construct.
	 * 
	 * @param in The construct
	 */
	public void assignConstruct(ClassConstruct in) {
		classConstruct = in;
	}
	
	//---------------------------------------------------------------------------
	
	//public EArrayList<InheritableObject> getParents() { return parents; }
	//public EList<EnvisionObject> getStaticMembers() { return staticMembers; }
	//public EList<ParsedStatement> getStaticStatements() { return staticStatements; }
	public EList<ParsedStatement> getBody() { return bodyStatements; }

	//---------------------------------------------------------------------------
	
	//public InheritableObject setParents(EArrayList<InheritableObject> in) { parents = in; return this; }
	//public EnvisionClass setStatics(EList<ParsedStatement> in) { staticStatements = in; return this; }
	public EnvisionClass setBody(EList<ParsedStatement> in) { bodyStatements = in; return this; }
	//public EnvisionClass setConstructors(EList<Stmt_FuncDef> in) { constructorStatements = in; return this; }
	
	//---------------------------------------------------------------------------
	
	//===========================
	// Instance Member Functions
	//===========================
	
	public static class IFunc_equals extends InstanceFunction<ClassInstance> {
		public IFunc_equals() { super(BOOLEAN, "equals", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.equals(args[0])));
		}
	}
	
	public static class IFunc_hash extends InstanceFunction<ClassInstance> {
		public IFunc_hash() { super(INT, "hash"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var hash = inst.getObjectHash();
			ret(EnvisionIntClass.valueOf(hash));
		}
	}
	
	public static class IFunc_hexHash extends InstanceFunction<ClassInstance> {
		public IFunc_hexHash() { super(STRING, "hexHash"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var hexHash = inst.getHexHash();
			ret(EnvisionStringClass.valueOf(hexHash));
		}
	}
	
	public static class IFunc_isStatic extends InstanceFunction<ClassInstance> {
		public IFunc_isStatic() { super(BOOLEAN, "isStatic"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var isStatic = inst.isStatic();
			ret(EnvisionBooleanClass.valueOf(isStatic));
		}
	}
	
	public static class IFunc_isFinal extends InstanceFunction<ClassInstance> {
		public IFunc_isFinal() { super(BOOLEAN, "isFinal"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var isFinal = inst.isFinal();
			ret(EnvisionBooleanClass.valueOf(isFinal));
		}
	}
	
	public static class IFunc_toString extends InstanceFunction<ClassInstance> {
		public IFunc_toString() { super(STRING, "toString"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var toString = inst.toString();
			ret(EnvisionStringClass.valueOf(toString));
		}
	}
	
	public static class IFunc_type extends InstanceFunction<ClassInstance> {
		public IFunc_type() { super(CLASS, "type"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getEClass());
		}
	}
	
	public static class IFunc_typeString extends InstanceFunction<ClassInstance> {
		public IFunc_typeString() { super(STRING, "typeString"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var typeString = inst.getDatatype().getStringValue() + "_" + inst.getHexHash();
			ret(EnvisionStringClass.valueOf(typeString));
		}
	}
	
	public static class IFunc_members extends InstanceFunction<ClassInstance> {
		public IFunc_members() { super(LIST, "members"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList members = EnvisionListClass.newList();
			members.addAll(inst.instanceScope.objects());
			ret(members);
		}
	}
	
}
