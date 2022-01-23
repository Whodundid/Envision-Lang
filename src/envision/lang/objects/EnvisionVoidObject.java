package envision.lang.objects;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;

/** A void object cannot be created by any normal means but is used to represent non-present return values from methods. */
public class EnvisionVoidObject extends EnvisionObject {
	
	public EnvisionVoidObject() {
		super(EnvisionDataType.VOID);
		hasObjectMethods = false;
	}
	
	@Override
	public String toString() {
		return "void";
	}
	
}
