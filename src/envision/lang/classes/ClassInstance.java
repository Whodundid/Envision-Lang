package envision.lang.classes;

import static envision.lang.util.Primitives.*;

import java.util.HashMap;

import envision.exceptions.errors.DuplicateFunctionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InstanceFunction;
import envision.lang.util.ParameterData;
import envision.tokenizer.IKeyword;
import envision.tokenizer.Operator;

/**
 * An instantiated version of an EnvisionClass.
 */
public class ClassInstance extends EnvisionObject {
	
	/**
	 * The scope of this instance. Directly inherited from the calling
	 * scope and the overarching class's scope from which this instance
	 * was defined from.
	 */
	protected final Scope instanceScope;
	
	/**
	 * Used for operator overloading. In the event that a class defines
	 * specific methods detailing operator functionality, the operators which
	 * have been overloaded will be statically referenced here.
	 */
	protected HashMap<Operator, EnvisionFunction> operators = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	public ClassInstance(EnvisionClass derivingClassIn, Scope instanceScopeIn) {
		super(derivingClassIn.getDatatype());
		internalClass = derivingClassIn;
		instanceScope = instanceScopeIn;
	}
	
	/**
	 * Creates a new ClassInstance without a direct instance scope. The
	 * object's instance scope is derived directly from the parent class's
	 * scope.
	 * 
	 * @param derivingClassIn
	 */
	protected ClassInstance(EnvisionClass derivingClassIn) {
		super(derivingClassIn.getDatatype());
		internalClass = derivingClassIn;
		instanceScope = new Scope(derivingClassIn.staticClassScope);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String getTypeString() {
		return internalClass.getTypeString();
	}
	
	@Override
	public String toString() {
		return getDatatype() + "_" + Integer.toHexString(hashCode());
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns the first class level function matching the given name. If
	 * no function with that name exists, null is returned instead.
	 */
	public EnvisionFunction getFunction(String funcName) {
		return instanceScope.functions().getFirst(m -> m.getFunctionName().equals(funcName));
	}
	
	/**
	 * Returns a class level method matching the given name and parameters
	 * (if it exists).
	 */
	public EnvisionFunction getFunction(String funcName, ParameterData params) {
		return instanceScope.functions().getFirst(
				m -> m.getFunctionName().equals(funcName) && m.getParams().compare(params));
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the scope of this class instance.
	 */
	public Scope getScope() {
		return instanceScope;
	}
	
	/**
	 * Returns a field value from this instance's scope.
	 */
	public EnvisionObject get(String name) {
		return instanceScope.get(name);
	}
	
	/**
	 * Returns the class for which this instance is derived from.
	 */
	public EnvisionClass getEClass() {
		return internalClass;
	}
	
	//---------
	// Setters
	//---------
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	public EnvisionObject set(String name, EnvisionObject in) {
		instanceScope.set(name, in);
		return in;
	}
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	public EnvisionObject set(String name, EnvisionDatatype type, EnvisionObject in) {
		instanceScope.set(name, type, in);
		return in;
	}

	/**
	 * Adds an operator overload to this instance which defines behavior
	 * for how to respond when this class instance is used in conjunction
	 * within expression statements.
	 * 
	 * @param opIn The operator being overloaded (ex: +, *, ++, etc.)
	 * @param func The method which defines the operator overload behavior
	 * @return this (the instance)
	 */
	public void addOperatorOverload(Operator operator, EnvisionFunction op) {
		if (operators.containsKey(operator)) {
			EnvisionFunction opFunc = operators.get(operator);
			String funcName = opFunc.getFunctionName();
			ParameterData incoming_params = op.getParams();
			
			//prevent duplicate overload
			if (opFunc.hasOverload(incoming_params)) throw new DuplicateFunctionError(funcName, incoming_params);
			
			opFunc.addOverload(op);
			
		}
		operators.put(operator, op);
	}
	
	/**
	 * Returns the operator overload method corresponding to the given
	 * operator. Returns null if there is no overload.
	 */
	public EnvisionFunction getOperator(IKeyword op) {
		return operators.get(op);
	}
	
	/**
	 * Returns the list of all operators being overloaded with their
	 * corresponding overload method.
	 */
	public HashMap<Operator, EnvisionFunction> getOperators() {
		return operators;
	}
	
	/**
	 * Returns true if this class instance (and more specifically the
	 * class for which this was derived from) supports the given operator
	 * overload.
	 */
	public boolean supportsOperator(Operator op) {
		return (op != null) ? operators.containsKey(op) : false;
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	public static class IFunc_equals<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_equals(E instIn) { super(instIn, BOOLEAN, "equals", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var eq = inst.equals((EnvisionInt) args[0]);
			ret(EnvisionBooleanClass.newBoolean(eq));
		}
	}
	
	public static class IFunc_hash<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_hash(E instIn) { super(instIn, INT, "hash"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var hash = inst.getObjectHash();
			ret(EnvisionIntClass.newInt(hash));
		}
	}
	
	public static class IFunc_hexHash<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_hexHash(E instIn) { super(instIn, STRING, "hexHash"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var hexHash = inst.getHexHash();
			ret(EnvisionStringClass.newString(hexHash));
		}
	}
	
	public static class IFunc_isStatic<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_isStatic(E instIn) { super(instIn, BOOLEAN, "isStatic"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var isStatic = inst.isStatic();
			ret(EnvisionBooleanClass.newBoolean(isStatic));
		}
	}
	
	public static class IFunc_isFinal<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_isFinal(E instIn) { super(instIn, BOOLEAN, "isFinal"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var isFinal = inst.isFinal();
			ret(EnvisionBooleanClass.newBoolean(isFinal));
		}
	}
	
	public static class IFunc_toString<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_toString(E instIn) { super(instIn, STRING, "toString"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var toString = inst.toString();
			ret(EnvisionStringClass.newString(toString));
		}
	}
	
	public static class IFunc_type<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_type(E instIn) { super(instIn, STRING, "type"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var type = inst.getDatatype().getType();
			ret(EnvisionStringClass.newString(type));
		}
	}
	
	public static class IFunc_typeString<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_typeString(E instIn) { super(instIn, STRING, "typeString"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var typeString = inst.getDatatype().getType() + "_" + inst.getHexHash();
			ret(EnvisionStringClass.newString(typeString));
		}
	}
	
	public static class IFunc_functions<E extends ClassInstance> extends InstanceFunction<E> {
		IFunc_functions(E instIn) { super(instIn, LIST, "functions"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList functions = EnvisionListClass.newList(EnvisionDatatype.FUNC_TYPE);
			for (var f : inst.getScope().getMethods()) functions.add(f);
			ret(functions);
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
}
