package envision.lang.util;

import envision.interpreter.util.scope.Scope;
import envision.lang.classes.ClassInstance;
import eutil.datatypes.EArrayList;

/**
 * Essentially a glorified list that is used to directly define a set of
 * internal function prototypes for a given native EnvisionObject.
 * <p>
 * Note: The intended use of this object is entirely internal and is not
 * designed to be interfaced with actual Envision code.
 * 
 * @author Hunter Bragg
 */
public class IPrototypeHandler {

	private final EArrayList<FunctionPrototype> prototypes = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public IPrototypeHandler() {}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Creates a new internal function prototype definition
	 * with the given function name.
	 * <p>
	 * Note: Duplicate function names will not throw any kind
	 * of error when being added. A duplicate internal prototype
	 * will simply overwrite the previous function prototype name.
	 * <p>
	 * Furthermore, it is the responsibility of the programmer
	 * to ensure that a defined prototype's name is not going to
	 * directly overwrite an already existing scope value.
	 * 
	 * @param func_name The name of the prototype to create
	 */
	public FunctionPrototype addFunction(String func_name) {
		return addFunction(func_name, EnvisionDatatype.VAR_TYPE, new ParameterData());
	}
	
	public FunctionPrototype addFunction(String func_name, Primitives rType) {
		return addFunction(func_name, rType.toDatatype(), new ParameterData());
	}
	
	public FunctionPrototype addFunction(String func_name, EnvisionDatatype rType) {
		return addFunction(func_name, rType, new ParameterData());
	}
	
	public FunctionPrototype addFunction(String func_name, Primitives rType, Primitives... params) {
		return addFunction(func_name, rType.toDatatype(), new ParameterData(params));
	}
	
	public FunctionPrototype addFunction(String func_name, EnvisionDatatype rType, Primitives... params) {
		return addFunction(func_name, rType, new ParameterData(params));
	}
	
	public FunctionPrototype addFunction(String func_name, Primitives rType, EnvisionDatatype... params) {
		return addFunction(func_name, rType.toDatatype(), new ParameterData(params));
	}
	
	public FunctionPrototype addFunction(String func_name, EnvisionDatatype rType, EnvisionDatatype... params) {
		return addFunction(func_name, rType, new ParameterData(params));
	}
	
	public FunctionPrototype addFunction(String func_name, Primitives rType, ParameterData params) {
		return addFunction(func_name, rType.toDatatype(), params);
	}
	
	public FunctionPrototype addFunction(String func_name, EnvisionDatatype rType, ParameterData params) {
		var p = new FunctionPrototype(func_name, rType, params);
		prototypes.add(p);
		return p;
	}
	
	/**
	 * Defines this handler's set of prototypes onto the given class instance's
	 * scope.
	 * 
	 * @param inst The class instance for which this handler is defining on
	 */
	public void defineOn(ClassInstance inst) {
		defineOn(inst.getScope());
	}
	
	/**
	 * Defines this handler's set of prototypes onto the given scope.
	 * 
	 * @param scope The scope for which this handler is defining on
	 */
	public void defineOn(Scope scope) {
		prototypes.forEach(p -> scope.define(p.getFunctionName(), EnvisionDatatype.FUNC_TYPE, p));
	}
	
}
