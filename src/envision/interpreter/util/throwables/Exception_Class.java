package envision.interpreter.util.throwables;

import static envision.lang.util.Primitives.*;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.data.ParameterData;
import eutil.strings.StringUtil;

public class Exception_Class extends EnvisionClass {
	
	public Exception_Class(EnvisionInterpreter interpreter) {
		super("Exception");
		classScope = new Scope(interpreter.scope());
		addConstructor(new EnvisionFunction(new ParameterData(STRING)));
	}
	
	public ClassInstance buildException(EnvisionInterpreter interpreter, Exception e) {
		// standard envision class creation
		Scope instanceScope = new Scope(classScope);
		ClassInstance inst = new ClassInstance(this, instanceScope) {
			@Override
			public String toString() {
				Exception e = (Exception) get("_iException_").getJavaObject();
				return StringUtil.combineAll(e.getStackTrace());
			}
		};
		instanceScope.define("this", inst);
		
		EnvisionObject wrappedException = EnvisionObject.javaObjectWrapper("_iException_", e);
		wrappedException.setRestricted();
		wrappedException.setFinal();
		instanceScope.define("_iException_", wrappedException);
		
		return inst;
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, Object[] args) {
		// standard envision class creation
		Scope instanceScope = new Scope(classScope);
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
	
}
