package envision.lang.classes;

import envision.exceptions.errors.classErrors.NotAConstructorError;
import envision.exceptions.errors.classErrors.UndefinedConstructorError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.DataModifier;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_FuncDef;
import eutil.datatypes.EArrayList;

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
 * from the overarching scope from which this class was originally
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
	
	/**
	 * The underlying datatype for all class objects.
	 */
	public static final EnvisionDatatype CLASS_TYPE = Primitives.CLASS.toDatatype();
	
	/**
	 * Instance creation optimization tool.
	 * 
	 * @see ClassConstruct
	 */
	private ClassConstruct classConstruct;
	
	/**
	 * The constructor function that enables instance creation. Note:
	 * objects without a constructor can still be defined as a default
	 * constructor will be automatically generated.
	 */
	private EnvisionFunction constructor;
	
	/**
	 * The static scope of this class. Any static
	 * fields/functions/classes/etc. that reside within this class's scope
	 * are located within this. This scope is ultimately derived from the
	 * scope in which the class was originally defined within. This means
	 * that any values native to that original defining scope are also
	 * visible to this static class scope in some capacity.
	 */
	protected Scope staticClassScope;
	
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
	protected EArrayList<EnvisionObject> staticMembers = new EArrayList();
	
	/**
	 * The entire list of static statements that have been declared within
	 * this class declaration.
	 */
	protected EArrayList<Statement> staticStatements = new EArrayList();
	
	/**
	 * The entire list of non-static statements that have been declared within
	 * this class declaration. These statements are used by class constructs
	 * to help improve instance creation.
	 */
	protected EArrayList<Statement> bodyStatements = new EArrayList();
	
	/**
	 * The entire list of constructor (initializer) statements that have been
	 * declared within this class declaration.
	 */
	protected EArrayList<Stmt_FuncDef> constructorStatements = new EArrayList();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Creates a new EnvisionClass with the given class name.
	 * 
	 * Note: the static class scope is undefined at this stage and must be
	 * assigned separately.
	 * 
	 * @param classNameIn
	 */
	public EnvisionClass(String classNameIn) {
		super(new EnvisionDatatype(classNameIn));
		
		//assign class name
		className = classNameIn;
		
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
	public EnvisionClass(Primitives primitiveType) {
		super(primitiveType.toDatatype());
		
		//assign primitive class name
		className = primitiveType.string_type;
		
		//assign default empty primitive class scope
		staticClassScope = new Scope();
		
		//assign native class object
		internalClass = this;
	}
	
	//---------
	// Methods
	//---------
	
	protected void defineScopeMembers(ClassInstance inst) {}
	
	public EnvisionClass addConstructor(EnvisionFunction constructorIn) {
		if (constructorIn == null) throw new UndefinedConstructorError();
		if (!constructorIn.isConstructor()) throw new NotAConstructorError(constructorIn);
		
		//assign this as the parent class
		constructorIn.assignParentClass(this);
		
		if (constructor == null) constructor = constructorIn;
		else {
			constructor.addOverload(constructorIn.getParams(), constructorIn.getBody());
		}
		
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
		if (in instanceof ClassInstance inst) {
			EnvisionClass instClass = inst.getEClass();
			int instHash = instClass.getObjectHash();
			//compare hashes and direct class equivalence
			return (instHash == getObjectHash()) && (instClass == this);
		}
		return false;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
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
	
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		Scope instanceScope = new Scope(staticClassScope);
		ClassInstance instance = new ClassInstance(this, instanceScope);
		
		//define 'this'
		instanceScope.define("this", instance);
		
		//build the body of the new instance
		interpreter.executeBlock(bodyStatements, instanceScope);
		
		//extract operator overloads from scope
		EArrayList<EnvisionObject> methods = instanceScope.values().filter(o -> o instanceof EnvisionFunction && ((EnvisionFunction) o).isOperator());
		EArrayList<EnvisionFunction> operators = methods.map(m -> (EnvisionFunction) m);
		
		//set the overloaded operators onto the class instance
		for (EnvisionFunction op : operators) {
			instance.addOperatorOverload(op.getOperator(), op);
		}
		
		//execute constructor -- if there is one
		if (constructor != null) {
			constructor.setScope(instanceScope);
			constructor.invoke_I(null, interpreter, args);
		}
		
		return instance;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Abstract classes cannot be directly instantiated and require
	 * a non-abstract inherting class to implement.
	 * 
	 * @return True if this is an abstract class.
	 */
	public boolean isAbstract() {
		return hasModifier(DataModifier.ABSTRACT);
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
	public Scope getClassScope() {
		return staticClassScope;
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
	
	//---------
	// Setters
	//---------
	
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
	 * @param in The incomming static scope
	 * @return This class
	 */
	public EnvisionClass setScope(Scope in) {
		staticClassScope = in;
		return this;
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
	public EArrayList<EnvisionObject> getStaticMembers() { return staticMembers; }
	public EArrayList<Statement> getStaticStatements() { return staticStatements; }
	public EArrayList<Statement> getBody() { return bodyStatements; }

	//---------------------------------------------------------------------------
	
	//public InheritableObject setParents(EArrayList<InheritableObject> in) { parents = in; return this; }
	public EnvisionClass setStatics(EArrayList<Statement> in) { staticStatements = in; return this; }
	public EnvisionClass setBody(EArrayList<Statement> in) { bodyStatements = in; return this; }
	public EnvisionClass setConstructors(EArrayList<Stmt_FuncDef> in) { constructorStatements = in; return this; }
	
	//---------------------------------------------------------------------------
	
}
