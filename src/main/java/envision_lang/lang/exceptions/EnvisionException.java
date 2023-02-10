package envision_lang.lang.exceptions;

import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;

/** Allows for in-language exceptions to be caught. */
public class EnvisionException extends ClassInstance {
	
	//=========
	// Statics
	//=========
	
	public static final IDatatype EXCEPTION_TYPE = EnvisionStaticTypes.EXCEPTION_TYPE;
	
	/** The language default exception which can be thrown anywhere, for any reason. */
	public static final EnvisionException DEFAULT_EXCEPTION = EnvisionExceptionClass.newInstance();

	//========
	// Fields
	//========
	
	/** The stated reason that this exception was thrown. */
	public String reason = "";
	
	//==============
	// Constructors
	//==============
	
	EnvisionException() {
		super(EnvisionExceptionClass.DEFAULT_EXCEPTION_CLASS);
	}
	
	EnvisionException(String reasonIn) {
		super(EnvisionExceptionClass.DEFAULT_EXCEPTION_CLASS);
		reason = reasonIn;
	}
	
	EnvisionException(EnvisionExceptionClass throwingClassIn) {
		super(throwingClassIn);
	}
	
	EnvisionException(EnvisionExceptionClass throwingClassIn, String reasonIn) {
		super(throwingClassIn);
		reason = reasonIn;
	}
	
	EnvisionException(EnvisionException toCopyIn) {
		super(toCopyIn.internalClass);
		reason = toCopyIn.reason;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionException exc &&
				internalClass.equals(exc.internalClass) &&
				reason.equals(exc.reason) &&
				super.equals(obj));
	}
	
	@Override
	public String toString() {
		return reason;
	}
	
	@Override
	public EnvisionException copy() {
		return EnvisionExceptionClass.copy(this);
	}
	
}
