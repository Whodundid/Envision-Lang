package envision_lang.lang.exceptions;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.natives.EnvisionStaticTypes;

public final class EnvisionExceptionClass extends EnvisionClass {
	
	//=========
	// Statics
	//=========
	
	public static final EnvisionExceptionClass DEFAULT_EXCEPTION_CLASS = new EnvisionExceptionClass();
	
	/** Exception function prototypes. */
	private static final IPrototypeHandler prototypes = new IPrototypeHandler();
	
	static {
		
	}
	
	//==============
	// Constructors
	//==============
	
	/**
	 * Private internal constructor used to create the DEFAULT_EXCEPTION_CLASS instance.
	 */
	private EnvisionExceptionClass() {
		super(EnvisionStaticTypes.EXCEPTION_TYPE.getStringValue());
	}
	
	/**
	 * Public constructor used for in-language custom exception class creation.
	 * 
	 * @param exceptionClassNameIn The name for this exception class
	 */
	public EnvisionExceptionClass(String exceptionClassNameIn) {
		super(exceptionClassNameIn);
	}
	
	//============================
	// Default Exception Creation
	//============================
	
	public static EnvisionException newInstance() { return newInstance(""); }
	public static EnvisionException newInstance(String reason) {
		EnvisionException newExc = new EnvisionException(reason);
		return newExc;
	}
	
	public static EnvisionException copy(EnvisionException exc) {
		EnvisionException newExc = new EnvisionException(exc);
		return newExc;
	}
	
	//===========================
	// Custom Exception Creation
	//===========================
	
}
