package envision.lang.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArgLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.EnvisionObject;
import envision.lang.util.data.ParameterData;
import eutil.datatypes.EArrayList;

public abstract class InternalFunction {
	
	private String name;
	private EnvisionDatatype rType;
	private boolean varags = false;
	private ParameterData params;
	private InternalFunction superMethod = null;
	private EArrayList<InternalFunction> differentArgs = new EArrayList();
	
	public InternalFunction(Primitives returnTypeIn, String nameIn, Primitives... paramsIn) {
		this(new EnvisionDatatype(returnTypeIn), nameIn, paramsIn);
	}
	public InternalFunction(EnvisionDatatype returnTypeIn, String nameIn, Primitives... paramsIn) {
		//super(nameIn);
		//super(returnTypeIn, nameIn);
		
		//returnType = returnTypeIn;
		rType = returnTypeIn;
		name = nameIn;
		params = ParameterData.fromTypes(paramsIn);
		
		for (Primitives t : paramsIn) {
			if (t.isArrayType()) {
				varags = true;
				break;
			}
		}
	}
	
	//-----------------------------------------------
	
	/** Called when actually executing body. */
	protected abstract void body(Object[] args);
	
	//-----------------------------------------------
	
	/** Called to begin checking args vs. params and execute internal method body. */
	//@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		InternalFunction theMeth = this;
		
		for (InternalFunction m : theMeth.differentArgs) {
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
	
	public boolean comapreParams(ParameterData in) {
		return params.compare(in);
	}
	
	/** Checks that arg num matches param num and that each passed arg type matches the coresponding param type. */
	protected void validateParams(Object[] args) {
		//if null args, wrap as empty array
		if (args == null) args = new Object[0];
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
	
	public InternalFunction getSuper() { return superMethod; }
	public String getIFuncName() { return name; }
	public EnvisionDatatype getReturnType() { return rType; }
	public ParameterData getParams() { return params; }
	public boolean isVoid() { return rType.isVoid(); }
	
}
