package envision.lang.util;

import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;

public abstract class InstanceFunction<E extends ClassInstance> extends EnvisionFunction {
	
	protected E inst;
	
	//--------------
	// Constructors
	//--------------
	
	public InstanceFunction(Primitives rt, String nameIn) {
		super(rt.toDatatype(), nameIn, new ParameterData());
	}
	
	public InstanceFunction(Primitives rt, String name, Primitives... params) {
		super(rt.toDatatype(), name, new ParameterData(params));
	}
	
	public InstanceFunction(Primitives rt, String name, EnvisionDatatype... params) {
		super(rt.toDatatype(), name, new ParameterData(params));
	}
	
	public InstanceFunction(EnvisionDatatype rt, String name, Primitives... params) {
		super(rt, name, new ParameterData(params));
	}
	
	public InstanceFunction(EnvisionDatatype rt, String name, EnvisionDatatype... params) {
		super(rt, name, new ParameterData(params));
	}
	
	public InstanceFunction(Primitives rTypeIn, String nameIn, ParameterData paramsIn) {
		super(rTypeIn, nameIn, paramsIn);
	}
	
	public InstanceFunction(EnvisionDatatype rTypeIn, String nameIn) {
		super(rTypeIn, nameIn, new ParameterData());
	}
	
	public InstanceFunction(EnvisionDatatype rTypeIn, String nameIn, ParameterData paramsIn) {
		super(rTypeIn, nameIn, paramsIn);
	}
	
	//---------
	// Setters
	//---------

	public InstanceFunction<E> setInst(E instIn) {
		inst = instIn;
		return this;
	}
	
}
