package envision_lang.lang.packages.native_packages.base;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.functions.EnvisionFunction;

/**
 * Returns true if the given class instance supports the given operator overload.
 * Takes in an instance of an EnvisionClass and an operator.
 */
public class HasAttribute extends EnvisionFunction {
	
	public HasAttribute() {
		super(BOOLEAN, "hasAttribute", VAR, STRING);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionObject obj = args[0];
		EnvisionString attributeName = (EnvisionString) args[1];
		
		boolean exists = false;
		
		if (obj instanceof EnvisionCodeFile cf) {
		    exists = cf.scope().exists(attributeName.string_val);
		}
		else if (obj instanceof EnvisionClass c) {
		    exists = c.getClassScope().exists(attributeName.string_val);
		}
		else if (obj instanceof ClassInstance ci) {
		    exists = ci.getScope().exists(attributeName.string_val);
		}
		
		ret(EnvisionBoolean.of(exists));
	}
	
}
