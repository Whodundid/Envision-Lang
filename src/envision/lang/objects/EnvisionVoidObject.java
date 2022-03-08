package envision.lang.objects;

import envision.lang.EnvisionObject;
import envision.lang.util.Primitives;

/** A void object cannot be created by any normal means but is used to represent non-present return values from methods. */
public class EnvisionVoidObject extends EnvisionObject {
	
	public EnvisionVoidObject() {
		super(Primitives.VOID);
		hasInternalFunctions = false;
	}
	
	@Override
	public String toString() {
		return "void";
	}
	
}
