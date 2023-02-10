package envision_lang.lang.datatypes;

import envision_lang.lang.classes.ClassInstance;

/** Allows for in-language exceptions to be caught. */
public class EnvisionException extends ClassInstance {
	
	public String reason = "";
	
	EnvisionException() {
		super(EnvisionExceptionClass.EXCEPTION_CLASS);
	}
	
	EnvisionException(String reasonIn) {
		super(EnvisionExceptionClass.EXCEPTION_CLASS);
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
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionException exc && reason.equals(exc.reason));
	}
	
	@Override
	public String toString() {
		return reason;
	}
	
	@Override
	public EnvisionException copy() {
		return EnvisionExceptionClass.newInstance(this);
	}
	
}
