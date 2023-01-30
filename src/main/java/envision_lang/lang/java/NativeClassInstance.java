package envision_lang.lang.java;

import java.util.Map;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.exceptions.errors.NotAFunctionError;
import envision_lang.lang.exceptions.errors.UndefinedFunctionError;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import envision_lang.tokenizer.Operator;

public class NativeClassInstance extends ClassInstance {

	private Object javaClassInstance;
	
	public NativeClassInstance(EnvisionClass derivingClassIn) {
		super(derivingClassIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public EnvisionFunction getFunction(String funcName) {
		return super.getFunction(funcName);
	}
	
	@Override
	public EnvisionFunction getFunction(String funcName, ParameterData params) {
		return super.getFunction(funcName, params);
	}
	
	@Override
	public <E extends EnvisionObject> E executeFunction
		(String funcName, EnvisionInterpreter interpreter)
			throws UndefinedFunctionError, NotAFunctionError
	{
		return super.executeFunction(funcName, interpreter);
	}
	
	@Override
	public <E extends EnvisionObject> E executeFunction
		(String funcName, EnvisionInterpreter interpreter, EnvisionObject[] args)
			throws UndefinedFunctionError, NotAFunctionError
	{
		return super.executeFunction(funcName, interpreter, args);
	}
	
	@Override
	public IScope getScope() {
		return super.getScope();
	}
	
	@Override
	public EnvisionObject get(String name) {
		return super.get(name);
	}
	
	@Override
	public void setScope(IScope in) {
		super.setScope(in);
	}
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	@Override
	public EnvisionObject set(String name, EnvisionObject in) {
		return super.set(name, in);
	}
	
	/**
	 * Assigns a field value within this instance's scope.
	 */
	@Override
	public EnvisionObject set(String name, IDatatype type, EnvisionObject in) {
		return super.set(name, type, in);
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
	@Override
	public void addOperatorOverload(Operator operator, EnvisionFunction op) {
		super.addOperatorOverload(operator, op);
	}
	
	/**
	 * Returns the operator overload method corresponding to the given
	 * operator. Returns null if there is no overload.
	 */
	@Override
	public EnvisionFunction getOperator(Operator op) {
		return super.getOperator(op);
	}
	
	/**
	 * Returns the list of all operators being overloaded with their
	 * corresponding overload method.
	 */
	@Override
	public Map<Operator, EnvisionFunction> getOperators() {
		return super.getOperators();
	}
	
	/**
	 * Returns true if this class instance (and more specifically the
	 * class for which this was derived from) supports the given operator
	 * overload.
	 */
	@Override
	public boolean supportsOperator(Operator op) {
		return super.supportsOperator(op);
	}
	
}
