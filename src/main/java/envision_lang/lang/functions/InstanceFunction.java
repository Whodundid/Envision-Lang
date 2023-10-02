package envision_lang.lang.functions;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;

public abstract class InstanceFunction<E extends EnvisionObject> extends EnvisionFunction {
	
	protected E inst;
	
	//--------------
	// Constructors
	//--------------
	
	protected InstanceFunction(IDatatype rt, String nameIn) {
		super(rt, nameIn, ParameterData.EMPTY_PARAMS);
	}
	
	protected InstanceFunction(IDatatype rt, String name, IDatatype... params) {
		super(rt, name, ParameterData.from(params));
	}
	
	protected InstanceFunction(IDatatype rt, String nameIn, ParameterData paramsIn) {
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
