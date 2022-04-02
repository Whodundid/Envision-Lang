package envision.lang.internal;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionBoolean;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.util.InstanceFunction;
import envision.lang.util.Primitives;

/**
 * The parent class for all Envision Functions. This is a special
 * class in that new function instances should not, and cannot be
 * directly instantiated from this class. Any attempt to do so will
 * result in an error being thrown. Native function creation is
 * strictly exclusive to the EnvisionInterpreter and its various
 * runtime handles.
 * 
 * @author Hunter Bragg
 */
public class EnvisionFunctionClass extends EnvisionClass {

	/**
	 * The single, static class instance for which all Envision Function's
	 * derive from. Do not use to instantiate new function instances. This
	 * class is exclusively intended to provide a structure backbone to
	 * functions in an object sense. This class is also responsible for
	 * holding any (and all) native EnvisionFunction static and abstract
	 * methods.
	 */
	public static final EnvisionFunctionClass FUNC_CLASS = new EnvisionFunctionClass();
	
	//--------------
	// Constructors
	//--------------
	
	private EnvisionFunctionClass() {
		super(Primitives.FUNCTION);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public ClassInstance newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new EnvisionError("Illegal Instantiation!");
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new EnvisionError("Illegal Instantiation!");
	}
	
	//---------
	// Methods
	//---------
	
	public void defineFunctionScopeMembers(EnvisionFunction func) {
		//define super object's members
		super.defineScopeMembers(func);
		
		Scope func_scope = func.getScope();
		
		var invoke = new IFunc_invoke(func);
		var hasOverloads = new IFunc_hasOverloads(func);
		var getOverloads = new IFunc_getOverloads(func);
		var getReturnType = new IFunc_getReturnType(func);
		var getParamTypes = new IFunc_getParamTypes(func);
		var getParamNames = new IFunc_getParamNames(func);
		
		func_scope.define(invoke.functionName, invoke.getDatatype(), invoke);
		func_scope.define(hasOverloads.functionName, hasOverloads.getDatatype(), hasOverloads);
		func_scope.define(getOverloads.functionName, getOverloads.getDatatype(), getOverloads);
		func_scope.define(getReturnType.functionName, getReturnType.getDatatype(), getReturnType);
		func_scope.define(getParamTypes.functionName, getParamTypes.getDatatype(), getParamTypes);
		func_scope.define(getParamNames.functionName, getParamNames.getDatatype(), getParamNames);
	}
	
	//---------------------------------
	// Static Function Class Functions
	//---------------------------------
	
	/**
	 * Wrapped invoke method for Envision function calls. Parameter
	 * checking is handled during runtime and relavent errors are
	 * propagated in the event of parameter mismatch.
	 *
	 * Note: This function disregards scope/instance visibility and
	 * carries the possibility of causing potentially undesired affects on
	 * code execution. For this reason especially, Function declarations
	 * should be closely guarded and only distributed to trusted members.
	 * 
	 * @param EnvisionObject (the same as the parameters of the
	 *                       function/overload being called)
	 * 						
	 * @return The result of the invoked function's execution (could be
	 *         something, could be void)
	 */
	private static class IFunc_invoke<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_invoke(E instIn) {
			super(instIn, instIn.getReturnType(), "invoke", instIn.params);
			//account for overload invokes
			for (var overload : instIn.overloads) addOverload(overload);
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.invoke(interpreter, args);
		}
	}
	
	private static class IFunc_hasOverloads<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_hasOverloads(E instIn) { super(instIn, BOOLEAN, "hasOverloads"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (inst.overloads.isNotEmpty()) ret(EnvisionBoolean.TRUE);
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	private static class IFunc_getOverloads<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getOverloads(E instIn) { super(instIn, LIST, "getOverloads"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList l = EnvisionListClass.newList();
			for (var overload : inst.getOverloads()) l.add(overload);
			ret(l);
		}
	}
	
	private static class IFunc_getReturnType<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getReturnType(E instIn) { super(instIn, STRING, "getReturnType"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionString rt = EnvisionStringClass.newString(inst.returnType);
			ret(rt);
		}
	}
	
	private static class IFunc_getParamTypes<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getParamTypes(E instIn) { super(instIn, LIST, "getParamTypes"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList types = EnvisionListClass.newList();
			for (var p : inst.getParamTypes()) types.add(EnvisionStringClass.newString(p.getType()));
			ret(types);
		}
	}
	
	private static class IFunc_getParamNames<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getParamNames(E instIn) { super(instIn, LIST, "getParamNames"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList names = EnvisionListClass.newList();
			for (var p : inst.getParamNames()) names.add(EnvisionStringClass.newString(p));
			ret(names);
		}
	}
	
}
