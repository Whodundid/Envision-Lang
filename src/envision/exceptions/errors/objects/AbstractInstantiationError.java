package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.classes.EnvisionClass;

/**
 * An error thrown when a class marked as abstract is attempted to be
 * instantiated.
 * 
 * <p>
 * Abstract classes cannot be directly instantiated and must be inherited
 * by some non-abstract child class in order to fully define any (and all)
 * abstract functions the parent(s) class(es) declare.
 * 
 * @author Hunter Bragg
 */
public class AbstractInstantiationError extends EnvisionError {
	
	public AbstractInstantiationError(EnvisionClass in) {
		super("The class '" + in.getClassName() + "' is abstract and cannot be directly instantiated!");
	}

}
