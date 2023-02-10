package envision_lang.lang.functions;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;

/**
 * The parent class for all Envision Functions. This is a special
 * class such that new function instances should not, furthermore
 * cannot, be directly instantiated from this class context. Any
 * attempt to do so will result in an error being thrown.
 * <p>
 * Native function creation is strictly exclusive to the
 * EnvisionInterpreter and its various runtime handles.
 * 
 * @author Hunter Bragg
 */
public class EnvisionFunctionClass extends EnvisionClass {

	/**
	 * The single, static class instance for which all Envision Function
	 * instances are derived from.
	 * <p>
	 * <b> DO NOT USE THIS CLASS TO INSTANTIATE NEW FUNCTION INSTANCES.
	 * </b>
	 * <p>
	 * This class is exclusively intended to provide a back-bone structure
	 * to functions in an object oriented context. This class is also
	 * responsible for holding any (and all) native EnvisionFunction
	 * static and abstract methods.
	 */
	public static final EnvisionFunctionClass FUNC_CLASS = new EnvisionFunctionClass();
	
	/**
	 * Character member function prototypes.
	 */
	private static final IPrototypeHandler FUNC_PROTOS = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		FUNC_PROTOS.define("invoke", VAR, VAR_A).assignDynamicClass(IFunc_invoke.class);
		FUNC_PROTOS.define("hasOverloads", BOOLEAN).assignDynamicClass(IFunc_hasOverloads.class);
		FUNC_PROTOS.define("getOverloads", LIST).assignDynamicClass(IFunc_getOverloads.class);
		FUNC_PROTOS.define("getReturnType", STRING).assignDynamicClass(IFunc_getReturnType.class);
		FUNC_PROTOS.define("getParamTypes", LIST).assignDynamicClass(IFunc_getParamTypes.class);
		FUNC_PROTOS.define("getParamNames", LIST).assignDynamicClass(IFunc_getParamNames.class);
	}
	
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
		throw new EnvisionLangError("Illegal Function Instantiation!");
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new EnvisionLangError("Illegal Function Instantiation!");
	}
	
	//---------
	// Methods
	//---------
	
	public void defineFunctionScopeMembers(EnvisionFunction func) {
		//define super object's members
		super.defineScopeMembers(func);
		//define scope members
		FUNC_PROTOS.defineOn(func);
	}
	
	//---------------------------------
	// Static Function Class Functions
	//---------------------------------
	
	/**
	 * Wrapped invoke method for Envision function calls. Parameter
	 * checking is handled during runtime and relevant errors are
	 * propagated in the event of parameter mismatches.
	 * <p>
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
			super(instIn.getReturnType(), "invoke", instIn.params);
			//account for overload invokes
			for (var overload : instIn.overloads) addOverload(overload);
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.invoke(interpreter, args);
		}
	}
	
	private static class IFunc_hasOverloads<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_hasOverloads(E instIn) { super(BOOLEAN, "hasOverloads"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (inst.overloads.isNotEmpty()) ret(EnvisionBoolean.TRUE);
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	private static class IFunc_getOverloads<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getOverloads(E instIn) { super(LIST, "getOverloads"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList l = EnvisionListClass.newList();
			for (var overload : inst.getOverloads()) l.add(overload);
			ret(l);
		}
	}
	
	private static class IFunc_getReturnType<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getReturnType(E instIn) { super(STRING, "getReturnType"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionString rt = EnvisionStringClass.valueOf(inst.returnType);
			ret(rt);
		}
	}
	
	private static class IFunc_getParamTypes<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getParamTypes(E instIn) { super(LIST, "getParamTypes"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList types = EnvisionListClass.newList();
			for (var p : inst.getParamTypes()) types.add(EnvisionStringClass.valueOf(p.getStringValue()));
			ret(types);
		}
	}
	
	private static class IFunc_getParamNames<E extends EnvisionFunction> extends InstanceFunction<E> {
		public IFunc_getParamNames(E instIn) { super(LIST, "getParamNames"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList names = EnvisionListClass.newList();
			for (var p : inst.getParamNames()) names.add(EnvisionStringClass.valueOf(p));
			ret(names);
		}
	}
	
}
