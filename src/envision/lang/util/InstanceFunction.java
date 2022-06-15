package envision.lang.util;

import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.IDatatype;

public abstract class InstanceFunction<E extends ClassInstance> extends EnvisionFunction {
	
	protected E inst;
	
	//--------------
	// Constructors
	//--------------
	
	public InstanceFunction(IDatatype rt, String nameIn) {
		super(rt, nameIn, new ParameterData());
	}
	
	public InstanceFunction(IDatatype rt, String name, IDatatype... params) {
		super(rt, name, new ParameterData(params));
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
