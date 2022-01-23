package envision.lang.classes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.classErrors.NotAConstructorError;
import envision.exceptions.errors.classErrors.UndefinedConstructorError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.DataModifier;
import envision.lang.util.structureTypes.InheritableObject;
import envision.lang.util.structureTypes.InstantiableObject;
import envision.parser.statements.Statement;
import eutil.datatypes.EArrayList;

public class EnvisionClass extends InstantiableObject {
	
	private EnvisionMethod constructor;
	protected Scope classScope;
	private String className;
	
	//instance creation optimization
	private ClassConstruct classConstruct;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionClass(EnvisionDataType typeIn, String nameIn) {
		super(typeIn, nameIn);
		className = typeIn.type;
	}
	
	public EnvisionClass(String nameIn) { this(nameIn, null, null); }
	public EnvisionClass(String nameIn, EArrayList<InheritableObject> parentsIn, EArrayList<Statement> bodyIn) {
		super(EnvisionDataType.CLASS, nameIn);
		className = nameIn;
		if (parentsIn != null) {
			parents.addAll(parentsIn);
		}
		if (bodyIn != null) {
			setBody(bodyIn);
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionClass copy() {
		//copy not supported
		throw new EnvisionError("Copy Not Supported! " + getInternalType());
	}
	
	@Override public String getTypeString() { return className; }
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ClassInstance instance = buildInstance(interpreter, args);
		ret(instance);
	}
	
	/**
	 * Overriden to allow for static members to be referenced.
	 */
	@Override
	public void runInternalMethod(String methodName, EnvisionInterpreter interpreter, Object[] args) {
		EnvisionObject obj = classScope.get(methodName);
		if (obj instanceof EnvisionMethod) ((EnvisionMethod) obj).call(interpreter, args);
		if (obj != null) ret(obj);
		super.runInternalMethod(methodName, interpreter, args);
	}
	
	//---------
	// Methods
	//---------
	
	public boolean isAbstract() { return hasModifier(DataModifier.ABSTRACT); }
	
	public EnvisionClass addConstructor(EnvisionMethod constructorIn) {
		if (constructorIn == null) { throw new UndefinedConstructorError(); }
		if (!constructorIn.isConstructor()) { throw new NotAConstructorError(constructorIn); }
		
		if (constructor == null) { constructor = constructorIn; }
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
	
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, Object[] args) {
		Scope instanceScope = new Scope(classScope);
		ClassInstance instance = new ClassInstance(this, instanceScope);
		
		//define 'this'
		instanceScope.define("this", instance);
		
		//build the body of the new instance
		interpreter.executeBlock(bodyStatements, instanceScope);
		
		//extract operator overloads from scope
		EArrayList<EnvisionObject> methods = instanceScope.values().filter(o -> o instanceof EnvisionMethod && ((EnvisionMethod) o).isOperator());
		EArrayList<EnvisionMethod> operators = methods.map(m -> (EnvisionMethod) m);
		
		//set the overloaded operators onto the class instance
		for (EnvisionMethod op : operators) {
			instance.addOperator(op.getOperator(), op);
		}
		
		//execute constructor -- if there is one
		if (constructor != null) {
			constructor.setScope(instanceScope);
			constructor.call(interpreter, args);
		}
		
		return instance;
	}
	
	//---------
	// Getters
	//---------
	
	public Scope getScope() { return classScope; }
	public EnvisionMethod getConstructor() { return constructor; }
	public ClassConstruct getClassConstruct() { return classConstruct; }
	
	//---------
	// Setters
	//---------
	
	public EnvisionClass setScope(Scope in) { classScope = in; return this; }
	
	public void assignConstruct(ClassConstruct in) { classConstruct = in; }
	
}
