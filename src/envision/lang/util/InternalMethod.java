package envision.lang.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArgLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.EnvisionObject;
import envision.lang.util.data.ParameterData;
import eutil.datatypes.EArrayList;

public abstract class InternalMethod {
	
	private String name;
	private String rType;
	private boolean varags = false;
	private ParameterData params;
	private InternalMethod superMethod = null;
	private EArrayList<InternalMethod> differentArgs = new EArrayList();
	
	public InternalMethod(EnvisionDataType returnTypeIn, String nameIn, EnvisionDataType... paramsIn) { this(returnTypeIn.type, nameIn, paramsIn); }
	public InternalMethod(String returnTypeIn, String nameIn, EnvisionDataType... paramsIn) {
		//super(nameIn);
		//super(returnTypeIn, nameIn);
		
		//returnType = returnTypeIn;
		rType = returnTypeIn;
		name = nameIn;
		params = ParameterData.fromTypes(paramsIn);
		
		for (EnvisionDataType t : paramsIn) {
			if (t.isArrayType()) { varags = true; break; }
		}
	}
	
	//-----------------------------------------------
	
	/** Called when actually executing body. */
	protected abstract void body(Object[] args);
	
	//-----------------------------------------------
	
	/** Called to begin checking args vs. params and execute internal method body. */
	//@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		InternalMethod theMeth = this;
		
		for (InternalMethod m : theMeth.differentArgs) {
			if (m.checkParams(args)) theMeth = m;
			break;
		}
		
		theMeth.validateParams(args);
		theMeth.body(args);
	}
	
	protected boolean checkParams(Object[] args) {
		try {
			
		}
		catch (EnvisionError e) {
			
		}
		
		return false;
	}
	
	/** Checks that arg num matches param num and that each passed arg type matches the coresponding param type. */
	protected void validateParams(Object[] args) {
		if (args == null) throw new EnvisionError(name + ": Null args error!");
		if (!varags && args.length != params.size()) throw new ArgLengthError(name, args.length, params.size());
		
		EArrayList<EnvisionObject> callArgs = ObjectCreator.createArgs(args);
		ParameterData callParams = new ParameterData(callArgs);
		
		if (!params.compare(callParams)) {
			throw new EnvisionError("TEMP: Invalid argument datatype! Expected '" + params + "' but got '" + callParams + "'!");
		}
		
		/*
		for (int i = 0; i < params.size(); i++) {
			Parameter p = params.get(i);
			Parameter a = callParams.get(i);
			
			//accept all vars
			if (p.datatype.equals("object")) { continue; }
			
			if (!p.compare(a)) {
				throw new EnvisionError("TEMP: Invalid argument datatype! Expected '" + p.datatype + "' but got '" + a.datatype + "'!");
			}
		}
		*/
	}
	
	//-----------------------------------------------
	
	public InternalMethod getSuper() { return superMethod; }
	public String getIMethodName() { return name; }
	public String getReturnType() { return rType; }
	public ParameterData getParams() { return params; }
	public boolean isVoid() { return EnvisionDataType.getDataType(getReturnType()) == EnvisionDataType.VOID; }
	
}
