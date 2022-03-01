package envision.lang.util.structureTypes;

import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.NegativeArgumentLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;

/**
 * An invocable object is any object that can be directly invoked to
 * perform some operation. For instance, all functions are invocable
 * objects as they are executed when they are invoked.
 * 
 * @author Hunter Bragg
 */
public abstract class InvocableObject extends EnvisionObject {
	
	//--------------
	// Constructors
	//--------------
	
	public InvocableObject() { this(EnvisionDatatype.prim_var(), DEFAULT_NAME); }
	public InvocableObject(String nameIn) { this(EnvisionDatatype.prim_var(), nameIn); }
	public InvocableObject(Primitives datatypeIn) { this(new EnvisionDatatype(datatypeIn), DEFAULT_NAME); }
	public InvocableObject(EnvisionDatatype typeIn) { this(typeIn, DEFAULT_NAME); }
	public InvocableObject(EnvisionDatatype datatypeIn, String nameIn) {
		super(datatypeIn, nameIn);
	}
	
	//------------------
	// Abstract Methods
	//------------------
	
	/**
	 * Called when the language actually runs the related callable object.
	 * Helper version to wrap singleton arguments into an array of length 1.
	 */
	public void invoke(EnvisionInterpreter interpreter, Object arg) {
		invoke(interpreter, new Object[] { arg });
	}
	
	/**
	 * Called when the language actually runs the related callable object.
	 */
	public abstract void invoke(EnvisionInterpreter interpreter, Object[] args);
	
	/**
	 * Should be called initially to check for valid arguments.
	 * If arguments are valid, then the actual invoke method is called.
	 */
	public void invoke_I(String func_name, EnvisionInterpreter interpreter, Object[] args) {
		//check to see if this call is for an internal method rather then an object member/function
		InternalMethod internal_meth = null;
		
		if (func_name != null) internal_meth = getInternalMethod(func_name, args);
		if (internal_meth != null) internal_meth.invoke(interpreter, args);
		else {
			checkArgs(args);
			invoke(interpreter, args);
		}
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * The expected number of arguments to be read. 255 is the absolute
	 * max.
	 */
	public int argLength() {
		return 255;
	}
	
	/**
	 * The expected datatypes of the arguments to be read. Return null to
	 * indicate that types do not matter.
	 */
	public ParameterData argTypes() {
		return null;
	}
	
	/**
	 * Checks for argument size and datatype errors.
	 */
	public void checkArgs(Object[] incoming_args) {
		int arg_length = argLength();
		ParameterData types = argTypes();
		
		// ensure that the expected number of arguments is possible
		if (arg_length < 0) throw new NegativeArgumentLengthError(this, arg_length);
		
		// if the incomming args are null -- ensure that the argSize is 0 -- otherwise error
		if (incoming_args == null && arg_length > 0) throw new ArgLengthError(this, 0, arg_length);
		
		// ensure that the expected number of arguments is not larger than the maximum allowed even if 
		if (arg_length > 255) throw new ArgLengthError(this, 255, arg_length);
		if (incoming_args.length > 255) throw new ArgLengthError(this, 255, incoming_args.length);
		
		// don't care about types if the expected ones are null
		if (types != null) {
			ParameterData args = new ParameterData(ObjectCreator.createArgs(incoming_args));
			
			//compare each parameter to ensure that they are compatible matches
			for (int i = 0, j = 0; i < incoming_args.length; i++) {
				Parameter type = types.get(j);
				Parameter obj = args.get(i);
				
				if (!type.compare(obj)) throw new InvalidDatatypeError(obj, type);
			}
		}
	}
	
}
