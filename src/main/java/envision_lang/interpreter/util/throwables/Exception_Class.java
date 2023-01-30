package envision_lang.interpreter.util.throwables;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.ParameterData;

public class Exception_Class extends EnvisionClass {
	
	public Exception_Class(EnvisionInterpreter interpreter) {
		super("Exception");
		staticScope = new Scope(interpreter.scope());
		addConstructor(new EnvisionFunction(ParameterData.from(STRING)));
	}
	/*
	public ClassInstance buildException(EnvisionInterpreter interpreter, Exception e) {
		// standard envision class creation
		Scope instanceScope = new Scope(staticClassScope);
		ClassInstance inst = new ClassInstance(this, instanceScope) {
			@Override
			public String toString() {
				Exception e = (Exception) get("_iException_").getJavaObject();
				return StringUtil.combineAll(e.getStackTrace());
			}
		};
		instanceScope.define("this", inst);
		
		EnvisionObject wrappedException = EnvisionObject.javaObjectWrapper("_iException_", e);
		//wrappedException.setRestricted();
		wrappedException.setFinal();
		instanceScope.define("_iException_", wrappedException);
		
		return inst;
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, Object[] args) {
		// standard envision class creation
		Scope instanceScope = new Scope(staticClassScope);
		ClassInstance inst = new ClassInstance(this, instanceScope) {
			@Override
			public String toString() {
				Exception e = (Exception) get("_iException_").getJavaObject();
				return StringUtil.combineAll(e.getStackTrace());
			}
		};
		instanceScope.define("this", inst);
		
		Exception e = createWrapException(interpreter, args[0]);
		EnvisionObject wrappedException = EnvisionObject.javaObjectWrapper("_iException_", e);
		wrappedException.setRestricted();
		wrappedException.setFinal();
		instanceScope.define("_iException_", wrappedException);
		
		return inst;
	}
	
	private Exception createWrapException(EnvisionInterpreter interpreter, Object errorIn) {
		String error = (errorIn != null) ? (String) convert(errorIn) : "";
		return new Exception(error);
	}
	*/
}
