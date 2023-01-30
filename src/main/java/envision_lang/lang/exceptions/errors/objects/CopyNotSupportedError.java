package envision_lang.lang.exceptions.errors.objects;

import envision_lang.lang.exceptions.EnvisionLangError;

/**
 * An error thrown in the event that an object, attempting to be
 * copied, does not actually support inherent object copying.
 * <p>
 * This error can be thrown for a multitude of reasons, most notably being that the object in question is not
 * a primitive type in some degree. For instance, base EnvisionObjects -- especially primitive types -- will
 * inherently support copying in some regard. User-Defined classes, unless explicitly declared, will not
 * support this functionality as there is no way to properly determine the correct resulting behavior for a copy.
 * <p>
 * Furthermore, in most situations, an object copy will simply create a new object instance with the underlying
 * primitive values copied over. However, EnvisionLists are an execption to this rule as copying list contents could
 * easily result in an undefined copy behavior scenario. As such, List copies will simply copy pointer values to
 * the already existing list elements.
 * 
 * @author Hunter Bragg
 */
public class CopyNotSupportedError extends EnvisionLangError {
	
	public CopyNotSupportedError() {
		super("Copy Not Supported!");
	}

}
