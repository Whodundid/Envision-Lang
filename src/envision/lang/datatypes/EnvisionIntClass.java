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

public class EnvisionIntClass extends EnvisionClass {

	/**
	 * The singular, static Int class for which all Envision:Int
	 * objects are derived from.
	 */
	public static final EnvisionIntClass INT_CLASS = new EnvisionIntClass();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionIntClass() {
		super(Primitives.INT);
		
		//define static members
		staticClassScope.defineFunction(new IFunc_static_valueOf());
		
		//set final to prevent user-extension
		setFinal();
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionInt newInt() { return newInt(0); }
	public static EnvisionInt newInt(boolean value) { return newInt(value ? 1 : 0); }
	public static EnvisionInt newInt(char value) { return newInt((int) value); }
	public static EnvisionInt newInt(EnvisionNumber value) { return newInt(value.intVal_i()); }
	public static EnvisionInt newInt(Number value) {
		EnvisionInt i = new EnvisionInt(value);
		INT_CLASS.defineScopeMembers(i);
		return i;
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
		EnvisionInt int_val = null;
		
		//if no args, return default char instance
		if (args.length == 0) int_val = new EnvisionInt();
		
		//otherwise, attempt to create from passed args
		
		//ensure there is at most 1 argument being passed
		if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		
		Object arg_val = false;
		if (args.length == 1) arg_val = args[0];
		
		//don't accept null arguments
		if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
		
		//check for invalid argument constructor datatypes
		if (arg_val instanceof Number n) int_val = new EnvisionInt(n.longValue());
		if (arg_val instanceof Boolean b) int_val = new EnvisionInt((b) ? 1l : 0l);
		
		if (int_val == null)
			throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		
		//define scope members
		defineScopeMembers(int_val);
		
		return int_val;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to boolean
		EnvisionInt i = (EnvisionInt) inst;
		
		//extract instance scope
		Scope inst_scope = i.getScope();
		
		//define instance members
		//inst_scope.defineFunction(new IFunc_get(i));
		//inst_scope.defineFunction(new IFunc_set(i));
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionInt> extends InstanceFunction<E> {
		IFunc_get(E instIn) { super(instIn, INT, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionIntClass.newInt(inst.int_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionInt> extends InstanceFunction<E> {
		IFunc_set(E instIn) { super(instIn, INT, "set", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.int_val = ((EnvisionInt) args[0]).int_val;
			ret(inst);
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(INT, "valueOf", NUMBER);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionNumber env_num) ret(EnvisionIntClass.newInt(env_num));
			else throw new EnvisionError("Invalid type -- should not have reached here!");
		}
	}
	
}