package envision.lang.util;

import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;

public abstract class InstanceFunction<E extends ClassInstance> extends EnvisionFunction {
	
	protected E inst;
	
	//--------------
	// Constructors
	//--------------
	
	public InstanceFunction(E instIn, Primitives rt, String nameIn) {
		super(rt.toDatatype(), nameIn, new ParameterData());
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, Primitives rt, String name, Primitives... params) {
		super(rt.toDatatype(), name, new ParameterData(params));
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, Primitives rt, String name, EnvisionDatatype... params) {
		super(rt.toDatatype(), name, new ParameterData(params));
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, EnvisionDatatype rt, String name, Primitives... params) {
		super(rt, name, new ParameterData(params));
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, EnvisionDatatype rt, String name, EnvisionDatatype... params) {
		super(rt, name, new ParameterData(params));
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, Primitives rTypeIn, String nameIn, ParameterData paramsIn) {
		super(rTypeIn, nameIn, paramsIn);
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, EnvisionDatatype rTypeIn, String nameIn) {
		super(rTypeIn, nameIn, new ParameterData());
		inst = instIn;
	}
	
	public InstanceFunction(E instIn, EnvisionDatatype rTypeIn, String nameIn, ParameterData paramsIn) {
		super(rTypeIn, nameIn, paramsIn);
		inst = instIn;
	}
	
	//---------
	// Setters
	//---------

	public InstanceFunction<E> setInst(E instIn) {
		inst = instIn;
		return this;
	}
	
}
