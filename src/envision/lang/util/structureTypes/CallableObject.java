package envision.lang.util.structureTypes;

import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.InvalidDataTypeError;
import envision.exceptions.errors.NegativeArgumentLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;

public interface CallableObject {
	
	/** The expected number of arguments to be read. 255 is the absolute max. */
	public default int argSize() { return 255; }
	
	/** The expected datatypes of the arguments to be read. Return null to indicate that types do not matter. */
	public default ParameterData argTypes() { return null; }
	
	/** Called initially to check for valid arguments. */
	public default void call_I(EnvisionInterpreter interpreter, Object[] args) { checkArgs(args); call(interpreter, args); }
	
	/** Called when the language actually runs the related callable object. */
	public void call(EnvisionInterpreter interpreter, Object[] args);
	
	/** Checks for argument size and datatype errors. */
	public default void checkArgs(Object[] args2) {
		int size = argSize();
		ParameterData types = argTypes();
		
		// ensure that the expected number of arguments is possible
		if (size < 0) { throw new NegativeArgumentLengthError(this, size); }
		
		// if the incomming args are null -- ensure that the argSize is 0 -- otherwise error
		if (args2 == null && size > 0) { throw new ArgLengthError(this, 0, size); }
		
		// ensure that the expected number of arguments is not larger than the maximum allowed even if 
		if (size > 255) { throw new ArgLengthError(this, 255, size); }
		if (args2.length > 255) { throw new ArgLengthError(this, 255, args2.length); }
		
		// don't care about types if the expected ones are null
		if (types != null) {
			ParameterData args = new ParameterData(ObjectCreator.createArgs(args2));
			
			for (int i = 0, j = 0; i < args2.length; i++) {
				Parameter type = types.get(j);
				Parameter obj = args.get(i);
				
				if (!type.compare(obj)) { throw new InvalidDataTypeError(obj, type); }
				
				//check for varags -- if not increment to the next argument
				//if (!type.isVarags()) { j++; }
			}
		}
	}
	
}
