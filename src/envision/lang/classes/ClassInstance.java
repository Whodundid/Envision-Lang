package envision.lang.classes;

import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.lang.util.data.ParameterData;
import envision.tokenizer.IKeyword;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;

/** An instantiated version of an EnvisionClass. */
public class ClassInstance extends EnvisionObject {
	
	/** The EnvisionClass for which this is an instance of. */
	public final EnvisionClass theClass;
	/** The scope of this instance. Directly inherited from the calling scope. */
	protected final Scope instanceScope;
	/** Used for operator overloading. */
	protected BoxList<IKeyword, EArrayList<EnvisionFunction>> operators = new BoxList();
	
	//--------------
	// Constructors
	//--------------
	
	public ClassInstance(EnvisionClass classIn, Scope in) {
		super(Primitives.CLASS_INSTANCE);
		setName(classIn.getName());
		theClass = classIn;
		instanceScope = in;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String getTypeString() {
		return theClass.getTypeString();
	}
	
	@Override
	public String toString() {
		return getDatatype() + "_" + Integer.toHexString(hashCode());
	}
	
	@Override
	public EnvisionDatatype getDatatype() {
		return theClass.getClassDatatype();
	}
	
	//---------
	// Methods
	//---------
	
	/** Returns the first class level method matching the given name. If no method with that name exists, null is returned instead. */
	public EnvisionFunction getMethod(String methodName) {
		return instanceScope.methods().getFirst(m -> m.getName().equals(methodName));
	}
	
	/** Returns a class level method matching the given name and parameters (if it exists. */
	public EnvisionFunction getMethod(String methodName, ParameterData params) {
		return instanceScope.methods().getFirst(m -> m.getName().equals(methodName) && m.getParams().compare(params));
	}
	
	/** Adds an operator overload to this instance which defines behavior for how to respond when this class instance is used in conjunction
	 *  within expression statements.
	 *  
	 * @param opIn The operator being overloaded (ex: +, *, ++, etc.)
	 * @param meth The method which defines the operator overload behavior
	 * @return this (the instance)
	 */
	public ClassInstance addOperator(IKeyword opIn, EnvisionFunction meth) {
		EArrayList<EnvisionFunction> list = operators.get(opIn);
		if (list == null) operators.add(opIn, new EArrayList().addRT(meth));
		else list.add(meth);
		return this;
	}
	
	/** Returns true if this class instance (and more specifically the class for which this was derived from) supports the given operator overload. */
	public boolean hasOperator(IKeyword op) {
		return (op != null) ? operators.contains(op) : false;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the scope of this class instance. */
	public Scope getScope() { return instanceScope; }
	/** Returns a field value from this instance's scope. */
	public EnvisionObject get(String name) { return instanceScope.get(name); }
	/** Returns the class for which this instance is derived from. */
	public EnvisionClass getEClass() { return theClass; }
	/** Returns the operator overload method corresponding to the given operator. Returns null if there is no overload. */
	public EArrayList<EnvisionFunction> getOperator(IKeyword op) { return operators.get(op); }
	/** Returns the list of all operators being overloaded with their corresponding overload method. */
	public BoxList<IKeyword, EArrayList<EnvisionFunction>> getOperators() { return operators; }
	
	//---------
	// Setters
	//---------
	
	/** Assigns a field value within this instance's scope. */
	public EnvisionObject set(String name, EnvisionObject in) {
		instanceScope.set(name, in);
		return in;
	}
	
	/** Assigns a field value within this instance's scope. */
	public EnvisionObject set(String name, EnvisionDatatype type, EnvisionObject in) {
		instanceScope.set(name, type, in);
		return in;
	}
	
}
