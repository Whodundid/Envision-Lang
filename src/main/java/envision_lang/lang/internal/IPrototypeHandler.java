package envision_lang.lang.internal;

import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.lang.util.ParameterData;
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
	public FunctionPrototype define(String func_name) {
		return define(func_name, StaticTypes.VAR_TYPE, ParameterData.EMPTY_PARAMS);
	}
	
	public FunctionPrototype define(String func_name, IDatatype rType) {
		return define(func_name, rType, ParameterData.EMPTY_PARAMS);
	}
	
	public FunctionPrototype define(String func_name, IDatatype rType, IDatatype... params) {
		return define(func_name, rType, ParameterData.from(params));
	}
	
	public FunctionPrototype define(String func_name, IDatatype rType, ParameterData params) {
		var p = new FunctionPrototype(func_name, rType, params);
		return prototypes.addR(p);
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
	public void defineOn(IScope scope) {
		for (var p : prototypes) {
			var name = p.getFunctionName();
			scope.define(name, StaticTypes.FUNC_TYPE, p);
		}
	}
	
}
