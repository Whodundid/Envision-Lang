package envision_lang.lang.datatypes;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.natives.Primitives;

public class EnvisionExceptionClass extends EnvisionClass {
	
	public static final IDatatype EXCEPTION_TYPE = NativeTypeManager.datatypeOf(Primitives.EXCEPTION);
	
	public static final EnvisionExceptionClass EXCEPTION_CLASS = new EnvisionExceptionClass();
	
	public static final IPrototypeHandler prototypes = new IPrototypeHandler();
	
	private String exceptionName;
	
	static {
		
	}
	
	public EnvisionExceptionClass() {
		super(EXCEPTION_TYPE.getStringValue());
	}
	
	public EnvisionExceptionClass(String exceptionNameIn) {
		super(exceptionNameIn);
		exceptionName = exceptionNameIn;
	}
	
	public static EnvisionException newInstance(EnvisionException exc) {
		EnvisionException newExc = new EnvisionException(exc);
		return newExc;
	}
	
}
