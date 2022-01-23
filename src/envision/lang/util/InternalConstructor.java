package envision.lang.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArgLengthError;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.EnvisionObject;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;
import eutil.datatypes.EArrayList;

public abstract class InternalConstructor {
	
	private boolean varags = false;
	private ParameterData params;
	
	public InternalConstructor(EnvisionDataType... paramsIn) { this(false, paramsIn); }
	public InternalConstructor(boolean varagsIn, EnvisionDataType... paramsIn) {
		params = ParameterData.fromTypes(paramsIn);
		varags = varagsIn;
	}
	
	//-----------------------------------------------
	
	/** Called when actually executing body. */
	protected abstract void body(Object[] args);
	
	//-----------------------------------------------
	
	/** Called to begin checking args vs. params and execute internal method body. */
	public void call(Object caller, Object[] args) {
		validateParams(caller, args);
		body(args);
	}
	
	/** Checks that arg num matches param num and that each passed arg type matches the coresponding param type. */
	protected void validateParams(Object caller, Object[] args) {
		if (args == null) { throw new EnvisionError(caller + ": Null args error!"); }
		if (!varags && args.length != params.size()) { throw new ArgLengthError(caller, args.length, params.size()); }
		
		EArrayList<EnvisionObject> callArgs = ObjectCreator.createArgs(args);
		ParameterData callParams = new ParameterData(callArgs);
		
		for (int i = 0; i < params.size(); i++) {
			Parameter p = params.get(i);
			Parameter a = callParams.get(i);
			
			//accept all vars
			if (p.datatype.equals("var") || p.datatype.equals("object")) { continue; }
			
			if (!p.compare(a)) {
				throw new EnvisionError("TEMP: Invalid argument datatype! Expected '" + p.datatype + "' but got '" + a.datatype + "'!");
			}
		}
	}
	
	//-----------------------------------------------
	
	public ParameterData getParams() { return params; }
	
}
