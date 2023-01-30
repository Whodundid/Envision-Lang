package envision_lang.lang.exceptions.errors.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;

/**
 * An error thrown in the event that an object, attempting to be
 * cast to another type, can not be directly cast to the given type.
 * 
 * @author Hunter Bragg
 */
public class ClassCastError extends EnvisionLangError {
	
	public ClassCastError(EnvisionObject obj, IDatatype castType) {
		super("Invalid Cast! Cannot cast '" + obj + "' to a '" + castType + "'!");
	}
	
	public ClassCastError(String msg) {
		super(msg);
	}

}