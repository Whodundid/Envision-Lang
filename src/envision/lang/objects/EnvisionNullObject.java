package envision.lang.objects;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;

public class EnvisionNullObject extends EnvisionObject {
	
	public EnvisionNullObject() {
		super(EnvisionDataType.NULL);
		hasObjectMethods = false;
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
}
