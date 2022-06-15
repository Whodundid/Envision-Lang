package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.natives.IDatatype;

/**
 * An error thrown in the event that an object, attempting to be
 * cast to another type, can not be directly cast to the given type.
 * 
 * @author Hunter Bragg
 */
public class ClassCastError extends EnvisionError {
	
	public ClassCastError(EnvisionObject obj, IDatatype castType) {
		super("Invalid Cast! Cannot cast '" + obj + "' to a '" + castType + "'!");
	}
	
	public ClassCastError(String msg) {
		super(msg);
	}

}