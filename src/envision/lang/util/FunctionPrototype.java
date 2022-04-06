package envision.lang.util;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;

/**
 * A special type of internal function which, for all intents and purposes, serves
 * as a placeholder function designating that there 'is' an unbuilt function of the
 * same name. Upon being called,
 * 
 * @author Hunter Bragg
 */
public class FunctionPrototype extends EnvisionObject {

	private final String func_name;
	private final Class<? extends EnvisionFunction> func_class;
	private ClassInstance instance;
	
	private EnvisionFunction built_func;
	private boolean built = false;
	
	//--------------
	// Constructors
	//--------------
	
	public FunctionPrototype(String nameIn, Class<? extends EnvisionFunction> classIn) {
		super(EnvisionDatatype.FUNC_TYPE);
		func_name = nameIn;
		func_class = classIn;
	}
	
	/**
	 * Executed when initially calling the function. The function is built
	 * and replaces the prototype placeholder within the current scope.
	 * 
	 * @param interpreter
	 * @param args
	 * @return
	 */
	public EnvisionFunction build(EnvisionObject[] args) {
		try {
			var pass_args = new Object[]{instance};
			var con = func_class.getDeclaredConstructor(ClassInstance.class);
			var acc = con.canAccess(null);
			
			if (!acc) con.setAccessible(true);
			built_func = con.newInstance(pass_args);
			if (!acc) con.setAccessible(false);
			
			built = true;
			//replace the prototype within the current scope
			instance.getScope().set(func_name, internalType, built_func);
			return built_func;
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new EnvisionError(e);
		}
	}
	
	/**
	 * Returns the name of this function.
	 * 
	 * @return The name of this function
	 */
	public String getFunctionName() {
		return func_name;
	}
	
	/**
	 * Returns the built function.
	 * Note: will return null if not built
	 * 
	 * @return The built function
	 */
	public EnvisionFunction getFunction() {
		return built_func;
	}
	
	/**
	 * Assigns the classInstance for which the internal function will be
	 * built from.
	 * 
	 * @param inst
	 * @return
	 */
	public FunctionPrototype setInstance(ClassInstance inst) {
		instance = inst;
		return this;
	}
	
}
