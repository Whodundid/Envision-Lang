package envision.lang.classes;

import envision.interpreter.util.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.tokenizer.Keyword;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxHolder;

/** An instantiated version of an EnvisionClass. */
public class ClassInstance extends EnvisionObject {
	
	/** The EnvisionClass for which this is an instance of. */
	public final EnvisionClass theClass;
	/** The scope of this instance. Directly inherited from the calling scope. */
	protected final Scope instanceScope;
	/** Used for operator overloading. */
	protected BoxHolder<Keyword, EArrayList<EnvisionMethod>> operators = new BoxHolder();
	
	//--------------
	// Constructors
	//--------------
	
	public ClassInstance(EnvisionClass classIn, Scope in) {
		super(EnvisionDataType.CLASS_INSTANCE);
		setName(classIn.getName());
		theClass = classIn;
		instanceScope = in;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String getTypeString() { return theClass.getTypeString(); }
	@Override public String toString() { return getTypeString() + "_" + Integer.toHexString(hashCode()); }
	
	//---------
	// Methods
	//---------
	
	/** Returns the first class level method matching the given name. If no method with that name exists, null is returned instead. */
	public EnvisionMethod getMethod(String methodName) {
		return instanceScope.methods().getFirst(m -> m.getName().equals(methodName));
	}
	
	/** Returns a class level method matching the given name and parameters (if it exists. */
	public EnvisionMethod getMethod(String methodName, EArrayList<String> params) {
		return instanceScope.methods().getFirst(m -> m.getName().equals(methodName) && m.getParams().compare(params));
	}
	
	/** Adds an operator overload to this instance which defines behavior for how to respond when this class instance is used in conjunction
	 *  within expression statements.
	 *  
	 * @param opIn The operator being overloaded (ex: +, *, ++, etc.)
	 * @param meth The method which defines the operator overload behavior
	 * @return this (the instance)
	 */
	public ClassInstance addOperator(Keyword opIn, EnvisionMethod meth) {
		EArrayList<EnvisionMethod> list = operators.get(opIn);
		if (list == null) operators.add(opIn, new EArrayList().addRT(meth));
		else list.add(meth);
		return this;
	}
	
	/** Returns true if this class instance (and more specifically the class for which this was derived from) supports the given operator overload. */
	public boolean hasOperator(Keyword op) {
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
	public EArrayList<EnvisionMethod> getOperator(Keyword op) { return operators.get(op); }
	/** Returns the list of all operators being overloaded with their corresponding overload method. */
	public BoxHolder<Keyword, EArrayList<EnvisionMethod>> getOperators() { return operators; }
	
	//---------
	// Setters
	//---------
	
	/** Assigns a field value within this instance's scope. */
	public EnvisionObject set(String name, EnvisionObject in) { instanceScope.set(name, in); return in; }
	
}
