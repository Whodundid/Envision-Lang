package envision.lang.util.structureTypes;

import envision.lang.util.EnvisionDataType;

public abstract class InstantiableObject extends InheritableObject implements CallableObject {

	protected boolean isInstantiable = true;
	
	protected InstantiableObject(EnvisionDataType internalTypeIn, String nameIn) {
		super(internalTypeIn, nameIn);
	}
	
	public void setInstantiable(boolean val) { isInstantiable = val; }
	public boolean isInstantiable() { return isInstantiable; }
	
}
