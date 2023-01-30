package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;

public abstract class InstanceFunction<E extends EnvisionObject> extends EnvisionFunction {
	
	protected E inst;
	
	//--------------
	// Constructors
	//--------------
	
	public InstanceFunction(IDatatype rt, String nameIn) {
		super(rt, nameIn, ParameterData.EMPTY_PARAMS);
	}
	
	public InstanceFunction(IDatatype rt, String name, IDatatype... params) {
		super(rt, name, ParameterData.from(params));
	}
	
	public InstanceFunction(IDatatype rt, String nameIn, ParameterData paramsIn) {
		super(rt, nameIn, paramsIn);
	}
	
	//---------
	// Setters
	//---------

	public InstanceFunction<E> setInst(E instIn) {
		inst = instIn;
		return this;
	}
	
}
