package envision.lang.classes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.classErrors.NotAConstructorError;
import envision.exceptions.errors.classErrors.UndefinedConstructorError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.lang.util.data.DataModifier;
import envision.lang.util.structureTypes.InheritableObject;
import envision.lang.util.structureTypes.InstantiableObject;
import envision.parser.statements.Statement;
import eutil.datatypes.EArrayList;

public class EnvisionClass extends InstantiableObject {
	
	private EnvisionFunction constructor;
	protected Scope classScope;
	private String className;
	private EnvisionDatatype classType;
	
	//instance creation optimization
	private ClassConstruct classConstruct;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionClass(EnvisionDatatype typeIn, String nameIn) {
		super(typeIn, nameIn);
		
		className = nameIn;
		classType = new EnvisionDatatype(className);
	}
	
	public EnvisionClass(String nameIn) { this(nameIn, null, null); }
	public EnvisionClass(String nameIn, EArrayList<InheritableObject> parentsIn, EArrayList<Statement> bodyIn) {
		super(Primitives.CLASS.toDatatype(), nameIn);
		className = nameIn;
		classType = new EnvisionDatatype(className);
		if (parentsIn != null) parents.addAll(parentsIn);
		if (bodyIn != null) setBody(bodyIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionClass copy() {
		//copy not supported
		throw new EnvisionError("Copy Not Supported! " + getDatatype());
	}
	
	@Override
	public String getTypeString() {
		return className;
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ClassInstance instance = buildInstance(interpreter, args);
		ret(instance);
	}
	
	/**
	 * Overriden to allow for static members to be referenced.
	 */
	@Override
	public void runInternalMethod(String methodName, EnvisionInterpreter interpreter, Object[] args) {
		EnvisionObject obj = null;
		if (classScope != null) obj = classScope.get(methodName);
		
		if (obj instanceof EnvisionFunction env_func) env_func.invoke_I(methodName, interpreter, args);
		if (obj != null) ret(obj);
		
		super.runInternalMethod(methodName, interpreter, args);
	}
	
	//---------
	// Methods
	//---------
	
	public boolean isAbstract() {
		return hasModifier(DataModifier.ABSTRACT);
	}
	
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
	
	public boolean isInstanceof(Object in) {
		if (in instanceof ClassInstance) {
			ClassInstance inst = (ClassInstance) in;
			return this.equals(inst.getEClass());
		}
		return false;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	@Override
	public ClassInstance newInstance(EnvisionInterpreter interpreter, Object[] args) {
		return (classConstruct != null) ? classConstruct.buildInstance(interpreter, args) :
										  buildInstance(interpreter, args);
	}
	
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, Object[] args) {
		Scope instanceScope = new Scope(classScope);
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
			instance.addOperator(op.getOperator(), op);
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
	
	public Scope getScope() { return classScope; }
	public EnvisionFunction getConstructor() { return constructor; }
	public ClassConstruct getClassConstruct() { return classConstruct; }
	public EnvisionDatatype getClassDatatype() { return classType; }
	
	//---------
	// Setters
	//---------
	
	public EnvisionClass setScope(Scope in) { classScope = in; return this; }
	
	public void assignConstruct(ClassConstruct in) { classConstruct = in; }
	
}
