package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.InstanceFunction;
import envision.lang.util.Primitives;

public class EnvisionDoubleClass extends EnvisionClass {

	/**
	 * The singular, static Double class for which all Envision:Double
	 * objects are derived from.
	 */
	public static final EnvisionDoubleClass DOUBLE_CLASS = new EnvisionDoubleClass();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionDoubleClass() {
		super(Primitives.DOUBLE);
		
		//define static members
		staticClassScope.defineFunction(new IFunc_static_valueOf());
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionDouble newDouble() { return newDouble(0.0); }
	public static EnvisionDouble newDouble(boolean value) { return newDouble(value ? 1.0 : 0.0); }
	public static EnvisionDouble newDouble(char value) { return newDouble((double) value); }
	public static EnvisionDouble newDouble(EnvisionNumber num) { return newDouble(num.doubleVal_i()); }
	public static EnvisionDouble newDouble(Number value) {
		EnvisionDouble d = new EnvisionDouble(value);
		DOUBLE_CLASS.defineScopeMembers(d);
		return d;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public ClassInstance newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionDouble double_val = null;
		
		//if no args, return default char instance
		if (args.length == 0) double_val = new EnvisionDouble();
		
		//otherwise, attempt to create from passed args
		
		//ensure there is at most 1 argument being passed
		if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		
		Object arg_val = false;
		if (args.length == 1) arg_val = args[0];
		
		//don't accept null arguments
		if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
		
		//check for invalid argument constructor datatypes
		if (arg_val instanceof Number n) double_val = new EnvisionDouble(n.doubleValue());
		if (arg_val instanceof Boolean b) double_val = new EnvisionDouble((b) ? 1.0 : 0.0);
		
		if (double_val == null)
			throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		
		//define scope memebers
		defineScopeMembers(double_val);
		
		return double_val;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to boolean
		EnvisionDouble d = (EnvisionDouble) inst;
		
		//extract instance scope
		Scope inst_scope = d.getScope();
		
		//define instance members
		inst_scope.defineFunction(new IFunc_get(d));
		inst_scope.defineFunction(new IFunc_set(d));
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionDouble> extends InstanceFunction<E> {
		IFunc_get(E instIn) { super(instIn, DOUBLE, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionDoubleClass.newDouble(inst.double_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionDouble> extends InstanceFunction<E> {
		IFunc_set(E instIn) { super(instIn, DOUBLE, "set", DOUBLE); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.double_val = ((EnvisionDouble) args[0]).double_val;
			ret(inst);
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(DOUBLE, "valueOf", NUMBER);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionNumber env_num) ret(EnvisionDoubleClass.newDouble(env_num));
			else throw new EnvisionError("Invalid type -- should not have reached here!");
		}
	}
	
}
